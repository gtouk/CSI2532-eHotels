package com.ehotel.service;

import com.ehotel.dto.response.HotelDetailsResponse;
import com.ehotel.dto.response.HotelSummaryResponse;
import com.ehotel.dto.response.RoomDetailsResponse;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.enums.RentalStatus;
import com.ehotel.enums.ReservationStatus;
import com.ehotel.model.Hotel;
import com.ehotel.model.Rental;
import com.ehotel.model.Room;
import com.ehotel.repository.HotelRepository;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.repository.RentalRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublicService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final RentalRepository rentalRepository;
    private final JdbcTemplate jdbcTemplate;

    public PublicService(
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            ReservationRepository reservationRepository,
            RentalRepository rentalRepository,
            JdbcTemplate jdbcTemplate) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.rentalRepository = rentalRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getHomeMessage() {
        return "Welcome to eHotel API";
    }

    public List<HotelSummaryResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::mapHotelToSummary)
                .collect(Collectors.toList());
    }

    public HotelDetailsResponse getHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return mapHotelToDetails(hotel);
    }

    public RoomDetailsResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return mapRoomToDetails(room, null);
    }

    /**
     * Recherche de chambres avec tous les filtres du frontend :
     * dates, capacité, chaîne, catégorie, prix, superficie.
     * Utilise une requête SQL native pour les jointures hotel/chain/address.
     */
    public List<RoomSummaryResponse> searchRooms(
            LocalDate startDate,
            LocalDate endDate,
            Long chainId,
            Long hotelId,
            Integer category,
            String capacity,
            String viewType,
            Boolean extendable,
            Double minPrice,
            Double maxPrice,
            Integer minSurface
    ) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("startDate and endDate sont obligatoires");
        }
        if (!startDate.isBefore(endDate)) {
            throw new RuntimeException("startDate doit être avant endDate");
        }

        StringBuilder sql = new StringBuilder("""
            SELECT r.room_id, r.room_number, r.price, r.capacity, r.view_type,
                   r.is_extendable, r.surface_area, r.status,
                   h.hotel_id, h.name AS hotel_name, h.category,
                   hc.chain_id, hc.name AS chain_name,
                   a.city
            FROM room r
            JOIN hotel h        ON r.hotel_id  = h.hotel_id
            JOIN hotel_chain hc ON h.chain_id  = hc.chain_id
            JOIN address a      ON h.address_id = a.address_id
            WHERE r.status = 'AVAILABLE'
              AND r.room_id NOT IN (
                  SELECT res.room_id FROM reservation res
                  WHERE res.status = 'RESERVED'
                    AND ? < res.end_date AND ? > res.start_date
              )
              AND r.room_id NOT IN (
                  SELECT ren.room_id FROM rental ren
                  WHERE ren.status = 'ACTIVE'
                    AND ? < ren.end_date AND ? > ren.start_date
              )
        """);

        java.util.List<Object> params = new java.util.ArrayList<>();
        // pour les sous-requêtes dates (4 fois)
        params.add(startDate);
        params.add(endDate);
        params.add(startDate);
        params.add(endDate);

        if (chainId != null) {
            sql.append(" AND hc.chain_id = ?");
            params.add(chainId);
        }
        if (hotelId != null) {
            sql.append(" AND h.hotel_id = ?");
            params.add(hotelId);
        }
        if (category != null) {
            sql.append(" AND h.category = ?");
            params.add(category);
        }
        if (capacity != null && !capacity.isBlank()) {
            sql.append(" AND r.capacity = ?");
            params.add(capacity);
        }
        if (viewType != null && !viewType.isBlank()) {
            sql.append(" AND r.view_type = ?");
            params.add(viewType.toUpperCase());
        }
        if (extendable != null) {
            sql.append(" AND r.is_extendable = ?");
            params.add(extendable);
        }
        if (minPrice != null) {
            sql.append(" AND r.price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND r.price <= ?");
            params.add(maxPrice);
        }
        if (minSurface != null) {
            sql.append(" AND r.surface_area >= ?");
            params.add(minSurface);
        }

        sql.append(" ORDER BY r.price ASC");

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            RoomSummaryResponse resp = new RoomSummaryResponse();
            resp.setRoomId(rs.getLong("room_id"));
            resp.setRoomNumber(rs.getString("room_number"));
            resp.setPricePerNight(rs.getBigDecimal("price"));
            resp.setCapacity(rs.getString("capacity"));
            resp.setViewType(rs.getString("view_type"));
            resp.setExtendable(rs.getBoolean("is_extendable"));
            resp.setSurfaceArea(rs.getObject("surface_area") != null ? rs.getInt("surface_area") : null);
            resp.setStatus(rs.getString("status"));
            resp.setHotelId(rs.getLong("hotel_id"));
            resp.setHotelName(rs.getString("hotel_name"));
            resp.setHotelCategory(rs.getInt("category"));
            resp.setChainName(rs.getString("chain_name"));
            resp.setCity(rs.getString("city"));
            return resp;
        });
    }

    private boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        boolean hasReservationOverlap = !reservationRepository
                .findByRoomAndStatusInAndEndDateAfterAndStartDateBefore(
                        room,
                        List.of(ReservationStatus.RESERVED),
                        startDate,
                        endDate)
                .isEmpty();

        if (hasReservationOverlap) return false;

        List<Rental> rentals = rentalRepository.findByRoom_RoomId(room.getRoomId());
        for (Rental rental : rentals) {
            if (rental.getStatus() == RentalStatus.CANCELLED || rental.getStatus() == RentalStatus.COMPLETED) continue;
            LocalDate rentalStart = rental.getCheckInDate();
            LocalDate rentalEnd = rental.getCheckOutDate() != null ? rental.getCheckOutDate() : rentalStart.plusDays(1);
            if (startDate.isBefore(rentalEnd) && endDate.isAfter(rentalStart)) return false;
        }
        return true;
    }

    private HotelSummaryResponse mapHotelToSummary(Hotel hotel) {
        HotelSummaryResponse response = new HotelSummaryResponse();
        response.setHotelId(hotel.getHotelId());
        response.setChainId(hotel.getChainId());
        response.setName(hotel.getName());
        response.setCategory(hotel.getCategory());
        response.setAddress(hotel.getAddress());
        response.setCity(hotel.getCity());
        response.setCountry(hotel.getCountry());
        response.setPostalCode(hotel.getPostalCode());
        return response;
    }

    private HotelDetailsResponse mapHotelToDetails(Hotel hotel) {
        HotelDetailsResponse response = new HotelDetailsResponse();
        response.setHotelId(hotel.getHotelId());
        response.setChainId(hotel.getChainId());
        response.setName(hotel.getName());
        response.setCategory(hotel.getCategory());
        response.setRoomCount(hotel.getRoomCount());
        response.setAddressId(hotel.getAddressId());
        response.setAddress(hotel.getAddress());
        response.setCity(hotel.getCity());
        response.setCountry(hotel.getCountry());
        response.setPostalCode(hotel.getPostalCode());
        return response;
    }

    private RoomDetailsResponse mapRoomToDetails(Room room, Map<String, Object> extra) {
        RoomDetailsResponse response = new RoomDetailsResponse();
        response.setRoomId(room.getRoomId());
        response.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerNight(room.getPricePerNight());
        response.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        response.setExtendable(room.getExtendable());
        response.setSurfaceArea(room.getSurfaceArea());
        response.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
        return response;
    }
}
