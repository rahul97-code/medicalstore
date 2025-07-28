//Developed by Arun kumar

package hms.store.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import hms.main.DBConnection;

public class CancelRestockFeeDB extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public CancelRestockFeeDB() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public ResultSet retrieveData1(String type)
	//to retrieve tax values from database...
	{
		String query="SELECT `id`,`restock_fee`,`days`,`month`,`return_gst`,`cutting_fee` from `cancel_restock_master_1` Where type='"+type+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveData(String type)
	//to retrieve tax values from database...
	{
		String query="SELECT `id`,`restock_fee`,`days`,`month`,`return_gst`,`cutting_fee` from `cancel_restock_master_1` Where type='"+type+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}

	public ResultSet retrieveDataNew(String index)
	//to retrieve tax values from database...
	{
		String query="SELECT `id`,`billing_cutting_chrg` from `cancel_restock_master_1` where `type`='"+index+"' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}

	public void updateData(String[] data,String ID,String type) {
		//to update tax values in database...
		String insertSQL = "UPDATE `cancel_restock_master_1` SET `restock_fee`= ? ,`days`=?,`month`=?,`return_gst`=? ,`cutting_fee`=? WHERE `id`='"+ID+"' and `type`='"+type+"'";
		System.out.println(insertSQL);
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement(insertSQL);
			for (int i = 1; i < 6; i++) { 
				preparedStatement.setString(i, data[i - 1]);
			}

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateData1(String cutting_chrg,String ID) throws SQLException {
		//to update tax values in database...
		String insertSQL = "UPDATE `cancel_restock_master_1` SET `billing_cutting_chrg`='"+cutting_chrg+"' WHERE `id`='"+ID+"'";
		System.out.println(insertSQL);
		statement.executeUpdate(insertSQL);
}
}
