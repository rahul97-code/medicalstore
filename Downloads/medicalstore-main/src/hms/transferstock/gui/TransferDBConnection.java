package hms.transferstock.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import hms.main.HMSDBConnection;

public class TransferDBConnection extends HMSDBConnection{
Connection con=null;
Statement stmt=null;
ResultSet rs = null;
	public TransferDBConnection() {
		// TODO Auto-generated constructor stub
	super();
	con = getConnection();
	stmt = getStatement();
	
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `department_id`, `department_name`, `department_username`, `department_password`, `department_type`, `department_room_name`, `department_room_no`, `last_login`, `department_status`, `entry_user`, `creation_date` FROM `departments_detail` WHERE 1";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllDoctorData() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `doctor_detail` WHERE `doc_active`="+1;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet searchItemWithIdOrNmae(String index)
	{

		String query = "SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_id LIKE '%" + index + "%' "
				+ "UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_name LIKE '%" + index + "%'"
				+ " UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_desc LIKE '%" + index + "%' "
				+ "UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_salt_name LIKE '%" + index + "%'";
		System.out.println(query);
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDptStock(String itemid, String departmentName) {
		String query = "SELECT  SUM(total_stock) FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND total_stock>0 and batch_id<>''";
		System.out.println(query);
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData2()
	{
	  String query="SELECT `supplier_id`, `supplier_name`, `supplier_mobile`, `supplier_credits`, `supplier_debits`, `supplier_rating` FROM `supplier_detail` ORDER BY  `supplier_name` ASC ";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveMainCategories()
	{
		String query="SELECT DISTINCT(`item_category`) FROM `items_detail` WHERE 1";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveSubCategories()
	{
		String query="SELECT DISTINCT(`item_category_name`) FROM `items_detail` WHERE 1";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public int inserIssuedData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `issued_department_register`( `department_id`, `department_name`, `person_name`, `persone_id`, `intent_slip_no`, `date`, `time`, `item_id`, `item_name`, `item_desc`, `issued_qty`, `previouse_stock`, `consumable`, `return_date`, `item_returned`, `expiry_date`, `issued_by`,`issued_text1`,`item_unit_price`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 20; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	public ResultSet retrieveData() {
		String query = "SELECT `doc_name` FROM `doctor_detail` ";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int inserDataExam(String[] data) throws Exception {
		String insertSQL = "INSERT INTO  `batch_tracking`(`item_id`, `item_name`, `item_desc`, `item_batch`, `item_stock`, `item_qauntity_entered`, `item_expiry`, `item_date`, `item_time`, `item_last_user`, `item_unitprice`, `item_mrp`, `item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_text3`,`item_unit_price_new`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(insertSQL);
		
		PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
			for (int i = 1; i < 20; i++) {

				preparedStatement.setString(i, data[i - 1]);
			}
			 preparedStatement.executeUpdate();
			 ResultSet rs = preparedStatement.getGeneratedKeys();
				rs.next();

				return rs.getInt(1);
		
	}
	public void inserDepartmentStock(String[] data) throws Exception {
		String insertSQL = "INSERT INTO  `department_stock_register`(`department_id`,`department_name`,`user_name`,`item_id`, `item_name`, `item_desc`, `total_stock`, `last_issued`,`expiry_date`,`item_active`,`batch_name`,`batch_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(insertSQL);
		
		PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
			for (int i = 1; i < 13; i++) {

				preparedStatement.setString(i, data[i - 1]);
			}
			 preparedStatement.executeUpdate();		
	}
	public ResultSet retrieveLastItemID()
	{
		String query="SELECT `item_id` from items_detail order by `item_id` desc limit 1 ";
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void inserData(String[] data) throws Exception
	{
		String insertSQL = "INSERT INTO `items_detail`(`item_name`,`item_code`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_location`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`,`item_igst`,`item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`,`item_maximum_units`,`item_reoder_level`,`item_salt_name`,`item_text1`, `item_text2`, `item_text3`, `formula_active`,`item_risk_type`,`item_category_type`,`doctor_refrence`,`item_other`,`batch_reqd`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
		for (int i = 1; i <44; i++) {
			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public ResultSet itemDetail2(String index)
	{
		String query="SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_location`, `item_surcharge`,`item_hsn_code`,`formula_active`,`stock_type`,`formula_applied`,`item_risk_type`,`batch_reqd` FROM `items_detail` where  `item_id` = '"+index+"'";
		System.out.println(query);
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
}
