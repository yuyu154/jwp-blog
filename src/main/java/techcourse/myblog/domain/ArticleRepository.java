package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private long latestId = 0;
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public Long add(final Article article) {
        article.setId(latestId());
        articles.add(article);
        return latestId;
    }

    private Long latestId() {
        latestId = latestId + 1;
        return latestId;
    }

    public Article findById(final Long latestId) {
        return articles.stream()
                .filter(article -> article.getId() == latestId)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
