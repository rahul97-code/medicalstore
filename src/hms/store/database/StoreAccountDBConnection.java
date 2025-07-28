package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class StoreAccountDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public StoreAccountDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public ResultSet retrieveData(String query) {
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllReceptionName(String department)
	{
	  String query="select * from (select 'Admin' as dept, admin_username from admin_account aa union select 'Store',store_name from store_detail sd)t where t.dept='"+department+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet saveActivity(String username, String id,String depart)
	{
		String query="DELETE FROM activity\r\n"
				+ "WHERE out_date < NOW() - INTERVAL 5 DAY";
		
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	   query="insert into activity (user_name, user_id, in_date, in_time,department) values('"+username+"','"+id+"',CURDATE(),CURTIME(),'"+depart+"')";
		try {
			 statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		
         query="select id from activity where in_date =CURDATE() and user_name='"+username+"' and department ='"+depart+"'order by in_time desc limit 1";
         try {
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet retrieveuseractivity(String username, String depart, String FromDate, String ToDate)
	{
	  String query="SELECT user_id, user_name, in_date, in_time, out_date, out_time, report_open  FROM activity where user_name='"+username+"' and department='"+depart+"' and in_date BETWEEN '"+FromDate+"' and '"+ToDate+"';";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void closeActivity( String id)
	{
	  String query="update activity set out_date=CURDATE(),out_time=CURTIME() where id='"+id+"'";
		try {
			 statement.executeUpdate(query);
			 System.out.print(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	public ResultSet CashReportsDATA(String fromDate,String toDate)
	{
	  String query="SELECT\r\n"
	  		+ "	user_name,\r\n"
	  		+ "	date,\r\n"
	  		+ "	yesbank_amount,\r\n"
	  		+ "	entered_cash_amount,\r\n"
	  		+ "	@a:=(yesbank_amount + entered_cash_amount) as total_enter,\r\n"
	  		+ "	@b:=bill_cash_amount,\r\n"
	  		+ "	@c:=(select round(COALESCE(sum(total_amount),0),2) from bills_entry b where bill_type like '%return%' and b.`date`=date(crd.date) and b.user_name=crd.user_name) as c,\r\n"
	  		+ " round(((@a+@c)-@b),2) as diff\r\n,"
	  		+ "	reason\r\n"
	  		+ "from\r\n"
	  		+ "	cash_reports_details crd\r\n"
	  		+ "where\r\n"
	  		+ "	CAST(`date` as date) BETWEEN '"+fromDate+"' and '"+toDate+"' ";
	  try {
		  System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void closeActivity1( String report)
	{
	  String query="update activity set report_open = report_open+1 where id='"+report+"'";
		try {
			 statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	public ResultSet retrieveAllData21()
	{
	  String query="SELECT DISTINCT admin_username  FROM admin_account ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	
	public ResultSet retrieveAllReceptionName()
	{
	  String query="SELECT DISTINCT `store_username`  FROM store_detail";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllReceptionName1()
	{
	  String query="SELECT DISTINCT `store_name`  FROM store_detail";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveAllReceptionName1(String fromdate, String toDate)
	{
	  String query="SELECT a.user_name from(\r\n"
	  		+ "SELECT DISTINCT user_name from bills_entry be where be.`date` between '"+fromdate+"' and '"+toDate+"'\r\n"
	  		+ "union\r\n"
	  		+ "select DISTINCT user_name from cancel_items ci where ci.`date` between '"+fromdate+"' and '"+toDate+"'\r\n"
	  		+ ") a GROUP BY a.user_name";
		try {
			rs = statement.executeQuery(query);
			System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "UPDATE `store_detail` SET `store_name`=?,`store_username`=?,`store_password`=?,`store_telephone`=?,`store_address`=?,`store_qualification`=?,`collection_report_access`=?,`store_active`=?,`update_item_access`=? WHERE  `store_id`="+data[9];
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <10; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void updateDataLastLogin(String storeID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `store_detail` set `store_lastlogin` = '"+timeStamp+"' where `store_id` = "+storeID);
	  }
	
	public void updateDataPassword(String storeUN,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `store_detail` SET `store_password`='"+password+"' where `store_username` = '"+storeUN+"'");
	  }
	public int retrieveCounterData()
	{
	  String query="SELECT * FROM `store_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
        try {
			while(rs.next()){
			NumberOfRows++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfRows;	
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `store_id`, `store_name`, `store_username`, `store_password`, `store_telephone`, `store_address`, `store_qualification` FROM `store_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData2(String index)
	{
	  String query="SELECT `store_id`, `store_name`, `store_telephone`, `store_address` FROM `store_detail` WHERE store_id LIKE '%"+index+"%' OR store_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData2()
	{
	  String query="SELECT `store_id`, `store_name`, `store_telephone`, `store_address` FROM `store_detail` WHERE1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex(String name)
	{
	  String query="SELECT `store_id` FROM `store_detail` WHERE `store_name` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	} 
	public ResultSet retrieveDataWithID(String opID)
	{
	  String query="SELECT `store_name`, `store_username`, `store_password`,`store_telephone`, `store_address`, `store_qualification`,`collection_report_access`,`store_active`,`update_item_access` FROM `store_detail` WHERE `store_id` = "+opID+"";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveUserPassword(String name,String pass)
	//`collection_report_access` not available column
	{
	  String query="SELECT `store_id`, `store_name`,`collection_report_access`,`update_item_access` FROM `store_detail` WHERE `store_username` = '"+name+"' AND `store_password` = '"+pass+"' AND store_active='1'";
	  System.out.print(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrievePassword(String storeUN)
	{
	  String query="SELECT `store_password` FROM `store_detail` WHERE `store_username` = '"+storeUN+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveUsernameDetail(String name)
	{
	  String query="SELECT  `store_id`, `store_name`,`store_lastlogin` FROM `store_detail` WHERE `store_username` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPatientWithIdOrNmae(String index)
	{
	  String query="SELECT * FROM `store_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM store_detail WHERE store_id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `store_detail`( `store_name`, `store_username`, `store_password`, `store_telephone`, `store_address`, `store_qualification`,`collection_report_access`) VALUES  (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
