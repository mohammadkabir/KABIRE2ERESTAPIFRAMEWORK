package com.kabir.restassured.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class BookingTestController {

	public static HashMap<String, Object> myTest(Map<String, String> testCaseData, HashMap<String, Object> dataBeanMap)
			throws Exception {

		return doTest(testCaseData, dataBeanMap);
	}

	public static HashMap<String, Object> doTest(Map<String, String> testCaseData, HashMap<String, Object> dataBeanMap)
			throws Exception {
		System.out.println("Sequence ID: " + testCaseData.get("SequenceID"));
		String apiMethodName = testCaseData.get("APIMethodName").toLowerCase().toString();
		System.out.println("Api Method Name ID: " + apiMethodName);

		switch (apiMethodName) {

		case "postbooking": {
			System.out.println("Executing : " + apiMethodName + " API");

			RestApiUtility.postBooking(dataBeanMap, testCaseData);

		}
			;
			break;

		case "getbooking": {
			System.out.println("Executing: " + testCaseData.get("APIMethodName") + " API");

			RestApiUtility.getBooking(dataBeanMap, testCaseData);
		}
			;
			break;

		case "gettoken": {
			System.out.println("Executing: " + testCaseData.get("APIMethodName") + " API");

			RestApiUtility.getToken(dataBeanMap, testCaseData);
		}
			;
			break;

		case "putbooking": {
			System.out.println("Executing:  " + testCaseData.get("APIMethodName") + " API");

			RestApiUtility.putBooking(dataBeanMap, testCaseData);
		}
			;
			break;

		case "patchbooking": {
			System.out.println("Executing:  " + testCaseData.get("APIMethodName") + " API");

			RestApiUtility.patchBooking(dataBeanMap, testCaseData);
		}
			;
			break;

		case "deletebooking": {
			System.out.println("Executing:  " + testCaseData.get("APIMethodName") + " API");

			RestApiUtility.deleteBooking(dataBeanMap, testCaseData);
		}
			;
			break;

		default:

			System.out.println(" CASE DIDN'T MATCH");

		}

		return dataBeanMap;

	}

}
