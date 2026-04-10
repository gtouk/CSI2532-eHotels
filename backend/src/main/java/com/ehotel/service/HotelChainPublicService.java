package com.ehotel.service;

import com.ehotel.dto.response.HotelChainDetailsResponse;
import com.ehotel.dto.response.HotelChainSummaryResponse;
import com.ehotel.dto.response.HotelSummaryResponse;
import com.ehotel.model.Address;
import com.ehotel.model.Hotel;
import com.ehotel.model.HotelChain;
import com.ehotel.repository.AddressRepository;
import com.ehotel.repository.HotelChainRepository;
import com.ehotel.repository.HotelRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class HotelChainPublicService {

    private final HotelChainRepository hotelChainRepository;
    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public HotelChainPublicService(
            HotelChainRepository hotelChainRepository,
            HotelRepository hotelRepository,
            AddressRepository addressRepository
    ) {
        this.hotelChainRepository = hotelChainRepository;
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
    }

    public List<HotelChainSummaryResponse> getAllChains() {
        return hotelChainRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    public HotelChainDetailsResponse getChainById(Long chainId) {
        HotelChain chain = hotelChainRepository.findById(chainId)
                .orElseThrow(() -> new RuntimeException("Hotel chain not found"));

        HotelChainDetailsResponse response = new HotelChainDetailsResponse();
        response.setChainId(chain.getChainId());
        response.setName(chain.getName());
        response.setHotelCount(chain.getHotelCount());

        Address address = addressRepository.findById(chain.getAddressId()).orElse(null);
        if (address != null) {
            response.setAddress(buildAddressLine(address));
            response.setCity(address.getCity());
            response.setProvince(address.getProvince());
            response.setCountry(address.getCountry());
            response.setPostalCode(address.getPostalCode());
        }

        response.setEmails(findEmailsByChainId(chain.getChainId()));
        response.setPhones(findPhonesByChainId(chain.getChainId()));

        List<HotelSummaryResponse> hotels = hotelRepository.findAll()
                .stream()
                .filter(h -> h.getChainId() != null && h.getChainId().equals(chainId))
                .map(this::mapHotelToSummary)
                .collect(Collectors.toList());

        response.setHotels(hotels);

        return response;
    }

    private HotelChainSummaryResponse mapToSummary(HotelChain chain) {
        HotelChainSummaryResponse response = new HotelChainSummaryResponse();
        response.setChainId(chain.getChainId());
        response.setName(chain.getName());
        response.setHotelCount(chain.getHotelCount());

        Address address = addressRepository.findById(chain.getAddressId()).orElse(null);
        if (address != null) {
            response.setAddress(buildAddressLine(address));
            response.setCity(address.getCity());
            response.setProvince(address.getProvince());
            response.setCountry(address.getCountry());
            response.setPostalCode(address.getPostalCode());
        }

        response.setEmails(findEmailsByChainId(chain.getChainId()));
        response.setPhones(findPhonesByChainId(chain.getChainId()));

        return response;
    }

    private List<String> findEmailsByChainId(Long chainId) {
        return entityManager.createNativeQuery(
                        "SELECT email FROM hotel_chain_email WHERE chain_id = :chainId ORDER BY email")
                .setParameter("chainId", chainId)
                .getResultList();
    }

    private List<String> findPhonesByChainId(Long chainId) {
        return entityManager.createNativeQuery(
                        "SELECT phone FROM hotel_chain_phone WHERE chain_id = :chainId ORDER BY phone")
                .setParameter("chainId", chainId)
                .getResultList();
    }

    private HotelSummaryResponse mapHotelToSummary(Hotel hotel) {
        HotelSummaryResponse response = new HotelSummaryResponse();
        response.setHotelId(hotel.getHotelId());
        response.setChainId(hotel.getChainId());
        response.setName(hotel.getName());
        response.setCategory(hotel.getCategory());

        if (hotel.getAddressId() != null) {
            Address address = addressRepository.findById(hotel.getAddressId()).orElse(null);
            if (address != null) {
                response.setAddress(buildAddressLine(address));
                response.setCity(address.getCity());
                response.setProvince(address.getProvince());
                response.setCountry(address.getCountry());
                response.setPostalCode(address.getPostalCode());
            }
        }

        return response;
    }

    private String buildAddressLine(Address address) {
        String number = address.getStreetNumber() != null ? String.valueOf(address.getStreetNumber()) : "";
        String street = address.getStreetName() != null ? address.getStreetName() : "";
        String full = (number + " " + street).trim();
        return full.isEmpty() ? null : full;
    }
}