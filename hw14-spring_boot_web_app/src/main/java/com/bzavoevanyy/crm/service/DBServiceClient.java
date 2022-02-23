package com.bzavoevanyy.crm.service;

import com.bzavoevanyy.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    void delete(long id);
}
