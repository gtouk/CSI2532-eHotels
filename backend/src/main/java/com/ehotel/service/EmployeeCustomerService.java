package com.ehotel.service;

import com.ehotel.dto.response.CustomerDetailsResponse;
import com.ehotel.dto.response.CustomerSummaryResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Customer;
import com.ehotel.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return mapToDetails(customer);
    }

    private CustomerSummaryResponse mapToSummary(Customer customer) {
        CustomerSummaryResponse response = new CustomerSummaryResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        return response;
    }

    private CustomerDetailsResponse mapToDetails(Customer customer) {
        CustomerDetailsResponse response = new CustomerDetailsResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIdType(customer.getIdType());
        response.setIdNumber(customer.getIdNumber());
        response.setRegistrationDate(customer.getRegistrationDate());
        return response;
    }
}