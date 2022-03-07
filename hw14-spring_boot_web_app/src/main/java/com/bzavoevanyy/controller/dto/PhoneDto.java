package com.bzavoevanyy.controller.dto;

import com.bzavoevanyy.crm.model.Phone;
import lombok.Data;

@Data
public class PhoneDto {
    private final Long phoneId;
    private final String number;

    public static PhoneDto toDto(Phone phone) {
        return new PhoneDto(phone.getPhoneId(), phone.getNumber());
    }
}
