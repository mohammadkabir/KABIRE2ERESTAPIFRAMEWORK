package com.kabir.e2eframework.tests;

import java.util.Map;
import java.util.HashMap;
import org.testng.annotations.Test;

import com.kabir.e2eframework.utils.BookingTestController;
import com.kabir.e2eframework.utils.ReadTestData;

public class DataDrivenTestingUsingExcelFile extends ReadTestData {
	public static HashMap<String, Object> dataBeanMap = new HashMap<String, Object>();
	@Test (dataProvider = "ExcelTestData")
	public void DataDrivenTesting(Map<String,String> testData) throws Exception {
		
		 dataBeanMap= BookingTestController.myTest( testData, dataBeanMap);
	}

}
