package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

public class TestMappers {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapToObject(MvcResult mvcResult, Class<T> targetClass)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), targetClass);
    }
}
