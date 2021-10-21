package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.university.model.Vacation;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.VacationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.TeacherControllerTest.TestData.expectedTeacher1;
import static ua.com.foxminded.university.controller.VacationControllerTest.TestData.expectedVacation1;

@ExtendWith(MockitoExtension.class)
class VacationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VacationService vacationService;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private VacationController vacationController;

    @BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenCorrectId_onDelete_shouldCallServiceDelete() throws Exception {
        mockMvc.perform(post("/vacations/delete/{id}",1))
                .andExpect(status().is3xxRedirection());

        verify(vacationService).delete(1);
    }

    @Test
    void givenVacationAndTeacherId_onAddVacation_shouldCreateVacationAndUpdateTeacher() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);
        mockMvc.perform(post("/vacations/create/{id}",1)
                        .flashAttr("vacation", expectedVacation1))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/vacations/for/1"));

        verify(vacationService).create(expectedVacation1);
        verify(teacherService).update(expectedTeacher1);
    }

    @Test
    void voidGivenTeacherId_onEditVacations_shouldShowFormForCorrectTeacher() throws Exception {
        when(teacherService.getById(1)).thenReturn(expectedTeacher1);
        mockMvc.perform(get("/vacations/for/{id}",1))
                .andExpect(view().name("teacher/vacations"))
                .andExpect(model().attribute("teacher", expectedTeacher1));
    }

    interface TestData {
        Vacation expectedVacation1 = new Vacation(1, LocalDate.of(2000, 01, 01), LocalDate.of(2000, 02, 01));
        Vacation expectedVacation2 = new Vacation(2, LocalDate.of(2000, 05, 01), LocalDate.of(2000, 06, 01));
        Vacation expectedVacation3 = new Vacation(3, LocalDate.of(2000, 01, 15), LocalDate.of(2000, 02, 15));
        Vacation expectedVacation4 = new Vacation(4, LocalDate.of(2000, 06, 01), LocalDate.of(2000, 07, 01));
        List<Vacation> expectedVacations1 = new ArrayList<>(Arrays.asList(expectedVacation1, expectedVacation2));
        List<Vacation> expectedVacations2 = new ArrayList<>(Arrays.asList(expectedVacation3, expectedVacation4));
    }
}

