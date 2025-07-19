package hms.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class GeneralDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public GeneralDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}


	public String retrieveFormula1() {
		String query = "SELECT `detail_desc` FROM `hms_detail` WHERE `detail_id`=2";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String version="";
		try {
			while (rs.next()) {
				version=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	public ResultSet retrieveFormulaData() {
		String query = " select * from formula_master order by ins_name";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveItemCategories() {
		String query = " select distinct item_cat_type from formula_master";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public String[] retrieveAllFormulas() {
		String query = " SHOW COLUMNS FROM formula_master LIKE 'formula'";
		try {
			rs = statement.executeQuery(query);
			if (rs.next()) {
			    String type = rs.getString("Type"); 
			    String enumStr = type.replaceAll("enum\\(|\\)", "");
			    String[] enumValues = enumStr.replace("'", "").split(",");
			    return enumValues;
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}	
		return null;
	}
	public void inserFormulaData(String[] data) throws Exception
	  {
		  String insertSQL = "REPLACE INTO formula_master(id, ins_id, ins_name, item_cat_type, formula)VALUES(?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <6; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
