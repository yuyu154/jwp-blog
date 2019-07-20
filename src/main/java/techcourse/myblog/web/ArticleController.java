package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) == null) {
            return "redirect:/login";
        }
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id).get();
        ArticleDto articleDto = new ArticleDto(article.getId(), article.getTitle(), article.getCoverUrl(), article.getContents());
        model.addAttribute("article", articleDto);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id).get();
        ArticleDto articleDto = new ArticleDto(article.getId(), article.getTitle(), article.getCoverUrl(), article.getContents());
        model.addAttribute("article", articleDto);
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto) {
        Article persistArticle = articleRepository.save(articleDto.toEntity());
        return "redirect:/articles/" + persistArticle.getId();
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto) {
        Article article = articleRepository.findById(id).get();
        article.updateArticle(articleDto);
        articleRepository.save(article);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id) {
        articleRepository.deleteById(id);
        return "redirect:/";
    }
}
