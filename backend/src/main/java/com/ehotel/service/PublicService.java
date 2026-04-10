package com.ehotel.service;

import com.ehotel.dto.response.HotelDetailsResponse;
import com.ehotel.dto.response.HotelSummaryResponse;
import com.ehotel.dto.response.RoomDetailsResponse;
import com.ehotel.dto.response.RoomSummaryResponse;
import com.ehotel.enums.RentalStatus;
import com.ehotel.enums.ReservationStatus;
import com.ehotel.enums.RoomStatus;
import com.ehotel.model.Hotel;
import com.ehotel.model.Rental;
import com.ehotel.model.Room;
import com.ehotel.repository.HotelRepository;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.repository.RentalRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final RentalRepository rentalRepository;

    public PublicService(
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            ReservationRepository reservationRepository,
            RentalRepository rentalRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.rentalRepository = rentalRepository;
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

        return mapRoomToDetails(room);
    }

    public List<RoomSummaryResponse> searchRooms(
            LocalDate startDate,
            LocalDate endDate,
            Long hotelId,
            String capacity,
            String viewType,
            Boolean extendable,
            Double minPrice,
            Double maxPrice
    ) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("startDate and endDate are required");
        }

        if (!startDate.isBefore(endDate)) {
            throw new RuntimeException("startDate must be before endDate");
        }

        return roomRepository.findAll()
                .stream()
                .filter(room -> room.getStatus() != RoomStatus.MAINTENANCE)
                .filter(room -> hotelId == null || (room.getHotel() != null && hotelId.equals(room.getHotel().getHotelId())))
                .filter(room -> capacity == null || capacity.isBlank() || capacity.equalsIgnoreCase(room.getCapacity()))
                .filter(room -> viewType == null || viewType.isBlank()
                        || (room.getViewType() != null && viewType.equalsIgnoreCase(room.getViewType().name())))
                .filter(room -> extendable == null || extendable.equals(room.getExtendable()))
                .filter(room -> minPrice == null || room.getPricePerNight().doubleValue() >= minPrice)
                .filter(room -> maxPrice == null || room.getPricePerNight().doubleValue() <= maxPrice)
                .filter(room -> isRoomAvailable(room, startDate, endDate))
                .map(this::mapRoomToSummary)
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        List<ReservationStatus> blockedReservationStatuses = List.of(ReservationStatus.RESERVED);

        boolean hasReservationOverlap = !reservationRepository
                .findByRoomAndStatusInAndEndDateAfterAndStartDateBefore(
                        room,
                        blockedReservationStatuses,
                        startDate,
                        endDate
                )
                .isEmpty();

        if (hasReservationOverlap) {
            return false;
        }

        List<Rental> rentals = rentalRepository.findByRoom_RoomId(room.getRoomId());
        for (Rental rental : rentals) {
            if (rental.getStatus() == RentalStatus.CANCELLED || rental.getStatus() == RentalStatus.COMPLETED) {
                continue;
            }

            LocalDate rentalStart = rental.getCheckInDate();
            LocalDate rentalEnd = rental.getCheckOutDate() != null
                    ? rental.getCheckOutDate()
                    : rental.getCheckInDate().plusDays(1);

            if (startDate.isBefore(rentalEnd) && endDate.isAfter(rentalStart)) {
                return false;
            }
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

    private RoomDetailsResponse mapRoomToDetails(Room room) {
        RoomDetailsResponse response = new RoomDetailsResponse();
        response.setRoomId(room.getRoomId());
        response.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerNight(room.getPricePerNight());
        response.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        response.setExtendable(room.getExtendable());
        response.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
        return response;
    }

    private RoomSummaryResponse mapRoomToSummary(Room room) {
        RoomSummaryResponse response = new RoomSummaryResponse();
        response.setRoomId(room.getRoomId());
        response.setHotelId(room.getHotel() != null ? room.getHotel().getHotelId() : null);
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerNight(room.getPricePerNight());
        response.setViewType(room.getViewType() != null ? room.getViewType().name() : null);
        response.setExtendable(room.getExtendable());
        response.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
        return response;
    }
}