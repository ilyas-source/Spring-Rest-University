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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.HolidayControllerTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;
    @InjectMocks
    private HolidayController holidayController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnHtmlPageWithAllHolidays() throws Exception {
        when(holidayService.findAll()).thenReturn(expectedHolidays);

        mockMvc.perform(get("/holidays"))
                .andExpect(view().name("holiday/all"))
                .andExpect(model().attribute("holidays", expectedHolidays));

        verify(holidayService).findAll();
    }

    @Test
    void givenCorrectGetRequest_onShowDetails_shouldReturnDetailsPageWithHoliday() throws Exception {
        when(holidayService.getById(1)).thenReturn(expectedHoliday1);

        mockMvc.perform(get("/holidays/1"))
                .andExpect(view().name("holiday/details"))
                .andExpect(model().attribute("holiday", expectedHoliday1));
    }

    @Test
    void givenIncorrectGetRequest_onShowDetails_shouldThrowException() throws Exception {
        when(holidayService.getById(1)).thenThrow(new EntityNotFoundException("Can't find holiday by id 1"));

        mockMvc.perform(get("/holidays/1")).andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "EntityNotFoundException"))
                .andExpect(model().attribute("message", "Can't find holiday by id 1"));
    }

    @Test
    void givenHoliday_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(post("/holidays/update")
                .flashAttr("holiday", expectedHoliday1))
                .andExpect(status().is3xxRedirection());

        verify(holidayService).update(expectedHoliday1);
    }

    @Test
    void onShowCreationForm_shouldShowFormWithEmptyHoliday() throws Exception {
        mockMvc.perform(get("/holidays/new"))
                .andExpect(view().name("holiday/create"))
                .andExpect(model().attribute("holiday", new Holiday()));
    }

    @Test
    void givenHoliday_onCreate_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/holidays/create").flashAttr("holiday", expectedHoliday1))
                .andExpect(status().is3xxRedirection());

        verify(holidayService).create(expectedHoliday1);
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/holidays/delete/1")).andExpect(status().is3xxRedirection());

        verify(holidayService).delete(1);
    }

    interface TestData {
        Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
        Holiday expectedHoliday2 = new Holiday(2, LocalDate.of(2000, 10, 30), "Halloween");
        Holiday expectedHoliday3 = new Holiday(3, LocalDate.of(2000, 03, 8), "International womens day");

        List<Holiday> expectedHolidays = new ArrayList<>(
                Arrays.asList(expectedHoliday1, expectedHoliday2, expectedHoliday3));
    }
}
