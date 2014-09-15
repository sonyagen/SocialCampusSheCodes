package il.ac.technion.logic;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeDateStringFactory {

	static public String getTimeStr(Calendar c){
		return new SimpleDateFormat("HH:mm").format(new Date(c.getTimeInMillis()));
	}
	static public String getDateStr(Calendar c){
		return new SimpleDateFormat("dd/MM").format(new Date(c.getTimeInMillis()));
	}
	static public String getTimeStr(Long t){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t);
		return new SimpleDateFormat("HH:mm").format(new Date(c.getTimeInMillis()));
	}
	static public String getDateStr(Long t){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t);
		return new SimpleDateFormat("dd/MM").format(new Date(c.getTimeInMillis()));
	}
}
