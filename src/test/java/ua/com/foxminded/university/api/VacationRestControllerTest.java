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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.api.dto.VacationDto;
import ua.com.foxminded.university.api.mapper.VacationMapper;
import ua.com.foxminded.university.controller.ControllerExceptionHandler;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.VacationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.api.TestMappers.mapToObject;
import static ua.com.foxminded.university.api.VacationRestControllerTest.TestData.*;

@DataJpaTest
public class VacationRestControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String expectedVacationJson;
    String expectedVacationsJson;

    @Mock
    private VacationService vacationService;
    @Mock
    private VacationMapper mapper;
    @InjectMocks
    private VacationRestController vacationRestController;

    @BeforeEach
    public void setMocks() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(vacationRestController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        expectedVacationJson = objectMapper.writeValueAsString(expectedVacation1);
        expectedVacationsJson = objectMapper.writeValueAsString(expectedVacations1);
    }

    @Test
    void givenCorrectGetRequest_onFindAll_shouldReturnCorrectJson() throws Exception {
        when(vacationService.findAll()).thenReturn(expectedVacations1);

        mockMvc.perform(get("/api/vacations"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedVacationsJson));
    }

    @Test
    void givenId_onGetVacation_shouldReturnCorrectJson() throws Exception {
        when(vacationService.getById(1)).thenReturn(expectedVacation1);

        mockMvc.perform(get("/api/vacations/{id}", vacationId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedVacationJson));

        verify(vacationService).getById(1);
    }

    @Test
    void givenVacation_onSave_shouldCallServiceCreate() throws Exception {
        mockMvc.perform(post("/api/vacations")
                        .content(expectedVacationJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(vacationService).create(expectedVacation1);
    }

    @Test
    void givenVacationDto_onUpdate_shouldCallServiceUpdate() throws Exception {
        when(mapper.vacationDtoToVacation(vacationDto)).thenReturn(expectedVacation1);
        MvcResult mvcResult = mockMvc.perform(put("/api/vacations/{id}", vacationId)
                        .content(objectMapper.writeValueAsString(vacationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var actual = mapToObject(mvcResult, Vacation.class);

        verify(vacationService).update(expectedVacation1);
        assertEquals(expectedVacation1, actual);
    }

    @Test
    void givenVacation_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(delete("/api/vacations/{id}", vacationId))
                .andExpect(status().isNoContent());

        verify(vacationService).delete(vacationId);
    }

    interface TestData {
        int vacationId = 1;
        VacationDto vacationDto = new VacationDto(LocalDate.of(2000, 1, 1), LocalDate.of(2000, 2, 1));

        Vacation expectedVacation1 = new Vacation(1, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 2, 1));
        Vacation expectedVacation2 = new Vacation(2, LocalDate.of(2000, 5, 1), LocalDate.of(2000, 6, 1));
        Vacation expectedVacation3 = new Vacation(3, LocalDate.of(2000, 1, 15), LocalDate.of(2000, 2, 15));
        Vacation expectedVacation4 = new Vacation(4, LocalDate.of(2000, 6, 1), LocalDate.of(2000, 7, 1));
        List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
        List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));
    }
}
