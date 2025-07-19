package hms.formula.manipulation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hms.store.database.CancelRestockFeeDB;

public class ReturnCharges {

	// TODO Auto-generated constructor stub
	public double getReturnCharges(String issuedDate,String type) {
		// TODO Auto-generated method stub
		CancelRestockFeeDB db=new CancelRestockFeeDB();
		ResultSet rs=db.retrieveData1(type);
		String[][] data=new String[5][5];
		int i=0;
		try {
			while(rs.next())
			{
				data[i][0]=rs.getObject(2).toString();
				data[i][1]=rs.getObject(3).toString();
				data[i][2]=rs.getObject(4).toString();
				data[i][3]=rs.getBoolean(5)+"";
				data[i][4]=rs.getObject(6).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal = Calendar.getInstance();  
		try{  
			cal.setTime(sdf.parse(issuedDate));  
		}catch(ParseException e){  
			e.printStackTrace();  
		}  
		String issdate = sdf.format(cal.getTime());  
		Date d = null;
		try {
			d = sdf.parse(issdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("issued date :" +sdf.format(d));
		cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0][1]));  
		cal.add(Calendar.MONTH, Integer.parseInt(data[0][2]));
		String dateAfter = sdf.format(cal.getTime());  
		Date d1 = null;
		try {
			d1 = sdf.parse(dateAfter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date currentDate = new Date(System.currentTimeMillis());

		System.out.println("date1 :" +sdf.format(d1));
		System.out.println("date2 :" +sdf.format(currentDate));

		if(currentDate.after(d) && currentDate.before(d1))
		{	System.out.println("d1");
		if(Boolean.parseBoolean(data[0][3]))
			return Double.parseDouble(data[0][0]);
		//			cuttingFee=Double.parseDouble(data[0][4]);
		}
		cal.add(Calendar.DAY_OF_MONTH, (Integer.parseInt(data[1][1])+Integer.parseInt(data[0][1])));  
		cal.add(Calendar.MONTH, Integer.parseInt(data[1][2])+Integer.parseInt(data[0][2]));
		String dateAfter1 = sdf.format(cal.getTime());  
		Date d3 = null;
		try {
			d3 = sdf.parse(dateAfter1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("date3 :" +sdf.format(d1));
		System.out.println("date4 :" +sdf.format(d3));

		if(currentDate.after(d1) && currentDate.before(d3)) 
		{System.out.println("d2");
		return Double.parseDouble(data[1][0]);
		//			cuttingFee=Double.parseDouble(data[1][4]);
		}
		cal.add(Calendar.DAY_OF_MONTH,( Integer.parseInt(data[2][1])+Integer.parseInt(data[0][1])+Integer.parseInt(data[1][1])));  
		cal.add(Calendar.MONTH, Integer.parseInt(data[2][2])+Integer.parseInt(data[0][2])+Integer.parseInt(data[1][2]));
		String dateAfter2 = sdf.format(cal.getTime());  
		Date d5 = null;
		try {
			d5 = sdf.parse(dateAfter2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("date5 :" +sdf.format(d3));
		System.out.println("date6 :" +sdf.format(d5));
		if(currentDate.after(d3))
		{System.out.println("d3");
		return Double.parseDouble(data[2][0]);
		//			cuttingFee=Double.parseDouble(data[2][4]);
		}
		return 0.0;

	}

}
