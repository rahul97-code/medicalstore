package hms.insurance.gui;

import hms.main.DBConnection;
import hms.main.HMSDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class InsuranceDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public InsuranceDBConnection() {

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
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <11; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public int retrieveDatacashexist(String ID) {
		int value = 0 ;
		try {
			String query="select if(Date_format(`date`, '%Y-%m-%d')=CURDATE(),1,0) as date from cash_reports_details where user_id='"+ID+"'";
			rs = statement.executeQuery(query);
			while(rs.next()) {
				 value=rs.getInt(1);
			}
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return value;
	}
	public int retrieveCounterData()
	{
	  String query="SELECT * FROM `patient_detail` WHERE 1";
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
	public int InsertData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO cash_reports_details(user_id,user_name,entered_cash_amount,bill_cash_amount,department,reason,report_status,yesbank_amount)VALUES(?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 9; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 return  rs.getInt(1);
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `ins_id`, `ins_name`, `ins_detail` FROM `insurance_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet CheckcashAmount(String FromDate,String ToDate,String username)
	{
	  String query="SELECT sum(t.cash_amount) from (SELECT\r\n"
	  		+ "	type,\r\n"
	  		+ "	insurance,\r\n"
	  		+ "	bills_count,\r\n"
	  		+ "	Bills_amount,\r\n"
	  		+ "	online_count,\r\n"
	  		+ "	online_amount,\r\n"
	  		+ "	sum(cancel_count) as cancel_count,\r\n"
	  		+ "	sum(cancel_amount) as cancel_amount,\r\n"
	  		+ "    round((case when `type`='IPD' then sum((`Bills_amount`+0)+(`online_amount`+0)-(`cancel_amount`+0)) else 0 end),2) as ipd_total_amount,\r\n"
	  		+ "    round((case when `type`='OPD' then sum((`Bills_amount`+0)+(`online_amount`+0)-(`cancel_amount`+0)) else 0 end),2) as opd_total_amount,\r\n"
	  		+ "    round((case when (`type`='OPD' and `insurance`!='Rotary') then sum((`Bills_amount`+0)-(`cancel_amount`+0)) else 0 end),2) as cash_amount\r\n"
	  		+ "    FROM\r\n"
	  		+ "	(\r\n"
	  		+ "	SELECT\r\n"
	  		+ "		be.bill_type as type,\r\n"
	  		+ "		be.insurance_type as insurance,\r\n"
	  		+ "		sum(case when be.payment_mode in('Cash','NA') then 1 else 0 end) as bills_count,\r\n"
	  		+ "		round(sum(case when be.payment_mode in('Cash','NA') then be.total_roundoff_amount else 0 end),2) as Bills_amount,\r\n"
	  		+ "		sum(case when be.payment_mode = 'Online' then 1 else 0 end) as online_count,\r\n"
	  		+ "		round(sum(case when be.payment_mode = 'Online' then be.total_roundoff_amount else 0 end),2) as online_amount,\r\n"
	  		+ "		'0' as cancel_count,\r\n"
	  		+ "		'0' as cancel_amount\r\n"
	  		+ "	from\r\n"
	  		+ "		bills_entry be\r\n"
	  		+ "	WHERE\r\n"
	  		+ "		be.`date` BETWEEN '"+FromDate+"' and '"+ToDate+"'\r\n"
	  		+ "		and be.user_name = '"+username+"'\r\n"
	  		+ "	GROUP by 1,2	\r\n"
	  		+ "UNION all\r\n"
	  		+ "	SELECT\r\n"
	  		+ "		be.bill_type,\r\n"
	  		+ "		ci.insurance_type ,\r\n"
	  		+ "		'0',\r\n"
	  		+ "		'0',\r\n"
	  		+ "		'0',\r\n"
	  		+ "		'0',\r\n"
	  		+ "		count(be.bill_id),\r\n"
	  		+ "		round(sum(ci.total_value),2)\r\n"
	  		+ "	FROM\r\n"
	  		+ "		cancel_items ci\r\n"
	  		+ "	left join bills_entry be on\r\n"
	  		+ "		be.bill_id = ci.bill_id\r\n"
	  		+ "	WHERE\r\n"
	  		+ "		 ci.cancel_item_text1 in ('PENDING', 'CANCEL')\r\n"
	  		+ "		and ci.`date` BETWEEN '2021-05-01' and '2021-05-02'\r\n"
	  		+ "		and ci.user_name = 'Ashwani'\r\n"
	  		+ "	GROUP by 1,2	\r\n"
	  		+ "	)T\r\n"
	  		+ "GROUP by\r\n"
	  		+ "	1,2\r\n"
	  		+ "order by `type` desc,`insurance` desc) t";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllInsurance()
	{
	  String query="SELECT `ins_id`, `ins_name`, `ins_detail` FROM `insurance_detail` order by ins_name desc";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public String[] retrieveDataWithInsuranceType(String name)
	{
		String[] values=new String[2];
		values[0]="0";
		values[1]="1";
	  String query="SELECT  `ins_ratepercentage`, `ins_ratetype` FROM `insurance_detail` WHERE `ins_name`= '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		try {
			while(rs.next()){
				values[0]=rs.getObject(1).toString();
						values[1]=rs.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
	public ResultSet searchPatientWithIdOrNmae(String index)
	{
	  String query="SELECT * FROM `doctor_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM doctor_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	
	public int inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `insurance_detail`( `ins_name`, `ins_detail`, `ins_ratepercentage`, `ins_ratetype`) VALUES (?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		  for (int i = 1; i <5; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		 
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	  }
	public void deleteItemInsuRow(String insuName,String itemID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `insurance_detail` WHERE `ins_name`=? and `ins_id`=?");
		preparedStatement.setString(1, insuName);
		preparedStatement.setString(2, itemID);
		preparedStatement.executeUpdate();
	}
	public ResultSet retrieveInsuranceItems()
	{
	  String query="SELECT `ins_id`,`ins_name`,`ins_detail` FROM `insurance_detail` ORDER BY `ins_name` ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
}
