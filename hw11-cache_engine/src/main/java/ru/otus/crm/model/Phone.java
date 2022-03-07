package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private Long phoneId;
    @Column(name = "number")
    private String number;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Phone() {
    }

    public Phone(Long phoneId, String number) {
        this.phoneId = phoneId;
        this.number = number;
    }

    public Phone(Long phoneId, String number, Client client) {
        this.phoneId = phoneId;
        this.number = number;
        this.client = client;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phone_id) {
        this.phoneId = phone_id;
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
                "phoneId=" + phoneId +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public Phone clone() {
        return new Phone(this.phoneId, this.number, this.client);
    }
}
