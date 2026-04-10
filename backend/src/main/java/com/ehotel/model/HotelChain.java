package com.ehotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hotel_chain")
public class HotelChain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chain_id")
    private Long chainId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "hotel_count", nullable = false)
    private Integer hotelCount = 0;

    @Column(name = "address_id", nullable = false)
    private Long addressId;

    public HotelChain() {
    }

    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(Integer hotelCount) {
        this.hotelCount = hotelCount;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}