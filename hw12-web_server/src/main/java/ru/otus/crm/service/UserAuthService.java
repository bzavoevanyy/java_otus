package ru.otus.crm.service;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
