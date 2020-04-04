package pl.moras.tracker;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import org.junit.Before;
import org.junit.experimental.results.ResultMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.moras.controllers.AuthController;
import pl.moras.model.User;
import pl.moras.model.UserDto;
import pl.moras.repo.UserRepo;
import pl.moras.services.IAuthService;

import java.io.ObjectOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
        when(authService.addUser(anyString(), anyString())).thenAnswer(arg->{
            User user = new User();
            user.setName(arg.getArgument(0));
            user.setPassword(arg.getArgument(1));
            return user;
        });
        when(authService.getUser(anyString())).thenAnswer(arg->{
            User user = new User();
            user.setName(arg.getArgument(0));
            user.setPassword("haslo");
            return user;
        });
    }


    @Test
    void should_register() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("user");
        userDto.setPassword("haslo");

        String requestBody = objectMapper.writeValueAsString(userDto);
        String responseBody = objectMapper.writeValueAsString(getUser("user"));

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    @WithMockUser(username = "principal")
    void should_login() throws Exception {
        String response = objectMapper.writeValueAsString(getUser("principal"));
        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    User getUser(String name){
        User user = new User();
        user.setName(name);
        user.setPassword("haslo");
        return user;
    }

}
