package pl.moras.tracker;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import pl.moras.tracker.controllers.AuthController;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;
import pl.moras.tracker.services.IAuthService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebFluxTest(AuthController.class)
class AuthControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IAuthService authService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
//        when(authService.addUser(anyString(), anyString())).thenAnswer(arg->{
//            User user = new User();
//            user.setName(arg.getArgument(0));
//            user.setPassword(arg.getArgument(1));
//            return user;
//        });
//        when(authService.getUser(anyString())).thenAnswer(arg->{
//            User user = new User();
//            user.setName(arg.getArgument(0));
//            user.setPassword("haslo");
//            return user;
//        });
    }


    @Test
    void should_register() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("user");
        userDto.setPassword("haslo");

        String requestBody = objectMapper.writeValueAsString(userDto);
        String responseBody = objectMapper.writeValueAsString(getUser("user"));

        webTestClient.post()
                .uri("/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(responseBody);
    }

//    @Test
//    @WithMockUser(username = "principal")
//    void should_authenticate() throws Exception {
//        String response = objectMapper.writeValueAsString(getUser("principal"));
//        mockMvc.perform(get("/auth"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(response));
//    }
//
//    @Test
//    @WithAnonymousUser
//    void should_fail_authenticate() throws Exception {
//        mockMvc.perform(get("/auth"))
//                .andExpect(status().isUnauthorized());
//    }

    User getUser(String name){
        User user = new User();
        user.setName(name);
        user.setPassword("haslo");
        return user;
    }

}
