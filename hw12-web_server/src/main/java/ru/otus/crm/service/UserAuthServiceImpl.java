package ru.otus.crm.service;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.User;


public class UserAuthServiceImpl implements UserAuthService {

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public UserAuthServiceImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }


    @Override
    public boolean authenticate(String login, String password) {
        return transactionManager
                .doInReadOnlyTransaction(session -> userDataTemplate
                        .findByEntityField(session, "login", login)).stream().findFirst()
                .map(user -> user.getPassword().equals(password)).orElse(false);
    }
}
