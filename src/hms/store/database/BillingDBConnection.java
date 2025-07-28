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

public class BillingDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BillingDBConnection() {

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
	public void updateStatus(String item_id,String data[]) throws Exception
	{
		statement.executeUpdate("UPDATE `bill_items` SET `quantity`='"+data[0]+"',`total_value`='"+data[1]+"',`bill_item_text3`='"+data[2]+"' WHERE `bill_item_id`="+item_id+"");
	}
	public  ResultSet retrieveBatchName(String id) {
		String query = "SELECT * from batch_tracking where id='"+id+"' ";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePillsData(String dateFrom , String dateTo) {
		String query ="SELECT \r\n"
				+ "    r.invoice_id,\r\n"
				+ "    r.dept_name,\r\n"
				+ "    r.ipd_id,\r\n"
				+ "    r.p_id,\r\n"
				+ "    r.p_name,\r\n"
				+ "    r.doctor_name,\r\n"
				+ "    r.user_name,\r\n"
				+ "    r.request_item_desc,\r\n"
				+ "    r.request_qty,\r\n"
				+ "    r.entry_datetime,\r\n"
				+ "    r.is_delete,\r\n"
				+ "    CASE \r\n"
				+ "        WHEN MAX(CASE WHEN r.status = 'DELIVERED' THEN 1 ELSE 0 END) = 1 THEN 'DELIVERED'\r\n"
				+ "        ELSE 'PENDING'\r\n"
				+ "    END AS status\r\n"
				+ "FROM \r\n"
				+ "    medicalstore_db.request_pills_register r\r\n"
				+ "WHERE \r\n"
				+ "    ((DATE(r.entry_datetime) BETWEEN '"+dateFrom+"' AND '"+dateTo+"')\r\n"
				+ "     OR ('"+dateFrom+"' = '' AND '"+dateTo+"' = ''))\r\n"
				+ "GROUP BY \r\n"
				+ "    r.invoice_id";



		ResultSet rs = null;
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllSummeryItemsNotSale(String toDate,String fromDate) {
		String query ="SELECT t.*,COUNT(*) as c from (select\r\n"
				+ "	id.item_id,\r\n"
				+ "	id.item_name,\r\n"
				+ "	id.doctor_refrence,\r\n"
				+ "	id.item_text1,\r\n"
				+ "	id.item_total_stock,\r\n"
				+ "	if(id.item_date='0000-00-00','',id.item_date) as date\r\n"
				+ "from\r\n"
				+ "	items_detail id\r\n"
				+ "	where id.item_total_stock>0\r\n"
				+ "union all	\r\n"
				+ "	select DISTINCT  bi.item_id,'','','','','' from bill_items bi \r\n"
				+ "WHERE\r\n"
				+ "	bi.date between '"+fromDate+"' and '"+toDate+"')t \r\n"
				+ "GROUP by 1\r\n"
				+ "HAVING c=1 and item_name<>''\r\n"
				+ "order by t.doctor_refrence,cast(t.item_id as UNSIGNED)";
		;		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet MedicalStoreSaleSummery(String toDate,String fromDate) {
		String query ="select `DATE` ,total_billing_amount ,total_cancel_bill_amount ,total_sale_amount  from daily_sale_medical_store_final dsmsf  where Date between '"+fromDate+"' and '"+toDate+"'order by `DATE` asc";
		;		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet MedicalStoreRequestpills(String id) {
		String query ="select request_item_desc, request_qty from request_pills_register where invoice_id = "+id+"";
		;		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet MedicalStoreGenMultiplePDF(String invoiceID) {
		String query ="select item_name, received_qty, batch_name, dept_name, expiry_date from request_pills_register where invoice_id = "+invoiceID+"";
		;		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet karunaMeddiscountreport(String toDate,String fromDate) {
		String query ="select `opd_no` ,patient_name ,doctor_name ,round(total_amount,0), date, karuna_discount_amount from bills_entry where Date between '"+fromDate+"' and '"+toDate+"' and karuna_discount_amount<>0 order by `DATE` asc";
		;		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			System.out.println(rs.getRow());
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData1(String dateFrom, String dateTo,String index) { 
		String query = "SELECT `bill_id`, `opd_no`, `patient_name`, `doctor_name`,  `date`,`total_surcharge_value`,`tax_amount`,`total_roundoff_amount`, `insurance_type`, `time`,`bill_type`,`user_name`,`karuna_discount_amount`,if(`bill_type` not like '%return%',if(is_bill_scanned,'YES','NO'),'N/A')as is_scanned FROM `bills_entry` WHERE `date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_no` LIKE '%"+index+"%' OR `patient_name` LIKE '%"+index+"%' ORDER BY `bill_id` DESC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);


		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDatauserwise(String dateFrom, String dateTo,String userid) { 
		String query = "SELECT `bill_id`, `opd_no`, `patient_name`, `doctor_name`,  `date`,`total_surcharge_value`,`tax_amount`,`total_roundoff_amount`, `insurance_type`, `time`,`bill_type`,`user_name`,`payment_mode`,`karuna_discount_amount`,if(`bill_type` not like '%return%',if(is_bill_scanned,'YES','NO'),'N/A')as is_scanned FROM `bills_entry` WHERE `date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' and user_id='"+userid+"' ORDER BY `bill_id` DESC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDatauserwiseSearch(String dateFrom, String dateTo,String userid,String index) { 
		String query = "SELECT `bill_id`, `opd_no`, `patient_name`, `doctor_name`,  `date`,`total_surcharge_value`,`tax_amount`,`total_roundoff_amount`, `insurance_type`, `time`,`bill_type`,`user_name`,`karuna_discount_amount`,if(`bill_type` not like '%return%',if(is_bill_scanned,'YES','NO'),'N/A')as is_scanned FROM `bills_entry` WHERE `date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' and user_id='"+userid+"' AND (`opd_no` LIKE '%"+index+"%' OR `patient_name` LIKE '%"+index+"%') ORDER BY `bill_id` DESC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `bill_id`, `opd_no`, `patient_name`, `doctor_name`,  `date`,`total_surcharge_value`,`tax_amount`,`total_roundoff_amount`, `insurance_type`, `time`,`bill_type`,`user_name`,`payment_mode`,`karuna_discount_amount`,if(`bill_type` not like '%return%',if(is_bill_scanned,'YES','NO'),'N/A')as is_scanned FROM `bills_entry` WHERE `date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `bill_id` DESC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveReportData(String dateFrom, String dateTo,String u_name) { 
//		String query="SELECT\r\n"
//				+ "	type,\r\n"
//				+ "	insurance,\r\n"
//				+ "	bills_count,\r\n"
//				+ "	Bills_amount,\r\n"
//				+ "	online_count,\r\n"
//				+ "	online_amount,\r\n"
//				+ "	sum(cancel_count) as cancel_count,\r\n"
//				+ "	sum(cancel_amount) as cancel_amount,\r\n"
//				+ "    round((case when `type`='IPD' then sum((`Bills_amount`+0)+(`online_amount`+0)-(`cancel_amount`+0)) else 0 end),2) as ipd_total_amount,\r\n"
//				+ "    round((case when `type`='OPD' then sum((`Bills_amount`+0)+(`online_amount`+0)-(`cancel_amount`+0)) else 0 end),2) as opd_total_amount,\r\n"
//				+ "    round((case when (`type`='OPD' and `insurance`!='Rotary') then sum((`Bills_amount`+0)-(`cancel_amount`+0)) else 0 end),2) as cash_amount\r\n"
//				+ "    FROM\r\n"
//				+ "	(\r\n"
//				+ "	SELECT\r\n"
//				+ "		be.bill_type as type,\r\n"
//				+ "		be.insurance_type as insurance,\r\n"
//				+ "		sum(case when be.payment_mode in('Cash','NA') then 1 else 0 end) as bills_count,\r\n"
//				+ "		round(sum(case when be.payment_mode in('Cash','NA') then be.total_roundoff_amount else 0 end),2) as Bills_amount,\r\n"
//				+ "		sum(case when be.payment_mode = 'Online' then 1 else 0 end) as online_count,\r\n"
//				+ "		round(sum(case when be.payment_mode = 'Online' then be.total_roundoff_amount else 0 end),2) as online_amount,\r\n"
//				+ "		'0' as cancel_count,\r\n"
//				+ "		'0' as cancel_amount\r\n"
//				+ "	from\r\n"
//				+ "		bills_entry be\r\n"
//				+ "	WHERE\r\n"
//				+ "		be.`date` BETWEEN '"+dateFrom+"' and '"+dateTo+"'\r\n"
//				+ "		and be.user_name = '"+u_name+"'\r\n"
//				+ "	GROUP by 1,2	\r\n"
//				+ "UNION all\r\n"
//				+ "	SELECT\r\n"
//				+ "		be.bill_type,\r\n"
//				+ "		ci.insurance_type ,\r\n"
//				+ "		'0',\r\n"
//				+ "		'0',\r\n"
//				+ "		'0',\r\n"
//				+ "		'0',\r\n"
//				+ "		count(be.bill_id),\r\n"
//				+ "		round(sum(ci.total_value),2)\r\n"
//				+ "	FROM\r\n"
//				+ "		cancel_items ci\r\n"
//				+ "	left join bills_entry be on\r\n"
//				+ "		be.bill_id = ci.bill_id\r\n"
//				+ "	WHERE\r\n"
//				+ "		 ci.cancel_item_text1 in ('PENDING', 'CANCEL')\r\n"
//				+ "		and ci.`date` BETWEEN '"+dateFrom+"' and '"+dateTo+"'\r\n"
//				+ "		and ci.user_name = '"+u_name+"'\r\n"
//				+ "	GROUP by 1,2	\r\n"
//				+ "	)T\r\n"
//				+ "GROUP by\r\n"
//				+ "	1,2\r\n"
//				+ "order by `type` desc,`insurance` desc\r\n"
//				+ "\r\n"
//				+ "\r\n"
//				+ "\r\n"
//				+ "";
		String query="SELECT\r\n"
				+ "	type,\r\n"
				+ "	insurance,\r\n"
				+ "	bills_count,\r\n"
				+ "	Bills_amount,\r\n"
				+ "	online_count,\r\n"
				+ "	online_amount,\r\n"
				+ "	sum(cancel_count) as cancel_count,\r\n"
				+ "	sum(cancel_amount) as cancel_amount,\r\n"
				+ "    round((case when `type`='IPD' then sum((`Bills_amount`+0)+(`online_amount`+0)) else 0 end)) as ipd_total_amount,\r\n"
				+ "    round((case when `type`='OPD' then sum((`Bills_amount`+0)+(`online_amount`+0)) else 0 end)) as opd_total_amount,\r\n"
				+ "    round((case when (`type`='OPD' and `insurance`!='Rotary') then sum((`Bills_amount`+0)+(`cancel_amount`+0)) else 0 end)) as cash_amount\r\n"
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
				+ "		be.`date` BETWEEN '"+dateFrom+"' and '"+dateTo+"'\r\n"
				+ "		and be.user_name = '"+u_name+"' and be.bill_type not like '%RETURN%'\r\n"
				+ "	GROUP by 1,2	\r\n"
				+ "UNION all\r\n"
				+ "	SELECT\r\n"
				+ "		if(be.bill_type='IPD RETURN','IPD','OPD') as type,\r\n"
				+ "		be.insurance_type  ,\r\n"
				+ "		'0',\r\n"
				+ "		'0',\r\n"
				+ "		'0',\r\n"
				+ "		'0',\r\n"
				+ "		count(be.bill_id),\r\n"
				+ "		round(sum(be.total_amount),2)\r\n"
				+ "	FROM\r\n"
				+ "		bills_entry be\r\n"
				+ "	WHERE\r\n"
				+ "		be.`date` BETWEEN '"+dateFrom+"' and '"+dateTo+"'\r\n"
				+ "		and be.user_name = '"+u_name+"' and be.bill_type like '%RETURN%'\r\n"
				+ "			GROUP by 1,2	\r\n"
				+ "	)T\r\n"
				+ "GROUP by\r\n"
				+ "	1,2\r\n"
				+ "order by `type` desc,`insurance` desc";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveCancelitemData(String receiptID) { 
		String query = "SELECT quantity,item_id FROM cancel_items WHERE bill_id = '"+receiptID+"' and `cancel_item_text1`='PENDING'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveUserName(String dateFrom, String dateTo) { 
		//		String query = "SELECT `bill_id`, `opd_no`, `patient_name`, `doctor_name`,  `date`,`total_surcharge_value`,`tax_amount`,`total_roundoff_amount`, `insurance_type`, `time` FROM `bills_entry` WHERE `date`  BETWEEN '"
		//				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `bill_id` DESC ";
		String query="SELECT user_name FROM bills_entry WHERE date BETWEEN '"+ dateFrom + "' AND '" + dateTo + "' GROUP BY user_name";
		try {
			//			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet purcahseOrder(String dateFrom, String dateTo) { 
		String query = "SELECT I.`item_id` AS 'ITEM CODE',I.`item_name` AS 'ITEM NAME', I.`item_desc` AS 'DESC', I.`item_brand` AS 'BRAND', I.`item_concentration` AS 'CONCENTRATION', I.`item_salt_name` AS 'SALT', I.`item_meas_unit` AS 'PACK SIZE', I.`item_mrp` AS 'MRP', ROUND((I.`item_mrp`/I.`item_meas_unit`),3) AS 'MRP/Unit', I.`item_purchase_price` AS 'PURCHASE PRICE',I.`item_total_stock` AS 'CURRENT STOCK', ROUND(SUM(B.`quantity`),0) AS 'Two Months Qty', ROUND(SUM(B.`quantity`)/2,0) AS 'One Month Qty',IF ((ROUND(SUM(B.`quantity`)/2,0)-I.`item_total_stock`)>0 ,ROUND(SUM(B.`quantity`)/2,0) , 'NO ORDER') AS 'ORDER STATUS'  FROM `bill_items` B JOIN `items_detail` I ON B.`item_id`=I.`item_id` WHERE  B.`date` BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND I.`item_oredered`='NO'  GROUP BY B.`item_id` ORDER BY I.`item_id` ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet ExcessStock() { 
		String query = "SELECT * FROM `excess_stock` WHERE `Excess_Stock`!='NA'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet toBeOrderItems() { 
		String query = "SELECT *,'true' as 'Check' FROM `to_be_ordered_view`";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet itemSaleData(String dateFrom, String dateTo) { 
		String query = "SELECT I.`item_id` AS 'ITEM CODE',I.`item_name` AS 'ITEM NAME', I.`item_desc` AS 'DESC', I.`item_brand` AS 'BRAND', I.`item_concentration` AS 'CONCENTRATION', I.`item_salt_name` AS 'SALT', I.`item_meas_unit` AS 'PACK SIZE', I.`item_mrp` AS 'MRP', ROUND((I.`item_mrp`/I.`item_meas_unit`),3) AS 'MRP/Unit', I.`item_purchase_price` AS 'PURCHASE PRICE',I.`item_total_stock` AS 'CURRENT STOCK', ROUND(SUM(B.`total_value`),0) AS 'TOTAL SALE VALUE', ROUND(SUM(B.`quantity`),0) AS 'TOTAL SALE QTY',(SELECT `invoice_supplier_name` FROM `item_last_purchase_view` WHERE `item_id`=I.`item_id` ORDER BY `invoice_date` DESC LIMIT 1) AS 'VENDORS' FROM `bill_items` B JOIN `items_detail` I ON B.`item_id`=I.`item_id` WHERE B.`date` BETWEEN '" + dateFrom + "' AND '" + dateTo + "' GROUP BY B.`item_id` ORDER BY I.`item_salt_name` ASC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveDoctorWiseData(String dateFrom, String dateTo) { 
		String query = "SELECT doctor_name as `Doctor Name`,count(`total_roundoff_amount`) as `Srno`,sum(`total_roundoff_amount`) as `Total Amount` FROM `bills_entry` where `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' group by doctor_name";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveSalesTaxReport(String dateFrom, String dateTo) { 
		String query = "SELECT Count(`bill_item_id`) as 'Total Items',sum(`total_value`) as 'After Tax Amount',`tax_percentage` as 'Tax Percentage',sum(`tax_amount`) as 'Tax Amount',`surchargePercentage` as 'Surcharge Percentage',sum(`surchargeValue`) as 'Surcharge Amount' FROM `bill_items` WHERE `date` between '"+dateFrom+"' and '"+dateTo+"' and `bill_item_text3`!='CANCEL' group by `tax_percentage`";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllPendingPills() {


		String query = "SELECT DISTINCT `ipd_or_misc_number`, `p_id`, `p_name`, `user_name`, `date` FROM store_pills_register WHERE `text1`='PENDING'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveBillData(String bill_no) {


		String query = "SELECT  `patient_name`, `mobile_no`, `doctor_name`, `insurance_type`, `payable`, `total_amount`,`total_roundoff_amount`, `date`, `time`,`bill_text1`,`bill_text2`,`user_name`,`karuna_discount_amount`,`opd_no` FROM `bills_entry` WHERE `bill_id`="+bill_no+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveBillDataDetail(String bill_no) {


		String query = "SELECT  `opd_no`, `patient_name`, `mobile_no`, `doctor_name`, `insurance_type`, `insurance_no`, `payable`, `total_amount`, `total_roundoff_amount`, `tax`, `discount`, `date`, `time` FROM `bills_entry` WHERE `bill_id`="+bill_no+" and `bill_type`='OPD'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveIPDBillDataDetail(String bill_no) {


		String query = "SELECT  `opd_no`, `patient_name`, `mobile_no`, `doctor_name`, `insurance_type`, `insurance_no`, `payable`, `total_amount`, `total_roundoff_amount`, `tax`, `discount`, `date`, `time` FROM `bills_entry` WHERE `bill_id`="+bill_no+" and `bill_type`='IPD'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveBillItemsData(String bill_no) {


		String query = "SELECT B.`item_name`, B.`item_hsn_code`,B.`item_batch`, B.`expiry_date`, B.`unit_price`,B.`quantity`,B.`tax_amount`,B.`tax_percentage`,B.`surchargeValue`, B.`surchargePercentage`, B.`total_value`,TRIM(I.`item_salt_name`),B.`mrp`,B.`pack_size`,B.`Karuna_discount`,`new_unit_price` FROM `bill_items` B LEFT JOIN `items_detail` I ON  B.`item_id`=I.`item_id` WHERE B.`bill_id`="+bill_no+"";
		System.out.print(query);	
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveBillItemsDataDetail(String bill_no) {
		String query = "SELECT `bill_item_id`,`item_id`,`item_name`,`item_batch_id`, `item_batch`, `unit_price`, `quantity`, (`tax_percentage`+`surchargePercentage`),`tax_amount`, `total_value`, `expiry_date`,`date` FROM `bill_items` WHERE `bill_id`="+bill_no+" AND `bill_item_text3`!= 'CANCEL' AND quantity>0";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveBillItemsDataDetailNew(String bill_no) {
		String query = "SELECT `bill_item_id`, `payable`, `item_id`, `item_name`, `item_desc`, " +
				"`item_batch_id`, `item_hsn_code`, `item_batch`, `unit_price`, sum(`quantity`) as quantity, " +
				"`tax_percentage`, `discount`, sum(`tax_amount`) as tax_amount, `surchargePercentage`, sum(`surchargeValue`) as surchargeValue, " +
				"round(sum(`total_value`),2) as total_value, `expiry_date`, `date`, `time`, `user_id`, `user_name`, `mrp`, " +
				"`pack_size`, `item_risk_type`, `karuna_discount`, `new_unit_price` " +
				"FROM `bill_items` WHERE `return_bill_id` = '"+bill_no+"' GROUP by return_bill_id,item_id,item_batch_id  ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedRegister(String fromDate,String toDate) {
		String query = "SELECT `issued_id`, `department_id`, `department_name`, `person_name`, `intent_slip_no`, `date`, `time`, `item_id`, `item_name`, `item_desc`, `issued_qty`, `previouse_stock`, `consumable`, `expiry_date`, `issued_by` FROM `issued_department_register` WHERE  `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllSummery(String fromDate,String toDate,String type,String mode,String t) {
		String query = "SELECT count(bill_id), sum(total_roundoff_amount) FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' AND `payment_mode`='"+mode+"' and `bill_type`='"+t+"'";
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
	public ResultSet retrieveAllSummerycash(String fromDate,String toDate,String type,String bill_type,String t) {
		String query = "SELECT count(bill_id), sum(total_roundoff_amount) FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' AND `payment_mode`='"+bill_type+"' and `bill_type`='"+t+"'";
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
	public ResultSet retrieveAllSummeryUserwiseinAdmin(String fromDate,String toDate,String userame,String paymentmode) {
		String query = "SELECT count(bill_id), sum(total_roundoff_amount) FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `user_name`='"+userame+"' AND `payment_mode`='"+paymentmode+"'";
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

	//	
	public ResultSet retrieveAllSummeryNew(String fromDate,String toDate,String type) {
		String query = "SELECT count(bill_id),(Select sum(total_roundoff_amount) from bills_entry where payment_mode='Cash' and `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' ),(Select sum(total_roundoff_amount) from bills_entry where payment_mode='Online' and `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' )  FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"'";
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

	public ResultSet retrieveAllSummeryUserWiseNew(String fromDate,String toDate,String type,String username,String bill_type,String t) {
		String query;
		if(!username.equals("")) {
			query = "SELECT   count(bill_id), (Select sum(total_roundoff_amount) from bills_entry where `date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"' and `payment_mode`='"+bill_type+"' and `bill_type`='"+t+"' ) FROM `bills_entry` WHERE `payment_mode`='"+bill_type+"' and `date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "' and `bill_type`='"+t+"' and `user_name`='"+username+"' and `insurance_type`='"+type+"'" ;
		}
		else {
			query = "SELECT   count(bill_id), (Select sum(total_roundoff_amount) from bills_entry where `date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `payment_mode`='"+bill_type+"' and `bill_type`='"+t+"' ) FROM `bills_entry` WHERE `payment_mode`='"+bill_type+"' and `date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "' and `bill_type`='"+t+"' and `insurance_type`='"+type+"'" ;
		}
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
	public ResultSet retrievecashamount(String fromDate,String toDate,String unknown,String username,String opd) {
		String query = "SELECT SUM(ci.total_value)  from cancel_items ci left join bills_entry be on be.bill_id =ci.bill_id  where ci.user_name ='"+username+"' and ci.`date` between '"+fromDate+"' and '"+toDate+"' and be.bill_type='"+opd+"'and be.insurance_type <>'Rotary'";
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
	public ResultSet retrievecancelamount(String fromDate,String toDate,String type,String username,String bill_type,String type1) {
		String query = "SELECT   count(bill_id), (Select sum(total_roundoff_amount) from bills_entry where payment_mode='"+type1+"' and `bill_type`='"+bill_type+"' and `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"' ) FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"' and `insurance_type`='"+type+"'and payment_mode='"+type1+"' and `bill_type`='"+bill_type+"'" ;
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
	public ResultSet retrievecashamount1(String fromDate,String toDate,String type,String bill_type,String type1) {
		String query = "SELECT   count(bill_id), (Select sum(total_roundoff_amount) from bills_entry where payment_mode='"+type1+"' and `bill_type`='"+bill_type+"' and `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"') FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `insurance_type`='"+type+"'and payment_mode='"+type1+"' and `bill_type`='"+bill_type+"'" ;
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
	public ResultSet retrieveAllSummeryUserWiseNewoline(String fromDate,String toDate,String type,String username,String type1,String t) {
		String query = "SELECT   count(bill_id), (Select sum(total_roundoff_amount) from bills_entry where payment_mode='"+type1+"' and `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"' and `bill_type`='"+t+"') FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"' and `insurance_type`='"+type+"'and payment_mode='"+type1+"' and `bill_type`='"+t+"'" ;
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
	public ResultSet retrieveAllSummeryHighRiskItem(String fromDate,String toDate,String userID) {
		String query = "SELECT  item_id, item_name,sum(quantity), sum(total_value)\r\n"
				+ "FROM medicalstore_db.bill_items WHERE  item_risk_type='High Risk.' and `date` BETWEEN '"+fromDate+"' and '"+toDate+"' and user_id ='"+userID+"' GROUP by item_id ;\r\n"
				+ "";
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
	//	public ResultSet retrieveAllSummeryUserWiseNewBillsCount(String fromDate,String toDate,String type,String username) {
	//		String query = "SELECT (Select count(bill_id) from bills_entry where payment_mode='Cash' and `date` BETWEEN '"
	//				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"'  and `user_name`='"+username+"'), (Select count(bill_id) from bills_entry where payment_mode='Online' and `date` BETWEEN '"
	//				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"'  and `user_name`='"+username+"')  FROM `bills_entry` WHERE `date` BETWEEN '"
	//				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"' limit 1" ;
	//		System.out.println(query);
	//		try {
	//			rs = statement.executeQuery(query);
	//
	//			System.out.println(rs.getRow());
	//		} catch (SQLException sqle) {
	//			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
	//					javax.swing.JOptionPane.ERROR_MESSAGE);
	//		}
	//		return rs;
	//	}
	public ResultSet retrieveAllSummeryUserWise(String fromDate,String toDate,String type,String username) {
		String query = "SELECT count(bill_id), sum(total_roundoff_amount) FROM `bills_entry` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "' AND `insurance_type`='"+type+"' and `user_name`='"+username+"'";
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
	public ResultSet retrieveAllIssuedPills(String fromDate,String toDate) {
		String query = "SELECT a.item_id,a.item_name,a.`item_brand`,a.`item_total_stock`,(select  COALESCE(sum(b.quantity), 0 ) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Main Store',(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Departments',(select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Total' FROM `items_detail` a";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveItemsStatus(String fromDate,String toDate) {
		String query = "SELECT I.`item_id` AS 'ITEM CODE',I.`item_name` AS 'ITEM NAME', I.`item_desc` AS 'DESC', I.`item_brand` AS 'BRAND', I.`item_concentration` AS 'CONCENTRATION', I.`item_salt_name` AS 'SALT', I.`item_meas_unit` AS 'PACK SIZE', I.`item_mrp` AS 'MRP', ROUND((I.`item_mrp`/I.`item_meas_unit`),3) AS 'MRP/Unit', I.`item_purchase_price` AS 'PURCHASE PRICE',I.`item_total_stock` AS 'CURRENT STOCK', ROUND(SUM(B.`total_value`),0) AS 'TOTAL SALE VALUE', ROUND(SUM(B.`quantity`),0) AS 'TOTAL SALE QTY',(SELECT A.`invoice_supplier_name` FROM `invoice_entry` A JOIN `invoice_items` B ON A.`invoice_id`=B.`invoice_id` WHERE B.`item_id`=I.`item_id` ORDER BY A.`invoice_date` DESC LIMIT 1) AS 'VENDORS' FROM `bill_items` B JOIN `items_detail` I ON B.`item_id`=I.`item_id` WHERE B.`date` BETWEEN '"+fromDate+"' AND '"+toDate+"' GROUP BY B.`item_id` ORDER BY I.`item_salt_name` ASC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedPillsNew(String fromDate3,String toDate3,String fromDate1,String toDate1) {
		String query = "SELECT a.item_id,a.item_name, a.`item_desc`,a.`item_brand`,a.`item_total_stock`,(select  COALESCE(sum(b.quantity), 0 ) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate1 + "' AND '" + toDate1 + "') as 'Last Month',(select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "') as 'Last Three Months', round(((select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "'))/6,0) as 'Reorder Level', round(((select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "'))/6,0) as 'Order Qty.' FROM `items_detail` a where ((select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate1 + "' AND '" + toDate1 + "'))>0 AND round(((select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "'))/12,0)>=a.`item_total_stock` AND a.`item_oredered`='NO'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public int inserStorePillsRegister(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `store_pills_register`(`doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`, `text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 18; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}


	public int retrieveCounterData() {
		String query = "SELECT `bill_id` FROM `bills_entry` ORDER BY `bill_id` DESC LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
		try {
			while (rs.next()) {
				NumberOfRows=rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NumberOfRows++;
		return NumberOfRows;
	}
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM issued_department_register WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserBillEntry(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `bills_entry`(`opd_no`, `patient_name`, `mobile_no`, `doctor_name`, `insurance_type`,`insurance_no`, `payable`, `total_amount`,`total_roundoff_amount`, `tax`, `discount`, `tax_amount`,`total_surcharge_value`, `date`, `time`, `user_id`, `user_name`,`bill_text1`,`bill_text2`,`payment_mode`,`bill_type`,`return_bill_id`,`return_reason`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println("hlo"+insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 24; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
	
	public void updateReturnBillId(int billID,int returnBillID) throws Exception
	{
		statement.executeUpdate("UPDATE `bills_entry` SET `return_bill_id`='"+returnBillID+"' WHERE `bill_id`="+billID+"");
	}
	public int inserOPDBillEntry(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `bills_entry`(`opd_no`, `patient_name`, `mobile_no`, `doctor_name`, `insurance_type`,`insurance_no`, `payable`, `total_amount`,`total_roundoff_amount`, `tax`, `discount`, `tax_amount`,`total_surcharge_value`, `date`, `time`, `user_id`, `user_name`,`bill_text1`,`bill_text2`,`payment_mode`,`bill_type`,`karuna_discount_amount`,`return_bill_id`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println("hlo"+insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 24; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
	public int insertBillingItems(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `bill_items`( `bill_id`, `patient_name`, `doctor_name`, `insurance_type`, `payable`, `item_id`, `item_name`, `item_desc`,`item_hsn_code`, `item_batch_id`, `item_batch`, `unit_price`, `quantity`, `tax_percentage`, `discount`, `tax_amount`, `total_value`, `surchargePercentage`, `surchargeValue`,`expiry_date`, `date`, `time`, `user_id`, `user_name`,`mrp`,`pack_size`,`item_risk_type`,`karuna_discount`,`new_unit_price`,`bill_item_text3`,`return_bill_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 32; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
}
