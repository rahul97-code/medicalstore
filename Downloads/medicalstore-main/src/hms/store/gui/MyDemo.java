package hms.store.gui;

import hms.gl.database.GLAccountDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MyDemo {

	Vector<String> id = new Vector<String>();
	Vector<Double> credits = new Vector<Double>();
	Vector<Double> debits = new Vector<Double>();
	Vector<Double> balance = new Vector<Double>();
	Vector<Double> itemDescV = new Vector<Double>();
	Vector<Double> measUnitV = new Vector<Double>();
	int currentsp = 0;
	String TYPE="";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new MyDemo("INSURANCE");
		new MyDemo("SUPPLIER");
	}
	public MyDemo(String type) {
		TYPE=type;
		if (type.equals("SUPPLIER")) {
			for (int i = 1; i <= lastSupplier(); i++) {

				currentsp = i;
				data(i,type);
				updateBalance();
			}
		}
		else {
			for (int i = 1; i <= getAllinsurance(); i++) {

				currentsp = i;
				data(i,type);
				updateBalanceInsu();
			}
		}
		JOptionPane.showMessageDialog(null,
				"Hello Super Admin You Done It Successfully", "INFO",
				JOptionPane.INFORMATION_MESSAGE);

	}

	public void data(int no,String type) {

		id.clear();
		credits.clear();
		debits.clear();
		balance.clear();
		try {
			GLAccountDBConnection db = new GLAccountDBConnection();
			ResultSet rs = db.retrieveMyDEmo(no + "",type);

			while (rs.next()) {
				id.add(rs.getObject(1).toString());
				credits.add(Double.parseDouble(rs.getObject(2).toString()));
				debits.add(Double.parseDouble(rs.getObject(3).toString()));
				balance.add(Double.parseDouble(rs.getObject(4).toString()));

			}
		} catch (SQLException ex) {

			System.out.println(ex);
		}
	}

	public void updateBalance() {
		double begb = 0;
		GLAccountDBConnection accountDBConnection=new GLAccountDBConnection();
		if (id.size() > 0) {
			begb = balance.get(0);
		}
		for (int i = 1; i < id.size(); i++) {

			begb = begb + credits.get(i);

			begb = begb - debits.get(i);

			System.out.println(TYPE+"   "+currentsp + "   " + id.get(i) + "    " + begb);
			try {
				accountDBConnection.updateDataBalance(begb+"", id.get(i), TYPE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		accountDBConnection.closeConnection();
	}
	public void updateBalanceInsu() {
		double begb = 0;
		if (id.size() > 0) {
			begb = balance.get(0);
		}
		for (int i = 1; i < id.size(); i++) {

			begb = begb - credits.get(i);

			begb = begb + debits.get(i);

			System.out.println(currentsp + "   " + id.get(i) + "    " + begb);
		}
	}

	public int lastSupplier() {
		int id = 0;
		SuppliersDBConnection db = new SuppliersDBConnection();
		ResultSet rs = db.lastSupplier();
		try {
			while (rs.next()) {

				id = Integer.parseInt(rs.getObject(1).toString());

			}
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return id;
	}
	
	public int getAllinsurance() {
	
		int i=0;
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				//insuranceModel.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		return i;
	}
}
