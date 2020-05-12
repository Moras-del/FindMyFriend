package pl.moras.tracker;


import io.netty.handler.codec.base64.Base64Encoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import pl.moras.fakes.PasswordEncoderTestImpl;
import pl.moras.fakes.UserRepoTestImpl;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.repo.UserRepo;
import pl.moras.tracker.services.AuthService;
import pl.moras.tracker.services.IAuthService;
import pl.moras.tracker.services.ITrackingService;
import pl.moras.tracker.services.TrackingService;

import java.beans.Encoder;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WithMockUser(username = "moras", password = "haslo")
public class SocketTests {

    @Mock
    Principal principal;

    WebSocketStompClient socketStompClient = new WebSocketStompClient(new StandardWebSocketClient());

    IAuthService authService = new AuthService(new UserRepoTestImpl(), new PasswordEncoderTestImpl());
    ITrackingService trackingService = new TrackingService(new UserRepoTestImpl());

    @BeforeEach
    void setup(){
        initMocks(this);
        when(principal.getName()).thenReturn("user");
    }

    @Test
    void hello() throws ExecutionException, InterruptedException {
        Authentication credentials = new UsernamePasswordAuthenticationToken("moras", "haslo");
        SecurityContextHolder.getContext().setAuthentication(credentials);
        socketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession stompSession = socketStompClient.connect("ws://localhost:8080/track", getHeaders(), new MyHandler()).get();
        Thread.sleep(1000);
    }

    private WebSocketHttpHeaders getHeaders(){
        WebSocketHttpHeaders webSocketHttpHeaders =new WebSocketHttpHeaders();
        String s = Base64.getEncoder().encodeToString("moras:haslo".getBytes());
        webSocketHttpHeaders.add("Authorization", "Basic "+s);
        return webSocketHttpHeaders;
    }


    class MyHandler extends StompSessionHandlerAdapter{


        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println(payload);
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/user/queue/reply", this);
            LocationDto locationDto = new LocationDto();
            locationDto.setLatitude(40);
            locationDto.setLongitude(20);
            session.send("/app/track", locationDto);
        }
    }

}
