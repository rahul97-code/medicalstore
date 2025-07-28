package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class PODBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public PODBConnection() {

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
	public String retrievePONUMBER() {
		String poNumber="";
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());

		try {
			ResultSet r = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM `po_entry`");
			r.next();
			int count = r.getInt("rowcount");
			r.close();
			count++;
			System.out.println("MyTable has " + count + " row(s).");
			poNumber="PO-"+formattedDate+"-"+String.format("%04d", count); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return poNumber;
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

		public void updateInvoiceStatus(String po_id,String payment_id) throws Exception
		  {
			statement.executeUpdate("UPDATE `po_entry` SET `po_text1`='"+payment_id+"' WHERE `po_id`="+po_id);
		  }
		public ResultSet retrieveAllInvoiceItems(String fromDate,String toDate) {
			String query = "SELECT `po_item_id`, `item_id`, `po_item_name`, `po_item_desc`, `po_id`, `item_meas_units`, `item_qty`, `item_unit_price`, `item_discount`, `item_tax`, `po_value`, `item_expiry_date`, `item_date`, `item_time` FROM `po_items` WHERE `item_date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "'";
			try {
				rs = statement.executeQuery(query);

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveRamainingPOItems(String ID) { 
			String query = "SELECT item_id,po_item_name,item_qty from po_items pi2 where po_id ='"+ID+"' and item_paid_quantity =0";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrievePOItemDataNew(String ID) { 
			String query = "SELECT\r\n"
					+ "	pi.item_id,\r\n"
					+ "	po_item_name,\r\n"
					+ "	po_item_desc,\r\n"
					+ "	item_meas_units  as 'Pack Size',\r\n"
					+ "	(item_qty-item_paid_quantity)+0 as Po_qty,\r\n"
					+ "	item_free_quantity+0 as Free_Qty,\r\n"
					+ "	item_unit_price+0 as PP,\r\n"
					+ "	item_discount+0 as Discount,\r\n"
					+ "	pi.item_tax+0,\r\n"
					+ "	pi.item_surcharge+0,\r\n"
					+ "	pi.item_tax_value+0,\r\n"
					+ "	pi.item_surcharge_value+0,\r\n"
					+ "	po_value+0 as Amt_Value,\r\n"
					+ "	'' as Expiry,\r\n"
					+ "	'' as Item_batch,\r\n"
					+ "	'' as PPV,\r\n"
					+ "	id.item_mrp+0 as MRP,\r\n"
					+ "	pi.item_igst+0 as IGST,\r\n"
					+ "	pi.item_igst_value+0 as IGST_Value\r\n"
					+ "FROM\r\n"
					+ "	po_items pi left join items_detail id on id.item_id =pi.item_id \r\n"
					+ "where\r\n"
					+ "	po_id = '"+ID+"' HAVING Po_qty>0";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveAllPending(String supplier_id) {
			String query = "SELECT `po_id`, `po_id2`, `po_ref_no`, `po_tax`, `po_total_amount`, `po_date` FROM `po_entry` WHERE `po_supplier_id`='"+supplier_id+"' AND `po_text1`='null'";
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveItems(String po_id) {
			String query = "SELECT `po_item_name`,(SELECT I.`item_hsn_code` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1),`item_qty`, `item_unit_price`,ROUND((`item_qty`*`item_unit_price`),2), `item_discount`,ROUND(((`item_qty`*`item_unit_price`)-`item_discount`), 2), `item_tax`,`item_tax_value`,`item_surcharge`, `item_surcharge_value`,`item_meas_units`,case WHEN (`item_qty` / `item_meas_units`) is null then \"0\" else (`item_qty` / `item_meas_units`) end as `no_of_packs` FROM `po_items` WHERE `po_id`='"+po_id+"'";
			
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
		public ResultSet retrieveReturnItems(String po_id) {
			String query = "SELECT `return_invoice_item_name`,(SELECT I.`item_hsn_code` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1),`item_qty`, `item_unit_price`,ROUND((`item_qty`*`item_unit_price`),2), `item_discount`,ROUND(((`item_qty`*`item_unit_price`)-`item_discount`), 2), `item_tax`,`taxValue`,`surcharge_percentage`, `surcharge_value`,(SELECT I.`item_meas_unit` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1 ),(`item_qty`/(SELECT I.`item_meas_unit` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1 )) as `no_of_packs`,`cutting_qty` FROM `return_invoice_items` WHERE `return_invoice_id`='"+po_id+"'";
			
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
		public ResultSet retrieveItemsInvoice(String invoice_id) {
			String query = "SELECT `invoice_item_name`,(SELECT I.`item_hsn_code` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1),`item_qty`, `item_unit_price`,ROUND((`item_qty`*`item_unit_price`),2), `item_discount`,ROUND(((`item_qty`*`item_unit_price`)-`item_discount`), 2), `item_tax`,`item_tax_value`,`item_surcharge`, `item_surcharge_value`,`item_paid_quantity`,`item_text1`,`item_batch_number`,`item_meas_units`,`item_expiry_date`,`item_free_quantity`,`item_id`,`batch_id`,(SELECT I.`formula_active` FROM `items_detail` I WHERE I.`item_id`=ii.`item_id` LIMIT 1) FROM `invoice_items` ii WHERE `invoice_id`='"+invoice_id+"'";
			
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
		public ResultSet retrieveWithID(String id) {
			String query = "SELECT `po_id`, `po_id2`, `po_ref_no`, `po_supplier_id`, `po_supplier_name`, `po_date`, `po_time`, `po_entry_user`, `po_total_amount`, `po_tax`, `po_discount`, `po_final_amount` FROM `po_entry` WHERE `po_id`="+id+"";
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveReturnInfo(String id) {
			String query = "SELECT `return_invoice_id`, `return_invoice_id2`, `return_invoice_supplier_id`, `return_invoice_supplier_name`, `return_invoice_date`, `return_invoice_time`, `return_invoice_entry_user`, `return_invoice_total_amount`, `return_invoice_tax`, `return_invoice_discount`, `return_invoice_final_amount`,`return_reason` FROM `return_invoice_entry` WHERE `return_invoice_id`="+id+"";
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveWithInvoiceID(String id) {
			String query = "SELECT `invoice_id`, `invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_entry_user`, `invoice_total_amount`, `invoice_tax`, `invoice_discount`, `invoice_final_amount`,`invoice_text2`,`invoice_text6`,`invoice_text7` FROM `invoice_entry` WHERE `invoice_id`="+id+"";
			try {
				rs = statement.executeQuery(query);

				System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet invoiceWithID(String id) {
			String query = "SELECT `po_id`, `po_id2`, `po_ref_no`, `po_supplier_id`, `po_supplier_name`, `po_date`, `po_time`, `po_due_date`, `po_total_amount`, `po_tax`, `po_discount`, `po_final_amount` FROM `po_entry` WHERE `po_id`='"+id+"'";
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
			String query = "SELECT `po_id`, `po_id2`, `po_supplier_name`, `po_date`, `po_time`,  `po_final_amount`,`po_status` FROM `po_entry` WHERE `po_paid`='NO'  AND `po_date`  BETWEEN '"
					+ dateFrom + "' AND '" + dateTo + "' ORDER BY `po_id` DESC ";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveAllItemsData(String dateFrom, String dateTo) { 
			String query = "SELECT `po_item_id`, `item_id`, `po_item_name`, `po_item_desc`, `po_id`, `item_meas_units`, `item_batch_number`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`, `po_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user` FROM `po_items` WHERE `item_date` BETWEEN '"
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
		public ResultSet retrievePOItemsData(String dateFrom, String dateTo) { 
			String query = "SELECT I.`po_id` AS 'PO ID',E.`po_id2` AS 'PO NUMBER', E.`po_ref_no` AS 'PO REF NO.', E.`po_supplier_name` AS 'VENDOR',I.`item_date` AS 'DATE',I.`po_item_name` AS 'ITEM', I.`item_qty` AS 'QTY.', I.`item_unit_price` AS 'UNIT PRICE', I.`po_value` AS 'VALUE' FROM `po_items` I JOIN `po_entry` E ON I.`po_id`=E.`po_id` WHERE `po_paid`='NO' AND `item_date` BETWEEN '"
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
			String query = "SELECT `po_id` as 'ID',`po_supplier_id`, `po_supplier_name`,`po_id2`,`po_ref_no`,`po_final_amount`, `po_discount`,`po_date`, `po_time` FROM `po_entry` where `po_date` between '"+dateFrom+"' and '"+dateTo+"'   ORDER BY `po_supplier_id`*1  ASC, `po_id` ASC";
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
			String query = "SELECT b.`po_id`,`po_supplier_id`, `po_supplier_name`,`po_id2` as 'Invoice Number',`po_ref_no`,`po_final_amount`, `po_discount`,`po_date`, `po_time`,`item_tax`, sum(`item_tax_value`) as 'TAX AMOUNT',`item_surcharge`, SUM(`item_surcharge_value`)as 'Surcharge Amount' FROM `po_entry` a,`po_items` b where a.`po_id`=b.`po_id` and b.`po_id` IN('"+id+"') group by `item_tax`";
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
	public void inserInvoiceItem(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `po_items`( `item_id`, `po_item_name`, `po_item_desc`, `po_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`po_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <21; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public int inserInvoice(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `po_entry`(`po_id2`, `po_ref_no`, `po_supplier_id`, `po_supplier_name`, `po_date`, `po_time`, `po_due_date`, `po_paid`, `po_entry_user`, `po_total_amount`, `po_tax`, `po_discount`, `po_final_amount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 14; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
}
