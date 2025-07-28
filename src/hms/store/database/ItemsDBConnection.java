package hms.store.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class ItemsDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	String fromDateSTR1, toDateSTR1;
	String fromDateSTR3, toDateSTR3;

	public ItemsDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	
	public String retrieveFormula(String ins,String item_cat) {
		String query="SELECT formula FROM `formula_master`,(SELECT \r\n"
				+ "  COALESCE(\r\n"
				+ "    (SELECT item_cat_type FROM formula_master fm2 WHERE ins_name='"+ins+"' and item_cat_type='"+item_cat+"' LIMIT 1),\r\n"
				+ "    'others'\r\n"
				+ "  ) as result)x where ins_name='"+ins+"' and item_cat_type=x.result";
		try {
			rs = statement.executeQuery(query);
			System.out.println(query);
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	
	public ResultSet PillsRequestHMS(String id)
	{
		String query="select request_item_desc, request_qty, entry_id from request_pills_register where invoice_id = "+id+"";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void updateRequestpills(String[] data, String invoice_ID, String entryID, String Ipd_id) throws Exception
	  {
		
		String insertSQL = "UPDATE `request_pills_register` SET `received_qty`=?,`item_id`=?,`item_name`=?,`item_desc`=?,`total_price`=?,`batch_id`=?,`batch_name`=?,`is_delivered`=?,`status`=?, `expiry_date`=? WHERE `invoice_id`="+invoice_ID+" and entry_id="+entryID+" and ipd_id="+Ipd_id+"";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 11; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();	  }
	
	public Double retrieveSellingPrice(String itemID) {
		String query = "select item_selling_price  from items_detail id where item_id='"+itemID+"'";
		try {
			rs = statement.executeQuery(query);
			System.out.println(query);
			while (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
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
	
	public void subtractStock(String itemID, String value, String departmentName, String batch_id)
			throws Exception {

		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`=`total_stock`-'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID
				+ " AND `department_name`='" + departmentName + "' AND `batch_id`='"+batch_id+"'");

	}

	public ResultSet retrieveHMSresult(String id)
	{
		String query="select hms_id from items_detail where item_id='"+id+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveStockbt(String itemid, String departmentName,String batch_id) {
		String query = "SELECT sum(total_stock), `expiry_date` FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND `batch_id`='"+batch_id+"' group by batch_id";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	
	public int inserDepartmentStock(String[] data) throws Exception {
	    int rowsAffected = 0;
	    PreparedStatement preparedStatement = null;

	    try {
	        // Assuming this method exists in your DB connection class and provides a valid connection
	        String sql = "INSERT INTO department_stock_register (department_id, department_name, user_name, item_id, item_name, item_desc, total_stock, entry_date, expiry_date, batch_id, batch_name, is_consumable,user_id,med_source,dept_user_name) " +
	                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        // Prepare the SQL statement
	        preparedStatement = connection.prepareStatement(sql);

	        // Set values from data array to the SQL query
	        preparedStatement.setString(1, data[0]);  // department_id (departmentID)
	        preparedStatement.setString(2, data[1]);  // department_name (departmentNameSTR)
	        preparedStatement.setString(3, data[2]);  // user_name (personTF or whoever this is)
	        preparedStatement.setString(4, data[3]);  // item_id (itemIDV)
	        preparedStatement.setString(5, data[4]);  // item_name (itemNameV)
	        preparedStatement.setString(6, data[5]);  // item_desc (itemDescV), should be String, if not adjust
	        preparedStatement.setInt(7, Integer.parseInt(data[6]));  // total_stock (issuedQtyV), should be int
	        preparedStatement.setString(8, data[7]);  // entry_date (issuedDateSTR)
	        preparedStatement.setString(9, data[8]);  // expiry (expiryDateV or itemBatchIDV)
	        preparedStatement.setString(10, data[9]); // batch_id (itemBatchIDV or itemBatch)
	        preparedStatement.setString(11, data[10]); // batch_name (itemBatchIDV or itemBatch)
	        preparedStatement.setString(12, data[11]); // batch_name (itemBatchIDV or itemBatch)
	        preparedStatement.setString(13, data[12]);
	        preparedStatement.setString(14, data[13]);
	        preparedStatement.setString(15, data[14]);

	        // Execute the insert statement and return the number of affected rows
	        rowsAffected = preparedStatement.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new Exception("Error inserting department stock", e);
	    } finally {
	        // Clean up database resources (Only close if you opened the connection)
	        if (preparedStatement != null) {
	            preparedStatement.close();
	        }
	        // Do not close connection if it's managed elsewhere
	        // if (connection != null) {
	        //    connection.close();
	        // }
	    }

	    return rowsAffected;
	}
	
	public ResultSet department_stock(String Depart) {
	    String query = "";
	    try {
	        // Check if the department is "Department Name" or a specific department
	        if (!Depart.equals("Department Name")) {
	            query = "SELECT `department_name`, `item_id`, `item_name`, `item_desc`, `user_name`, `total_stock`, `batch_id`, `batch_name`, `expiry_date` "
	                    + "FROM `department_stock_register` WHERE `department_name` = ?";
	        } else {
	            query = "SELECT `department_name`, `item_id`, `item_name`, `item_desc`, `user_name`, `total_stock`, `batch_id`, `batch_name`, `expiry_date` "
	                    + "FROM `department_stock_register`";
	        }

	        // Prepare the SQL query
	        PreparedStatement preparedStatement = connection.prepareStatement(query);

	        // If the department is not "Department Name", bind the value
	        if (!Depart.equals("Department Name")) {
	            preparedStatement.setString(1, Depart);
	        }

	        // Execute the query and return the result set
	        rs = preparedStatement.executeQuery();

	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
	                javax.swing.JOptionPane.ERROR_MESSAGE);
	    }
	    return rs;
	}
	
	public void updateHMScode(String id, int hmsid)
	{
		String insertSQL = "UPDATE `items_detail` SET `hms_id`=? WHERE `item_id`='"+id+"'";
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection
					.prepareStatement(insertSQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			preparedStatement.setInt(1, hmsid);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateItemPriceData(String itemID, String[] data,boolean isCgstSgst) throws Exception {
		if(isCgstSgst)
			statement.executeUpdate("UPDATE `items_detail` SET `item_meas_unit`='"+data[4]+"',item_igst='"+data[3]+"',item_tax_value='"+data[5]+"',item_surcharge='"+data[2]+"',item_mrp='"+data[1]+"',item_purchase_price='"+data[0]+"' WHERE `item_id`=" + itemID);
		else 		
			statement.executeUpdate("UPDATE `items_detail` SET `item_meas_unit`='"+data[4]+"',item_igst='"+data[3]+"',item_mrp='"+data[1]+"',item_purchase_price='"+data[0]+"' WHERE `item_id`=" + itemID);

	}
	public void updateData(String[] data) throws Exception {
		String insertSQL = "UPDATE `items_detail` SET `item_name`=?,`item_code`=?, `item_desc`=?, `item_brand`=?, `item_hsn_code`=?, `item_treatment`=?, `item_lab`=?, `item_surgery`=?, `item_drug`=?, `item_imaging`=?, `item_inpatient`=?, `item_medic_income`=?, `item_category`=?, `item_category_name`=?, `item_meas_unit`=?, `item_type`=?, `item_tax_type`=?, `item_tax_value`=?, `item_surcharge`=?, `item_igst`=?,`item_purchase_price`=?, `item_selling_price`=?, `item_total_stock`=?, `item_mrp`=?, `item_minimum_units`=?, `item_status`=?, `item_expiry_date`=?, `item_date`=?, `item_time`=?, `item_entry_user`=?, `item_location`=?,`item_maximum_units`=?,`item_reoder_level`=?,`item_salt_name`=?,`item_text1`=?, `item_text2`=?, `item_text3`=?, `item_text4`=?,`item_risk_type`=?,`doctor_refrence`=?,`formula_active`=?  WHERE `item_id` = "
				+ data[41] + "";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 42; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}

	public ResultSet retrieveLastItemID()
	{
		String query="SELECT `item_id` from items_detail order by `item_id` desc limit 1 ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public void updateOrderStaus(String item_id, String status,
			String items_order) throws Exception {

		if (status.equals("YES")) {
			String updatesql = "update `items_detail` set `item_oredered` = '"
					+ status + "',`item_order_qty`=item_order_qty+'"
					+ items_order + "' where `item_id` = " + item_id + "";
			// System.out.println(sql);
			statement.executeUpdate(updatesql);

		} else {
			String query = "SELECT * FROM `items_detail` WHERE `item_id`='"
					+ item_id + "'";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			int item_ordered = 0;
			while (rs.next()) {
				item_ordered = Integer.parseInt(rs.getString("item_order_qty"));
			}
			System.out.println("item_order" + item_ordered);
			int check = item_ordered - Integer.parseInt(items_order);
			System.out.println("check" + check);
			if (check < 0) {
				statement
						.executeUpdate("update `items_detail` set `item_oredered` = '"
								+ status
								+ "',`item_order_qty`='0' where `item_id` = "
								+ item_id + " AND `item_order_qty`>0");
			} else {
				statement
						.executeUpdate("update `items_detail` set `item_oredered` = '"
								+ status
								+ "',`item_order_qty`=item_order_qty-'"
								+ items_order
								+ "' where `item_id` = "
								+ item_id + " AND `item_order_qty`>0");
			}

			//
		}
	}

	public void resetStock(String item_id) throws Exception {
		statement
				.executeUpdate("UPDATE  `items_detail` SET  `item_total_stock` =  '0' WHERE  `item_id` =  "
						+ item_id);
	}

	public void updateDataCounter(String docID, String counter)
			throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET `doc_text2`='"
				+ counter + "' where `doc_id` = " + docID);
	}

	public void updateprice(String itemID, String purchase, String previouse)
			throws Exception {

		statement
				.executeUpdate("UPDATE `items_detail` SET `item_purchase_price`='"
						+ purchase
						+ "', `item_prevouse_price`='"
						+ previouse
						+ "' WHERE `item_id`=" + itemID);
	}

	public void mrpupdateprice(String itemID, String mrpprice) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET `item_mrp`='"
				+ mrpprice + "' WHERE `item_id`=" + itemID);
	}
	public void UpdateHighRisk(String itemID) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET `item_risk_type`='High Risk.' WHERE `item_id`=" + itemID);
	}
	public void UpdateHighRiskRemove(String itemID) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET `item_risk_type`='High Risk' WHERE `item_id`=" + itemID);
	}
	public void addStock(String itemID, String value, String expiryDate,
			String unitprice) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET  `item_purchase_price`='" + unitprice
				+ "',`item_total_stock`=`item_total_stock`+'" + value
				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
				+ itemID);
	}

	public void addStockByReturn1(String itemID, String value) throws Exception {

		statement
				.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'"
						+ value + "' WHERE `item_id`=" + itemID);
	}

	public void subtractStock(String itemID, String value) throws Exception {

		statement
				.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`-'"
						+ value + "' WHERE `item_id`=" + itemID);
	}

	public int retrieveCounterData() {
		String query = "SELECT * FROM `patient_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
		try {
			while (rs.next()) {
				NumberOfRows++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfRows;
	}

	public ResultSet retrieveMainCategories() {
		String query = "SELECT DISTINCT(`item_category`) FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveSubCategories() {
		String query = "SELECT DISTINCT(`item_category_name`) FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllData() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllHighRiskData() {
		String query = "SELECT item_id, item_name, item_risk_type FROM items_detail WHERE item_risk_type ='High Risk'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllHighRiskData1() {
		String query = "SELECT item_id, item_name, item_risk_type FROM items_detail WHERE item_risk_type ='High Risk.'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData2(String index) {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_active` FROM `items_detail` WHERE doc_id LIKE '%"
				+ index + "%' OR doc_name LIKE '%" + index + "%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllData2() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_active` FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDataWithIndex(String name) {
		String query = "SELECT `doc_id`, `doc_specialization`, `doc_opdroom` FROM `items_detail` WHERE `doc_name` = '"
				+ name + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDataWithID(String docID) {
		String query = "SELECT `doc_name`, `doc_username`, `doc_password`, `doc_specialization`, `doc_opdroom`, `doc_telephone`, `doc_address`, `doc_qualification` FROM `items_detail` WHERE `doc_id` = "
				+ docID + "";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveUserPassword(String name, String pass) {
		String query = "SELECT * FROM `items_detail` WHERE `doc_username` = '"
				+ name + "' AND `doc_password` = '" + pass + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveItemDetail(String id) {
		String query = "SELECT `item_name`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`, `item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_location`,`item_igst`,`item_code`,`item_maximum_units`,`item_reoder_level`,`item_text1`, `item_text2`, `item_text3`, `item_text4`,`item_salt_name`,`item_risk_type`,`doctor_refrence`,`item_entry_user`,`formula_active` FROM `items_detail` WHERE `item_id` = '"
				+ id + "' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievetTotalStock() {
		fromDateSTR1 = DateFormatChange.StringToMysqlDate(DateFormatChange
				.addMonths(new Date(), -1));
		toDateSTR1 = DateFormatChange.StringToMysqlDate(new Date());
		fromDateSTR3 = DateFormatChange.StringToMysqlDate(DateFormatChange
				.addMonths(new Date(), -3));
		toDateSTR3 = DateFormatChange.StringToMysqlDate(new Date());
		String query = "SELECT `item_id`, `item_name`, `item_desc`, `item_brand`,coalesce(FORMAT(`item_mrp`/`item_meas_unit`,2), `item_mrp`), `item_purchase_price`, `item_total_stock`,  `item_minimum_units`, (CASE WHEN CONVERT(`item_total_stock`,DECIMAL) >=CONVERT(`item_minimum_units`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `items_detail` a WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievetAllItems() {
		String query = "SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_purchase_price`, `item_selling_price`, `item_status` ,  `item_meas_unit` FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievetAllItemsNew() {
		String query = "SELECT `item_id`, `item_name`, `doctor_refrence`, `item_brand`, `item_text1`,`item_text2`, `item_total_stock`, `item_status` ,  `item_meas_unit`,`item_risk_type` FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievetAllBatches(String item_id) {
		String query = "SELECT `item_batch`, `item_qauntity_entered`, `item_stock`, `item_date`, `item_mrp`, `item_measunits`, `item_unit_price_new`,`item_unitprice` FROM `batch_tracking` WHERE `item_id`='"
				+ item_id + "' order by id desc";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievetAllBatchesNew(String item_id) {
		String query = "SELECT `item_batch`,`item_expiry`, `item_qauntity_entered`, `item_stock`, `item_date`, `item_mrp`, `item_measunits`, `item_unit_price_new`,`item_unitprice`,`item_tax`,`item_surcharge`,`item_igst` FROM `batch_tracking` WHERE `item_id`='"
				+ item_id + "' order by id desc";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievetAllItemsExcel() {
		String query = "SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_salt_name`,`item_mrp`, `item_purchase_price`, `item_prevouse_price`,CASE WHEN `item_purchase_price`*`hms_detail`.`detail_desc` > `item_mrp` AND  `item_mrp`!='N/A'  THEN `item_mrp`  ELSE `item_purchase_price`*`hms_detail`.`detail_desc` END AS Value, `item_total_stock`,`item_minimum_units`, `item_status`, `item_expiry_date`, `item_entry_user` FROM `items_detail`,`hms_detail` WHERE `hms_detail`.`detail_id`=2 ";
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

	public ResultSet searchItemWithIdNew(String index) {
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price` FROM `items_detail` where item_id like '%"+index+"%' or `item_name` like '%"+index+"%' or `item_desc` like '%"+index+"%'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet itemDetail(String index) {
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`, `item_surcharge`,`item_igst`,`item_oredered` FROM `items_detail` where  `item_id` = '"
				+ index + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet itemDetail2(String index) {
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_location`, `item_surcharge`,`item_hsn_code`, `item_meas_unit`,`item_med_type`,`formula_active`,`item_category`,`changed_unit_price` FROM `items_detail` where  `item_id` = '"
				+ index + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	
	public  ResultSet getBatchName(String item_id,String dept_id) {

		String query = "select batch_name, batch_id from department_stock_register dsr  where department_id = "+dept_id+" and item_id = "+item_id+" group by batch_id HAVING (batch_id<>'' or batch_id<>'0') ORDER BY CAST(expiry_date AS UNSIGNED ), CAST( total_stock AS UNSIGNED ) ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

		return rs;
	}
	public ResultSet itemDetailNew(String index) {
		String query = "SELECT `item_risk_type` FROM `items_detail` where  `item_id` = '"
				+ index + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet totaldeptqty(String item_id,String dept_id) {
		String query = "select SUM(total_stock) from department_stock_register where department_id ="+dept_id+" and item_id = "+item_id+"";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet itemDetailBatch2(String index) {
		String query = "SELECT `item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_stock`,`item_desc`,`item_expiry` FROM `batch_tracking` where  `id` = '"
				+ index + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet itemDetailBatchNew(String index,String batch_name) {
		String query = "SELECT `item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_stock` FROM `batch_tracking` where  `item_id` = '"
				+ index + "' and `item_batch`='"+batch_name+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet itemDetail3(String index) {
		String query = "SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_location` FROM `items_detail` WHERE `item_id`= '"
				+ index + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM items_detail WHERE `item_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	public void inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `items_detail`(`item_name`,`item_code`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_location`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`,`item_igst`,`item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`,`item_maximum_units`,`item_reoder_level`,`item_salt_name`,`item_text1`, `item_text2`, `item_text3`, `item_text4`,`item_risk_type`,`doctor_refrence`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 41; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
}