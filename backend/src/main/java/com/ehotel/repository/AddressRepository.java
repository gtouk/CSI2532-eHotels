package com.ehotel.repository;

<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehotel.model.Address;

@Repository
=======
import com.ehotel.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

>>>>>>> origin/main
public interface AddressRepository extends JpaRepository<Address, Long> {
}