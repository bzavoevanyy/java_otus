package ru.otus.crm.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private Long phone_id;
    @Column(name = "number")
    private String number;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Phone() {
    }

    public Phone(Long phone_id, String number) {
        this.phone_id = phone_id;
        this.number = number;
    }

    public Phone(Long phone_id, String number, Client client) {
        this.phone_id = phone_id;
        this.number = number;
        this.client = client;
    }

    public Long getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(Long phone_id) {
        this.phone_id = phone_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "client_id=" + phone_id +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public Phone clone() {
        return new Phone(this.phone_id, this.number, this.client);
    }
}
