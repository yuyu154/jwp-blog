package techcourse.myblog.service;

<<<<<<< HEAD
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.article.Article;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    private static final String TITLE = "TEST";
    private static final String COVER_URL = "https://img.com";
    private static final String CONTENTS = "TEST_CONTENTS";

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    public void setUp() {
        articleService.deleteAll();
    }

    @Test
    @DisplayName("Article_추가")
    public void saveTest() {
        Article expected = new Article(TITLE, COVER_URL, CONTENTS);
        Article actual = articleService.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Article_ID_조회")
    public void findByIdTest() {
        Article expected = articleService.save(new Article(TITLE, COVER_URL, CONTENTS));
        Article actual = articleService.findById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void updateByIdTest() {
        Article expected = articleService.save(new Article(TITLE, COVER_URL, CONTENTS));
        expected.setTitle("MODIFY");
        expected.setContents("CHANGE");

        long actualId = articleService.update(expected.getId(), expected);
        Article actual = articleService.findById(actualId);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteByIdTest() {
        Article article = articleService.save(new Article(TITLE, COVER_URL, CONTENTS));
        articleService.deleteById(article.getId());

        assertThrows(NoSuchElementException.class, () -> {
            articleService.findById(article.getId());
        });
    }

    @Test
    @DisplayName("Article_목록_조회")
    public void findAllTest() {
        Article article1 = new Article(TITLE, COVER_URL, CONTENTS);
        Article article2 = new Article(TITLE + 1, COVER_URL + "1", CONTENTS + 1);
        articleService.save(article1);
        articleService.save(article2);

        Iterable<Article> actual = articleService.findAll();
        assertThat(actual, Matchers.contains(article1, article2));
    }

}
=======
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.service.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    @Autowired
    ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService.save(new ArticleDto(1L, 1L, "title", "coverUrl", "contents"));
    }

    @Test
    void Article_userId와_수정하려는_User의_Id가_다르면_수정_실패() {
        ArticleDto articleDto = new ArticleDto(1L, 1L, "title1", "coverUrl1", "contents1");

        articleService.update(1L, 2L, articleDto);
        ArticleDto updateFailArticle = articleService.findById(1L);

        assertThat(updateFailArticle.getTitle()).isEqualTo("title");
        assertThat(updateFailArticle.getCoverUrl()).isEqualTo("coverUrl");
        assertThat(updateFailArticle.getContents()).isEqualTo("contents");

    }

    @Test
    void Article_userId와_삭제하려는_User의_Id가_다르면_삭제_실패() {
        articleService.delete(1L, 2L);
        ArticleDto deleteFailArticle = articleService.findById(1L);

        assertThat(deleteFailArticle.getTitle()).isEqualTo("title");
        assertThat(deleteFailArticle.getCoverUrl()).isEqualTo("coverUrl");
        assertThat(deleteFailArticle.getContents()).isEqualTo("contents");
    }
}
>>>>>>> yk1028
