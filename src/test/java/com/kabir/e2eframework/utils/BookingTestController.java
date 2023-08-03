package com.kabir.e2eframework.utils;

import java.util.HashMap;
import java.util.Map;

public class BookingTestController {

	 public static HashMap<String, Object> myTest(
			 Map<String,String> testCaseData,HashMap<String, Object> dataBeanMap) throws Exception {
	         
		 return doTest (testCaseData,dataBeanMap);
	    }


	 public static HashMap<String, Object> doTest(Map<String,String>testCaseData,HashMap<String, Object> dataBeanMap ) throws Exception{
		 System.out.println("Sequence ID: "+testCaseData.get("SequenceID"));
		 String apiMethodName=testCaseData.get("APIMethodName").toLowerCase().toString();
		 System.out.println("Api Method Name ID: "+apiMethodName);
		 
		 
		 switch (apiMethodName) {
		 
		 case "postbooking": {
			 System.out.println("Executing : "+apiMethodName+ " API");
                   
			  RestApiUtility.postBooking( dataBeanMap, testCaseData);
				
         } ;
             break;
		 
		 case "getbooking": {
			 System.out.println("Entering Api Method Name ID: "+testCaseData.get("APIMethodName"));
                   
			  RestApiUtility.getBooking( dataBeanMap, testCaseData);
         } ;
             break;
		 
		 }
		 
		 
		 return dataBeanMap;	 
		 
	 }

}
