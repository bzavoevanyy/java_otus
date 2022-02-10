package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> hwCache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate,
                               HwCache<String, Client> hwCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.hwCache = hwCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            // remove from cache
            hwCache.remove(String.valueOf(clientCloned.getId()));
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    // Cacheable
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var client = Optional.ofNullable(hwCache.get(String.valueOf(id)));
            if (client.isEmpty()) {
                client = clientDataTemplate.findById(session, id);
                hwCache.put(String.valueOf(id), client.orElseThrow(() -> new RuntimeException("Client not fount")));
            }
            log.info("client: {}", client);
            return client;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
