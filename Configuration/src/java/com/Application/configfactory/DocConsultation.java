package com.medlife.configfactory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.medlife.controller.DigideskController;
import com.medlife.databasefactory.MedlifeMongoDbConnect;
import com.medlife.dataproviderfactory.TestDataFactory;

import io.restassured.response.Response;

public class DoctorConsultationConfig {
	public static Logger logger = Logger.getLogger(DoctorConsultationConfig.class.getSimpleName());

	public void doctorConsultationConfig(TestDataFactory dataFactory,File htmlFile)
			throws IOException, InterruptedException {
		String isEnabled = new MedlifeMongoDbConnect().getFieldValue("FeatureStatus", "name", "consultation",
				"isEnabled");
		logger.log(Level.INFO,
				Thread.currentThread().getName() + "Consultation flag in FeatureStatus ==> " + isEnabled);

		boolean consultationEnabled = Boolean.valueOf(isEnabled);
		if (consultationEnabled) {
			String isRxSourceExist = new MedlifeMongoDbConnect().getFieldValue("FeatureStatus", "name", "consultation",
					"presciptionSource");
			String source = dataFactory.getRxSource();
			boolean rxSourceEligible = false;
			if (isRxSourceExist.contains(source)) {
				rxSourceEligible = true;
			}

			new DigideskController().LoginToDigidesk(dataFactory, htmlFile);
			// Get Blacklisted Drugtype based on pincode & customer mobile #
			Response res = new DigideskController().drugTypePincodeCheck(dataFactory, htmlFile);
			String blackListedScheduledDrugTypes =  res.jsonPath().getString("drugTypes");
			logger.log(Level.INFO, Thread.currentThread().getName() + "Blacklisted DrugTypes " + dataFactory.getFcId()
					+ " ==> " + blackListedScheduledDrugTypes);

			boolean dcpBlackListed = false;
			if (rxSourceEligible) {
				JSONArray prodarray = dataFactory.getTestCaseParameters().optJSONArray("products");

				for (int j = 0; j < prodarray.length(); j++) {
					JSONObject prodObj = (JSONObject) prodarray.get(j);
					String drugType = prodObj.getString("drugType");
					if (blackListedScheduledDrugTypes.contains(drugType)) {
						dcpBlackListed = true;
						logger.log(Level.INFO, "DRUGTYPE '" + drugType
								+ "' ==> BLACKLISTED FOR [DCP] DOCTOR CONSULTATION " + dataFactory.getFcId());
						break;
					} else {
						dcpBlackListed = false;
						logger.log(Level.INFO, "DRUGTYPE " + drugType
								+ " ==> NOT BLACKLISTED FOR [DCP] DOCTOR CONSULTATION " + dataFactory.getFcId());
					}
				}
			}
			dataFactory.setDCPBlackListed(dcpBlackListed);
			logger.log(Level.INFO, "DCP Blacklisted ? ==> " + dataFactory.isDCPBlackListed());

		}
	}

}
