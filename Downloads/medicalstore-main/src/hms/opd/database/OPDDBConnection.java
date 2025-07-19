package hms.opd.database;

import hms.main.DateFormatChange;
import hms.main.HMSDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;

public class OPDDBConnection extends HMSDBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public OPDDBConnection() {

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
	
	public void updateCancellation(String opdID) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `opd_entery` SET `opd_text3`='CANCEL' WHERE `opd_id`= "+opdID);
	  }
	
	public void updatePrescriptionData(String[] data) throws Exception {
		String insertSQL = "UPDATE `opd_entery` SET `opd_symptom`=?,`opd_prescription`=? WHERE `opd_id`="+data[2];

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 3; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public ResultSet retrievedetails(String id,String ipd) {
		String query = "select `expense_desc`,`item_meas_unit`,`item_risk_type`,`expense_text3`,`item_mrp` from ipd_expenses where charges_id="+id+" and ipd_id="+ipd+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public int retrieveCounterData() {
		String query = "SELECT * FROM `opd_entery` WHERE 1";
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
		NumberOfRows++;
		return NumberOfRows;
	}

	public int retrieveCounterTodayToken() {
		new DateFormatChange();
		String query = "SELECT * FROM `opd_entery` WHERE `opd_date`= '"
				+ DateFormatChange.StringToMysqlDate(new Date()) + "'";
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
		NumberOfRows++;
		return NumberOfRows;
	}

	public int retrieveCounterTodayTokenDoctorWise(String doctorName) {
		new DateFormatChange();
		String query = "SELECT * FROM `opd_entery` WHERE `opd_date`= '"
				+ DateFormatChange.StringToMysqlDate(new Date()) + "'  AND `opd_doctor`='"+doctorName+"'";
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
		NumberOfRows++;
		return NumberOfRows;
	}
	public ResultSet retrieveAllData() {
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_type` FROM `opd_entery` WHERE 1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveipdid(String ipd_id) {
		String query = "SELECT\r\n"
				+ "	ie.p_id ,ie.ipd_discharged \r\n"
				+ "FROM\r\n"
				+ "	ipd_entery ie  \r\n"
				+ "where \r\n"
				+ "	ie.ipd_id = '"+ipd_id+"' ;";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveIpdTotalCharge(String ipd_id) {
		String query = "SELECT ipd_total_charges  from ipd_entery ie WHERE ipd_id2 = '"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllData1(String dateFrom, String dateTo,String index) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND p_id LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataExcel(String dateFrom, String dateTo) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_diseasestype`, `opd_charge`,`opd_date`, `opd_entry_time`,`opd_entry_user` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo, String services) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_diseasestype`='"+services+"' AND `opd_text3` IS NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllDataInsurance(String insuranceType,String dateFrom, String dateTo, String services) { 
		String query = "SELECT  O.`opd_id`, O.`p_id`, O.`p_name`, O.`opd_doctor`, O.`opd_date`, O.`opd_token`, O.`opd_diseasestype`, O.`opd_charge` FROM `opd_entery` O,`patient_detail` P WHERE O.p_id=P.`pid1` AND O.opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND O.`opd_diseasestype`='"+services+"' AND O.`opd_text3` IS NULL AND P.`p_insurancetype`='"+insuranceType+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	
	public ResultSet retrieveAllDataDoctor(String dateFrom, String dateTo, String services,String doctor) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_diseasestype`='"+services+"' AND `opd_doctor`='"+doctor+"' AND `opd_text3` IS NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCancelledData(String dateFrom, String dateTo, String services) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_diseasestype`='"+services+"' AND `opd_text3` IS NOT NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorWise(String dateFrom, String dateTo, String doctorname) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_doctor`='"+doctorname+"' AND `opd_text3` IS NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataWithOpdId(String opd_id) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_symptom`, `opd_prescription`, `opd_token`, `opd_type`, `opd_charge`,`opd_entry_time` FROM `opd_entery` WHERE opd_id ='"
				+ opd_id +"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataWithOpdId2(String opd_id) { 
		String query = "SELECT\r\n"
				+ "	oe.p_id ,\r\n"
				+ "	oe.p_name ,\r\n"
				+ "	oe.opd_doctor ,\r\n"
				+ "	if(kmd.discount_isconsumed or cast(kmd.date_time as date)<>CURRENT_DATE(),0,COALESCE(kmd.discount,0)) as discount\r\n"
				+ "from\r\n"
				+ "	opd_entery oe\r\n"
				+ "left join\r\n"
				+ "	karuna_med_discount kmd\r\n"
				+ "	on kmd.opd_id =oe.opd_id \r\n"
				+ "where oe.opd_id ='"+opd_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllKarunaMSDS(String dateFrom, String dateTo) { 
		String query = "SELECT `id`,`opd_id`, `p_name`, `p_id`, `receipt_id`,`consultant`,`bill_amount`,`bill_discount_amount`,`p_age`,`p_sex`,`p_mobile` FROM `karuna_med_discount` WHERE cast(date_time as date)  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `date_time` DESC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataWithIPDId(String ipd_id) { 
		String query = "SELECT `p_id`, `p_name`, `ipd_doctor_refference`, `ipd_entry_date`, `ipd_total_charges`,`ipd_id`,`insurance_type`  FROM `ipd_entery` WHERE ipd_id2='"
				+ ipd_id +"' AND `ipd_discharged`='No' ORDER BY ipd_id ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataPatientID(String patientID) {
		String query = "SELECT  `opd_id`,`opd_date` FROM `opd_entery` WHERE `p_id` = "
				+ patientID+" ORDER BY `opd_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public String retrieveLastOpdPatient(String index) {
		String date = "";
		String query = "SELECT  `opd_doctor`, `opd_date` FROM `opd_entery` WHERE `p_id` = "
				+ index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				date = rs.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public boolean opdPatientAllowed(int token,String index) {
		int tokenNo =0;
		
		String query = "SELECT `opd_token` FROM `opd_entery` WHERE `p_id` = "
				+ index+" and opd_date='"
						+ DateFormatChange.StringToMysqlDate(new Date()) + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				tokenNo =Integer.parseInt(rs.getObject(1).toString());
				System.out.println(DateFormatChange.StringToMysqlDate(new Date())+"    "+tokenNo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tokenNo<=token)
		{
			return false;
		}
		return true;
	}
	public ResultSet searchPatientWithIdOrNmae(String index) {
		String query = "SELECT * FROM `patient_detail` where pid1 LIKE '%"
				+ index + "%' OR p_name LIKE '%" + index + "%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getNextOPD(String doctorname, String date, String index) {
		String query = "SELECT `opd_id`,`p_id`,`opd_date`,`opd_token` FROM `opd_entery` a where "
				+ index
				+ "= (select count(distinct(opd_id)) from `opd_entery` where opd_id<=a.opd_id and opd_date='"
				+ date
				+ "' and opd_doctor='"
				+ doctorname
				+ "' ) and opd_doctor='"
				+ doctorname
				+ "' and opd_date='"
				+ date + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int getDoctorTodayOPD(String doctorname, String date) {
		int counter=0;
		String query = "select count(distinct(opd_id)) from `opd_entery` where opd_id<=opd_id and opd_date='"
				+ date + "' and opd_doctor='" + doctorname + "'";
		try {
			rs = statement.executeQuery(query);
			while (rs.next()) {
				counter=Integer.parseInt( rs.getString(1));
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return counter;
	}
	public void updateTotalAmount(String opdID,String amount) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `ipd_entery` SET `ipd_total_charges` ='"+amount+"' WHERE `ipd_id`= "+opdID+"");
	  }
	public void updateKarunaDiscount(String opdID,String[] data) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `karuna_med_discount` SET `bill_id` ='"+data[0]+"',`bill_amount`='"+data[1]+"',`bill_discount_amount`='"+data[2]+"',`bill_generated_by`='"+data[3]+"',`bill_generated_by_id`='"+data[4]+"',`discount_isconsumed`='"+data[5]+"' WHERE `opd_id`= '"+opdID+"' and cast(`date_time` as date)=CURRENT_DATE() and discount_isconsumed <>1;");
	  }
	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM inventory WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserDataIpd(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`item_mrp`,`batch_id`,`batch_name`,`med_source`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 19; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserDataIpdExpense(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`item_mrp`,`batch_id`,`batch_name`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 15; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int cancelinserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`item_mrp`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 15; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
}
