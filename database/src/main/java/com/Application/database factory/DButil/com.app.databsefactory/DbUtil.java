package com.medlife.databasefactory;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class DbUtil {

    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public static final String DB_UTIL = System.getProperty("user.dir") + "/../database/src/main/resources/dbQueries";
    public static DB db;
    public static MedlifeMongoDbConnect medlifeDb = new MedlifeMongoDbConnect();




    public String getOtpFromDB(String customerMobile) {
        String myDoc = medlifeDb.getData("OTPs", "mobileNumber", customerMobile);
        JSONObject document = new JSONObject(myDoc);
        String otp = document.optString("otpNumber");
        return otp;
    }

    public JSONObject dbQueryJSONReader(String jsonFilePath, String collectionName) throws IOException {
        Collection<Object[]> provider = new ArrayList<Object[]>();
        File file = new File(jsonFilePath);
        String content = FileUtils.readFileToString(new File(file.getCanonicalPath()), "utf-8");
        JSONObject apiObject = new JSONObject(content);
//        System.out.println(Thread.currentThread().getName()+" ***** "+"apiObject==>" + apiObject);
        JSONArray serviceApi = new JSONArray(apiObject.get("services").toString());
        JSONObject serviceObj = new JSONObject();
        for (int i = 0; i < serviceApi.length(); i++) {
            if (serviceApi.getJSONObject(i).names().toString().contains(collectionName)) {
                JSONObject data = (JSONObject) serviceApi.get(i);
                serviceObj = (JSONObject) data.get(collectionName);
//                System.out.println(Thread.currentThread().getName()+" ***** "+"serviceObj==>" + serviceObj);
            }
        }
        return serviceObj;
    }

    public String getRxIdFromDraftRxId(String draftRxId) {
        String myDoc = medlifeDb.getLatestUpdatedData("Prescription", "draftRxId", draftRxId, "date");
        JSONObject document = new JSONObject(myDoc);
        return document.optString("_id");
    }

    public String getCityFromFcId(String fcId) {
        String fieldValue = medlifeDb.getFieldValue("fcData", "fcId", fcId, "city");
        logger.log(Level.INFO,Thread.currentThread().getName() + "City is " + fieldValue);
        return fieldValue;
    }

    public String getRandomLocalityFromFcdata(String fcid) {
        List<DBObject> dbObjectList = medlifeDb.getDataList("fcData", "fcId", fcid);
        DBObject dbObject = getRandomElementFromList(dbObjectList);
        logger.log(Level.INFO,Thread.currentThread().getName() + "DB object ==> " + dbObject);
        return dbObject.get("subLocation").toString();
    }

    public DBObject getRandomElementFromList(List<DBObject> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public String getOrderIdFromPrescriptionId(String presId) {
        String fieldValue = medlifeDb.getFieldValue("order", "prescriptionId", presId, "_id");
        return fieldValue;
    }

    public String checkAdminIdExists(String adminEmailId, String fcId) {
        String myDoc = medlifeDb.getData("Admins", "email", adminEmailId);
        if (myDoc != null) {
            JSONObject document = new JSONObject(myDoc);
            String existingDaAgentId = document.optString("_id");
            return existingDaAgentId;
        } else {
            return null;
        }
    }

    //check whether product is already exists
    public String checkProductExists(String productId) {
        String myDoc = medlifeDb.getData("Product", "_id", productId);
        if (myDoc != null) {
            JSONObject document = new JSONObject(myDoc);
            String existingProductId = document.optString("_id");
            return existingProductId;
        } else {
            return null;
        }
    }

    public String checkInventoryExists(String productId, String batchNumber) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("Inventory", "item.product.$id", productId, "item.batchNo",
                batchNumber);
        if (myDoc != null) {
            return myDoc;
        } else {
            return null;
        }
    }

    public String CheckSlotExists(String locality, String routeType,String route_id) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("route", "locality", locality, "routeType", routeType);
        String myDoc1 = medlifeDb.getDocumentFromAndQuery("route", "_id", route_id, "routeType", routeType);
        if(myDoc==null && myDoc1==null) {
        	return null;
        }
        else {
            return myDoc==null?myDoc1:myDoc;
        }
    }

    public String checkLocationExists(String fcId, String product, int batchNumber) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("Location", "fcId", fcId, "productId", product, "batchNo",
                "BATCH0" + batchNumber);
        if (myDoc != null) {
            JSONObject document = new JSONObject(myDoc);
            String existingProductId = document.optString("productId");
            return existingProductId;
        } else {
            return null;
        }
    }

    public String checkWarehouseExists(String fcId, String prefixType) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("WarehouseMapping", "fcId", fcId, "type", prefixType);
        if (myDoc != null) {
            JSONObject document = new JSONObject(myDoc);
            String id = document.optString("_id");
            return id;
        } else {
            return null;
        }
    }
    
    public boolean checkPromoCodeExists(String promoCode) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("Promotions", "promoCode", promoCode);
        if (myDoc != null) {
            return true;
        } else {
            return false;
        }
	}
    
    public boolean checkDiscountConfig(String productId) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("DiscountConfigs", "name", productId);
        if(myDoc !=null) {
        	return true;
        }
        else {
        	return false;
        }
	}

    public String checkProdVariantExists(String brandId) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("ProductVariant", "brand", brandId);
        if (myDoc != null)
            return myDoc;
        else
            return null;
    }

    public static String getXCode() {
        String myDoc = medlifeDb.getData("Properties", "key", "xcode", "value");
        if (myDoc != null)
            return myDoc;
        else
            return null;
    }
    
    public String checkSemiProdVariantExists(String brandId) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("SemiProductVariant", "brand", brandId);
        if (myDoc != null)
            return myDoc;
        else
            return null;
    }

    public String getTripId(String OrderId) {
        LinkedHashMap<String, String> keyvalue=new LinkedHashMap<>();
        keyvalue.put("status", "UnAssigned");
        keyvalue.put("tripList.tripList.orderId",OrderId);
        String tripid = medlifeDb.getData("Trips", keyvalue, "_id");
        logger.log(Level.INFO,"Trip Id ====> " + tripid);
        return tripid;
    }

    public String getStateFromFcId(String fcId) {
            String fieldValue = medlifeDb.getFieldValue("fcData", "fcId", fcId, "state");
            logger.log(Level.INFO,Thread.currentThread().getName() + "state is " + fieldValue);
            return fieldValue;
        }

    
    public void cancelActiveOrdersForProduct(String productId) {
        String myDoc = medlifeDb.getDocumentFromAndQuery("order","orderItems.item.productId", productId);
        try {
			JSONObject document = new JSONObject(myDoc);
			String orderId = document.optString("_id");
	        logger.log(Level.INFO,orderId+" current state ==> " + new DbUtil().getOrderState(orderId));
            medlifeDb.updateValue("order", "orderItems.item.productId", productId, "state._class",
					"com.medlife.order.state.Cancelled");
	        logger.log(Level.INFO,orderId+" is now ==> " + new DbUtil().getOrderState(orderId));
		} catch (Exception e) {
	        logger.log(Level.INFO,"No orders with this SKU");
		}
    }

    public void validateRequestTypeFromDraftRxId(String draftRxId, String requestType) {
		String requestTypeDB =  medlifeDb.getFieldValue("DraftRx", "_id", draftRxId, "requestType");
        logger.log(Level.INFO, "requestType for " + draftRxId + " ==> " +requestTypeDB);
        Assert.assertEquals(requestTypeDB, requestType,"mismatch in the request Type");
		}

	
	public String getOrderState(String orderId) {
		String state1 = getOrderStatus(orderId);
		String state2 = state1.replaceAll("com.medlife.order.state.", "");
		String state3 = state2.replaceAll("[^\\w]", "");
		String orderState = state3.replaceAll("_class", "");
		return orderState;
		}
	
	public  String getOrderStatus(String orderId) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        String orderstatus = medlifeDb.getFieldValue("order", "_id", orderId, "state");
		return orderstatus.toString();
	}

	public String getDoctorMobileNumber(String doctorId) {
		 String myDoc = medlifeDb.getData("Doctors", "_id", doctorId);
	     JSONObject document = new JSONObject(myDoc);
	     String mobileNumber = document.optString("mobile");
	     return mobileNumber;
	   
	}
	
	public String getClientId(String clientName) {
		String clientId =  medlifeDb.getFieldValue("clients", "name", clientName, "clientId");
		return clientId;
		
	}
	
	public String getClientSecret(String clientName) {
		String clientSecret =  medlifeDb.getFieldValue("clients", "name", clientName, "clientSecret");
		return clientSecret;
		
	}

	public String getDataFromCollection(String collection, String key, String value) {
        String myDoc = medlifeDb.getData(collection, key, value);
		return myDoc;
	}

	public void insertRetentionOrder( String mobileNumber,boolean connected, boolean active) {
		JSONObject obj=new JSONObject();
		obj.put("_id",mobileNumber);
		obj.put("connected", connected);
		obj.put("active", active);
		medlifeDb.insertDocument("RetentionOrder", obj);
	}
	
	
	public String getOrderType(String orderId) {
        String orderType = medlifeDb.getData("order", "_id", orderId, "orderType");
		return orderType;
	}

	public boolean checkDoctorExists(String string) {
		String myDoc = medlifeDb.getData("Doctors", "_id", string);
		if(myDoc==null)
			return false;
		return true;
	}
	
	public String getDoctorFirstName(String doctorId) {
		String firstName = medlifeDb.getFieldValue("Doctors", "_id", doctorId, "firstName");
		logger.log(Level.INFO, "firstName==> " + firstName);
		return firstName;
	}

	public String getDoctorLasttName(String doctorId) {
		String lastName = medlifeDb.getFieldValue("Doctors", "_id", doctorId, "lastName");
		logger.log(Level.INFO, "lastName==> " + lastName);
		return lastName;
	}

	public String getDoctorRegistrationId(String doctorId) {
		String registrationId = medlifeDb.getFieldValue("Doctors", "_id", doctorId, "licenseNo");
		logger.log(Level.INFO, "registrationId==> " + registrationId);
		return registrationId;
	}

	public String getDoctorClinicName(String doctorId) {
		String myDoc = medlifeDb.getData("Doctors", "_id", doctorId);
		JSONObject document = new JSONObject(myDoc);
		JSONArray plsObj = document.getJSONArray("pls");
		JSONObject plName = plsObj.getJSONObject(0);
		String clinicName = plName.getString("plName");
		logger.log(Level.INFO, "clinicName ==> " + clinicName);
		return clinicName;
	}

	public String getDoctorPincode(String doctorId) {
		String myDoc = medlifeDb.getData("Doctors", "_id", doctorId);
		JSONObject document = new JSONObject(myDoc);
		JSONArray plsObj = document.getJSONArray("pls");
		JSONObject plAdrs = plsObj.getJSONObject(0);
		JSONObject plAdrsObj = plAdrs.getJSONObject("plAdrs");
		String pinCode = plAdrsObj.getString("state");
		logger.log(Level.INFO, "Dr Clinic Pincode ==> " + pinCode);
		return pinCode;
	}

	public String getDoctorCity(String doctorId) {
		String myDoc = medlifeDb.getData("Doctors", "_id", doctorId);
		JSONObject document = new JSONObject(myDoc);
		JSONArray plsObj = document.getJSONArray("pls");
		JSONObject plAdrs = plsObj.getJSONObject(0);
		JSONObject plAdrsObj = plAdrs.getJSONObject("plAdrs");
		String city = plAdrsObj.getString("city");
		logger.log(Level.INFO, "Dr Clinic City ==> " + city);
		return city;
	}

	public String getDoctorState(String doctorId) {
		String myDoc = medlifeDb.getData("Doctors", "_id", doctorId);
		JSONObject document = new JSONObject(myDoc);
		JSONArray plsObj = document.getJSONArray("pls");
		JSONObject plAdrs = plsObj.getJSONObject(0);
		JSONObject plAdrsObj = plAdrs.getJSONObject("plAdrs");
		String state = plAdrsObj.getString("state");
		logger.log(Level.INFO, "Dr Clinic State ==> " + state);
		return state;
	}

	
}
