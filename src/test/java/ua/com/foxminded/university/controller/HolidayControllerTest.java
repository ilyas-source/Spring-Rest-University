package ua.com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.dao.HolidayDaoTest.TestData.expectedHolidays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.service.HolidayService;

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
}
