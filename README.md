Read Me:
Version:1
KABIRE2ERESTAPIFRAMEWORK is an End-to-End Data driven test framework with test cases written in excel files. The test framework is designed in such a way that the framework can be easily used by cross functional team to automate end to end/integration test cases. Here multiple API calls are grouped together to execute sequentially to validate certain E2E functionality. In each API call we validate the response against expectations and at the end of the cycle it will tell us which API validation failed. While designing the framework one of the aspects that was in my mind was to make this framework as loose coupling as possible so cross functional implementation team/teams can change this according to their team needs. 


Version:2
I am still working on the project when time permits and adding the following features in version 2:
a)	Update test execution report with Allure Report
b)	More Configuration from testNg.xml file
c)	Added log4j logging support for better logging and debugging.
d)	Updated validation method.
e)	Adding better configuration files to keep various data separate.
f)	Adding support to run the test cases with testNG.xml files as well as Jenkins Job run.


How to Run the test Case: 
Right click on DataDrivenTestingUsingExcelFile.java ->Run as->TestNG Test. It will run the test cases and show the test result like below:
