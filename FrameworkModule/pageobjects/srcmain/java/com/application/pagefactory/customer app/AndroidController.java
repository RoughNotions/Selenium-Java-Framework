package com.medlife.pagefactory.customerapp;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import com.medlife.pages.android.customerapp.reactnative.*;
import org.testng.Assert;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class AndroidController {

	public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private AndroidDriver<AndroidElement> driver;
	public AndroidController(AndroidDriver<AndroidElement> driver) {
		 this.driver = driver;
	 }

	public void loginWithCredentials(AndroidDriver driver,String mobileNumber, String password) throws InterruptedException {
		HomePage homepage = new HomePage(driver);
		//handling first installation pop up message
//		homepage.handlePermissionPopUp(driver);
		AccountMenuPage sidemenu = new AccountMenuPage(driver);
		homepage.clickFunction("Account");
		sidemenu.clickSignIn(driver);
		LoginPage loginpage = new LoginPage(driver);
		Assert.assertEquals(loginpage.verifyLandingPage("Login"), true);
		loginpage.clickFunction("PASSWORD");
		loginpage.login(mobileNumber, password);
		Thread.sleep(3000);
	}
	
	public void verifyLogin(Element testDataElement) {
		HomePage homepage = new HomePage(driver);
		LoginPage loginpage = new LoginPage(driver);
		homepage.clickFunction("Account");
//		Assert.assertEquals(loginpage.verifyLogin(ReadXML.getElementsByTag(testDataElement, "userid"),driver), true);
//		Assert.assertEquals(loginpage.verifyLogin(ReadXML.getElementsByTag(testDataElement, "userName"),driver), true);
		homepage.clickFunction("Home");
	}
	
//	public void uploadRxAndMedicine(AndroidDriver driver, TestDataFactory dataFactory) throws InterruptedException {
//		uploadRx(driver);
//		searchNAddMeds(testDataElement);
//	}


	public void searchNAddMeds(String productId) throws InterruptedException {
		SearchAddMedicinePage searchandadd = new SearchAddMedicinePage(driver);
		searchandadd.addMedicine(productId);
		searchandadd.clickNext();
	}
	
	
	public void uploadRx(AndroidDriver driver) throws InterruptedException {
		HomePage homepage = new HomePage(driver);
		homepage.clickFunction("ORDER NOW");
		homepage.clickFunction("Upload Precription");
		UploadPrescriptionPage uploadpres = new UploadPrescriptionPage(driver);
		Assert.assertEquals(uploadpres.verifyLandingPage("Valid prescription tips"), true, "Unable to load upload Prescription page");		
//		uploadpres.clickFunction("GALLERY");
//		if(uploadpres.verifyLandingPage("Allow Medlife to access photos, media, and files on your device?")) {
//			uploadpres.clickFunction("allowButton");
//		}
		uploadpres.uploadPhoto("GALLERY");
//		Assert.assertEquals(uploadpres.verifyLandingPage("Valid prescription tips"), true, "Unable to load upload Prescription page after adding image from gallery");
		uploadpres.clickFunction("PROCEED");
	}
	
	public void uploadRxViaCameraNMedicine(String productId) throws InterruptedException {
		HomePage homepage = new HomePage(driver);
//		homepage.clickUploadpresription();
		homepage.clickFunction("ORDER NOW");
		homepage.clickFunction("Upload Precription");
		UploadPrescriptionPage uploadpres = new UploadPrescriptionPage(driver);
		Assert.assertEquals(uploadpres.verifyLandingPage("Valid prescription tips"), true, "Unable to load upload Prescription page");	
		uploadpres.uploadPhoto("CAMERA");
//		Assert.assertEquals(uploadpres.verifyLandingPage("Valid prescription tips"), true, "Unable to load upload Prescription page after adding image from gallery");
		uploadpres.clickFunction("PROCEED");
		searchNAddMeds(productId);
	}

	public void checkOutWithDetails() {
		checkoutWithoutDets();	
		addPatient();		
		addAddress();
	}

	public void checkoutWithoutDets() {
		CartPage cart = new CartPage(driver);
		Assert.assertEquals(cart.verifyLandingPage("Cart"), true, "Failed to load cart page");
		Assert.assertEquals(cart.verifyAddedMeds(), true,"Added medicines not available in cart page");
		cart.clickFunction("CHECKOUT");
	}


	public void addAddress() {
		selectAddress();
//		mAddress.addAddress();
		DeliveryDetsPage deliveryPage = new DeliveryDetsPage(driver);
		Assert.assertEquals(deliveryPage.verifyLanding("Delivery Options"),true);
		deliveryPage.clickProceed();
	}


	public void selectAddress() {
		ManageAddressPage mAddress = new ManageAddressPage(driver);
		Assert.assertEquals(mAddress.verifyLandingPage("Manage Address"),true);
		mAddress.clickFunction("SELECT");
	}


	public void addPatient() {
		ManagePatientsPage mPatient = new ManagePatientsPage(driver);
		Assert.assertEquals(mPatient.verifyLandingPage("Manage Patients"),true);
		mPatient.clickFunction("ADD NEW PATIENTS");		
		Assert.assertEquals(mPatient.verifyLandingPage("Add Patients"), true,"Failed to load add patient page");
		mPatient.AddPatient();
		mPatient.clickFunction("SAVE");		
		mPatient.clickFunction("Patient Name");
	}
	

	public void validateFeatureServiceNPlaceOrder(Element testDataElement) throws InterruptedException {
		OrderDetailsPage odetails = new OrderDetailsPage(driver);
		//validate the service response for estimated delivery and time
		validateFeatureService(testDataElement);
		Assert.assertEquals(odetails.validateDeliveryCostAndTime(), true);		
		Assert.assertEquals(odetails.validateTotalAmount(), true);
		odetails.clickPlaceorderGoRx();
	}


	public void validateFeatureService(Element testDataElement) {
		OrderDetailsPage orderDetails = new OrderDetailsPage(driver);
//		String[] fieldsToValidateInResp = ReadXML.getElementsByTag(testDataElement, "fieldsToValidate").toString().split(",");
//		odetails.validateFeatureDeliveryService(ReadXML.getElementsByTag(testDataElement, "ServiceURL"), ReadXML.getElementsByTag(testDataElement, "expectesStatusCode"),fieldsToValidateInResp);
	}
	
	public void validateOrderConfirmaionNGetOrderId(Element testDataElement) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException, TransformerException {
		OrderConfirmationPage oConfirmed = new OrderConfirmationPage(driver);
		Assert.assertEquals(oConfirmed.verifyGoRxOrderSuccess("Thank you for ordering with MEDLIFE"), true);
//		ReadXML.writeXmlData(testDataElement, oConfirmed.getOrderId(), "Output");
	}


	public void navToOrderHistoryNCancelOrder() throws InterruptedException {
		navToOrderHistory();
		MyOrdersPage myOrderObj = new MyOrdersPage(driver);
		CancelOrderPage cancelOrderObj = new CancelOrderPage(driver);
		myOrderObj.getspecificOrderToCancel(OrderConfirmationPage.createdOrderId);
		cancelOrderObj.verifyLandingPage("CANCEL ORDER");
		cancelOrderObj.clickFunction("CANCEL ORDER");
		cancelOrderObj.verifyLandingPage("Select a reason for cancellation");
		cancelOrderObj.clickFunction("Select");
		cancelOrderObj.clickFunction("Going out of town");
		cancelOrderObj.clickFunction("CONFIRM");
		Assert.assertEquals(myOrderObj.verifyCancelledOrderId(OrderConfirmationPage.createdOrderId), true,"Failure while cancelling order");
		//open the order again to verify cancellation
		myOrderObj.getspecificOrderToCancel(OrderConfirmationPage.createdOrderId);
		cancelOrderObj.verifyLandingPage("Cancelled");
	}

	public void navToOrderHistory() {
		HomePage homePage = new HomePage(driver);
		homePage.clickHamburger(driver);
		LinksPage linkPageObj = new LinksPage(driver);
		// order history
		linkPageObj.clickLinkFunction("Order History");
		Assert.assertEquals(linkPageObj.verifyLandingPage("MY ORDERS"),true);
	}
	

	public void noRxSelfDigitization(String productId) throws InterruptedException {
		HomePage homepage = new HomePage(driver);
		homepage.clickFunction("ORDER NOW");
		
		homepage.clickFunction("DON'T HAVE PRESCRIPTION");
		DontHavePrescriptionPage noRx = new DontHavePrescriptionPage(driver);
		Assert.assertEquals(noRx.verifyLandingPage("Search for medicines, health products.."),true);
		//search and add med
		noRx.clickFunction("Search for medicines");
		searchNAddMeds(productId);
	}
	
	public void navToFreeConsultation() {
		HomePage homepage = new HomePage(driver);
		homepage.clickFunction("ORDER NOW");
		homepage.clickFunction("DON'T HAVE PRESCRIPTION");
		DontHavePrescriptionPage noRx = new DontHavePrescriptionPage(driver);
		Assert.assertEquals(noRx.verifyLandingPage("Search for medicines, health products.."),true,"Failed while loading dont have prescription page");
		noRx.clickFunction("Consult Doctor");
		Assert.assertEquals(noRx.verifyLandingPage("Book free consultation"),true,"Failed while loading dont have prescription page");
	}
	
	public void bookConsultWithDets() {
		DontHavePrescriptionPage noRx = new DontHavePrescriptionPage(driver);
		noRx.clickFunction("Add Patient");
		addPatient();
		noRx.clickFunction("Add Address");
		selectAddress();
		noRx.clickFunction("NEXT");
		Assert.assertEquals(noRx.verifyLandingPage("Thank you for booking with MEDLIFE"), true,"Failed while loading confirmation page");
	}

	public void validateSubsDetsNConfirm(Element testDataElement,String typeOfSubs) throws ParseException {
		SubscriptionPage subscribePageObj = new SubscriptionPage(driver);
		Assert.assertEquals(subscribePageObj.verifyLandingPage("Subscription Details"),true);
		//validate subscription details page
		if(typeOfSubs.equalsIgnoreCase("Default")) {
//			Assert.assertEquals(subscribePageObj.validateSubsPlan(ReadXML.getElementsByTag(testDataElement, "subscriptionPlan")),true,"Subscription plan displayed in details page is wrong");
		}
		else if(typeOfSubs.equalsIgnoreCase("Custom")) {
//			String customSubsPlan = ReadXML.getElementsByTag(testDataElement, "Frequency")+","+ReadXML.getElementsByTag(testDataElement, "Duration");
//			Assert.assertEquals(subscribePageObj.validateSubsPlan(customSubsPlan.split(",")[1]),true,"Subscription plan displayed in details page is wrong");
		}
//		Assert.assertEquals(subscribePageObj.validateSubsDate(ReadXML.getElementsByTag(testDataElement, "startDate")),true,"Subscription date displayed in details page is wrong");
		subscribePageObj.clickFunction("Meds List");
		subscribePageObj.verifyAddedMeds();
		subscribePageObj.clickFunction("Meds List");		
		subscribePageObj.clickFunction("CONFIRM SUBSCRIPTION");			
		Assert.assertEquals(subscribePageObj.verifyLandingPage("Subscription Confirmed"),true);
		Assert.assertEquals(subscribePageObj.verifyLandingPage("Thank you for subscribing with MEDLIFE"),true);
	}


	public void navToSubsNSelectPlanNDate(Element testDataElement,String typeOfSubs)
			throws ParseException, InterruptedException {
		HomePage homepage = new HomePage(driver);
		homepage.clickFunction("Subscribe Now");
		SubscriptionPage subscribePageObj = new SubscriptionPage(driver);
		Assert.assertEquals(subscribePageObj.verifyLandingPage("Create Subscription"),true);
		if(typeOfSubs.equalsIgnoreCase("Default")) {
//			Assert.assertEquals(subscribePageObj.selectStartDateAndPlan(ReadXML.getElementsByTag(testDataElement, "subscriptionPlan"), ReadXML.getElementsByTag(testDataElement, "startDate")),true,"Failed while selecting subscription date and plan");
		}
		else if(typeOfSubs.equalsIgnoreCase("Custom")) {
//			String customSubsPlan = ReadXML.getElementsByTag(testDataElement, "Frequency")+","+ReadXML.getElementsByTag(testDataElement, "Duration");
//			Assert.assertEquals(subscribePageObj.selectStartDateAndPlan(customSubsPlan, ReadXML.getElementsByTag(testDataElement, "startDate")),true,"Failed while selecting subscription date and plan");
		}
	
	}
	
	public void navToSubsNCancel() {
		navToOrderHistory();
		MyOrdersPage myOrderObj = new MyOrdersPage(driver);
		myOrderObj.clickFunction("MY SUBSCRIPTIONS");
		Assert.assertEquals(myOrderObj.verifyLandingPage("CREATE SUBSCRIPTION"), true,"Failed while asserting subscription landing page");
		myOrderObj.clickFunction("Created Subscription");		
		CancelOrderPage cancelOrderObj = new CancelOrderPage(driver);
		Assert.assertEquals(cancelOrderObj.verifyLandingPage("CANCEL SUBSCRIPTION"), true,"Failed while loading subscription details page in My orders");
		cancelOrderObj.verifyLandingPage("CANCEL SUBSCRIPTION");
		cancelOrderObj.clickFunction("CANCEL SUBSCRIPTION");
		cancelOrderObj.verifyLandingPage("Select a reason for cancellation");
		cancelOrderObj.clickFunction("Select");
		cancelOrderObj.clickFunction("Going out of town");
		cancelOrderObj.clickFunction("Done");
	}
	

	public void addNVerifySubstitueMed() {
		SubstitutionPage substituteObj = new SubstitutionPage(driver);
		Assert.assertEquals(substituteObj.verifyLandingPage("SUBSTITUTE"), true);
		substituteObj.clickFunction("SUBSTITUTE");
		Assert.assertEquals(substituteObj.verifyLandingPage("Substitutes with same composition"), true);
		String selectedSubstituteMed = substituteObj.getTextValue("First substitute Med Text");
		substituteObj.clickFunction("First substitute result");
		Assert.assertEquals(substituteObj.verifyLandingPage("REVERT"), true);
		//verify that substituted med is not same as old one
		substituteObj.verifySubstitutedMed(selectedSubstituteMed);
	}
	

	public void reorderDeliveredOrder() {
		MyOrdersPage myOrderObj = new MyOrdersPage(driver);
		Assert.assertEquals(myOrderObj.swipeToCompleteOrderTab("LEFT"),true,"Failed while clicking on completed order filter");
		myOrderObj.clickFunction("Last delivered order");
		Assert.assertEquals(myOrderObj.verifyLandingPage("REORDER"), true, "Failed while navigating to order details page from my order page");
		myOrderObj.clickFunction("REORDER");
		myOrderObj.clickFunction("NEXT");
		Assert.assertEquals(myOrderObj.verifyLandingPage("Payment Summary"), true, "Failed while navigating to order details page from my order page");
		myOrderObj.clickFunction("Place Order");
	}
}
