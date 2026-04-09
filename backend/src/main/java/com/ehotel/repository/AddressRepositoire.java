package com.ehotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehotel.model.Address;

public interface AddressRepositoire extends JpaRepository<Address, Long> {
}
