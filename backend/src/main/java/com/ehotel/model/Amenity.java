package com.ehotel.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "amenity")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Long amenityId;

    @Column(name = "amenity_name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private Set<Room> rooms = new HashSet<>();

    public Amenity() {
    }

    public Amenity(String name) {
        this.name = name;
    }

    public Long getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Long amenityId) {
        this.amenityId = amenityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amenity amenity)) return false;
        return Objects.equals(amenityId, amenity.amenityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amenityId);
    }
}