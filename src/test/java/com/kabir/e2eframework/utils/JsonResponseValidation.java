package com.kabir.e2eframework.utils;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.testng.asserts.SoftAssert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.kabir.e2eframework.utils.FileNameConstants.ValidationEnum;
import net.minidev.json.JSONArray;

import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONObject;

public class JsonResponseValidation {

	public static void jsonValidation(String responseJSONString, String baseJSONString, SoftAssert sAssert)
			throws Exception {

		String responseJSONStringData = "";
		List<String[]> baseKVjSONPathList = null;
		List<String> actualJSONPathValue = null;
		responseJSONStringData = responseJSONString;
		baseKVjSONPathList = loadStringDataToList(baseJSONString);
		String validationTypeKey = "JSONPath";

		try {
			for (String[] strArry : baseKVjSONPathList) {

				System.out.println("The Array Value here:" + strArry.toString());

				actualJSONPathValue = executeJSONorXPATH(null, "", responseJSONStringData, strArry[0],
						ValidationEnum.JSONVALIDATION.toString());

				doValidation(strArry, actualJSONPathValue, sAssert, validationTypeKey, "1234");

			}
		} catch (Exception e) {

			sAssert.assertTrue(false, "Exception in jsonValidation()" + e.getMessage());
		} finally {

		}
	}

	/**
	 * <b>Name:</b>loadStringDataToList<br>
	 * <b>Description:</b> This is method is used to load input data from a string
	 * to List with respect to delimiters (];[ and ],[).<br>
	 *
	 * @param inputString
	 *
	 * @throws IOException
	 * @return List
	 **/
	private static List<String[]> loadStringDataToList(String inputString) {

		List<String> list = null;
		if (inputString.contains("\n") || inputString.contains(FileNameConstants.SPLITSEMICOLONPATTERN)) {
			String pattern = "";
			if (inputString.contains("\n"))
				pattern = "\n";
			else
				pattern = FileNameConstants.SPLITSEMICOLONPATTERN;
			list = new ArrayList<String>(Arrays.asList(inputString.split(Pattern.quote(pattern))));
		} else {
			list = new ArrayList<String>();
			list.add(inputString);
		}

		List<String[]> returnList = new ArrayList<String[]>();
		for (String str : list) {
			String inputStringTrim = str.substring(1, str.length() - 1);
			String[] keyValuePairs = splitStringBasedOnPattern(inputStringTrim, FileNameConstants.SPLITSCOMMAPATTERN);
			returnList.add(keyValuePairs);
		}
		return returnList;

	}

	private static String[] splitStringBasedOnPattern(String baselineXMLString, String patternString) {
		String[] array = baselineXMLString.split(Pattern.quote(patternString));
		return array;
	}

	private static List<String> executeJSONorXPATH(String respXMLdocument, String basexPath, String respJsonString,
			String baseJsonPath, String validationType, String... validationKey) throws XPathExpressionException {
		List<String> list = new ArrayList<String>();

		list = getJSONValueUsingJSONPath(respJsonString, baseJsonPath);

		return list;
	}

	public static List<String> getJSONValueUsingJSONPath(String respJsonString, String baseJsonPath) {

		List<String> list = new ArrayList<String>();

		try {

			if (baseJsonPath.startsWith("$.")) {

				String fieldName = baseJsonPath.substring(2, baseJsonPath.length());
				String value = "";

				// JsonPrimitive value =JsonPath.read(respJsonString, baseJsonPath);

				if (JsonPath.read(respJsonString, baseJsonPath) instanceof JsonObject) {
					System.out.print("Returning JSONObject");
					value = JsonPath.read(respJsonString, baseJsonPath).toString();
				}

				else if (JsonPath.read(respJsonString, baseJsonPath) instanceof JsonArray) {
					System.out.print("Returning Json Array");
					value = JsonPath.read(respJsonString, baseJsonPath).toString();
				}
				// else if(JsonPath.read(respJsonString, baseJsonPath) instanceof String ) {

				else if (JsonPath.read(respJsonString, baseJsonPath) instanceof String) {
					System.out.print("Returning Some String Value");
					value = JsonPath.read(respJsonString, baseJsonPath);

				} else if (JsonPath.read(respJsonString, baseJsonPath) instanceof Integer) {
					System.out.print("Returning Some INT Value");
					value = (String) JsonPath.read(respJsonString, baseJsonPath);
				} else if (JsonPath.read(respJsonString, baseJsonPath) instanceof Boolean) {
					System.out.print("Returning a Boolean Value");
					value = (String) JsonPath.read(respJsonString, baseJsonPath);
				} else {

					System.out.print("Returning a Different Kind of Value");
				}

				System.out.println(" The " + fieldName + " found in the response:!!!! " + value);
				list.add(value);
			}

			else {
				list.add(baseJsonPath);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * <b>Name:</b> getStringOccuranceData <br>
	 * <b>Description:</b> Method to check if payload string contains base string or
	 * not and if yes then how many times does it occur. <br>
	 *
	 * @param actualXPathORJSONValue The collection containing actual payload data.
	 * @param baseXPathORJSONArray   The array containing base data.
	 * @param isContainsQuery        CONTAINS query indicator flag.
	 * @return hasContent The boolean value indication presence <true> or absence
	 *         <false> of base string in payload string.
	 */
	protected static Map<String, Object> getStringOccuranceData(List<String> actualXPathORJSONValue,
			String[] baseXPathORJSONArray, boolean isContainsQuery) {
		int occuranceCount = 0;
		boolean hasContent = false;
		String processedActualValue, processedBaseValue;
		Map<String, Object> resultantMap = new HashMap<String, Object>();

		try {
			hasContent = actualXPathORJSONValue.toString().contains(baseXPathORJSONArray[2]);

			if (hasContent) {
				if (isContainsQuery) {
					occuranceCount = StringUtils.countMatches(actualXPathORJSONValue.toString(),
							baseXPathORJSONArray[2]);
				}
			} else {
				processedActualValue = actualXPathORJSONValue.toString();
				processedBaseValue = baseXPathORJSONArray[2].replaceAll("/", "\\\\/");

				hasContent = processedActualValue.contains(processedBaseValue);

				if (hasContent && isContainsQuery) {
					occuranceCount = StringUtils.countMatches(processedActualValue, processedBaseValue);
				}
			}
		} finally {
			resultantMap.put(FileNameConstants.HAS_CONTENT, hasContent);

			if (isContainsQuery) {
				resultantMap.put(FileNameConstants.OCCURANCE_COUNT, occuranceCount);
			}
		}

		return resultantMap;
	}

	/* Start of doValidation Method */

	private static void doValidation(String[] baseXPathORJSONArray, List<String> actualXPathORJSONValue,
			SoftAssert sAssert, String validationTypeKey, String defaultJIRA_ID) throws Exception {
		Object object;
		validationTypeKey = validationTypeKey.trim();

		ValidationEnum validationKeyWordEnum = ValidationEnum.valueOf(baseXPathORJSONArray[1]);

		int actualListSize = 0;

		try {

			if (null == actualXPathORJSONValue
					&& !(baseXPathORJSONArray[1].equals(validationKeyWordEnum.KEYEXISTS.toString())
							|| baseXPathORJSONArray[1].equals(validationKeyWordEnum.ISNULL.toString())))

			{
				sAssert.assertTrue(false, "Service Response does not contain the " + validationTypeKey + " provided!"
						+ baseXPathORJSONArray[0]);
				throw new Exception("Service Response does not contain the " + validationTypeKey + " provided!"
						+ baseXPathORJSONArray[0]);

			}

			else if (actualXPathORJSONValue instanceof List && ((List<?>) actualXPathORJSONValue).isEmpty()
					&& !baseXPathORJSONArray[1].equals(validationKeyWordEnum.KEYEXISTS.toString()))

			{
				System.out.println(validationTypeKey + " result " + baseXPathORJSONArray[0] + " is empty.");
				sAssert.assertTrue(false, validationTypeKey + " result " + baseXPathORJSONArray[0] + " is empty.");
			}

			else {
				Boolean isEqual = false;
				String expectedCount = "-1";
				Map<String, Object> resultantMap = null;

				try {
					/** Log test case description. */
					for (int i = baseXPathORJSONArray.length - 1; 2 < i; i--) {
						if (StringUtils.isNotBlank(baseXPathORJSONArray[i])
								&& baseXPathORJSONArray[i].contains(FileNameConstants.TOKEN_VALIDATION_DESCRIPTION))

						{

							System.out.println(baseXPathORJSONArray[i]);
							break;
						}

					}

					if (actualXPathORJSONValue != null)
						actualListSize = actualXPathORJSONValue.size();

					switch (validationKeyWordEnum) {
					case EQUALS:
						String extractedString = baseXPathORJSONArray[2];
						String[] array = extractedString.split(Pattern.quote("|"));
						List<String> baseList = new ArrayList<String>();
						baseList = Arrays.asList(array);

						if (baseXPathORJSONArray[2].contains("|")) {
							List<String> actualList = new ArrayList<String>();
							for (Object val : actualXPathORJSONValue) {
								actualList.add(val.toString());
							}
							isEqual = baseList.toString().equals(actualList.toString());
							sAssert.assertTrue(isEqual,
									" Query: EQUALS, Base Value: \"" + baseList + "\", Actual Value: \" "
											+ actualXPathORJSONValue.toString()
											+ "\", Base value not equals actual value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALS,  Base Value: \"" + baseList.toString()
										+ "\", Actual Value: \"" + actualXPathORJSONValue.toString() + "\"");
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALS,  Base Value: \"" + baseList.toString()
										+ "\", Actual Value: \"" + actualXPathORJSONValue.toString()
										+ "\", Base value not equals actual value.");
							}
						} else {
							isEqual = baseXPathORJSONArray[2].toString().equals(actualXPathORJSONValue.get(0));
							sAssert.assertTrue(isEqual,
									" Query: EQUALS,  Base Value: \"" + baseXPathORJSONArray[2] + "\", Actual Value: \""
											+ actualXPathORJSONValue.get(0)
											+ "\", Base value not equals actual value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALS,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0) + "\"");
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALS,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0)
										+ "\", Base value not equals actual value.");
							}
						}

						if (baseXPathORJSONArray.length >= 4 && !baseXPathORJSONArray[3].trim().isEmpty()) {
							isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: EQUALS,  Base Count: " + baseXPathORJSONArray[3] + ", Actual Count: "
											+ actualListSize + ", Base value count not equals to actual value count.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALS,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ " Query: EQUALS,  Base Count: " + baseXPathORJSONArray[3] + ", Actual Count: "
										+ actualListSize + ", Base value count not equals to actual value count.");
							}

						}
						break;

					case EQUALSIGNORECASE:
						String extraString = baseXPathORJSONArray[2];
						String[] arr = extraString.split(Pattern.quote("|"));
						List<String> basList = new ArrayList<String>();
						baseList = Arrays.asList(arr);

						if (baseXPathORJSONArray[2].contains("|")) {
							List<String> actualList = new ArrayList<String>();
							for (Object val : actualXPathORJSONValue) {
								actualList.add(val.toString());
							}
							isEqual = basList.toString().toLowerCase().equals(actualList.toString().toLowerCase());
							sAssert.assertTrue(isEqual,
									" Query: EQUALSIGNORECASE, Base Value: \"" + basList + "\", Actual Value: \" "
											+ actualXPathORJSONValue.toString()
											+ "\", Base value not equals actual value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALSIGNORECASE,  Base Value: \"" + basList.toString()
										+ "\", Actual Value: \"" + actualXPathORJSONValue.toString() + "\"");
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALSIGNORECASE,  Base Value: \"" + basList.toString()
										+ "\", Actual Value: \"" + actualXPathORJSONValue.toString()
										+ "\", Base value not equals actual value.");
							}
						} else {
							isEqual = baseXPathORJSONArray[2].toString().toLowerCase()
									.equals(actualXPathORJSONValue.get(0).toLowerCase());
							sAssert.assertTrue(isEqual,
									" Query: EQUALSIGNORECASE,  Base Value: \"" + baseXPathORJSONArray[2]
											+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0)
											+ "\", Base value not equals actual value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALSIGNORECASE,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0) + "\"");
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALSIGNORECASE,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0)
										+ "\", Base value not equals actual value.");
							}
						}

						if (baseXPathORJSONArray.length >= 4 && !baseXPathORJSONArray[3].trim().isEmpty()) {
							isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: EQUALSIGNORECASE,  Base Count: " + baseXPathORJSONArray[3]
											+ ", Actual Count: " + actualListSize
											+ ", Base value count not equals to actual value count.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: EQUALSIGNORECASE,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ " Query: EQUALSIGNORECASE,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize
										+ ", Base value count not equals to actual value count.");
							}
						}
						break;

					case CONTAINS:
						resultantMap = getStringOccuranceData(actualXPathORJSONValue, baseXPathORJSONArray, true);

						Validate.notNull(resultantMap);
						Validate.notEmpty(resultantMap);

						isEqual = (Boolean) resultantMap.get(FileNameConstants.HAS_CONTENT);
						actualListSize = (int) resultantMap.get(FileNameConstants.OCCURANCE_COUNT);

						sAssert.assertTrue(isEqual,
								" Query: CONTAINS, Base Value: \"" + baseXPathORJSONArray[2] + ",  Actual Value: \""
										+ actualXPathORJSONValue.toString()
										+ ", Base value not contained in actual value.");

						if (isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: CONTAINS,  Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.toString() + "\"");
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: CONTAINS, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.toString()
									+ "\", Base value not contained in actual value.");
						}

						if (3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty()) {
							for (int i = baseXPathORJSONArray.length - 1; 2 < i; i--) {
								if (StringUtils.isNotBlank(baseXPathORJSONArray[i])
										&& !baseXPathORJSONArray[i]
												.contains(FileNameConstants.TOKEN_VALIDATION_DESCRIPTION)
										&& !baseXPathORJSONArray[i].contains(FileNameConstants.TOKEN_JIRAID)) {
									expectedCount = (null == baseXPathORJSONArray[i]) ? baseXPathORJSONArray[i]
											: baseXPathORJSONArray[i].trim();
									break;
								}
							}

							isEqual = expectedCount.equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: CONTAINS, Base Count: " + expectedCount + ", Actual Count:"
											+ actualListSize + ", Base count value not matching actual count value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: CONTAINS,  Base Count: " + expectedCount + ", Actual Count: "
										+ actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: CONTAINS,  Base Count: " + expectedCount + ", Actual Count:"
										+ actualListSize + ", Base count value not matching actual count value.");
							}
						}

						break;

					case DOESNOTCONTAIN:
						resultantMap = getStringOccuranceData(actualXPathORJSONValue, baseXPathORJSONArray, false);

						Validate.notNull(resultantMap);
						Validate.notEmpty(resultantMap);

						isEqual = (Boolean) resultantMap.get(FileNameConstants.HAS_CONTENT);

						sAssert.assertFalse(isEqual,
								" Query: DOESNOTCONTAIN, Base Value: \"" + baseXPathORJSONArray[2]
										+ "\",  Actual Value: \"" + actualXPathORJSONValue.toString()
										+ "\", Base value is present in actual value which is not as Expected.");

						if (!isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: DOESNOTCONTAIN, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.toString()
									+ "\", Base value is not present in actual value as Expected");
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: DOESNOTCONTAIN, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.toString()
									+ "\", Base value is present in actual value which is not as Expected.");
						}
						break;

					case ISEMPTY:
						Boolean checkEmptyFlag = false;

						StringBuilder actualValue = new StringBuilder();
						for (Object val : actualXPathORJSONValue) {
							actualValue.append(val);
						}

						if (actualValue.toString().trim().isEmpty())
							checkEmptyFlag = true;

						isEqual = Boolean.valueOf(baseXPathORJSONArray[2]).equals(checkEmptyFlag);
						sAssert.assertTrue(isEqual, " Query: ISEMPTY, Base Value: " + baseXPathORJSONArray[2]
								+ ",  Actual Value: " + checkEmptyFlag + ", Base value is not matching actual value.");

						if (isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: ISEMPTY, Base Value: " + baseXPathORJSONArray[2] + ", Actual Value: "
									+ checkEmptyFlag);
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: ISEMPTY, Base Value: " + baseXPathORJSONArray[2] + ", Actual Value: "
									+ checkEmptyFlag + ", Base value is not matching actual value.");
						}
						break;

					case GREATERTHAN:
						object = actualXPathORJSONValue.get(0);
						isEqual = Double.parseDouble(object.toString()) > Double.parseDouble(baseXPathORJSONArray[2]);
						sAssert.assertTrue(isEqual,
								" Query: GREATERTHAN,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + object.toString()
										+ "\", Actual value is not greater than base value.");

						if (isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: GREATERTHAN, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + object.toString() + "\"");
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: GREATERTHAN, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + object.toString()
									+ "\", Actual value is not greater than base value.");
						}

						if (3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty()) {
							isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: GREATERTHAN,  Base Count: " + baseXPathORJSONArray[3] + ", Actual Count:"
											+ actualListSize + " Base count value is not matching actual count value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: GREATERTHAN, Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: GREATERTHAN, Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize
										+ " Base count value is not matching actual count value.");
							}
						}
						break;

					case LESSTHAN:
						object = actualXPathORJSONValue.get(0);
						isEqual = Double.parseDouble(object.toString()) < Double.parseDouble(baseXPathORJSONArray[2]);
						sAssert.assertTrue(isEqual,
								" Query: LESSTHAN,  Base Value: \"" + baseXPathORJSONArray[2] + "\", Actual Value: \""
										+ actualXPathORJSONValue.get(0)
										+ "\", Actual value is not less than base value.");

						if (isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: LESSTHAN, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0) + "\"");
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: LESSTHAN,  Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0)
									+ "\", Actual value is not less than base value.");
						}

						if (3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty()) {
							isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: LESSTHAN, Base Count: " + baseXPathORJSONArray[3] + ", Actual Count:"
											+ actualListSize
											+ ", Base count value is not matching actual count value.");

							if (isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: LESSTHAN,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: LESSTHAN,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count:" + actualListSize
										+ ", Base count value is not matching actual count value.");
							}
						}
						break;

					case GREATERTHANOREQUALS:
						object = actualXPathORJSONValue.get(0);
						isEqual = Double.parseDouble(object.toString()) > Double.parseDouble(baseXPathORJSONArray[2]);

						sAssert.assertTrue(isEqual,
								" Query: GREATERTHANOREQUALS,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0)
										+ "\", Actual value is not less than base value.");

						if (!isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: GREATERTHANOREQUALS, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0) + "\"");
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: GREATERTHANOREQUALS,  Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + actualXPathORJSONValue.get(0)
									+ "\", Actual value is less than base value.");
						}

						if (3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty()) {
							isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: GREATERTHANOREQUALS, Base Count: " + baseXPathORJSONArray[3]
											+ ", Actual Count:" + actualListSize
											+ ", Base count value is not matching actual count value.");

							if (!isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: GREATERTHANOREQUALS,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: GREATERTHANOREQUALS,  Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count:" + actualListSize
										+ ", Base count value is not matching actual count value.");
							}

						}
						break;

					case LESSTHANOREQUALS:
						object = actualXPathORJSONValue.get(0);
						isEqual = Double.parseDouble(object.toString()) < Double.parseDouble(baseXPathORJSONArray[2]);
						sAssert.assertTrue(isEqual,
								" Query: LESSTHANOREQUALS,  Base Value: \"" + baseXPathORJSONArray[2]
										+ "\", Actual Value: \"" + object.toString()
										+ "\", Actual value is not greater than base value.");

						if (!isEqual) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: LESSTHANOREQUALS, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + object.toString() + "\"");
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: LESSTHANOREQUALS, Base Value: \"" + baseXPathORJSONArray[2]
									+ "\", Actual Value: \"" + object.toString()
									+ "\", Actual value is less than base value.");
						}

						if (3 < baseXPathORJSONArray.length && !baseXPathORJSONArray[3].trim().isEmpty()) {
							isEqual = baseXPathORJSONArray[3].equals(Integer.toString(actualListSize));
							sAssert.assertTrue(isEqual,
									" Query: LESSTHANOREQUALS,  Base Count: " + baseXPathORJSONArray[3]
											+ ", Actual Count:" + actualListSize
											+ " Base count value is not matching actual count value.");

							if (!isEqual) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: LESSTHANOREQUALS, Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize);
							} else {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: LESSTHANOREQUALS, Base Count: " + baseXPathORJSONArray[3]
										+ ", Actual Count: " + actualListSize
										+ " Base count value is not matching actual count value.");
							}
						}
						break;

					case KEYEXISTS:
						Boolean isKeyExists = actualXPathORJSONValue == null ? false : true;
						sAssert.assertEquals(isKeyExists, Boolean.valueOf(baseXPathORJSONArray[2]),
								" Query: KEYEXISTS, Base Value: " + baseXPathORJSONArray[2] + ", Actual Value:"
										+ isKeyExists + ", Base value is not matching actual value.");

						if (isKeyExists.equals(Boolean.valueOf(baseXPathORJSONArray[2]))) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: KEYEXISTS, Base Value: " + baseXPathORJSONArray[2] + ", Actual Value: "
									+ isKeyExists);
						} else {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: KEYEXISTS,  Base Value: " + baseXPathORJSONArray[2] + ", Actual Value:"
									+ isKeyExists + ", Base value is not matching actual value.");
						}
						break;

					case XMLCHILDNODEEXISTS:
						if (validationTypeKey.equalsIgnoreCase("JSONPath")) {
							sAssert.assertTrue(false,
									"XMLCHILDNODEEXISTS supports only for XPath Validation, not for JSONPath Validation");
							System.out.println(
									"XMLCHILDNODEEXISTS supports only for XPath Validation, not for JSONPath Validation");
						} else {
							object = actualXPathORJSONValue.get(0);
							Boolean isChildNodeExists = Integer.parseInt(object.toString()) > 1 ? true : false;

							sAssert.assertEquals(isChildNodeExists, Boolean.valueOf(baseXPathORJSONArray[2]),
									" Query: XMLCHILDNODEEXISTS, Base Value: " + baseXPathORJSONArray[2]
											+ ", Actual Value:" + isChildNodeExists
											+ ", Base value is not matching actual value.");

							if (isChildNodeExists.equals(Boolean.valueOf(baseXPathORJSONArray[2]))) {
								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: XMLCHILDNODEEXISTS, Base Value: " + baseXPathORJSONArray[2]
										+ ", Actual Value: " + isChildNodeExists);
							} else {

								System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
										+ ", Query: XMLCHILDNODEEXISTS,  Base Value: " + baseXPathORJSONArray[2]
										+ ", Actual Value:" + isChildNodeExists
										+ ", Base value is not matching actual value.");
							}
						}
						break;

					case ISNULL:
						Boolean isNull = actualXPathORJSONValue == null ? true : false;
						sAssert.assertEquals(isNull, Boolean.valueOf(baseXPathORJSONArray[2]),
								" Query: ISNULL, Base Value: " + baseXPathORJSONArray[2] + ", Actual Value:" + isNull
										+ ", Base value is not matching actual value.");

						if (isNull.equals(Boolean.valueOf(baseXPathORJSONArray[2]))) {
							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: ISNULL, Base Value: " + baseXPathORJSONArray[2] + ", Actual Value: "
									+ isNull);
						} else {

							System.out.println(validationTypeKey + " is: " + baseXPathORJSONArray[0]
									+ ", Query: ISNULL,  Base Value: " + baseXPathORJSONArray[2] + ", Actual Value:"
									+ isNull + ", Base value is not matching actual value.");
						}
						break;

					default:
						throw new Exception(
								"QUERY keyword '" + validationKeyWordEnum + "'is not supported by the validaiton API.");
					}
				} finally {
					if (null != resultantMap) {
						resultantMap.clear();
						resultantMap = null;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in doValidation(): " + e.getMessage());
			System.out.println("Exception StackTrace: " + e.getStackTrace());
			sAssert.assertTrue(false, "Exception in doValidation()" + e.getMessage());
		}

	}

}
