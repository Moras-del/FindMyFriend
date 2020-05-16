package pl.moras.tracker;

import org.junit.jupiter.api.Test;
import pl.moras.fakes.MongoDaoTestImpl;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.MongoDao;
import pl.moras.tracker.services.FriendsService;
import pl.moras.tracker.services.IFriendsService;
import reactor.test.StepVerifier;

class FriendServiceTests {

    private MongoDao mongoDao = new MongoDaoTestImpl();

    private IFriendsService friendsService = new FriendsService(mongoDao);

    @Test
    void should_send_request(){
        StepVerifier
                .create(friendsService.sendFriendRequest("principal", "other"))
                .expectNextMatches(responseEntity -> responseEntity.getStatusCodeValue() == 200)
                .verifyComplete();
    }

    @Test
    void should_send_request_to_self(){
        StepVerifier
                .create(friendsService.sendFriendRequest("principal", "principal"))
                .expectErrorMessage("You can't add yourself")
                .verify();
    }

    @Test
    void should_accept_request(){
        StepVerifier
                .create(friendsService.acceptRequest("principal", "other"))
                .expectNextMatches(User::hasAnyFriends);
    }

    @Test
    void should_cancel_request(){
        StepVerifier
                .create(friendsService.cancelRequest("principal", "other"))
                .expectNextMatches(user -> !user.hasAnyFriendRequest());
    }

    @Test
    void should_delete_friend(){
        StepVerifier
                .create(friendsService.deleteFriend("principal", "other"))
                .expectNextMatches(user -> !user.hasAnyFriends());
    }
}
