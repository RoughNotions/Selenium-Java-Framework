package com.medlife.utilityfactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseUtil {

    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public String isoDateFormat(int year, int month, int day,int hours) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, year); // to get next year add 1; to get previous year -1
        cal.add(Calendar.MONTH, month); // to get next month add 1; to get previous month -1
        cal.add(Calendar.DAY_OF_MONTH, day); // to get next day add 1; to get previous day -1
        cal.add(Calendar.HOUR_OF_DAY, hours);
        Date expiryDate = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.000'Z'");
        String strDate = formatter.format(expiryDate);
        return strDate;
    }
    
    public String reportDate() {
		String date =null;
		String pattern = "yyyy_MM_dd_HH_mm_ss";
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	date = simpleDateFormat.format(new Date());
    	return date;
	}
}
