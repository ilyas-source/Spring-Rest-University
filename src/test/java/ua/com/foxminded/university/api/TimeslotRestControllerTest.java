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
import ua.com.foxminded.university.api.dto.TimeslotDto;
import ua.com.foxminded.university.api.mapper.TimeslotMapper;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.TimeslotService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.TestMappers.mapToList;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;
import static ua.com.foxminded.university.api.TimeslotRestControllerTest.TestData.*;

@ExtendWith(MockitoExtension.class)
public class TimeslotRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private TimeslotService timeslotService;
    @Mock
    private TimeslotMapper mapper;
    @InjectMocks
    private TimeslotRestController timeslotRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(timeslotRestController).build();
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(timeslotService.findAll()).thenReturn(expectedTimeslots);

        MvcResult mvcResult = mockMvc.perform(get("/api/timeslots"))
                .andExpect(status().isOk())
                .andReturn();
        List<Timeslot> actual = mapToList(mvcResult, Timeslot.class);

        assertEquals(expectedTimeslots, actual);
    }

    @Test
    void givenId_onGetTimeslot_shouldReturnCorrectJson() throws Exception {
        when(timeslotService.getById(timeslotId)).thenReturn(expectedTimeslot1);

        MvcResult mvcResult = mockMvc.perform(get("/api/timeslots/{id}", timeslotId))
                .andExpect(status().isOk()).andReturn();

        var actual = mapToObject(mvcResult, Timeslot.class);

        verify(timeslotService).getById(timeslotId);
        assertEquals(expectedTimeslot1, actual);
    }

    @Test
    void givenTimeslotDto_onSave_shouldCallServiceCreate() throws Exception {
        when(mapper.timeslotDtoToTimeslot(timeslotDto)).thenReturn(expectedTimeslot1);
        mockMvc.perform(post("/api/timeslots")
                        .content(objectMapper.writeValueAsString(timeslotDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/timeslots/1"))
                .andReturn();
        verify(timeslotService).create(expectedTimeslot1);
    }

    @Test
    void givenTimeslotDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.timeslotDtoToTimeslot(timeslotDto)).thenReturn(expectedTimeslot1);
        MvcResult mvcResult = mockMvc.perform(put("/api/timeslots/{id}", timeslotId)
                        .content(objectMapper.writeValueAsString(timeslotDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Timeslot.class);

        verify(timeslotService).update(expectedTimeslot1);
        assertEquals(expectedTimeslot1, actual);
    }

    @Test
    void givenTimeslot_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/timeslots/{id}", timeslotId))
                .andExpect(status().isNoContent());

        verify(timeslotService).delete(1);
    }

    interface TestData {
        int timeslotId = 1;

        TimeslotDto timeslotDto = new TimeslotDto(LocalTime.of(9, 0), LocalTime.of(9, 45));

        Timeslot expectedTimeslot1 = new Timeslot(1, LocalTime.of(9, 0), LocalTime.of(9, 45));
        Timeslot expectedTimeslot2 = new Timeslot(2, LocalTime.of(10, 0), LocalTime.of(10, 45));

        List<Timeslot> expectedTimeslots = new ArrayList<>(
                Arrays.asList(expectedTimeslot1, expectedTimeslot2));
    }
}
