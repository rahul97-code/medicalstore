package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ChallanDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ChallanDBConnection() {

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

		public void updateChallanStatus(String challan_id,String payment_id) throws Exception
		  {
			statement.executeUpdate("UPDATE `challan_entry` SET `challan_text1`='"+payment_id+"' WHERE `challan_id`="+challan_id);
		  }
		public void updateChallanStatus1(String challan_id) throws Exception
		  {
			statement.executeUpdate("UPDATE `challan_items` SET `item_text6`='USED' WHERE `challan_item_id`="+challan_id);
		  }
		public ResultSet retrieveAllchallanItems(String fromDate,String toDate) {
			String query = "SELECT `challan_item_id`, `item_id`, `challan_item_name`, `challan_item_desc`, `challan_id`, `item_meas_units`, `item_qty`, `item_unit_price`, `item_discount`, `item_tax`, `challan_value`, `item_expiry_date`, `item_date`, `item_time` FROM `challan_items` WHERE `item_date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "'";
			try {
				rs = statement.executeQuery(query);

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveChallan(String search,String supplier_id) {
			String query = "SELECT `item_id`, `challan_item_name`, `challan_item_desc`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`challan_value`, `item_expiry_date`, `item_date`,`item_batch_number`,`challan_item_id` FROM `challan_items` WHERE `challan_supplier_id`='"+supplier_id+"' AND `challan_id`='"+search+"' AND `item_text6` ='null'";
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
			String query = "SELECT `challan_id`, `challan_id2`, `challan_order_no`, `challan_supplier_id`, `challan_supplier_name`, `challan_date`, `challan_time`, `challan_entry_user`, `challan_total_amount` FROM `challan_entry` WHERE `challan_text1`='"+id+"'";
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
	public void inserChallanItem(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `challan_items`(  `challan_id`,`challan_supplier_id`,`challan_supplier_name`,`item_id`, `challan_item_name`, `challan_item_desc`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`challan_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <23; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
}
