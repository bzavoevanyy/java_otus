package com.bzavoevanyy.controller;


import com.bzavoevanyy.controller.dto.ClientDto;
import com.bzavoevanyy.controller.dto.PhoneDto;
import com.bzavoevanyy.crm.model.Address;
import com.bzavoevanyy.crm.model.Client;
import com.bzavoevanyy.crm.model.Phone;
import com.bzavoevanyy.crm.service.DBServiceClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@WebMvcTest(ClientController.class)
@DisplayName("ClientController test")
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private DBServiceClient dbServiceClient;

    private final static Phone TEST_PHONE = new Phone(1L, "number", 1L);
    private final static Address TEST_ADDRESS = new Address("test address");
    private final static Client TEST_CLIENT =
            Client.builder()
                    .id(1L)
                    .name("test client")
                    .address(TEST_ADDRESS)
                    .phones(Set.of(TEST_PHONE)).build();

    @Test
    @DisplayName(" should return correct model and view for GET /client")
    void should_return_correct_model_and_view_get_all() throws Exception {
        given(dbServiceClient.findAll()).willReturn(List.of(TEST_CLIENT));
        mvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("clients", List.of(ClientDto.toDto(TEST_CLIENT))))
                .andExpect(view().name("client"));
    }

    @Test
    @DisplayName(" should return correct view for GET /client/edit")
    void should_return_correct_and_view_edit() throws Exception {
        mvc.perform(get("/client/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-client"));
    }

    @Test
    @DisplayName(" should return correct model and view for GET /client/edit/{id}")
    void should_return_correct_model_and_view_edit() throws Exception {
        given(dbServiceClient.getClient(anyLong())).willReturn(Optional.of(TEST_CLIENT));
        mvc.perform(get("/client/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("client", ClientDto.toDto(TEST_CLIENT)))
                .andExpect(view().name("edit-client"));
    }

    @Test
    @DisplayName(" should create client POST /client/edit/")
    void should_create_client() throws Exception {
        final var requestClient = ClientDto.builder()
                .name("test client")
                .street("test street")
                .phones(List.of(new PhoneDto(null, "number")))
                .build();
        mvc.perform(post("/client/edit")
                        .param("name", "test client")
                        .param("street", "test street")
                        .param("number", "number"))
                .andExpect(status().is3xxRedirection());
        verify(dbServiceClient, times(1)).saveClient(ClientDto.toDomainObject(requestClient));
    }

    @Test
    @DisplayName(" should delete client POST /client/delete/{id}")
    void should_delete_client_and_redirect() throws Exception {
        doNothing().when(dbServiceClient).delete(anyLong());
        mvc.perform(post("/client/delete/1"))
                .andExpect(status().is3xxRedirection());
        verify(dbServiceClient, times(1)).delete(1L);
    }
}