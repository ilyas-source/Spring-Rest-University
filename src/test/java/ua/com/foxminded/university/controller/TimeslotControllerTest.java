package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Timeslot;
import ua.com.foxminded.university.service.TimeslotService;

import java.time.LocalTime;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.TimeslotControllerTest.TestData.invalidTimeslot;

@ExtendWith(MockitoExtension.class)
public class TimeslotControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TimeslotService timeslotService;
    @InjectMocks
    private TimeslotController timeslotController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(timeslotController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenInvalidTimeslot_onCreate_shouldThrowValidationException() throws Exception {
        mockMvc.perform(post("/timeslots/create")
                        .flashAttr("timeslot", invalidTimeslot))
                .andExpect(view().name("exceptions/error"))
           //     .andExpect(model().attribute("title", "ValidationException"))
                .andExpect(model().attribute("message", "{timerange.invalid}"));;

        verify(timeslotService, never()).create(invalidTimeslot);
    }

    @Test
    void givenInvalidTimeslot_onUpdate_shouldThrowValidationException() throws Exception {
        mockMvc.perform(post("/timeslots/update")
                        .flashAttr("timeslot", invalidTimeslot))
                .andExpect(view().name("exceptions/error"))
                .andExpect(model().attribute("title", "ValidationException"))
                .andExpect(model().attribute("message", "{timerange.invalid}"));;

        verify(timeslotService, never()).create(invalidTimeslot);
    }

    interface TestData {
        Timeslot invalidTimeslot = new Timeslot( LocalTime.of(12, 0), LocalTime.of(11, 45));
    }

}
