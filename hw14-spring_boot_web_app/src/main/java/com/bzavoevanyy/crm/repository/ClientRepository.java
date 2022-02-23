package com.bzavoevanyy.crm.repository;

import com.bzavoevanyy.crm.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Override
    List<Client> findAll();
}
