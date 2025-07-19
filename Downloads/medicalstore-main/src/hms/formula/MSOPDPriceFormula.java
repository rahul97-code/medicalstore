package hms.formula;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import hms.main.GeneralDBConnection;
import hms.store.database.ItemsDBConnection;

public class MSOPDPriceFormula {

	private double mrp;
	private double purchase;
	private double price;
	private double taxValue;
	private double surchargeValue;
	private double mrptotal;
	private int packSize = 1;
	private int quantity;
	private double taxAmountValue=0;
	private double surchargeAmountValue=0;
	private String formulaActive;

	public void processItemBatch(String index,String formulaActive,String itemID) {
		ItemsDBConnection DB;
		DB = new ItemsDBConnection();
		ResultSet resultSet = DB.itemDetailBatch2(index);

		try {
			while (resultSet.next()) {
				initializeValues(resultSet);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DB.closeConnection();
		price=roundPrice(calculateSellingPriceOPD());

	}

	private void initializeValues(ResultSet resultSet) throws SQLException {
		taxValue = Double.parseDouble(resultSet.getObject(4).toString());
		purchase = Double.parseDouble(resultSet.getObject(1).toString());
		quantity = Integer.parseInt(resultSet.getObject(8).toString());
		surchargeValue = Double.parseDouble(resultSet.getObject(5).toString());

		try {
			packSize = Integer.parseInt(resultSet.getObject(3).toString().trim());
		} catch (Exception e) {
			packSize = 1;
		}
		mrp = Double.parseDouble(resultSet.getObject(2).toString());
		mrptotal = mrp;
	}



	private double calculateSellingPriceOPD() {
		double mrpWithoutTax = calculateMrpWithoutTax();
		System.out.println("mrpWithoutTax: "+mrpWithoutTax);
		double margin = (mrpWithoutTax - purchase) / purchase * 100;
		System.out.println("margin: "+margin);
		if (purchase >= 10000 && purchase <= 20000) {
			return roundPrice(purchase * 1.15);
		} else if (purchase > 20000 && purchase <= 30000) {
			return roundPrice(purchase * 1.10);
		} else if (purchase >= 30000) {
			return roundPrice(purchase * 1.05);
		} else {
			return getDaySellingPrice(margin, mrpWithoutTax);

		}
	}


	private double calculateMrpWithoutTax() {
		double tempVar1 = mrp / packSize;
		double tempVar2 = tempVar1 * ((taxValue + surchargeValue) / 100);
		return tempVar1 - tempVar2;
	}

	private double getDaySellingPrice(double margin, double mrpWithoutTax) {
		double value;
		if (margin > 70) {
			value = 1.5;
		} else if (margin > 60) {
			value = 1.4;
		} else if (margin > 50) {
			value = 1.3;
		} else if (margin > 40) {
			value = 1.2;
		} else if (margin > 30) {
			value = 1.15;
		} else {
			value = 1.15;  
		}

		double sp = roundPrice(purchase * value);
		double mrpLess1Percent = mrpWithoutTax - (mrpWithoutTax * 0.01);
		return sp < mrpLess1Percent ? sp : mrpLess1Percent;
	}

	private double roundPrice(double price) {
		return Math.round(price * 100.0) / 100.0;
	}
	public double addGst(double amt,double cgst,double sgst) {
		double c = (amt*cgst) / 100.00f;
		double s = (amt*sgst) / 100.00f;
		taxAmountValue = c;
		surchargeAmountValue = s;
		return (amt+c+s);
	}

	public double addGstTemp(double amt,double cgst,double sgst) {
		double c = (amt*cgst) / 100.00f;
		double s = (amt*sgst) / 100.00f;
		return (amt+c+s);
	}
	public double getMrp() {
		return mrp;
	}

	public double getPurchase() {
		return purchase;
	}

	public double getPrice() {
		return price;
	}

	public double getCgstValue() {
		return taxAmountValue;
	}

	public double getSgstValue() {
		return surchargeAmountValue;
	}

	public double getTaxValue() {
		return taxValue;
	}

	public double getSurchargeValue() {
		return surchargeValue;
	}

	public double getMrpTotal() {
		return mrptotal;
	}

	public int getPackSize() {
		return packSize;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getFormulaActive() {
		return formulaActive;
	}
}
