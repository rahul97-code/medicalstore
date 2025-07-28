package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class CancelBillDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public CancelBillDBConnection() {

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
	public Boolean Exist(String billNo) {
		String query="SELECT case when count(*)>0 then 1 else 0 end from cancel_items WHERE bill_id = '"+billNo+"' and `cancel_item_text1`='PENDING'\r\n"
				+ "";
		try {
			rs = statement.executeQuery(query);
			while(rs.next())
			{
				return rs.getBoolean(1);
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	public ResultSet retrieveCancelBillItemsData(String bill_no,String status) {

		String query = "SELECT `item_name`, `item_batch`, `unit_price`, `quantity`, `tax_amount`, `total_value`, `expiry_date`, `remarks`,`date`,`time`,`user_name` FROM `cancel_items` WHERE `bill_id`="
				+ bill_no + " and cancel_item_text1 like '%"+status+"%'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public void updateData(String receiptNo) throws Exception {
		String insertSQL = "DELETE FROM `cancel_items` WHERE bill_id='"+receiptNo+"' AND `cancel_item_text1`='PENDING'";
		statement.executeUpdate(insertSQL);
	}
	public void updateReturnBillStatus(String receiptNo) throws Exception {
		String insertSQL = "UPDATE `bills_entry` SET `bill_text3`='OK' WHERE bill_id='"+receiptNo+"'";
		statement.executeUpdate(insertSQL);
	}
	public ResultSet retrieveCancelBills() {

		String query = "SELECT\r\n"
				+ "	be.user_name,\r\n"
				+ "	be.bill_id,\r\n"
				+ "	be.insurance_type,\r\n"
				+ "	be.total_roundoff_amount,\r\n"
				+ "	be.`date`,\r\n"
				+ "	be.`time`,\r\n"
				+ "	be.bill_text3,\r\n"
				+ "	COALESCE(be.return_reason,''),\r\n"
				+ "	be.bill_type \r\n"
				+ "FROM\r\n"
				+ "	 bills_entry be  \r\n"
				+ "where\r\n"
				+ "	bill_text3 = 'n/a' and bill_type like '%return%'\r\n"
				+ "ORDER BY\r\n"
				+ "	CAST(be.date as UNSIGNED)";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveCancelIPDBills() {

		String query = "SELECT user_name, bill_id, insurance_type, SUM(total_value), `date`, `time`, cancel_item_text1, remarks FROM cancel_items where cancel_item_text1='PENDING' and insurance_type != 'Unknown' GROUP BY bill_id ORDER BY CAST(date as UNSIGNED)";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveReturnBillItemsData(String bill_no) {
		String query = "select `item_name`, `item_batch`, `unit_price`, `quantity`, (bi.tax_percentage+bi.surchargePercentage) as tax, `total_value`, `expiry_date`,be.return_reason ,bi.`date`,bi.`time`,bi.`user_name` from bill_items bi left join bills_entry be on be.bill_id =bi.bill_id  where be.bill_id="
				+ bill_no + "";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public String retrieveBillDate(String bill_no) {
		String query = "select concat(date,' ',time)as datetime  from bills_entry where bill_id =(select return_bill_id from bills_entry where bill_id='"+bill_no+"')";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
			while(rs.next()) {
				return rs.getString(1);
			}

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return "";
	}
	public ResultSet retrieveAllSummery(String fromDate,String toDate,String type,String bill_type,String t) {
		String query = "SELECT\r\n"
				+ "	count(ci.bill_id),\r\n"
				+ "	ROUND(sum(ci.total_value),2) \r\n"
				+ "FROM\r\n"
				+ "	cancel_items ci\r\n"
				+ "left join bills_entry be \r\n"
				+ "on be.insurance_type =ci.insurance_type \r\n"
				+ "WHERE \r\n"
				+ "be.payment_mode ='"+bill_type+"' and\r\n"
				+ "be.bill_type ='"+t+"' and\r\n"
				+ "	ci.date BETWEEN  '"+fromDate+"'  AND '"+toDate+"' \r\n"
				+ "	AND ci.insurance_type = '"+type+"'\r\n";
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllSummery1(String fromDate,String toDate,String type,String username) {
		//		String query = "SELECT count(ci.bill_id), sum(ci.total_value) FROM cancel_items ci join bills_entry be WHERE ci.date BETWEEN '"
		//				+ fromDate + "' AND '" + toDate + "' AND ci.nsurance_type='"+type+"' AND  ci.user_name='"+username+"' AND be.bill_type <> 'IPD'";
		//	System.out.println(query);
		String query;
		if(username.equals(" "))
		{
			query="SELECT\r\n"
					+ "	count(ci.bill_id),\r\n"
					+ "	round(sum(ci.total_value),2),\r\n"
					+ "	be.bill_id as bill_item ,\r\n"
					+ "	ci.bill_id as cancel_item\r\n"
					+ "FROM\r\n"
					+ "	cancel_items ci\r\n"
					+ "	join bills_entry be on be.bill_id =ci.bill_id \r\n"
					+ "WHERE\r\n"
					+ "	ci.date BETWEEN '"+fromDate +"' AND '" + toDate + "'\r\n"
					+ "	AND ci.insurance_type = '"+type+"'\r\n"
					+ "	and be.bill_type <> 'IPD';";
		}else {
			query="SELECT\r\n"
					+ "	count(ci.bill_id),\r\n"
					+ "	round(sum(ci.total_value),2),\r\n"
					+ "	be.bill_id as bill_item ,\r\n"
					+ "	ci.bill_id as cancel_item\r\n"
					+ "FROM\r\n"
					+ "	cancel_items ci\r\n"
					+ "	join bills_entry be on be.bill_id =ci.bill_id \r\n"
					+ "WHERE\r\n"
					+ "	ci.date BETWEEN '"+fromDate +"' AND '" + toDate + "'\r\n"
					+ "	AND ci.insurance_type = '"+type+"'\r\n"
					+ "	AND ci.user_name ='"+username+"'\r\n"
					+ "	and be.bill_type <> 'IPD';";
		}
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllSummeryIPD(String fromDate,String toDate,String type,String username) {
		//		String query = "SELECT count(ci.bill_id), sum(ci.total_value) FROM cancel_items ci join bills_entry be WHERE ci.date BETWEEN '"
		//				+ fromDate + "' AND '" + toDate + "' AND ci.nsurance_type='"+type+"' AND  ci.user_name='"+username+"' AND be.bill_type <> 'IPD'";
		//	System.out.println(query);
		String query="";
		if(username.equals(" ")) {
			query="SELECT\r\n"
					+ "	count(ci.bill_id),\r\n"
					+ "	round(sum(ci.total_value),2),\r\n"
					+ "	be.bill_id as bill_item ,\r\n"
					+ "	ci.bill_id as cancel_item\r\n"
					+ "FROM\r\n"
					+ "	cancel_items ci\r\n"
					+ "	join bills_entry be on be.bill_id =ci.bill_id \r\n"
					+ "WHERE\r\n"
					+ "	ci.date BETWEEN '"+fromDate +"' AND '" + toDate + "'\r\n"
					+ "	AND ci.insurance_type = '"+type+"'\r\n"
					+ "	and be.bill_type = 'IPD';";
		}
		else {
			query="SELECT\r\n"
					+ "	count(ci.bill_id),\r\n"
					+ "	round(sum(ci.total_value),2),\r\n"
					+ "	be.bill_id as bill_item ,\r\n"
					+ "	ci.bill_id as cancel_item\r\n"
					+ "FROM\r\n"
					+ "	cancel_items ci\r\n"
					+ "	join bills_entry be on be.bill_id =ci.bill_id \r\n"
					+ "WHERE\r\n"
					+ "	ci.date BETWEEN '"+fromDate +"' AND '" + toDate + "'\r\n"
					+ "	AND ci.insurance_type = '"+type+"'\r\n"
					+ "	AND ci.user_name ='"+username+"'\r\n"
					+ "	and be.bill_type = 'IPD';";
		}

		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllSummeryAdmin(String fromDate,String toDate,String username,String bill_type) {
		String query = "SELECT\r\n"
				+ "	count(ci.bill_id),\r\n"
				+ "	ROUND(sum(ci.total_value),2) \r\n"
				+ "FROM\r\n"
				+ "	cancel_items ci\r\n"
				+ "left join bills_entry be \r\n"
				+ "on be.insurance_type =ci.insurance_type \r\n"
				+ "WHERE \r\n"
				+ "be.bill_type ='"+bill_type+"' and\r\n"
				+ "	ci.date BETWEEN  '"+fromDate+"'  AND '"+toDate+"' \r\n"
				+ "	and ci.user_name = '"+username+"'";
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllSummeryUserWise(String fromDate,String toDate,String type,String username,String bill_type,String t) {
		String query = "SELECT\r\n"
				+ "	count(ci.bill_id),\r\n"
				+ "	ROUND(sum(ci.total_value),2) \r\n"
				+ "FROM\r\n"
				+ "	cancel_items ci\r\n"
				+ "left join bills_entry be \r\n"
				+ "on be.insurance_type =ci.insurance_type and be.bill_id=ci.bill_id\r\n"
				+ "WHERE \r\n"
				+ "be.bill_type ='"+t+"' and\r\n"
				+ "	ci.date BETWEEN  '"+fromDate+"'  AND '"+toDate+"' \r\n"
				+ "	AND ci.insurance_type = '"+type+"'\r\n"
				+ "	and ci.user_name = '"+username+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM items_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	public int insertCancel(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `cancel_items`(`bill_id`, `item_id`, `item_name`, `item_batch`,`insurance_type`,`unit_price`, `quantity`, `tax_amount`, `total_value`, `expiry_date`, `final_total_value`, `remarks`, `date`, `time`, `user_id`, `user_name`,`cancel_item_text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 18; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
}
