package pl.moras.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.moras.tracker.model.User;
import pl.moras.tracker.services.IFriendsService;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "principal")
class FriendsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFriendsService friendsService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();

    }

    @Test
    void should_fail_send_request() throws Exception {
        mockMvc.perform(post("/friends/request"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_send_request() throws Exception {
        when(friendsService.sendFriendRequest(anyString(), anyString())).thenReturn(Mono.just(ResponseEntity.ok().build()));

        mockMvc.perform(post("/friends/request")
                .param("friendName", "friend"))
                .andExpect(status().isOk());
    }

    @Test
    void should_accept_friend_request() throws Exception {
        when(friendsService.acceptRequest(anyString(), anyString())).thenAnswer(arg->{
            User user = getUser(arg.getArgument(0));
            user.addFriend(getUser(arg.getArgument(1)));
            return user;
        });

        User user = getUser("principal");
        user.addFriend(getUser("friend"));
        String response = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/friends/accept")
                .param("friendName", "friend"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void should_cancel_friend_request() throws Exception {
        when(friendsService.cancelRequest(anyString(), anyString())).thenReturn(Mono.just(getUser("user")));
        String response = objectMapper.writeValueAsString(getUser("user"));

        mockMvc.perform(post("/friends/cancel")
                        .param("friendName", "friend"))
                        .andExpect(status().isOk())
                        .andExpect(content().json(response));
    }

    @Test
    void should_delete_friend() throws Exception {
        when(friendsService.deleteFriend(anyString(), anyString())).thenReturn(Mono.just(getUser("user")));
        String response = objectMapper.writeValueAsString(getUser("user"));

        mockMvc.perform(post("/friends/delete")
                        .param("friendName", "friend"))
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
