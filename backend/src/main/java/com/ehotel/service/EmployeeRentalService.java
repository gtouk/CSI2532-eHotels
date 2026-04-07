package com.ehotel.service;

import com.ehotel.dto.request.CreateRentalRequest;
import com.ehotel.dto.response.RentalSummaryResponse;
import com.ehotel.enums.RentalStatus;
import com.ehotel.enums.ReservationStatus;
import com.ehotel.enums.RoomStatus;
import com.ehotel.exception.ResourceNotFoundException;
import com.ehotel.model.Customer;
import com.ehotel.model.Employee;
import com.ehotel.model.Rental;
import com.ehotel.model.Reservation;
import com.ehotel.model.Room;
import com.ehotel.repository.CustomerRepository;
import com.ehotel.repository.EmployeeRepository;
import com.ehotel.repository.RentalRepository;
import com.ehotel.repository.ReservationRepository;
import com.ehotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeRentalService {

    private final RentalRepository rentalRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeRentalService(
            RentalRepository rentalRepository,
            ReservationRepository reservationRepository,
            CustomerRepository customerRepository,
            RoomRepository roomRepository,
            EmployeeRepository employeeRepository
    ) {
        this.rentalRepository = rentalRepository;
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<RentalSummaryResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RentalSummaryResponse getRentalById(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        return mapToSummary(rental);
    }

    @Transactional
    public RentalSummaryResponse createRental(CreateRentalRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Rental rental = new Rental();
        rental.setCustomer(customer);
        rental.setRoom(room);
        rental.setEmployee(employee);
        rental.setStatus(RentalStatus.ACTIVE);

        if (request.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(request.getReservationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

            if (reservation.getStatus() != ReservationStatus.RESERVED) {
                throw new IllegalArgumentException("Only RESERVED reservations can be converted to rental");
            }

            if (!reservation.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                throw new IllegalArgumentException("Reservation customer does not match request customer");
            }

            if (!reservation.getRoom().getRoomId().equals(room.getRoomId())) {
                throw new IllegalArgumentException("Reservation room does not match request room");
            }

            // Important: complete reservation first to avoid DB overlap trigger conflict
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);
            reservationRepository.flush();

            rental.setReservation(reservation);
            rental.setCheckInDate(reservation.getStartDate());
            rental.setCheckOutDate(reservation.getEndDate());
        } else {
            LocalDate start = request.getCheckInDate() != null ? request.getCheckInDate() : LocalDate.now();
            rental.setCheckInDate(start);
            rental.setCheckOutDate(start.plusDays(1));
        }

        Rental savedRental = rentalRepository.save(rental);

        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return mapToSummary(savedRental);
    }

    @Transactional
    public RentalSummaryResponse checkoutRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new IllegalArgumentException("Only ACTIVE rentals can be checked out");
        }

        LocalDate checkoutDate = LocalDate.now();

        if (checkoutDate == null || !checkoutDate.isAfter(rental.getCheckInDate())) {
            checkoutDate = rental.getCheckInDate().plusDays(1);
        }

        rental.setStatus(RentalStatus.COMPLETED);
        rental.setCheckOutDate(checkoutDate);

        Room room = rental.getRoom();
        if (room != null) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        Rental savedRental = rentalRepository.save(rental);
        return mapToSummary(savedRental);
    }

    private RentalSummaryResponse mapToSummary(Rental rental) {
        RentalSummaryResponse response = new RentalSummaryResponse();

        response.setRentalId(rental.getRentalId());

        Customer customer = rental.getCustomer();
        if (customer != null) {
            response.setCustomerId(customer.getCustomerId());
            response.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        }

        Room room = rental.getRoom();
        if (room != null) {
            response.setRoomId(room.getRoomId());
            response.setRoomNumber(room.getRoomNumber());
        }

        Employee employee = rental.getEmployee();
        if (employee != null) {
            response.setEmployeeId(employee.getEmployeeId());
            response.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        }

        response.setCheckInDate(rental.getCheckInDate());
        response.setCheckOutDate(rental.getCheckOutDate());
        response.setStatus(rental.getStatus() != null ? rental.getStatus().name() : null);

        return response;
    }
}