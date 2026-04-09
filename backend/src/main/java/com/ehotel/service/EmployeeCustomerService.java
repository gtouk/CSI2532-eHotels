package com.ehotel.service;

import com.ehotel.dto.response.CustomerDetailsResponse;
import com.ehotel.dto.response.CustomerSummaryResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Address;
import com.ehotel.model.Customer;
import com.ehotel.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeCustomerService {

    private final CustomerRepository customerRepository;

    public EmployeeCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDetailsResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(Objects.requireNonNull(customerId))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return mapToDetails(customer);
    }

    private CustomerSummaryResponse mapToSummary(Customer customer) {
        CustomerSummaryResponse response = new CustomerSummaryResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        return response;
    }

    private CustomerDetailsResponse mapToDetails(Customer customer) {
        CustomerDetailsResponse response = new CustomerDetailsResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setSsn(customer.getSsn());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setRegistrationDate(customer.getRegistrationDate());
        response.setRole(customer.getRole());
        response.setAddress(formatAddress(customer.getAddress()));
        return response;
    }

    private String formatAddress(Address address) {
        if (address == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        if (address.getStreetNumber() != null) {
            sb.append(address.getStreetNumber()).append(" ");
        }
        if (address.getStreetName() != null) {
            sb.append(address.getStreetName());
        }
        if (address.getCity() != null && !address.getCity().isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getCity());
        }
        if (address.getProvince() != null && !address.getProvince().isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getProvince());
        }
        if (address.getPostalCode() != null && !address.getPostalCode().isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getPostalCode());
        }
        if (address.getCountry() != null && !address.getCountry().isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getCountry());
        }

        return sb.toString();
    }
}