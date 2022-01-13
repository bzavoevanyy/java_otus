package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long address_id;
    @Column(name = "street")
    private String street;

    public Address() {
    }

    public Address(String street) {
        this.address_id = null;
        this.street = street;
    }

    public Address(Long address_id, String street) {
        this.address_id = address_id;
        this.street = street;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long id) {
        this.address_id = id;
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
                "id=" + address_id +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public Address clone() {
        return new Address(this.address_id, this.street);
    }
}
