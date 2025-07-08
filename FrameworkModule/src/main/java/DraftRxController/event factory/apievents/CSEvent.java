package com.medlife.eventfactory.apievents;

import com.medlife.databasefactory.DbUtil;
import com.medlife.databasefactory.SMSNotificationDbUtil;
import com.medlife.dataproviderfactory.TestDataFactory;
import com.medlife.eventfactory.IEvent;
import com.medlife.controller.CustomerController;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSEvent implements IEvent {

	public Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	@Override
	public void processEvent(String event, TestDataFactory dataFactory, File htmlFile)
			throws Exception {
		if (event.contains("uploadImage")) {
			new CustomerController().isRegistered(dataFactory, dataFactory.getCustomerMobileNumber(), htmlFile);
			new CustomerController().getOTP(dataFactory, dataFactory.getCustomerMobileNumber(), htmlFile);
			String otpNumber = new DbUtil().getOtpFromDB(dataFactory.getCustomerMobileNumber());
			logger.log(Level.INFO, Thread.currentThread().getName() + "OTP number => " + otpNumber);
			new CustomerController().verifyOTP(dataFactory, otpNumber, htmlFile);
			new CustomerController().signUpUser(dataFactory, htmlFile);
			if (dataFactory.getTestCaseParameters().optString("user").equalsIgnoreCase("signedIn")) {
				new CustomerController().signInUser(dataFactory, htmlFile);
			}
			new CustomerController().createDraftRxId(dataFactory, htmlFile);
			new CustomerController().uploadImageToDraftRxId(dataFactory, htmlFile);
			new SMSNotificationDbUtil().isSMSSent("customer_otp_verification", true);
			new SMSNotificationDbUtil().isSMSSent("go_rx_all_medicine", true);
		} else if (event.contains("reOrder")) {
			new CustomerController().signInUser(dataFactory, htmlFile);
			new CustomerController().createReOrder(dataFactory, htmlFile);
		} else if (event.contains("subscription")) {
			new CustomerController().isRegistered(dataFactory, dataFactory.getCustomerMobileNumber(), htmlFile);
			new CustomerController().getOTP(dataFactory, dataFactory.getCustomerMobileNumber(), htmlFile);
			String otpNumber = new DbUtil().getOtpFromDB(dataFactory.getCustomerMobileNumber());
			logger.log(Level.INFO, Thread.currentThread().getName() + "OTP number => " + otpNumber);
			new CustomerController().verifyOTP(dataFactory, otpNumber, htmlFile);
			new CustomerController().signUpUser(dataFactory, htmlFile);
			new CustomerController().signInUser(dataFactory, htmlFile);
			new CustomerController().createSubscription(dataFactory, htmlFile);
		} else if (event.contains("selfDigitize")) {
			new CustomerController().isRegistered(dataFactory, dataFactory.getCustomerMobileNumber(), htmlFile);
			new CustomerController().getOTP(dataFactory, dataFactory.getCustomerMobileNumber(), htmlFile);
			String otpNumber = new DbUtil().getOtpFromDB(dataFactory.getCustomerMobileNumber());
			logger.log(Level.INFO, Thread.currentThread().getName() + "OTP number => " + otpNumber);
			new CustomerController().verifyOTP(dataFactory, otpNumber, htmlFile);
			new CustomerController().signUpUser(dataFactory, htmlFile);
			if (dataFactory.getTestCaseParameters().optString("user").equalsIgnoreCase("signedIn")) {
				new CustomerController().signInUser(dataFactory, htmlFile);
			}
			new CustomerController().createDraftRxId(dataFactory, htmlFile);
			new CustomerController().uploadImageToDraftRxId(dataFactory, htmlFile);
			new CustomerController().selfDigitize(dataFactory,htmlFile);
		} else if (event.contains("NORXselfDigitize")) {
			// TODO
			/**
			 * Implement the logic
			 */

		} else if (event.contains("noRxOrder")) {
			// TODO
			/**
			 * Implement the logic
			 */

		}

	}
}
