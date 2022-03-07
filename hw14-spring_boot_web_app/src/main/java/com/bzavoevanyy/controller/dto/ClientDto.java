package com.bzavoevanyy.controller.dto;

import com.bzavoevanyy.crm.model.Address;
import com.bzavoevanyy.crm.model.Client;
import com.bzavoevanyy.crm.model.Phone;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ClientDto {
    private final Long id;
    private final String name;
    private final String street;
    private List<PhoneDto> phones;

    public static ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .street(client.getAddress().getStreet())
                .phones(client.getPhones().stream().map(PhoneDto::toDto).toList())
                .build();
    }

    public static Client toDomainObject(ClientDto clientDto) {
        return Client.builder()
                .id(clientDto.getId())
                .name(clientDto.getName())
                .address(new Address(clientDto.getStreet()))
                .phones(clientDto.getPhones().stream()
                        .map(phoneDto -> new Phone(phoneDto.getPhoneId(), phoneDto.getNumber(), clientDto.getId()))
                        .collect(Collectors.toSet())).build();
    }
}
