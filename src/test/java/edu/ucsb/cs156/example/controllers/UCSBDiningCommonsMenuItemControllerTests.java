package edu.ucsb.cs156.example.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItem;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemRepository;
import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = UCSBDiningCommonsMenuItemController.class)
@Import(TestConfig.class)
public class UCSBDiningCommonsMenuItemControllerTests extends ControllerTestCase {

  @MockitoBean UCSBDiningCommonsMenuItemRepository ucsbDiningCommonsMenuItemRepository;

  @MockitoBean UserRepository userRepository;

  private static final String BASE_URL = "/api/UCSBDiningCommonsMenuItem";

  @Test
  public void logged_out_users_cannot_get_all() throws Exception {
    mockMvc.perform(get(BASE_URL + "/all")).andExpect(status().isForbidden());
  }

  @WithMockUser(roles = {"USER"})
  @Test
  public void logged_in_users_can_get_all() throws Exception {
    UCSBDiningCommonsMenuItem item1 =
        UCSBDiningCommonsMenuItem.builder()
            .id(1L)
            .diningCommonsCode("carrillo")
            .name("Pizza")
            .station("Pizza Station")
            .build();

    UCSBDiningCommonsMenuItem item2 =
        UCSBDiningCommonsMenuItem.builder()
            .id(2L)
            .diningCommonsCode("portola")
            .name("Pasta")
            .station("Pasta Station")
            .build();

    List<UCSBDiningCommonsMenuItem> items = List.of(item1, item2);

    when(ucsbDiningCommonsMenuItemRepository.findAll()).thenReturn(items);

    MvcResult response =
        mockMvc.perform(get(BASE_URL + "/all")).andExpect(status().isOk()).andReturn();

    verify(ucsbDiningCommonsMenuItemRepository, times(1)).findAll();
    assertEquals(mapper.writeValueAsString(items), response.getResponse().getContentAsString());
  }

  @WithMockUser(roles = {"USER"})
  @Test
  public void logged_in_users_can_get_empty_list() throws Exception {
    when(ucsbDiningCommonsMenuItemRepository.findAll()).thenReturn(List.of());

    MvcResult response =
        mockMvc.perform(get(BASE_URL + "/all")).andExpect(status().isOk()).andReturn();

    verify(ucsbDiningCommonsMenuItemRepository, times(1)).findAll();
    assertEquals("[]", response.getResponse().getContentAsString());
  }

  @Test
  public void logged_out_users_cannot_post() throws Exception {
    mockMvc
        .perform(
            post(BASE_URL + "/post")
                .param("diningCommonsCode", "carrillo")
                .param("name", "Pizza")
                .param("station", "Pizza Station")
                .with(csrf()))
        .andExpect(status().isForbidden());
  }

  @WithMockUser(roles = {"USER"})
  @Test
  public void regular_users_cannot_post() throws Exception {
    mockMvc
        .perform(
            post(BASE_URL + "/post")
                .param("diningCommonsCode", "carrillo")
                .param("name", "Pizza")
                .param("station", "Pizza Station")
                .with(csrf()))
        .andExpect(status().isForbidden());
  }

  @WithMockUser(roles = {"ADMIN", "USER"})
  @Test
  public void admin_can_post_new_menu_item() throws Exception {
    UCSBDiningCommonsMenuItem incoming =
        UCSBDiningCommonsMenuItem.builder()
            .diningCommonsCode("carrillo")
            .name("Pizza")
            .station("Pizza Station")
            .build();

    UCSBDiningCommonsMenuItem saved =
        UCSBDiningCommonsMenuItem.builder()
            .id(1L)
            .diningCommonsCode("carrillo")
            .name("Pizza")
            .station("Pizza Station")
            .build();

    when(ucsbDiningCommonsMenuItemRepository.save(incoming)).thenReturn(saved);

    MvcResult response =
        mockMvc
            .perform(
                post(BASE_URL + "/post")
                    .param("diningCommonsCode", "carrillo")
                    .param("name", "Pizza")
                    .param("station", "Pizza Station")
                    .with(csrf()))
            .andExpect(status().isOk())
            .andReturn();

    verify(ucsbDiningCommonsMenuItemRepository, times(1)).save(incoming);
    assertEquals(mapper.writeValueAsString(saved), response.getResponse().getContentAsString());
  }
}
