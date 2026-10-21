package edu.ucsb.cs156.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ucsb.cs156.example.entities.UCSBOrganization;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBOrganizationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This is a REST controller for UCSBDates */
@Tag(name = "UCSBOrganization")
@RequestMapping("/api/ucsborganizations")
@RestController
@Slf4j
public class UCSBOrganizationController extends ApiController {

  @Autowired UCSBOrganizationRepository ucsbOrganizationRepository;

  /**
   * List all UCSB organizations
   *
   * @return an iterable of UCSBOrganization
   */
  @Operation(summary = "List all ucsb organizations")
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/all")
  public Iterable<UCSBOrganization> allUCSBOrganizations() {
    Iterable<UCSBOrganization> organizations = ucsbOrganizationRepository.findAll();
    return organizations;
  }

  /**
   * Get a single organization by its orgcode
   *
   * @param orgCode the oroganization code
   * @return an Organization
   */
  @Operation(summary = "Get a single organization")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("")
  public UCSBOrganization getById(@Parameter(name = "orgCode") @RequestParam String orgCode) {
    UCSBOrganization ucsbOrganization =
        ucsbOrganizationRepository
            .findById(orgCode)
            .orElseThrow(() -> new EntityNotFoundException(UCSBOrganization.class, orgCode));

    return ucsbOrganization;
  }

  /**
   * Create a new date
   *
   * @param orgCode organization code
   * @param orgTranslationShort short translation of organization name
   * @param orgTranslation full translation of organization name
   * @param inactive true if organization is inactive, false if organization is active
   * @return the saved UCSBOrganization
   */
  @Operation(summary = "Create a new organization")
  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/post")
  public UCSBOrganization postUCSBOrganization(
      @Parameter(name = "orgCode") @RequestParam String orgCode,
      @Parameter(name = "orgTranslationShort") @RequestParam String orgTranslationShort,
      @Parameter(name = "orgTranslation") @RequestParam String orgTranslation,
      @Parameter(name = "inactive") @RequestParam boolean inactive)
      throws JsonProcessingException {

    // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    // See: https://www.baeldung.com/spring-date-parameters

    log.info("orgCode={}", orgCode);

    UCSBOrganization ucsbOrganization = new UCSBOrganization();
    ucsbOrganization.setOrgCode(orgCode);
    ucsbOrganization.setOrgTranslationShort(orgTranslationShort);
    ucsbOrganization.setOrgTranslation(orgTranslation);
    ucsbOrganization.setInactive(inactive);

    UCSBOrganization savedUcsbOrganization = ucsbOrganizationRepository.save(ucsbOrganization);

    return savedUcsbOrganization;
  }

  /**
   * Delete a UCSBDate
   *
   * @param id the id of the date to delete
   * @return a message indicating the date was deleted
   */
  //  @Operation(summary = "Delete a UCSBDate")
  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
  //  @DeleteMapping("")
  //  public Object deleteUCSBDate(@Parameter(name = "id") @RequestParam Long id) {
  //    UCSBDate ucsbDate =
  //        ucsbDateRepository
  //            .findById(id)
  //            .orElseThrow(() -> new EntityNotFoundException(UCSBDate.class, id));

  //    ucsbDateRepository.delete(ucsbDate);
  //    return genericMessage("UCSBDate with id %s deleted".formatted(id));
  //  }

  /**
   * Update a single date
   *
   * @param id id of the date to update
   * @param incoming the new date
   * @return the updated date object
   */
  //  @Operation(summary = "Update a single date")
  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
  //  @PutMapping("")
  //  public UCSBDate updateUCSBDate(
  //      @Parameter(name = "id") @RequestParam Long id, @RequestBody @Valid UCSBDate incoming) {

  //    UCSBDate ucsbDate =
  //        ucsbDateRepository
  //            .findById(id)
  //            .orElseThrow(() -> new EntityNotFoundException(UCSBDate.class, id));

  //    ucsbDate.setQuarterYYYYQ(incoming.getQuarterYYYYQ());
  //    ucsbDate.setName(incoming.getName());
  //    ucsbDate.setLocalDateTime(incoming.getLocalDateTime());

  //    ucsbDateRepository.save(ucsbDate);

  //    return ucsbDate;
  //  }
}
