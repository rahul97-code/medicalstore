package hms.patient.database;

import hms.main.HMSDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class PatientDBConnection extends HMSDBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public PatientDBConnection() {

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
		String insertSQL     ="UPDATE `patient_detail` SET `p_name`= ?, `p_agetype`= ?, `p_age`= ?, `p_birthdate`= ?, `p_sex`= ?, `p_addresss`= ?, `p_city`= ?, `p_telephone`= ?, `p_bloodtype`= ?, `p_guardiantype`= ?, `p_father_husband`= ?, `p_insurancetype`= ?,`p_text1`= ?,`p_insurance_no`= ?  WHERE `pid1` = '"+data[14]+"'";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <15; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }

	public void updateDataEmail(String p_id,String email) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `patient_detail` SET `p_text6`='"+email+"' where `pid1` = '"+p_id+"'");
	  }
	public void updateDataPatientID(String autoINC,String p_code) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `patient_detail` SET `pid1`='"+p_code+"',`pid2`='"+p_code+"' where `id` = "+autoINC);
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
	public ResultSet retrieveDataWithIndex(String index)
	{
	  String query="SELECT  `p_name`, `p_age`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_insurancetype`,`p_insurance_no` FROM `patient_detail` WHERE `pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveEmailWithIndex(String index)
	{
	  String query="SELECT  `p_text6` FROM `patient_detail` WHERE `pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex1(String index)
	{
	  String query="SELECT `pid1`, `p_name`, `p_guardiantype`, `p_father_husband`, `p_age`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_insurancetype` FROM `patient_detail` WHERE`pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex2(String index)
	{
	  String query="SELECT `p_name`, `p_agetype`, `p_age`, `p_birthdate`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_bloodtype`, `p_guardiantype`, `p_father_husband`, `p_insurancetype`, `p_insurance_no`, `p_text1` FROM `patient_detail` WHERE`pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `id`, `pid1`, `p_name`, `p_father_husband`, `p_insurancetype` ,`p_age`,`p_sex` FROM `patient_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData(String index)
	{
	  String query="SELECT `id`, `pid1`, `p_name`, `p_father_husband`, `p_insurancetype` ,`p_age`,`p_sex` FROM `patient_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
	  String query="SELECT * FROM `patient_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `patient_detail` WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `patient_detail`(`pid1`, `pid2`, `p_name`, `p_agetype`, `p_age`, `p_birthdate`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_bloodtype`, `p_guardiantype`, `p_father_husband`, `p_insurancetype`,`p_text1`,`p_insurance_no`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		  for (int i = 1; i <17; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
		  
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	  }
}
