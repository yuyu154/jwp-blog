package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.ArticleDto;
import techcourse.myblog.domain.ArticleRepository;
=======
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
>>>>>>> yk1028

import java.util.List;

@Controller
public class ArticleController {
<<<<<<< HEAD

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ArticleDto> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView addArticle(final ArticleDto articleParam) {
        long latestId = articleRepository.add(articleParam);
        return new RedirectView("/articles/" + latestId);
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable final long articleId, final Model model) {
        ArticleDto article = articleRepository.findById(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable final long articleId, final ArticleDto articleParam) {
        long updateId = articleRepository.updateById(articleParam, articleId);
        return new RedirectView("/articles/" + updateId);
    }

    @DeleteMapping("articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable final long articleId) {
        articleRepository.deleteById(articleId);
        return new RedirectView("/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable final long articleId, final Model model) {
        ArticleDto article = articleRepository.findById(articleId);
        model.addAttribute("article", article);
        return "article-edit";
=======
    private static final String LOGGED_IN_USER = "loggedInUser";

    private ArticleService articleService;
    private UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage(HttpServletRequest httpServletRequest) {
        getLoggedInUser(httpServletRequest);
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleDto = articleService.findById(id);
        model.addAttribute("article", articleDto);

        UserPublicInfoDto userPublicInfoDto = userService.findById(articleDto.getUserId());
        model.addAttribute("articleUser", userPublicInfoDto);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest) {
        ArticleDto articleDto = articleService.findById(id);
        if (getLoggedInUser(httpServletRequest).getId().equals(articleDto.getUserId())) {
            model.addAttribute("article", articleDto);
            return "article-edit";
        }
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        articleDto.setUserId(getLoggedInUser(httpServletRequest).getId());
        ArticleDto savedArticleDto = articleService.save(articleDto);
        return "redirect:/articles/" + savedArticleDto.getId();
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        articleService.update(id, getLoggedInUser(httpServletRequest).getId(), articleDto);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id, HttpServletRequest httpServletRequest) {
        articleService.delete(id, getLoggedInUser(httpServletRequest).getId());
        return "redirect:/";
    }

    private UserPublicInfoDto getLoggedInUser(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserPublicInfoDto user = (UserPublicInfoDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return user;
>>>>>>> yk1028
    }
}
