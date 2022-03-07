package com.bzavoevanyy.crm.service;

import com.bzavoevanyy.crm.model.Address;
import com.bzavoevanyy.crm.model.Client;
import com.bzavoevanyy.crm.model.Phone;
import com.bzavoevanyy.crm.repository.ClientRepository;
import com.bzavoevanyy.sessionmanager.TransactionManager;
import com.bzavoevanyy.sessionmanager.TransactionManagerSpring;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DbServiceClient test")
class DbServiceClientImplTest {

    private final TransactionManager transactionManager = new TransactionManagerSpring();

    private final ClientRepository clientRepository = mock(ClientRepository.class);

    private final DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionManager, clientRepository);

    private final static Phone TEST_PHONE = new Phone(1L, "number", 1L);
    private final static Address TEST_ADDRESS = new Address("test address");
    private final static Client TEST_CLIENT =
            Client.builder()
                    .id(1L)
                    .name("test client")
                    .address(TEST_ADDRESS)
                    .phones(Set.of(TEST_PHONE)).build();

    @Test
    @DisplayName(" should save client and return saved client")
    void should_save_client_and_return_saved() {
        when(clientRepository.save(any())).thenReturn(TEST_CLIENT);
        final var savedClient = dbServiceClient.saveClient(TEST_CLIENT);
        assertThat(savedClient).isNotNull().usingRecursiveComparison().isEqualTo(TEST_CLIENT);
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    @DisplayName(" should return client by id")
    void should_return_client_by_id() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(TEST_CLIENT));
        final var client = dbServiceClient.getClient(1L);
        assertThat(client).get().usingRecursiveComparison().isEqualTo(TEST_CLIENT);
        verify(clientRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName(" should return list of clients")
    void should_return_list_of_clients() {
        when(clientRepository.findAll()).thenReturn(List.of(TEST_CLIENT));
        final var clients = dbServiceClient.findAll();
        assertThat(clients).hasSize(1).first().usingRecursiveComparison().isEqualTo(TEST_CLIENT);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName(" should delete client")
    void should_delete_client() {
        doNothing().when(clientRepository).deleteById(anyLong());
        dbServiceClient.delete(1L);
        verify(clientRepository, times(1)).deleteById(anyLong());
    }
}