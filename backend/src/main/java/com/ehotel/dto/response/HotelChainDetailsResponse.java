package com.ehotel.dto.response;

import java.util.ArrayList;
import java.util.List;

public class HotelChainDetailsResponse {

    private Long chainId;
    private String name;
    private Integer hotelCount;
    private String address;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private List<String> emails = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    private List<HotelSummaryResponse> hotels = new ArrayList<>();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<HotelSummaryResponse> getHotels() {
        return hotels;
    }

    public void setHotels(List<HotelSummaryResponse> hotels) {
        this.hotels = hotels;
    }
}