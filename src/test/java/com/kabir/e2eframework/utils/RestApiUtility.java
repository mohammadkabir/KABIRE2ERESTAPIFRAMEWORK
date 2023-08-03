package com.kabir.e2eframework.utils;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import com.kabir.e2eframework.listener.RestAssuredListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class RestApiUtility {

	static SoftAssert softAssert = new SoftAssert();

	public static void postBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws JsonProcessingException {

		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.POST_BOOKING_URI;
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

	public static void getBooking(HashMap<String, Object> dataBeanMap, Map<String, String> testCaseData)
			throws Exception {
		String baseURI = FileNameConstants.BASE_URL + FileNameConstants.POST_BOOKING_URI;
		String bkId = dataBeanMap.get("booking_id").toString();
		Integer bookingId = Integer.valueOf(bkId);
		String validationContent = testCaseData.get("ValidationContent").toString();

		@SuppressWarnings("unused")
		Response response = RestAssured.given().contentType(ContentType.JSON).baseUri(baseURI).when()
				.get("/{bookingId}", bookingId).then().assertThat().statusCode(200)
				.body("firstname", Matchers.equalTo(dataBeanMap.get("firstName").toString()))
				.body("lastname", Matchers.equalTo(dataBeanMap.get("lastName").toString())).extract().response();

		JsonResponseValidation.jsonValidation(response.body().asString(), validationContent, softAssert);

	}

}
