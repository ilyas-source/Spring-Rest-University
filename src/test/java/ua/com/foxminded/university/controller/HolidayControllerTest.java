package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.exception.EntityNotFoundException;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHoliday1;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHolidays;

@ExtendWith(MockitoExtension.class)
class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;
    @InjectMocks
    private HolidayController holidayController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllHolidays() throws Exception {
        when(holidayService.findAll()).thenReturn(expectedHolidays);

        mockMvc.perform(get("/holidays"))
                .andExpect(view().name("holidaysView"))
                .andExpect(model().attribute("holidays", expectedHolidays));
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithHoliday() throws Exception {
        when(holidayService.findById(1)).thenReturn(java.util.Optional.of(expectedHoliday1));

        mockMvc.perform(get("/holidays/1"))
                .andExpect(view().name("/details/holiday"))
                .andExpect(model().attribute("holiday", expectedHoliday1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        String expected = "Can't find holiday by id 1";
        when(holidayService.findById(1)).thenReturn(Optional.empty());
        Throwable thrown = assertThrows(org.springframework.web.util.NestedServletException.class,
                                        () -> mockMvc.perform(get("/holidays/1")));
        Throwable cause = thrown.getCause();

        assertEquals(cause.getClass(), EntityNotFoundException.class);
        assertEquals(expected, cause.getMessage());
    }

    @Test
    void givenHoliday_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/holidays/update").flashAttr("holiday", expectedHoliday1))
                .andExpect(status().is3xxRedirection());

        verify(holidayService).update(expectedHoliday1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyHoliday() throws Exception {
        mockMvc.perform(get("/holidays/new"))
                .andExpect(view().name("/create/holiday"))
                .andExpect(model().attribute("holiday", new Holiday()));
    }

    @Test
    void givenHoliday_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/holidays/create").flashAttr("holiday", expectedHoliday1))
                .andExpect(status().is3xxRedirection());

        verify(holidayService).create(expectedHoliday1);
    }
}
