package com.kabir.e2eframework.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ReadTestData {
	@DataProvider(name = "ExcelTestData")
	public Object[][] getTestData() {

		String query = "select * from Sheet1 where Test_Case_Name='FlightBookingE2E'";

		Object[][] objArray = null;
		Map<String, String> testData = null;
		List<Map<String, String>> testDataList = null;

		Fillo fillo = new Fillo();
		Connection connection = null;
		Recordset recordset = null;

		try {
			connection = fillo.getConnection(FileNameConstants.EXCEL_TEST_DATA);
			recordset = connection.executeQuery(query);

			testDataList = new ArrayList<Map<String, String>>();

			while (recordset.next()) {
				testData = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

				for (String field : recordset.getFieldNames()) {
					testData.put(field, recordset.getField(field));
				}

				testDataList.add(testData);
			}

			objArray = new Object[testDataList.size()][1];

			for (int i = 0; i < testDataList.size(); i++) {
				objArray[i][0] = testDataList.get(i);
			}

		} catch (FilloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return objArray;
	}

}
