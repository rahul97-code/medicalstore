package hms.departments.database;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

import hms.main.DBConnection;
import hms.main.HMSDBConnection;

public class DepartmentDBConnection extends HMSDBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public DepartmentDBConnection() {

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
		String insertSQL     = "UPDATE `departments_detail` SET `operator_name`=?,`operator_username`=?,`operator_password`=?,`operator_labname`=?,`operator_opdroom`=?,`operator_telephone`=?,`operator_address`=?,`operator_qualification`=? WHERE  `operator_id`="+data[8];
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <9; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void updateDataLastLogin(String operatorID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `departments_detail` set `operator_lastlogin` = '"+timeStamp+"' where `operator_id` = "+operatorID);
	  }
	
	public void updateDataPassword(String operatorID,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `departments_detail` SET `operator_password`='"+password+"' where `operator_id` = "+operatorID);
	  }
	public ResultSet retrieveDptStock(String itemid, String departmentName) {
		String query = "SELECT  SUM(total_stock) FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND total_stock>0 and batch_id<>''";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
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
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `department_id`, `department_name`, `department_username`, `department_password`, `department_type`, `department_room_name`, `department_room_no`, `last_login`, `department_status`, `entry_user`, `creation_date` FROM `departments_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet searchItemWithIdOrNmae(String index) {
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_risk_type` FROM `items_detail` where item_id LIKE '%"
				+ index
				+ "%' OR item_name LIKE '%"
				+ index
				+ "%' OR item_desc LIKE '%"
				+ index
				+ "%'  OR item_salt_name LIKE '%" + index + "%'";
		String q="SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_risk_type` FROM `items_detail` where item_id LIKE '%" + index + "%' "
				+ "union all SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_risk_type` FROM `items_detail` where item_name LIKE '%" + index + "%'"
				+ " union all SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_risk_type` FROM `items_detail` where item_desc LIKE '%" + index + "%' "
				+ " union all SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_risk_type` FROM `items_detail` where item_salt_name LIKE '%" + index + "%'"
				+ " GROUP by item_id ";
		System.out.println(q);
		try {
			rs = statement.executeQuery(q);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllWardType()
	{
	  String query="SELECT  DISTINCT(`ward_type`) FROM `ward_cahrges` WHERE 1";
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
	  String query="SELECT  `operator_id`, `operator_name`, `operator_labname`, `operator_opdroom`, `operator_telephone` FROM `departments_detail` WHERE operator_id LIKE '%"+index+"%' OR operator_name LIKE '%"+index+"%'";
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
	  String query="SELECT  `operator_id`, `operator_name`, `operator_labname`, `operator_opdroom`, `operator_telephone` FROM `departments_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDataWithName(String name)
	{
	  String query="SELECT `department_id`, `department_name`, `department_username`, `department_password`, `department_type`, `department_room_name`, `department_room_no`, `last_login`, `department_status`, `entry_user`, `creation_date` FROM `departments_detail` WHERE `department_name`='"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDptLastWeekLimit(String item_id, String departID) {
		String query = "SELECT case when SUM(issued_qty)<>'' THEN SUM(issued_qty) else 0 end as qty from issued_department_register idr WHERE item_id ='"+item_id+"' and department_id ='"+departID+"' and `date` BETWEEN DATE_SUB(CURRENT_DATE() , INTERVAL 10 DAY) and  CURRENT_DATE() ";
		System.out.println(query);
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
	  String query="SELECT `operator_name`, `operator_username`, `operator_password`, `operator_labname`, `operator_opdroom`, `operator_telephone`, `operator_address`, `operator_qualification` FROM `departments_detail` WHERE `operator_id` = "+opID+"";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveUserPassword(String name,String pass)
	{
	  String query="SELECT `department_name`,`department_id` FROM `departments_detail` WHERE  `department_username` = '"+name+"' AND  `department_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrievePassword(String operatorID)
	{
	  String query="SELECT `operator_password` FROM `departments_detail` WHERE `operator_id` = '"+operatorID+"'";
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
	  String query="SELECT  `operator_id`, `operator_name`, `operator_labname`, `operator_opdroom`,`operator_username`,`operator_password`,`operator_lastlogin` FROM `departments_detail` WHERE `operator_username` = '"+name+"'";
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
	  String query="SELECT * FROM `departments_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieve_department()
	{
	  String query="SELECT `department_name` FROM `departments_detail`";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM departments_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `departments_detail`(`department_name`, `department_username`, `department_password`, `department_type`, `department_room_name`, `department_room_no`, `department_status`, `entry_user`, `creation_date`) VALUES (?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <10; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
