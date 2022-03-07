package com.bzavoevanyy.crm.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("address")
public class Address {
    private final String street;
}