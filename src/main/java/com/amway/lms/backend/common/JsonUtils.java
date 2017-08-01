package com.amway.lms.backend.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtils {

	public static String toJson(Object obj){
		ObjectMapper mapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
        mapper.setDateFormat(df);
        try {
        	return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			throw new RuntimeException("Could not convert object to json string", e);
		} catch (JsonMappingException e) {
			throw new RuntimeException("Could not convert object to json string", e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Could not convert object to json string", e);
		} catch (IOException e) {
			throw new RuntimeException("Could not convert object to json string", e);
		}
	}

}