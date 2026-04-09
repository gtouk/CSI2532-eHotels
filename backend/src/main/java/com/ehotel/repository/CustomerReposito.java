package com.ehotel.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ehotel.model.Customer;

@Repository
public class CustomerReposito {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        return jdbcTemplate.queryForMap(sql, email);
    }

    public Customer save(Customer customer) {
        String sql = "INSERT INTO customer (ssn, first_name, last_name, registration_date, address_id, email, password_hash) "
                +
                "VALUES (?, ?, ?, CURRENT_DATE, ?, ?, ?)";

        jdbcTemplate.update(sql,
                customer.getSsn(),
                customer.getFirstName(),
                customer.getLastName(),
                // customer.getAddressId(),
                customer.getEmail(),
                customer.getPasswordHash());
        return customer;
    }
}