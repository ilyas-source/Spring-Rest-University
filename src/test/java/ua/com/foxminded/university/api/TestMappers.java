package ua.com.foxminded.university.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TestMappers {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapToObject(MvcResult mvcResult, Class<T> targetClass)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), targetClass);
    }

    static <T> List<T> mapToList(MvcResult mvcResult, Class<T> targetClass) throws IOException {
        var json = mvcResult.getResponse().getContentAsString();
        CollectionType listType =
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, targetClass);
        return objectMapper.readValue(json, listType);
    }
}
