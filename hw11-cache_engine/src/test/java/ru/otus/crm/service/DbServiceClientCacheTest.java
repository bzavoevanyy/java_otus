package ru.otus.crm.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Client;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@DisplayName("Класс DbServiceClient")
public class DbServiceClientCacheTest {
    private DataTemplateHibernate<Client> clientDataTemplate;
    private Session session;

    private DbServiceClientImpl dbServiceClient;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        this.session = Mockito.mock(Session.class);
        Transaction transaction = Mockito.mock(Transaction.class);
        TransactionManagerHibernate transactionManager = new TransactionManagerHibernate(sessionFactory);
        this.clientDataTemplate = (DataTemplateHibernate<Client>) Mockito.mock(DataTemplateHibernate.class);
        HwCache<String, Client> hwCache = new MyCache<>();
        this.dbServiceClient = new DbServiceClientImpl(transactionManager, clientDataTemplate, hwCache);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doNothing().when(session).setDefaultReadOnly(anyBoolean());
        doNothing().when(transaction).commit();

    }

    @Test
    @DisplayName(" должен использоваться кэш")
    void shouldUseCacheEngine() {
        for (long i = 0; i < 10; i++) {
            when(clientDataTemplate.findById(session, i))
                    .thenReturn(Optional.of(new Client(i, "client", null, null)));
            dbServiceClient.getClient(i);
        }
        verify(clientDataTemplate, times(10)).findById(any(), anyLong());

        for (long i = 0; i < 10; i++) {
            when(clientDataTemplate.findById(session, i))
                    .thenReturn(Optional.of(new Client(i, "client", null, null)));
            dbServiceClient.getClient(i);
        }

        verify(clientDataTemplate, times(10)).findById(any(), anyLong());

        for (long i = 0; i < 20; i++) {
            when(clientDataTemplate.findById(session, i))
                    .thenReturn(Optional.of(new Client(i, "client", null, null)));
            dbServiceClient.getClient(i);
        }

        verify(clientDataTemplate, times(20)).findById(any(), anyLong());
    }

    @Test
    @DisplayName(" кэш должен обнуляться при нехватке памяти")
    void shouldEraseCache() {
        for (long i = 0; i < 10; i++) {
            when(clientDataTemplate.findById(session, i))
                    .thenReturn(Optional.of(new Client(i, "client", null, null)));
            dbServiceClient.getClient(i);
        }
        verify(clientDataTemplate, times(10)).findById(any(), anyLong());

        System.gc();

        for (long i = 0; i < 10; i++) {
            when(clientDataTemplate.findById(session, i))
                    .thenReturn(Optional.of(new Client(i, "client", null, null)));
            dbServiceClient.getClient(i);
        }
        verify(clientDataTemplate, times(20)).findById(any(), anyLong());
    }
}
