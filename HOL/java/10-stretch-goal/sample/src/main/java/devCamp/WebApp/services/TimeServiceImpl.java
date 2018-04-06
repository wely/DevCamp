package devCamp.WebApp.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {
	
	public String startDateToString(Date date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH':00:00'");
		//SimpleDateFormat df2 = new SimpleDateFormat("HH");
		//return df1.format(date) + "T" + df2.format(date) + ":00:00";
		return df1.format(date);
	}
    
    @SuppressWarnings("deprecation")
	public String endDateToString(Date date) {
    	date.setHours(date.getHours() + 1);
    	return startDateToString(date);
    }
    
    @SuppressWarnings("deprecation")
    public Date getTime() {
    	Date result = new Date();
		result.setDate(result.getDate() + 1);
		while (!IsValidTime(result))
		{
			result.setHours(result.getHours() + 3);
		}
		return result;
    }
    
	private	boolean IsValidTime(Date time)
	{
		if (time.getHours() < 8)
		{
			return false;
		}
		if (time.getHours() > 17)
		{
			return false;
		}
		if (time.getDay() ==6)
		{
			return false;
		}
		if (time.getDay() == 0)
		{
			return false;
		}
		return true;
	}
}
