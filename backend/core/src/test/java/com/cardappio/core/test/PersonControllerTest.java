package com.cardappio.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @InjectMocks
    private PersonController controller;

    @Mock
    private PersonService service;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String RESOURCE = "/people";

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        controller.setService(service);
    }

    @Test
    @SneakyThrows
    void findAll() {

        mockMvc.perform(get(RESOURCE + "?size=100"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAll(Pageable.ofSize(100));
        verifyNoMoreInteractions(service);
    }

    @Test
    @SneakyThrows
    void findAllDTO() {

        mockMvc.perform(get(RESOURCE + "/dto?size=100"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllDTO(Pageable.ofSize(100));
        verifyNoMoreInteractions(service);
    }

    @Test
    @SneakyThrows
    void findAllRSQL() {

        mockMvc.perform(get(RESOURCE + "?size=100&search=nome='ricardo';id=27"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllRSQL("nome='ricardo';id=27", Pageable.ofSize(100));
        verifyNoMoreInteractions(service);
    }

    @Test
    @SneakyThrows
    void findAllRSQLDTO() {

        mockMvc.perform(get(RESOURCE + "/dto?size=100&search=nome='ricardo';id=27"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllRSQLDTO("nome='ricardo';id=27", Pageable.ofSize(100));
        verifyNoMoreInteractions(service);
    }

    @Test
    @SneakyThrows
    void findById() {

        mockMvc.perform(get(RESOURCE + "/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).findById(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    @SneakyThrows
    void create() {

        CreateDTO dto = new CreateDTO(1L, "Jean");

        mockMvc.perform(post(RESOURCE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).create(dto);
        verifyNoMoreInteractions(service);
    }

    @Test
    @SneakyThrows
    void update() {

        CreateDTO dto = new CreateDTO(1L, "Jean");

        mockMvc.perform(put(RESOURCE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service, times(1)).update(1L, dto);
        verifyNoMoreInteractions(service);

    }

    @Test
    @SneakyThrows
    void delete() {

        mockMvc.perform(MockMvcRequestBuilders.delete(RESOURCE + "/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
        verifyNoMoreInteractions(service);

    }

}
