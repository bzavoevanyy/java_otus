package com.bzavoevanyy.controller;

import com.bzavoevanyy.controller.dto.ClientDto;
import com.bzavoevanyy.controller.dto.PhoneDto;
import com.bzavoevanyy.crm.exception.ClientNotFoundException;
import com.bzavoevanyy.crm.service.DBServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final DBServiceClient dbServiceClient;

    @GetMapping("/client")
    public String getAllClients(Model model) {
        final var clients = dbServiceClient.findAll().stream()
                .map(ClientDto::toDto).sorted(Comparator.comparing(ClientDto::getId)).collect(Collectors.toList());
        model.addAttribute("clients", clients);
        return "client";
    }

    @GetMapping(value = {"/client/edit/{id}", "/client/edit"})
    public String getEditFrom(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            final var clientDto = dbServiceClient.getClient(id).map(ClientDto::toDto)
                    .orElseThrow(ClientNotFoundException::new);
            model.addAttribute("client", clientDto);
        }
        return "edit-client";
    }

    @PostMapping("/client/edit")
    public String saveClient(ClientDto clientDto, @RequestParam MultiValueMap<String, String> params) {
        if (clientDto.getId() == null) {
            final var phoneDtos = params.get("number")
                    .stream().filter(number -> !number.isBlank())
                    .map(number -> new PhoneDto(null, number)).toList();
            clientDto.setPhones(phoneDtos);

        } else {
            final var phoneIds = params.get("phoneId").stream().map(Long::parseLong).toList();
            final var numbers = params.get("number");
            final var phoneDtos = IntStream.range(0, numbers.size()).boxed()
                    .map(i -> {
                        Long phoneId = null;
                        if (phoneIds.size() > i) {
                            phoneId = phoneIds.get(i);
                        }
                        return new PhoneDto(phoneId, numbers.get(i));
                    })
                    .filter(phone -> !phone.getNumber().isBlank()).toList();
            clientDto.setPhones(phoneDtos);
        }
        final var client = ClientDto.toDomainObject(clientDto);
        dbServiceClient.saveClient(client);
        return "redirect:/client";
    }

    @PostMapping("/client/delete/{id}")
    public String delete(@PathVariable Long id) {
        dbServiceClient.delete(id);
        return "redirect:/client";
    }
}
