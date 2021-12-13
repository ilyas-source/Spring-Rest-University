package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.mapper.HolidayMapper;
import ua.com.foxminded.university.controller.ControllerExceptionHandler;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.HolidayRestControllerTest.TestData.expectedHoliday1;
import static ua.com.foxminded.university.api.HolidayRestControllerTest.TestData.expectedHolidays;

@DataJpaTest
public class HolidayRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    private int holidayId = 1;
    String expectedHolidayJson;
    String expectedHolidaysJson;

    @Mock
    private HolidayService holidayService;
    @Mock
    private HolidayMapper mapper;
    @InjectMocks
    private HolidayRestController holidayRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedHolidayJson = objectMapper.writeValueAsString(expectedHoliday1);
        expectedHolidaysJson = objectMapper.writeValueAsString(expectedHolidays);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(holidayService.findAll()).thenReturn(expectedHolidays);

        mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedHolidaysJson));

        verify(holidayService).findAll();
    }

    @Test
    void givenId_onGetHoliday_shouldReturnCorrectJson() throws Exception {
        when(holidayService.getById(1)).thenReturn(expectedHoliday1);

        var result = mockMvc.perform(get("/api/holidays/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedHolidayJson));

        verify(holidayService).getById(1);
    }

    @Test
    void givenHoliday_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/holidays")
                        .content(expectedHolidayJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(holidayService).create(expectedHoliday1);
    }

    @Test
    void givenHoliday_onUpdate_shouldCallServiceUpdate() throws Exception {
        mockMvc.perform(put("/api/holidays/{id}", 1)
                        .content(expectedHolidayJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(holidayService).update(expectedHoliday1);
    }

    @Test
    void givenHoliday_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/holidays/{id}", 1))
                .andExpect(status().isOk());

        verify(holidayService).delete(1);
    }

    interface TestData {
        Holiday holidayToCreate = new Holiday(0, LocalDate.of(2000, 12, 25), "Christmas");
        Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
        Holiday expectedHoliday2 = new Holiday(2, LocalDate.of(2000, 10, 30), "Halloween");
        Holiday expectedHoliday3 = new Holiday(3, LocalDate.of(2000, 3, 8), "International womens day");

        List<Holiday> expectedHolidays = new ArrayList<>(
                Arrays.asList(expectedHoliday1, expectedHoliday2, expectedHoliday3));
    }
}


