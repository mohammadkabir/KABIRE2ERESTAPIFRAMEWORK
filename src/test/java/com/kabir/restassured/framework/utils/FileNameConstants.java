package com.kabir.restassured.framework.utils;

public class FileNameConstants {
	
	public static final String BASE_PATH ="./src/test/resources/";
	
	
	public static final String POST_API_REQUEST_BODY = BASE_PATH+"postapirequestbody.txt";
	
	public static final String JSON_SCHEMA = BASE_PATH+"expectedjsonschema.txt";
	
	public static final String TOKEN_API_REQUEST_BODY = BASE_PATH+"tokenapirequestbody.txt";
	
	public static final String PUT_API_REQUEST_BODY = BASE_PATH+"putapirequestbody.txt";
	
	public static final String PATCH_API_REQUEST_BODY = BASE_PATH+"patchapirequestbody.txt";
	
	public static final String JSON_TEST_DATA = BASE_PATH+"testdatajson.json";
	
	public static final String CSV_TEST_DATA = BASE_PATH+"testdatacsv.csv";
	
	public static final String EXCEL_TEST_DATA = BASE_PATH+"exceltestdata.xlsx";
	//public static final String EXCEL_TEST_DATA = BASE_PATH+"EClosingCreatePackageFlow_Tests_Data.xlsx";
	

	// Base URL and URIs
	
	public static final String BASE_URL = "https://restful-booker.herokuapp.com";
	public static final String BOOKING_URI = "/booking";
	public static final String TOKEN_URI="/auth";
	public static String SPLITSEMICOLONPATTERN = "];[";
	public static String SPLITSCOMMAPATTERN = "],[";
	public static final String TOKEN_JIRAID = "JIRAID:";
	public static final String TOKEN_VALIDATION_DESCRIPTION = "ValidationDescription:";

	public static final String TOKEN_FILE = "file;";
	public static final String HAS_CONTENT = "hasContent";
	public static final String OCCURANCE_COUNT = "occuranceCount";
	
	
	
	
	
	public enum ValidationEnum {
		CONTAINS, EQUALS, EQUALSIGNORECASE, SCHEMA, ISEMPTY, GREATERTHAN, GREATERTHANOREQUALS, LESSTHAN, LESSTHANOREQUALS, DOESNOTCONTAIN, JSONVALIDATION, XPATHVALIDATION, KEYEXISTS, KEYNOTEXISTS, XMLCHILDNODEEXISTS, ISNULL
		}


	




}