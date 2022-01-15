package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;
    @Column(name = "street")
    private String street;

    public Address() {
    }

    public Address(String street) {
        this.addressId = null;
        this.street = street;
    }

    public Address(Long addressId, String street) {
        this.addressId = addressId;
        this.street = street;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long id) {
        this.addressId = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + addressId +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public Address clone() {
        return new Address(this.addressId, this.street);
    }
}
