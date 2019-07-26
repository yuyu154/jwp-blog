package techcourse.myblog.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import techcourse.myblog.domain.article.Article;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest1 {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findById() {
        User user = new User("이름", "test@mail.com", "123qweASD!");
        User persistUser = testEntityManager.persist(user);

        Article article = new Article("title", "https://contents.com", "conetns");
        article.setAuthor(persistUser);
        testEntityManager.persist(article);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(1);
    }

    @Test
    public void findById2() {
        Article article = new Article("title", "https://contents.com", "conetns");
        Article persistArticle = testEntityManager.persist(article);

        User user = new User("이름", "test@mail.com", "123qweASD!");
        user.addArticle(persistArticle);    // user가 종속적이기 때문에 반영 X
        User persistUser = testEntityManager.persist(user);

        testEntityManager.flush();
        testEntityManager.clear();

        User actualUser = userRepository.findById(persistUser.getId()).get();

        assertThat(actualUser.getArticles().size()).isEqualTo(0);
    }
}