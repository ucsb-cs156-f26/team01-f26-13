package edu.ucsb.cs156.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ucsb.cs156.example.entities.MenuItemReview;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.MenuItemReviewRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This is a REST controller for MenuItemReview */
@Tag(name = "MenuItemReview")
@RequestMapping("/api/MenuItemReview")
@RestController
@Slf4j
public class MenuItemReviewController extends ApiController {

  @Autowired MenuItemReviewRepository menuItemReviewRepository;

  // list all menu item reviews @return an iterable of MenuItemReview
  @Operation(summary = "List all menu item reviews")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/all")
  public Iterable<MenuItemReview> allMenuItemReviews() {
    Iterable<MenuItemReview> menuItemReviews = menuItemReviewRepository.findAll();
    return menuItemReviews;
  }

  /**
   * Create a new menu item review
   *
   * @param itemId items id
   * @param reviewerEmail the email of the reviewer
   * @param stars rating stars
   * @param dateReviewed the date
   * @param comments the comments
   * @return the saved menu item review
   */
  @Operation(summary = "Create a new menu item review")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/post")
  public MenuItemReview postMenuItemReview(
      @Parameter(name = "itemId") @RequestParam Long itemId,
      @Parameter(name = "reviewerEmail") @RequestParam String reviewerEmail,
      @Parameter(name = "stars") @RequestParam Integer stars,
      @Parameter(
              name = "dateReviewed",
              description =
                  "date (in iso format, e.g. YYYY-mm-ddTHH:MM:SS; see https://en.wikipedia.org/wiki/ISO_8601)")
          @RequestParam("dateReviewed")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime dateReviewed,
      @Parameter(name = "comments") @RequestParam String comments)
      throws JsonProcessingException {

    // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    // See: https://www.baeldung.com/spring-date-parameters

    log.info("dateReviewed={}", dateReviewed);

    MenuItemReview review = new MenuItemReview();
    review.setItemId(itemId);
    review.setComments(comments);
    review.setReviewerEmail(reviewerEmail);
    review.setStars(stars);
    review.setDateReviewed(dateReviewed);

    MenuItemReview savedMenuItemReview = menuItemReviewRepository.save(review);

    return savedMenuItemReview;
  }

  /**
   * Get a single review by id
   *
   * @param id the id of the review
   * @return a menuitemreview
   */
  @Operation(summary = "Get a single review")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("")
  public MenuItemReview getById(@Parameter(name = "id") @RequestParam Long id) {
    MenuItemReview menuItemReview =
        menuItemReviewRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(MenuItemReview.class, id));

    return menuItemReview;
  }
}
