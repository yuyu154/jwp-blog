package techcourse.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTest {
    private static final String NAME = "yusi";
    private static final String EMAIL = "temp@mail.com";
    private static final String PASSWORD = "12345abc";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        userService.deleteAll();
    }

    @Test
    @DisplayName("정상_회원가입_테스트")
    public void addUserTest() {
        User user = new User(NAME, EMAIL, PASSWORD);
        save(user);
    }

    private void save(User user) {
        save(user, response -> {
            String uri = response.getResponseHeaders().get("Location").get(0);
            assertTrue(uri.contains("/login"));
        });
    }

    private void save(final User user, final Consumer<EntityExchangeResult<byte[]>> consumer) {
        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", user.getName())
                        .with("email", user.getEmail())
                        .with("password", user.getPassword()))
                .exchange()
                .expectStatus().isFound()
                .expectBody()
                .consumeWith(consumer);
    }

    @Test
    public void 이메일_중복일때_가입_테스트() {
        User user = new User(NAME, EMAIL, PASSWORD);
        save(user);

        User other = new User("other", EMAIL, PASSWORD);

        webTestClient.post().uri("/users")
                .body(BodyInserters
                        .fromFormData("name", other.getName())
                        .with("email", other.getEmail())
                        .with("password", other.getPassword()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void 회원_목록_조회() {
        User user = new User(NAME, EMAIL, PASSWORD);
        save(user);

        final int count = 1;
        String element = "class=\"card-body\"";

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertEquals(count, StringUtils.countOccurrencesOf(body, element));
                });
    }

    @Test
    public void 마이페이지의_수정_버튼클릭시_수정페이지_이동() {
        User user = new User(NAME, EMAIL, PASSWORD);
        save(user);

        UserRequestDto.LoginRequestDto loginRequestDto = new UserRequestDto.LoginRequestDto(EMAIL, PASSWORD);
        User check = loginService.findByLoginRequestDto(loginRequestDto);

        webTestClient.get().uri("/users/edit/" + check.getId())
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    public void 회원_탈퇴() {
        User user = new User(NAME, EMAIL, PASSWORD);
        save(user);
        webTestClient.delete().uri("/users/{email}", EMAIL)
                .exchange()
                .expectStatus().isFound();
    }
}