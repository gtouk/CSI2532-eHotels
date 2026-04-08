package com.ehotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehotel.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}