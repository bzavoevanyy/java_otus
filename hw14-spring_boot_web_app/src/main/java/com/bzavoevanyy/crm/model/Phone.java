package com.bzavoevanyy.crm.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

@Table("phone")
@Data
public class Phone {
    @Id
    @Nullable
    private final Long phoneId;

    private final String number;

    private final Long clientId;

    public Phone(String number, Long clientId) {
        this(null, number, clientId);
    }

    @PersistenceConstructor
    public Phone(@Nullable Long phoneId, String number, Long clientId) {
        this.phoneId = phoneId;
        this.number = number;
        this.clientId = clientId;
    }
}