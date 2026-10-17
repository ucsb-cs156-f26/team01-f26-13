package edu.ucsb.cs156.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ucsb.cs156.example.entities.Recommendation_Request;
import edu.ucsb.cs156.example.repositories.RecommendationRequestRepository;
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

/** This is a REST controller for Recommendation Request */
@Tag(name = "RecommendationRequest")
@RequestMapping("/api/recommendationRequest")
@RestController
@Slf4j
public class RecommendationRequestController extends ApiController {

  @Autowired RecommendationRequestRepository recommendationRequestRepository;

  /**
   * List all UCSB dates
   *
   * @return an iterable of UCSBDate
   */
  @Operation(summary = "List all Recommendation Requests")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/all")
  public Iterable<Recommendation_Request> allRecommendationRequests() {
    Iterable<Recommendation_Request> recommendationRequests =
        recommendationRequestRepository.findAll();
    return recommendationRequests;
  }

  /**
   * Create a new Recommendation Request
   *
   * @param requesterEmail; requester's email
   * @param professorEmail; professor's email
   * @param explanation; explaination for request
   * @param dateRequested; date requested for
   * @param dateNeeded; date needed by
   * @param done; is it done?
   * @return the saved recommendation request
   */
  @Operation(summary = "Create a new recommendation request")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/post")
  public Recommendation_Request postRecommendation_Request(
      @Parameter(name = "requesterEmail") @RequestParam String requesterEmail,
      @Parameter(name = "professorEmail") @RequestParam String professorEmail,
      @Parameter(name = "explaination") @RequestParam String explaination,
      @Parameter(
              name = "dateRequested",
              description =
                  "date (in iso format, e.g. YYYY-mm-ddTHH:MM:SS; see https://en.wikipedia.org/wiki/ISO_8601)")
          @RequestParam("dateRequested")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime dateRequested,
      @Parameter(
              name = "dateNeeded",
              description =
                  "date (in iso format, e.g. YYYY-mm-ddTHH:MM:SS; see https://en.wikipedia.org/wiki/ISO_8601)")
          @RequestParam("dateNeeded")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime dateNeeded,
      @Parameter(name = "done") @RequestParam boolean done)
      throws JsonProcessingException {

    // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    // See: https://www.baeldung.com/spring-date-parameters

    log.info("dateRequested={}", dateRequested);
    log.info("dataNeeded={}", dateNeeded);

    Recommendation_Request recommendation_Request = new Recommendation_Request();
    recommendation_Request.setRequesterEmail(requesterEmail);
    recommendation_Request.setProfessorEmail(professorEmail);
    recommendation_Request.setExplanation(explaination);
    recommendation_Request.setDateRequested(dateRequested);
    recommendation_Request.setDateNeeded(dateNeeded);
    recommendation_Request.setDone(done);

    Recommendation_Request savedRecommendation_Request =
        recommendationRequestRepository.save(recommendation_Request);

    return savedRecommendation_Request;
  }

  /**
   * Delete a UCSBDate
   *
   * @param id the id of the date to delete
   * @return a message indicating the date was deleted @Operation(summary = "Delete a
   *     UCSBDate") @PreAuthorize("hasRole('ROLE_ADMIN')") @DeleteMapping("") public Object
   *     deleteUCSBDate(@Parameter(name = "id") @RequestParam Long id) { UCSBDate ucsbDate =
   *     ucsbDateRepository .findById(id) .orElseThrow(() -> new
   *     EntityNotFoundException(UCSBDate.class, id));
   *     <p>ucsbDateRepository.delete(ucsbDate); return genericMessage("UCSBDate with id %s
   *     deleted".formatted(id)); }
   */
}
