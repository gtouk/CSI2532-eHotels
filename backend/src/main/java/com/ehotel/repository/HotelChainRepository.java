package com.ehotel.repository;

import com.ehotel.model.HotelChain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelChainRepository extends JpaRepository<HotelChain, Long> {
}