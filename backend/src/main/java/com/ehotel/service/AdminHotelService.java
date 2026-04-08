package com.ehotel.service;

import com.ehotel.dto.request.AdminHotelRequest;
import com.ehotel.dto.response.HotelSummaryResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Address;
import com.ehotel.model.Hotel;
import com.ehotel.repository.AddressRepository;
import com.ehotel.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminHotelService {

    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;

    public AdminHotelService(HotelRepository hotelRepository, AddressRepository addressRepository) {
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional
    public HotelSummaryResponse createHotel(AdminHotelRequest request) {
        Address address = buildAddressFromRequest(request);
        Address savedAddress = addressRepository.save(Objects.requireNonNull(address));

        Hotel hotel = new Hotel();
        hotel.setChainId(request.getChainId());
        hotel.setName(request.getName());
        hotel.setCategory(request.getCategory());
        hotel.setRoomCount(0);
        hotel.setAddressId(savedAddress.getAddressId());

        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setCountry(request.getCountry());
        hotel.setPostalCode(request.getPostalCode());

        Hotel savedHotel = hotelRepository.save(hotel);
        return mapToSummary(savedHotel);
    }

    @Transactional
    public HotelSummaryResponse updateHotel(Long hotelId, AdminHotelRequest request) {
        Hotel hotel = hotelRepository.findById(Objects.requireNonNull(hotelId))
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Address address = buildAddressFromRequest(request);
        Address savedAddress = addressRepository.save(Objects.requireNonNull(address));

        hotel.setChainId(request.getChainId());
        hotel.setName(request.getName());
        hotel.setCategory(request.getCategory());
        hotel.setAddressId(savedAddress.getAddressId());

        if (hotel.getRoomCount() == null) {
            hotel.setRoomCount(0);
        }

        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setCountry(request.getCountry());
        hotel.setPostalCode(request.getPostalCode());

        Hotel savedHotel = hotelRepository.save(hotel);
        return mapToSummary(savedHotel);
    }

    @Transactional
    public void deleteHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(Objects.requireNonNull(hotelId))
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        hotelRepository.delete(Objects.requireNonNull(hotel));
    }

    private Address buildAddressFromRequest(AdminHotelRequest request) {
        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }

        String trimmed = request.getAddress().trim();
        String[] parts = trimmed.split("\\s+", 2);

        Integer streetNumber = 0;
        String streetName = trimmed;

        if (parts.length == 2) {
            try {
                streetNumber = Integer.parseInt(parts[0]);
                streetName = parts[1];
            } catch (NumberFormatException ignored) {
                streetName = trimmed;
            }
        }

        Address address = new Address();
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setCity(request.getCity() != null ? request.getCity() : "Ottawa");
        address.setProvince(request.getProvince() != null ? request.getProvince() : "Ontario");
        address.setPostalCode(request.getPostalCode() != null ? request.getPostalCode() : "UNKNOWN");
        address.setCountry(request.getCountry() != null ? request.getCountry() : "Canada");

        return address;
    }

    private HotelSummaryResponse mapToSummary(Hotel hotel) {
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
}