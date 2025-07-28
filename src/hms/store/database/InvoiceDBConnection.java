package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class InvoiceDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public InvoiceDBConnection() {
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
		for (int i = 1; i <9; i++) {

			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public void UpdateInvoiceBatchID(int inv_id,int batch_id) throws Exception
	{
		statement.executeUpdate("UPDATE `invoice_items` SET `batch_id`='"+batch_id+"' WHERE `invoice_item_id`='"+inv_id+"'");
	}
	public void updateInvoiceStatus(String invoice_id,String payment_id) throws Exception
	{
		statement.executeUpdate("UPDATE `invoice_entry` SET `invoice_text1`='"+payment_id+"' WHERE `invoice_id`="+invoice_id);
	}
	public void UpdateReceivedQtyInPO(String po_id,String item_id,String qty) throws Exception
	{
		statement.executeUpdate("UPDATE `po_items` SET `item_paid_quantity`=`item_paid_quantity`+'"+qty+"' WHERE `po_id`='"+po_id+"' and `item_id`='"+item_id+"'");
	}
	public void updateInvoicePaid(String invoice_id) throws Exception
	{
		statement.executeUpdate("UPDATE `invoice_entry` SET `invoice_paid`='Yes' WHERE `invoice_id`='"+invoice_id+"'");
	}
	public void UpdatePoStatusNew(String po_id) throws Exception
	{
		statement.executeUpdate("UPDATE po_entry SET po_status=(CASE WHEN (SELECT (sum(IF(item_paid_quantity>=item_qty,0,1))='0') AS res  FROM po_items where po_id = '"+po_id+"') THEN 'CLOSE' ELSE 'PENDING' END ) WHERE po_id = '"+po_id+"'");
	}
	public void UpdatePoStatus(String po_id,String status) throws Exception
	{
		statement.executeUpdate("UPDATE `po_entry` SET `po_status`='"+status+"' WHERE `po_id`='"+po_id+"'");
	}
	public void UpdateBatchID(String item_id,int batch_id,String batch_name) throws Exception
	{
		statement.executeUpdate("UPDATE `invoice_items` SET `batch_id`='"+batch_id+"' WHERE `item_id`='"+item_id+"' and item_batch_number='"+batch_name+"'");
	}
	public ResultSet retrieveAllInvoiceItems(String fromDate,String toDate) {
		String query = "SELECT `invoice_item_id`, `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_unit_price`, `item_discount`, `item_tax`, `invoice_value`, `item_expiry_date`, `item_date`, `item_time` FROM `invoice_items` WHERE `item_date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public  ResultSet retrievePOReceQty(String supplier_id) {
		String query = "SELECT * from po_items where po_id='"+supplier_id+"' ";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllPending(String supplier_id) {
		String query = "SELECT `invoice_id`, `invoice_id2`, `invoice_order_no`, `invoice_tax`, `invoice_total_amount`, `invoice_date` FROM `invoice_entry` WHERE `invoice_supplier_id`='"+supplier_id+"' AND `invoice_text1`='null'";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveItems(String invoice_id) {
		String query = "SELECT `item_id`, `invoice_item_name`, `invoice_item_desc`,`item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`, `invoice_value`, `item_expiry_date`, `item_date`, `item_batch_number`,`invoice_item_id` FROM `invoice_items` WHERE `invoice_id` ='"+invoice_id+"'";

		System.out.println("HELLO : "+query);
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet lastPurchaseItem(String item_id) {
		String query = "SELECT A.`invoice_supplier_name` AS 'Vender Name',B.`item_qty` AS 'Qty', B.`item_unit_price` AS 'Unit Price',A.`invoice_date` AS 'Date' FROM `invoice_entry` A JOIN `invoice_items` B ON A.`invoice_id`=B.`invoice_id` WHERE B.`item_id`='"+item_id+"' ORDER BY A.`invoice_date` DESC ";

		System.out.println("HELLO : "+query);
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveInvoiceItems(String ID) { 
		String query = "SELECT \r\n"
				+ "    invoice_item_id,\r\n"
				+ "    item_id,\r\n"
				+ "    invoice_item_name,\r\n"
				+ "    invoice_item_desc,\r\n"
				+ "    item_meas_units,\r\n"
				+ "    item_batch_number,\r\n"
				+ "    item_qty,\r\n"
				+ "    item_free_quantity,\r\n"
				+ "    item_paid_quantity,\r\n"
				+ "    item_unit_price_new,\r\n"
				+ "    item_discount,\r\n"
				+ "    item_tax,\r\n"
				+ "    item_surcharge,\r\n"
				+ "    item_tax_value,\r\n"
				+ "    item_surcharge_value,\r\n"
				+ "    invoice_value,\r\n"
				+ "    item_expiry_date,\r\n"
				+ "    item_text1,\r\n"
				+ "    item_unit_price_new,\r\n"
				+ "    batch_id,\r\n"
				+ "  COALESCE( item_igst,0),\r\n"
				+ "  COALESCE( item_igst_value,0),\r\n"
				+ "    item_date,\r\n"
				+ "    item_time\r\n"
				+ "FROM \r\n"
				+ "    invoice_items where invoice_id ='"+ID+"';";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int retrieveCheckInvoice(String invoice_id) throws SQLException {
		String query = "SELECT * FROM `invoice_entry` WHERE `invoice_id2` ='"+invoice_id+"'";

		System.out.println("HELLO : "+query);
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		rs.last();
		int rows = rs.getRow();
		rs.beforeFirst();
		return rows;
	}
	public ResultSet retrieveWithID(String id) {
		String query = "SELECT `invoice_id`, `invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_entry_user`, `invoice_total_amount` FROM `invoice_entry` WHERE `invoice_text1`='"+id+"'";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet invoiceWithID(String id) {
		String query = "SELECT `invoice_id`, `invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_due_date`, `invoice_total_amount`, `invoice_tax`, `invoice_discount`, `invoice_final_amount`,`invoice_entry_user`,`invoice_text2`,`invoice_text7` FROM `invoice_entry` WHERE `invoice_id`='"+id+"'";
		try {
			rs = statement.executeQuery(query);
			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `invoice_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`,`invoice_id2`,  `invoice_final_amount`,`invoice_paid`,COALESCE(`po_id`,'') FROM `invoice_entry` WHERE `invoice_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `invoice_id` DESC ";
		System.out.println(query);
		try {
			//				System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExpiryData(){
		//			String query="SELECT bti.item_id,bti.item_name,bti.item_desc,bti.item_batch,bti.item_expiry,bti.item_stock,bti.item_mrp,bti.item_measunits,iit.invoice_id2,iit.invoice_supplier_name,iit.invoice_date FROM `batch_tracking` `bti` LEFT JOIN `invoice_details_with_items` `iit` ON bti.item_batch = iit.item_batch_number WHERE 	bti.item_expiry < (curdate() + INTERVAL 115 DAY) AND bti.item_stock > 0 GROUP BY bti.item_batch ORDER BY iit.invoice_supplier_name";
		String query="SELECT item_id,item_name,item_desc,item_batch,item_expiry,item_stock,item_mrp,item_measunits,invoice_id2,invoice_supplier_name,invoice_date FROM `expiry_report_view`";

		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataReturn(String dateFrom, String dateTo) { 
		String query = "SELECT `return_invoice_id`, `return_invoice_id2`, `return_invoice_supplier_name`, `return_invoice_date`, `return_invoice_time`,  `return_invoice_final_amount` FROM `return_invoice_entry` WHERE `return_invoice_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `return_invoice_id` DESC ";
		System.out.println(query);
		try {
			//				System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllItemsData(String dateFrom, String dateTo) { 
		String query = "SELECT `invoice_item_id`, `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_batch_number`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`, `invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user` FROM `invoice_items` WHERE `item_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveSuppliersSalesTax1(String dateFrom, String dateTo) { 
		String query = "SELECT `invoice_id` as 'ID',`invoice_supplier_id`, `invoice_supplier_name`,`invoice_id2`,`invoice_order_no`,`invoice_final_amount`, `invoice_discount`,`invoice_date`, `invoice_time` FROM `invoice_entry` where `invoice_date` between '"+dateFrom+"' and '"+dateTo+"'   ORDER BY `invoice_supplier_id`*1  ASC, `invoice_id` ASC";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveSalesTaxReport(String id) { 
		String query = "SELECT b.`invoice_id`,`invoice_supplier_id`, `invoice_supplier_name`,`invoice_id2` as 'Invoice Number',`invoice_order_no`,`invoice_final_amount`, `invoice_discount`,`invoice_date`, `invoice_time`,`item_tax`, sum(`item_tax_value`) as 'TAX AMOUNT',`item_surcharge`, SUM(`item_surcharge_value`)as 'Surcharge Amount' FROM `invoice_entry` a,`invoice_items` b where a.`invoice_id`=b.`invoice_id` and b.`invoice_id` IN('"+id+"') group by `item_tax`";
		try {
			System.out.println(query);
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
	public int inserInvoiceNew(String[] data) throws Exception {
		String insertSQL = "REPLACE INTO `invoice_entry`(`invoice_id`,`invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_due_date`, `invoice_paid`, `invoice_entry_user`, `invoice_total_amount`, `invoice_tax`, `invoice_discount`, `invoice_final_amount`,`invoice_text2`,`po_id`,`invoice_text7`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 18; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return  rs.getInt(1);
	}
	public int inserInvoiceItemNew(String[] data) throws Exception
	{
		String insertSQL = "REPLACE INTO `invoice_items`(`invoice_item_id`, `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price_new`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`,`item_text1`,`item_unit_price`,`batch_id`,`item_igst`,`item_igst_value`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i <27; i++) {

			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return  rs.getInt(1);
	}
	public void inserInvoiceItem(String[] data) throws Exception
	{
		String insertSQL = "INSERT INTO `invoice_items`( `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price_new`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`,`item_text1`,`item_unit_price`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		for (int i = 1; i <23; i++) {

			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateInvoiceItem(String[] data,String id) throws Exception
	{
		String updateSQL=" UPDATE `invoice_items` SET `item_id`='"+data[0]+"',`invoice_item_name`='"+data[1]+"',`invoice_item_desc`='"+data[2]+"',`invoice_id`='"+data[3]+"',`item_meas_units`='"+data[4]+"',`item_qty`='"+data[5]+"',`item_free_quantity`='"+data[6]+"',`item_paid_quantity`='"+data[7]+"',`item_unit_price`='"+data[8]+"',`item_discount`='"+data[9]+"',`item_tax`='"+data[10]+"',`item_surcharge`='"+data[11]+"',`item_tax_value`='"+data[12]+"', `item_surcharge_value`='"+data[13]+"',`invoice_value`='"+data[14]+"', `item_expiry_date`='"+data[15]+"', `item_date`='"+data[16]+"', `item_time`='"+data[17]+"', `item_entry_user`='"+data[18]+"', `item_batch_number`='"+data[19]+"' where item_id='"+data[0]+"' and invoice_id="+id+"";
		//		  String insertSQL = "INSERT INTO `invoice_items`( `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(updateSQL); 
		System.out.println(updateSQL);
		//		  for (int i = 1; i <21; i++) {
		//			
		//				  preparedStatement.setString(i, data[i-1]);
		//			}
		preparedStatement.executeUpdate();
	}
	public int inserInvoice(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `invoice_entry`(`invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_due_date`, `invoice_paid`, `invoice_entry_user`, `invoice_total_amount`, `invoice_tax`, `invoice_discount`, `invoice_final_amount`,`invoice_text2`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 15; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
	public int updateInvoiceEntry(String[] data,String id) throws Exception {
		String insertSQL = "UPDATE `invoice_entry` SET `invoice_id2`=?, `invoice_order_no`=?, `invoice_supplier_id`=?, `invoice_supplier_name`=?, `invoice_date`=?, `invoice_time`=?, `invoice_due_date`=?, `invoice_paid`=?, `invoice_entry_user`=?, `invoice_total_amount`=?, `invoice_tax`=?, `invoice_discount`=?, `invoice_final_amount`=?,`invoice_text7`=?,`invoice_text2`=?,`invoice_text6`=? Where `invoice_id`="+id+"";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 17; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
}
