Selenium Automation Framework - Modular based Hybrid framework
--

Medlife Automation Framework uses modular based framwork is a combination of Hybrid, Data driven & Keyword driven.

## Modules:
1. Configuration
2. Database
3. DataProvider
4. DriverFactory
5. Framework
6. PageObjects
7. Reports
8. Services
9. Utilities

### Configuration module
This module covers all below application logics 
1. Compliance (RxPickup logic), 
2. ReOrder, 
3. InterFC, 
4. DAP (DoctorApprovalPending)
5. DCP (DoctorConsulatationPending)
6. SAP (SubstitutionAprovalPending)


### Database module:
This module covers all databases
1. MongoDB (Medlife)
2. MySQL (Myra Integration)

### DataProvider module:
This module covers setting up TestData
1. DataFactory used for every single test case
2. TestData for TestNG suite in JSON format

### DriverFactory module:
1. WebDriverManager
2. WebDriver / Remote Driver (Selenium Grid)
3. ChromeDriver
4. AppiumDriver

### Framework module:
1. Regression and Feature wise TestNG class file, ideally all test cases
2. BaseTest for common util methods and java state machine with multiple controllers (DraftRxController/EventController/OrderControlller)
    1. DraftRxController - 
    1. EventController
        1. EventProcessFactory
        1. IEvent - Interface
        1. XXEvent - User actions. It can be API based or UI web based selenium actions
    1. OrderControlller

### PageObjects module:
Application wise web page xpaths

### Reports module:
1. Html Builder - Medlife own report template. Template for TestSuite Report and Testcase Tracker. Link will be exposed in Allure report after execution completes
2. LiveLogsListerner - Extends HtmlBuilder and implements ITestListener

### Services module:
1. API Headers class - To set different types headers across application based on condition
2. API Method class - Generic method to get all HTTP methods (POST,PUT,GET), RequestSpecBuilder using REST ASSURED 
3. 

### Utilities module:
1.
2.

### Framework Architecture Diagram:
[![Semantic description of image](/framework_architecture.png "Framework Architecture")

### Write Testcases for new APIs:
1. API TEMPLATE File - Under service-module ~/src/main/java/resources, create a <micro-service-name>ServiceURIs.json TEMPLATE file. JSON format should be {"services":["apiName":{"httpMethod":"POST/PUT/GET/DELETE","uri":"","apiHeader":["ContentType","Authorization","Device-ID","X-Code"],"requestBody":{},"statusCode":200]}. Make sure apiName is unique
2. API java class file (for parameterising) - Under service-module ~/src/main/java/resources, create a class file for new micro-service <micro-service-name>Service.java class and create a method for individual api by passing URIs.json JSON fileName and apiName. 
3. TEST_NG java class file and xml execution file - Under framework-module ~/src/test/java/api, create a <micro-service-name>ServiceTestNG.java class file to write a test cases either by using startMachine or directly calling apis for execution and create <micro-service-name>ServiceTestNG.xml for TestNG suite execution
4. TESTCASE in JSON format - Under dataprovider-module ~/src/main/resources/testSuiteData, create <micro-service-name>Service.json TESTCASE file. JSON format should be {"TestCases":[{"testCaseId":"API_01","testCaseName": "uploadImage","testCaseDescription": "1. Upload Rx image from customer app, 2. Create GoRxCTO prescription from Digidesk app","testCaseParameters": {},"fcId": "BLR10","rxSource": "GO_RX","scenarioFlow": "E-CSuploadImage","execute": true/false}]}
