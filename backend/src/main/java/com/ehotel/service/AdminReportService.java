package com.ehotel.service;

import com.ehotel.dto.response.AdminReportResponse;
import com.ehotel.model.Rental;
import com.ehotel.repository.CustomerRepository;
import com.ehotel.repository.EmployeeRepository;
import com.ehotel.repository.HotelRepository;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.repository.RentalRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReportService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationRepository reservationRepository;
    private final RentalRepository rentalRepository;

    public AdminReportService(
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            ReservationRepository reservationRepository,
            RentalRepository rentalRepository
    ) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.reservationRepository = reservationRepository;
        this.rentalRepository = rentalRepository;
    }

    public AdminReportResponse getReports() {
        AdminReportResponse response = new AdminReportResponse();

        response.setTotalHotels(hotelRepository.count());
        response.setTotalRooms(roomRepository.count());
        response.setTotalCustomers(customerRepository.count());
        response.setTotalEmployees(employeeRepository.count());
        response.setTotalReservations(reservationRepository.count());

        List<Rental> rentals = rentalRepository.findAll();

        long activeRentals = rentals.stream()
                .filter(r -> r.getCheckOutDate() == null)
                .count();

        long completedRentals = rentals.stream()
                .filter(r -> r.getCheckOutDate() != null)
                .count();

        response.setActiveRentals(activeRentals);
        response.setCompletedRentals(completedRentals);

        return response;
    }
}