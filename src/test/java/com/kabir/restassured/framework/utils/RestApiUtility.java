package com.kabir.restassured.framework.utils;

import java.util.HashMap;
import java.util.Map;

import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kabir.restassured.framework.listener.RestAssuredListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class RestApiUtility {

	static SoftAssert softAssert = new SoftAssert();

	// Post(Create) Booking method

	public static void postBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws JsonProcessingException {

		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.BOOKING_URI;
		String requestBody = testCaseData.get("InputData").toString();

		Response response = RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON)
				.body(requestBody).baseUri(baseURI).when().post().then().assertThat().statusCode(200).extract()
				.response();
		JSONArray jsonArrayFirstName = JsonPath.read(response.body().asString(), "$.booking..firstname");
		String firstName = (String) jsonArrayFirstName.get(0);

		JSONArray jsonArrayLastName = JsonPath.read(response.body().asString(), "$.booking..lastname");
		String lastName = (String) jsonArrayLastName.get(0);

		int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
		System.out.println("My booking id from Response:::" + bookingId);

		dataBeanMap.put("booking_id", bookingId);
		System.out.println("DataBean Map Data:::" + dataBeanMap.get("booking_id"));
		dataBeanMap.put("firstName", firstName);
		System.out.println("DataBean First Name::" + dataBeanMap.get("firstName"));
		dataBeanMap.put("lastName", lastName);
		System.out.println("DataBean Last Name::" + dataBeanMap.get("lastName"));

	}

	// Get Booking method
	public static void getBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws Exception {
		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.BOOKING_URI;
		String bkId = dataBeanMap.get("booking_id").toString();
		Integer bookingId = Integer.valueOf(bkId);
		String validationContent = testCaseData.get("ValidationContent").toString();

		@SuppressWarnings("unused")
		Response response = RestAssured.given().contentType(ContentType.JSON).baseUri(baseURI).when()
				.get("/{bookingId}", bookingId).then().assertThat().statusCode(200).extract().response();

		JsonResponseValidation.jsonValidation(response.body().asString(), validationContent, softAssert);

	}

//token generation

	public static void getToken(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws Exception {
		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.TOKEN_URI;
		String requestBody = testCaseData.get("InputData").toString();
		Integer statusCode = Integer.valueOf(testCaseData.get("BaseLineStatusCode"));
		String validationContent = testCaseData.get("ValidationContent").toString();
		Response response = RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON)
				.body(requestBody).baseUri(baseURI).when().post().then().assertThat().statusCode(statusCode).extract()
				.response();

		String token = JsonPath.read(response.body().asString(), "$.token");
		System.out.println("Token Id : " + token);
		dataBeanMap.put("token", token);
		JsonResponseValidation.jsonValidation(response.body().asString(), validationContent, softAssert);

	}

//Put Booking method

	public static void putBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws Exception {
		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.BOOKING_URI;
		String bkId = dataBeanMap.get("booking_id").toString();
		Integer bookingId = Integer.valueOf(bkId);
		String requestBody = testCaseData.get("InputData").toString();
		String validationContent = testCaseData.get("ValidationContent").toString();
		Integer statusCode = Integer.valueOf(testCaseData.get("BaseLineStatusCode"));
		String token = (String) dataBeanMap.get("token");

		Response response = RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON)
				.body(requestBody).header("Cookie", "token=" + token).baseUri(baseURI).when()
				.put("/{bookingId}", bookingId).then().assertThat().statusCode(statusCode).extract().response();

		JsonResponseValidation.jsonValidation(response.body().asString(), validationContent, softAssert);

	}

// patchBooking	Method.
	public static void patchBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws Exception {
		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.BOOKING_URI;
		String bkId = dataBeanMap.get("booking_id").toString();
		Integer bookingId = Integer.valueOf(bkId);
		String requestBody = testCaseData.get("InputData").toString();
		String validationContent = testCaseData.get("ValidationContent").toString();
		Integer statusCode = Integer.valueOf(testCaseData.get("BaseLineStatusCode"));
		String token = (String) dataBeanMap.get("token");

		Response response = RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON)
				.body(requestBody).header("Cookie", "token=" + token).baseUri(baseURI).when()
				.patch("/{bookingId}", bookingId).then().assertThat().statusCode(statusCode).extract().response();

		JsonResponseValidation.jsonValidation(response.body().asString(), validationContent, softAssert);

	}

	// deleteBooking Method.
	public static void deleteBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws Exception {
		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.BOOKING_URI;
		String bkId = dataBeanMap.get("booking_id").toString();
		Integer bookingId = Integer.valueOf(bkId);
		Integer statusCode = Integer.valueOf(testCaseData.get("BaseLineStatusCode"));
		String token = (String) dataBeanMap.get("token");

		Response response = RestAssured.given().filter(new RestAssuredListener()).contentType(ContentType.JSON)
				.header("Cookie", "token=" + token).baseUri(baseURI).when().delete("/{bookingId}", bookingId).then()
				.assertThat().statusCode(statusCode).extract().response();

	}

}
