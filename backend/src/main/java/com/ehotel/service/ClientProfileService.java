package com.ehotel.service;

import com.ehotel.dto.request.ClientUpdateProfileRequest;
import com.ehotel.dto.response.ClientSummaryResponse;
import com.ehotel.model.Address;
import com.ehotel.model.Customer;
import com.ehotel.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientProfileService {

    private final CustomerRepository customerRepository;

    public ClientProfileService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public ClientSummaryResponse getProfile(Long clientId) {
        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return mapToSummary(customer);
    }

    @Transactional
    public ClientSummaryResponse updateProfile(ClientUpdateProfileRequest request) {
        Customer customer = customerRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setSsn(request.getSsn());

        Address address = customer.getAddress();
        if (address != null) {
            address.setStreetNumber(request.getStreetNumber());
            address.setStreetName(request.getStreetName());
            address.setCity(request.getCity());
            address.setProvince(request.getProvince());
            address.setPostalCode(request.getPostalCode());
            address.setCountry(request.getCountry());
        }

        customer = customerRepository.save(customer);
        return mapToSummary(customer);
    }

    private ClientSummaryResponse mapToSummary(Customer customer) {
        ClientSummaryResponse response = new ClientSummaryResponse();

        response.setClientId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setSsn(customer.getSsn());

        if (customer.getAddress() != null) {
            response.setAddressId(customer.getAddress().getAddressId());
            response.setStreetNumber(customer.getAddress().getStreetNumber());
            response.setStreetName(customer.getAddress().getStreetName());
            response.setCity(customer.getAddress().getCity());
            response.setProvince(customer.getAddress().getProvince());
            response.setPostalCode(customer.getAddress().getPostalCode());
            response.setCountry(customer.getAddress().getCountry());
        }

        return response;
    }
}