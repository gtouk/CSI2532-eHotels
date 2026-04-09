package com.ehotel.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ehotel.dto.request.ClientLoginRequest;
import com.ehotel.dto.request.ClientRegisterRequest;
import com.ehotel.dto.response.CustomerAuthData;
import com.ehotel.model.Address;
import com.ehotel.model.Customer;
import com.ehotel.repository.AddressRepository;
import com.ehotel.repository.CustomerRepository;

@Service
public class ClientAuthService {

    @Autowired
    private CustomerRepository customerRepository; // nom correct pour JPA

    @Autowired
    private AddressRepository addressRepository; // nom correct pour JPA

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // LOGIN
    public CustomerAuthData login(ClientLoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!encoder.matches(request.getPassword(), customer.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        CustomerAuthData data = new CustomerAuthData();
        data.setClientId(customer.getCustomerId());
        data.setEmail(customer.getEmail());
        data.getrole(); // tu peux adapter selon ton champ Role si nécessaire

        return data;
    }

    public CustomerAuthData register(ClientRegisterRequest request) {

        // 1. Vérifier email unique
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 2. Créer l'adresse
        Address address = new Address();
        address.setStreetNumber(request.getStreetNumber());
        address.setStreetName(request.getStreetName());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        address = addressRepository.save(address);

        // 3. Hasher le mot de passe
        String hash = encoder.encode(request.getPassword());

        // 4. Créer le customer
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setSsn(request.getSsn());
        customer.setPasswordHash(hash);
        customer.setRegistrationDate(LocalDate.now());
        customer.setAddress(address);

        customer = customerRepository.save(customer); // pas besoin de cast

        // 5. Préparer la réponse
        CustomerAuthData data = new CustomerAuthData();
        data.setClientId(customer.getCustomerId());
        data.setEmail(customer.getEmail());
        data.getrole(); // corrige getrole() -> setRole

        return data;
    }
}