package pl.moras.tracker;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


@Component
public class MySocketHandler implements WebSocketHandler {


    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession
                .send(
                        webSocketSession.receive()
                                .map(webSocketMessage -> webSocketMessage.getPayloadAsText() + Thread.currentThread().getName())
                                .flatMap(message -> Mono.just(webSocketSession.textMessage(message)))
                );
    }
}
