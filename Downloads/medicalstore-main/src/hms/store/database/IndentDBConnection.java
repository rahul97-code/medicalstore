package hms.store.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;
import hms.store.gui.StoreMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class IndentDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public IndentDBConnection() {

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
	public void updateStatus(String indent_id) throws Exception
	{
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");	
		statement.executeUpdate("UPDATE `indent_entry` SET `approved_status`='YES',`approved_user_by`='"+StoreMain.userName+"',`approved_date`='"+DateFormatChange.StringToMysqlDate(new Date())+"',`approved_time`='"+timeFormat.format(cal1.getTime())+"' WHERE `indent_id`="+indent_id+"");
	}
	public String retrieveIndentNUMBER() {
		String poNumber="";
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());

		try {
			ResultSet r = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM `indent_entry`");
			r.next();
			int count = r.getInt("rowcount");
			r.close();
			count++;
			poNumber="INTEND-"+formattedDate+"-"+String.format("%04d", count); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return poNumber;
	}

	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `indent_id`, `indent_name`, `indent_date`, `indent_time`, `indent_user_by`,`approved_user_by`, `approved_date`,`approved_status` FROM `indent_entry` WHERE `indent_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `indent_id` DESC ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String indent_id) { 
		String query = "SELECT `entry_id` AS 'ID', `item_id` AS 'Item ID', `itme_name` AS 'Item Name', `item_desc` AS 'Item Desc', `item_brand` AS 'Item Brand', `item_salt` AS 'Item Salt', `item_pack_size` AS 'Pack Size', `item_mrp` AS 'MRP', `item_purchase_price` AS 'Purchase Price', `item_stock` AS 'Item Stock', `item_15_day` AS '15 Days Qty', `item_already_order` AS 'Already Ordered', `item_to_be_ordered` AS 'Auto Generate Qty', `item_indent_qty` AS 'Requested Qty', `item_preferred_vendor` AS 'Preferred Vendor' FROM `indent_items` WHERE `indent_id`='"+indent_id+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePOData(String vendor_name) { 
		String query = "SELECT	`entry_id` AS 'ID',	indent_items.`item_id` AS 'Item ID',indent_items.`itme_name` AS 'Item Name',indent_items.`item_desc` AS 'Item Desc',indent_items.`item_brand` AS 'Item Brand',indent_items.`item_salt` AS 'Item Salt',indent_items.`item_pack_size` AS 'Pack Size',	indent_items.`item_mrp` AS 'MRP',indent_items.`item_purchase_price` AS 'Purchase Price',indent_items.`item_stock` AS 'Item Stock',indent_items.`item_15_day` AS '15 Days Qty',indent_items.`item_already_order` AS 'Already Ordered',indent_items.`item_to_be_ordered` AS 'Auto Generate Qty',indent_items.`item_indent_qty` AS 'Requested Qty',indent_items.`item_preferred_vendor` AS 'Preferred Vendor' FROM	`indent_items` LEFT JOIN `items_detail` ON((`items_detail`.`item_id` = CONVERT(`indent_items`.`item_id` USING utf8))) WHERE	indent_items.`item_preferred_vendor` ='"+vendor_name+"' AND	`items_detail`.`item_oredered` <> 'YES'";
		//		String query = "SELECT `entry_id` AS 'ID', `item_id` AS 'Item ID', `itme_name` AS 'Item Name', `item_desc` AS 'Item Desc', `item_brand` AS 'Item Brand', `item_salt` AS 'Item Salt', `item_pack_size` AS 'Pack Size', `item_mrp` AS 'MRP', `item_purchase_price` AS 'Purchase Price', `item_stock` AS 'Item Stock', `item_15_day` AS '15 Days Qty', `item_already_order` AS 'Already Ordered', `item_to_be_ordered` AS 'Auto Generate Qty', `item_indent_qty` AS 'Requested Qty', `item_preferred_vendor` AS 'Preferred Vendor' FROM `indent_items` WHERE  `item_preferred_vendor`='"+vendor_name+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrievePOItemData(String ID) { 
		String query = "SELECT	`po`.`po_item_name`,`po`.`item_qty`,`po`.`item_id`,`po`.`item_paid_quantity`  FROM	 `po_items` `po` LEFT JOIN `items_detail` ON((`items_detail`.`item_id` = CONVERT(`po`.`item_id` USING utf8))) WHERE	`po`.`po_id` ='"+ID+"'";
		System.out.println(query);
		//		String query = "SELECT `entry_id` AS 'ID', `item_id` AS 'Item ID', `itme_name` AS 'Item Name', `item_desc` AS 'Item Desc', `item_brand` AS 'Item Brand', `item_salt` AS 'Item Salt', `item_pack_size` AS 'Pack Size', `item_mrp` AS 'MRP', `item_purchase_price` AS 'Purchase Price', `item_stock` AS 'Item Stock', `item_15_day` AS '15 Days Qty', `item_already_order` AS 'Already Ordered', `item_to_be_ordered` AS 'Auto Generate Qty', `item_indent_qty` AS 'Requested Qty', `item_preferred_vendor` AS 'Preferred Vendor' FROM `indent_items` WHERE  `item_preferred_vendor`='"+vendor_name+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveApprovedData() { 
		String query = "SELECT * FROM `approved_list_display`";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM items_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	//	public void insertIndentItems(String subQuery) throws SQLException
	//	{
	//		String insertSQL = "INSERT INTO `indent_items`(`item_id`, `itme_name`, `item_desc`, `item_brand`, `item_concentration`, `item_salt`, `item_pack_size`, `item_mrp`, `item_purchase_price`, `item_stock`, `item_two_months`, `item_15_day`, `item_already_order`, `item_to_be_ordered`, `item_preferred_vendor`, `item_indent_qty`) "+subQuery;
	//	System.out.println(insertSQL);
	//		//String insertSQL = "INSERT INTO salt_master_2 (SELECT * FROM salt_master WHERE "+like+" CHAR_LENGTH(f4) <="+len+" AND f12!='ROTARY' ORDER BY CAST(f7 AS DECIMAL) ASC LIMIT 1)";
	//		PreparedStatement preparedStatement = connection
	//				.prepareStatement(insertSQL);
	//		preparedStatement.executeUpdate();
	//	 
	//	}
	//	public void insertIndentItems(String subQuery) throws SQLException
	//	{
	//		String insertSQL = "INSERT INTO `indent_items` (`indent_id`, `item_id`, `itme_name`, `item_desc`, `item_brand`, `item_concentration`, `item_salt`, `item_pack_size`, `item_mrp`, `item_purchase_price`, `item_stock`, `item_two_months`, `item_15_day`, `item_already_order`, `item_to_be_ordered`, `item_preferred_vendor`, `item_indent_qty`) "+subQuery;
	////		String insertSQL = "INSERT INTO salt_master_2 (SELECT * FROM salt_master WHERE "+like+" CHAR_LENGTH(f4) <="+len+" AND f12!='ROTARY' ORDER BY CAST(f7 AS DECIMAL) ASC LIMIT 1)";
	//		System.out.println(insertSQL);
	//		PreparedStatement preparedStatement = connection
	//				.prepareStatement(insertSQL);
	//		preparedStatement.executeUpdate();
	//	 
	//	}
	public void insertIndentItems(String[] data) throws SQLException
	{
		String insertSQL = "INSERT INTO `indent_items`(`indent_id`, `item_id`, `itme_name`, `item_desc`, `item_brand`, `item_concentration`, `item_salt`, `item_pack_size`, `item_mrp`, `item_purchase_price`, `item_stock`, `item_two_months`, `item_15_day`, `item_already_order`, `item_to_be_ordered`, `item_preferred_vendor`, `item_indent_qty`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//		String insertSQL = "INSERT INTO salt_master_2 (SELECT * FROM salt_master WHERE "+like+" CHAR_LENGTH(f4) <="+len+" AND f12!='ROTARY' ORDER BY CAST(f7 AS DECIMAL) ASC LIMIT 1)";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		for (int i = 1; i < 18; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		//		 ResultSet rs = preparedStatement.getGeneratedKeys();
		//		  rs.next();

		//		 return  rs.getInt(1);
	}
	//	}
	public int inserIndent(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `indent_entry`(`indent_name`, `indent_date`, `indent_time`, `indent_user_by`) VALUES (?,?,?,?)";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
	public ResultSet displayauto(String item_id) { 
		String query = "SELECT * FROM `auto_purchase_order` where `ITEM CODE`="+item_id+"";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
}

