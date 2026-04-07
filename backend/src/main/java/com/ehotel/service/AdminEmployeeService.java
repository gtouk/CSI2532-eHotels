package com.ehotel.service;

import com.ehotel.dto.request.AdminEmployeeRequest;
import com.ehotel.dto.response.EmployeeSummaryResponse;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Address;
import com.ehotel.model.Employee;
import com.ehotel.model.Hotel;
import com.ehotel.repository.AddressRepository;
import com.ehotel.repository.EmployeeRepository;
import com.ehotel.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;

    public AdminEmployeeService(
            EmployeeRepository employeeRepository,
            HotelRepository hotelRepository,
            AddressRepository addressRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeSummaryResponse createEmployee(AdminEmployeeRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Address address = buildAddressFromRequest(request);
        Address savedAddress = addressRepository.save(address);
        Employee employee = new Employee();
        employee.setHotel(hotel);
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setSsn(request.getSsn());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setPassword(request.getPassword());
        employee.setRole(request.getRole());
        employee.setAddressId(savedAddress.getAddressId());
        employee.setActive(request.getActive() != null ? request.getActive() : true);

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToSummary(savedEmployee);
    }

    @Transactional
    public EmployeeSummaryResponse updateEmployee(Long employeeId, AdminEmployeeRequest request) {
        Address address = buildAddressFromRequest(request);
        Address savedAddress = addressRepository.save(address);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        employee.setHotel(hotel);
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setSsn(request.getSsn());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setRole(request.getRole());
        employee.setAddressId(savedAddress.getAddressId());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            employee.setPassword(request.getPassword());
        }

        employee.setActive(request.getActive() != null ? request.getActive() : employee.getActive());

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToSummary(savedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private EmployeeSummaryResponse mapToSummary(Employee employee) {
        EmployeeSummaryResponse response = new EmployeeSummaryResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setHotelId(employee.getHotel() != null ? employee.getHotel().getHotelId() : null);
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setRole(employee.getRole() != null ? employee.getRole().name() : null);
        response.setActive(employee.getActive());
        return response;
    }

    private Address buildAddressFromRequest(AdminEmployeeRequest request) {
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
}