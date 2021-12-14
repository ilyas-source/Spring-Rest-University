package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.dto.HolidayDto;
import ua.com.foxminded.university.api.mapper.HolidayMapper;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.service.HolidayService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.HolidayRestControllerTest.TestData.*;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;

@ExtendWith(MockitoExtension.class)
public class HolidayRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private HolidayService holidayService;
    @Mock
    private HolidayMapper mapper;
    @InjectMocks
    private HolidayRestController holidayRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(holidayService.findAll()).thenReturn(expectedHolidays);

        MvcResult mvcResult = mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andReturn();
        List<Holiday> actual = mapToList(mvcResult, Holiday.class);

        assertEquals(expectedHolidays, actual);
    }

    @Test
    void givenId_onGetHoliday_shouldReturnCorrectJson() throws Exception {
        when(holidayService.getById(holidayId)).thenReturn(expectedHoliday1);

        MvcResult mvcResult = mockMvc.perform(get("/api/holidays/{id}", holidayId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Holiday.class);

        verify(holidayService).getById(holidayId);
        assertEquals(expectedHoliday1, actual);
    }

    @Test
    void givenHolidayDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.holidayDtoToHoliday(holidayDto)).thenReturn(expectedHoliday1);
        mockMvc.perform(post("/api/holidays")
                        .content(objectMapper.writeValueAsString(holidayDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/holidays/1"));

        verify(holidayService).create(expectedHoliday1);
    }

    @Test
    void givenHolidayDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.holidayDtoToHoliday(holidayDto)).thenReturn(expectedHoliday1);
        MvcResult mvcResult = mockMvc.perform(put("/api/holidays/{id}", holidayId)
                        .content(objectMapper.writeValueAsString(holidayDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Holiday.class);

        verify(holidayService).update(expectedHoliday1);
        assertEquals(expectedHoliday1, actual);
    }

    @Test
    void givenHoliday_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/holidays/{id}", holidayId))
                .andExpect(status().isNoContent());

        verify(holidayService).delete(holidayId);
    }

    interface TestData {
        int holidayId = 1;

        HolidayDto holidayDto=new HolidayDto( LocalDate.of(2000, 12, 25), "Christmas");

        Holiday expectedHoliday1 = new Holiday(1, LocalDate.of(2000, 12, 25), "Christmas");
        Holiday expectedHoliday2 = new Holiday(2, LocalDate.of(2000, 10, 30), "Halloween");
        Holiday expectedHoliday3 = new Holiday(3, LocalDate.of(2000, 3, 8), "International womens day");

        List<Holiday> expectedHolidays = new ArrayList<>(
                Arrays.asList(expectedHoliday1, expectedHoliday2, expectedHoliday3));
    }
}


