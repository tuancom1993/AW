package com.amway.lms.backend.common;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.model.AcceptCourse;
import com.amway.lms.backend.model.PostTrainingSurvey;

public final class Utils {
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static final SimpleDateFormat SDF_TRAINING_ACTION = new SimpleDateFormat("MM/dd/yyyy");

	public static ResponseEntity<?> generateSuccessResponseEntity(Object responseData) {
		ResponseStatus status = new ResponseStatus(ErrorCode.CODE_SUCCESS, ErrorCode.MSG_SUCCESS);
		ResponseBody body = new ResponseBody(status, responseData);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	public static ResponseEntity<?> generateFailureResponseEntity(int errorCode, String errorMsg) {
		ResponseStatus status = new ResponseStatus(errorCode, errorMsg);
		ResponseBody body = new ResponseBody(status, "");
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	public static String generateSuccessResponseString(Object responseData) {
		ObjectMapper mapper = new ObjectMapper();

		ResponseStatus status = new ResponseStatus(ErrorCode.CODE_SUCCESS, ErrorCode.MSG_SUCCESS);
		ResponseBody body = new ResponseBody(status, responseData);

		String response = "";
		try {
			response = mapper.writeValueAsString(body);
		} catch (JsonGenerationException e) {
			// Do nothing
		} catch (JsonMappingException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		}

		return response;
	}

	public static String generateFailureResponseString(int errorCode, String errorMsg) {
		ObjectMapper mapper = new ObjectMapper();

		ResponseStatus status = new ResponseStatus(errorCode, errorMsg);
		ResponseBody body = new ResponseBody(status, "");

		String response = "";
		try {
			response = mapper.writeValueAsString(body);
		} catch (JsonGenerationException e) {
			// Do nothing
		} catch (JsonMappingException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		}

		return response;
	}

	public static Timestamp getCurrentTime() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	private Utils() {
		super();
	}

	public static String encode(String str) {
		return DatatypeConverter.printBase64Binary(str.getBytes());
	}

	public static String decode(String str) {
		byte[] decodeBytes = DatatypeConverter.parseBase64Binary(str);
		return new String(decodeBytes);
	}

	public static String convertDateToString(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return SDF.format(calendar.getTime());
		}
		return "";
	}

	public static Date convertStringToDate(String date) {
		try {
			if (date != null) {
				return SDF.parse(date);
			}
		} catch (Exception e) {

		}

		return null;
	}

	public static Date convertStringToDateTrainingAction(String date) {
		try {
			if (date != null) {
				return SDF_TRAINING_ACTION.parse(date);
			}
		} catch (Exception e) {

		}

		return null;
	}

	public static String convertDateToStringTrainingAction(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return SDF_TRAINING_ACTION.format(calendar.getTime());
		}
		return "";
	}

	public static String converTimeToSendEmail(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		String time = sdf.format(date);
		String dateMonthYear = convertDateToStringTrainingAction(date);
		return time + " " + dateMonthYear;
	}

	public static String encrypt(String value) {
		byte[] bytesEncoded = Base64.encodeBase64(value.getBytes());
		return new String(bytesEncoded);
	}

	public static String decrypt(String encrypted) {
		byte[] valueDecoded = Base64.decodeBase64(encrypted);
		return new String(valueDecoded);
	}

	public static String ObjectToJsonString(AcceptCourse acceptCourse) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(acceptCourse);
		return jsonInString;

	}

	public static AcceptCourse JsonStringToObject(String JsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		AcceptCourse acceptCourse = new AcceptCourse();
		acceptCourse = mapper.readValue(JsonString, AcceptCourse.class);
		return acceptCourse;
	}

	public static String getTimeByDate(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
			String time = sdf.format(date);
			return time;
		}
		return "";
	}
	
	public static String objectToJsonString(Object obj) throws JsonGenerationException, JsonMappingException, IOException, Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(obj);
        return jsonInString;

    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonStringToObject(String jsonString, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException, Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object object = mapper.readValue(jsonString, clazz);
        return (T) object;
    }
}
