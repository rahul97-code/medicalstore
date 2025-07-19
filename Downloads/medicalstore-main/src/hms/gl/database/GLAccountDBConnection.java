package hms.gl.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class GLAccountDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public GLAccountDBConnection() {

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

	public void updateData(String[] data) throws Exception {
		String insertSQL = "";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 9; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateDataBalance(String bal,String id,String type) throws Exception
	  {
		statement.executeUpdate("UPDATE `gl_account` SET `beg_balance`='"+bal+"' WHERE `gl_id`="+id+" AND `gl_type`='"+type+"'");
	  }
	public ResultSet retrieveAllInvoiceItems(String fromDate, String toDate) {
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

	public ResultSet retrieveAllOutstanding(String supplierId) {
		String query = "SELECT `gl_no`, `gl_name`, `beg_balance` FROM `gl_account` WHERE `gl_type`='SUPPLIER' AND `gl_no`='"
				+ supplierId + "' order by `gl_id` desc limit 1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllGLDetail(String name, String type,
			String fromDate, String toDate) {
		String query = "SELECT `gl_id`, `credit`, `debit`, `beg_balance`, `date`, `time`, `text1` FROM `gl_account` WHERE `gl_name`='"
				+ name
				+ "' AND `gl_type`='"
				+ type
				+ "' AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveMyDEmo(String NO, String type) {
		String query = "SELECT `gl_id`, `credit`, `debit`, `beg_balance`, `date`, `time`, `text1` FROM `gl_account` WHERE `gl_no`='"
				+ NO
				+ "' AND `gl_type`='"
				+ type
				+ "'";
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
				.prepareStatement("DELETE FROM items_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	public void inserInvoiceItem(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `invoice_items`( `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 21; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}

	public int inserGLAccountInsurance(String[] data) throws Exception {

		double begBal = 0;

		String query = "SELECT `beg_balance` FROM `gl_account` WHERE `gl_name`='"
				+ data[1] + "' AND `gl_type`='" + data[2] + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}

		try {
			while (rs.next()) {

				begBal = Double.parseDouble(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (data[4].equals("0")) {
			data[5] = (begBal - Double.parseDouble(data[3])) + "";
		} else {
			data[5] = (begBal + Double.parseDouble(data[4])) + "";
		}
		String insertSQL = "INSERT INTO `gl_account`( `gl_no`,`gl_name`, `gl_type`,`credit`, `debit`, `beg_balance`, `ending_balance`, `date`, `time`, `user`,`text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 12; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}

	public int inserGLAccountBank(String[] data) throws Exception {

		double begBal = 0;

		String query = "SELECT `beg_balance` FROM `gl_account` WHERE `gl_name`='"
				+ data[1] + "' AND `gl_type`='" + data[2] + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}

		try {
			while (rs.next()) {

				begBal = Double.parseDouble(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (data[4].equals("0")) {
			data[5] = (begBal + Double.parseDouble(data[3])) + "";
		} else {
			data[5] = (begBal - Double.parseDouble(data[4])) + "";
		}
		String insertSQL = "INSERT INTO `gl_account`( `gl_no`,`gl_name`, `gl_type`,`credit`, `debit`, `beg_balance`, `ending_balance`, `date`, `time`, `user`,`text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 12; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}

	public int inserGLAccountSupplier(String[] data) throws Exception {

		double begBal = 0;

		String query = "SELECT `beg_balance` FROM `gl_account` WHERE `gl_name`='"
				+ data[1] + "' AND `gl_type`='" + data[2] + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {

				begBal = Double.parseDouble(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (data[4].equals("0")) {
			data[5] = (Double.parseDouble(data[3]) + begBal) + "";
		} else {
			data[5] = (begBal - Double.parseDouble(data[4])) + "";
		}

		String insertSQL = "INSERT INTO `gl_account`( `gl_no`,`gl_name`, `gl_type`,`credit`, `debit`, `beg_balance`, `ending_balance`, `date`, `time`, `user`,`text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 12; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}

	public int inserGLAccount1(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `gl_account`( `gl_no`,`gl_name`, `gl_type`,`credit`, `debit`, `beg_balance`, `ending_balance`, `date`, `time`, `user`,`text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 12; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
}
