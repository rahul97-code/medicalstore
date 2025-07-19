package hms.formula;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import hms.main.GeneralDBConnection;
import hms.main.HMSDBConnection;
import hms.store.database.ItemsDBConnection;

public class MSIPDPriceFormula {

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
	public boolean isUserDefined;


	public void processItemBatch(String index,String formulaActive,String ins,String item_cat,String itemID) {
		ItemsDBConnection DB;
		this.formulaActive=formulaActive;
		DB= new ItemsDBConnection();
		String spType=DB.retrieveFormula(ins, item_cat); 
		DB.closeConnection();
		spType="0".equals(formulaActive)?spType:"NA";
		System.out.println("SP: TYEP :"+spType);
		DB= new ItemsDBConnection();
		ResultSet resultSet = DB.itemDetailBatch2(index);
		//enum('HMS','MSIPD','MSOPD','PP','MRP','USRDFND')
		try {
			while (resultSet.next()) {
				initializeValues(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DB.closeConnection();
		DB= new ItemsDBConnection();

		switch(spType) {
		case "NA":
			price= roundPrice(calculateFixedPrice(DB.retrieveSellingPrice(itemID)));
			break;
		case "HMS":
			calculateSellingPriceHMS();
			break;
		case "MSOPD":
			calculateSellingPriceMSOPD();
			break; 
		case "MRP":
			calculateSellingPriceMRPIPD();
			break;	
		case "PP":
			calculateSellingPriceImplant();
			break;
		case "MSIPD":
			calculateSellingPriceMSIPD();
			break;	
		case "USRDFND":
			double price=0;
			String input = JOptionPane.showInputDialog("Enter the U price:");
			if (input!=null && !input.isEmpty()) 
				price = Double.parseDouble(input);
			calculateSPUserDefined(price);
			break;
		default:
			calculateSellingPriceMSIPD();
		}
		DB.closeConnection();

	}
	public double calculateFixedPrice(double sp) {
		sp=sp>=mrp?((mrp*100)/(100+(taxValue+surchargeValue)))/packSize:sp;
		return sp;
	}
	private void calculateSellingPriceMSOPD() {
		double sp=0.0;
		double mrpWithoutTax = calculateMrpWithoutTax();
		System.out.println("mrpWithoutTax: "+mrpWithoutTax);
		double margin = (mrpWithoutTax - purchase) / purchase * 100;
		System.out.println("margin: "+margin);
		if (purchase >= 10000 && purchase <= 20000) {
			sp= roundPrice(purchase * 1.15);
		} else if (purchase > 20000 && purchase <= 30000) {
			sp= roundPrice(purchase * 1.10);
		} else if (purchase >= 30000) {
			sp= roundPrice(purchase * 1.05);
		} else {
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

			sp = roundPrice(purchase * value);
			double mrpLess1Percent = mrpWithoutTax - (mrpWithoutTax * 0.01);
			sp= sp < mrpLess1Percent ? sp : mrpLess1Percent;
		}
		price = roundPrice(sp);
	}
	private void calculateSellingPriceHMS() {
		double  tot = 0, tot1 = 0, sp = 0;
		HMSDBConnection DB=new HMSDBConnection();
		double value = Double.parseDouble(DB.retrieveFormula1());
		DB.closeConnection();
		tot = (double) Math.round(purchase * value * 100) / 100;
		tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
		double tax = taxValue + surchargeValue;

		if (purchase > 10000 && purchase <= 20000) {
			System.out.println("10000" + purchase);

			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			System.out.println("tempvar2" + tempvar2);
			double mrpwithouttax = tempvar1 - tempvar2;
			System.out.println("mrpwithouttax" + mrpwithouttax);
			double temp = 1.15 * purchase;
			System.out.println("temp" + temp);
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
			// sp = purchase * 1.15;
		} else if (purchase > 20000 && purchase <= 30000) {
			System.out.println("20000" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 1.10 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
			// sp = purchase * 1.10;
		} else if (purchase > 30000) {
			System.out.println("30000" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 1.05 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
			// sp = purchase * 1.05;
		} else if (purchase > 5000 && purchase <= 10000) {
			System.out.println("5000" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 1.25 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
		} else if (purchase > 1000 && purchase <= 5000) {
			System.out.println("1000:" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 1.30 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
		} else if (purchase > 250 && purchase <= 1000) {
			System.out.println("250:" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 1.5 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
		} else if (purchase > 0 && purchase <= 250) {
			System.out.println("0:" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 2.5 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
		} else {
			System.out.println("mmmmmyyyyy" + purchase);
			double tempvar1 = mrp / packSize;
			double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
			double mrpwithouttax = tempvar1 - tempvar2;
			double temp = 2.5 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;

			} else {
				double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
		}
		price = roundPrice(sp);
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

	private void calculateSellingPriceMSIPD() {
		double sp=0;

		if (purchase >= 10000 && purchase <= 20000) {
			sp = purchase * 1.15;
		} else if (purchase > 20000 && purchase <= 30000) {
			sp = purchase * 1.10;
		} else if(purchase>=30000){
			sp = purchase * 1.05;
		}else {
			double tempvar1 = mrp / packSize;
			double mrpwithouttax = (tempvar1 *100)/(100 + taxValue + surchargeValue);
			double temp = 2.5 * purchase;
			if (mrpwithouttax > temp) {
				sp = temp;
			} else {
				double mrpless1prcnt = mrpwithouttax- (mrpwithouttax * 0.01);
				sp = mrpless1prcnt;
			}
		}

		price = roundPrice(sp);
	}
	public void calculateSellingPriceMRPIPD() {
		double sp=((mrptotal*100)/(100+(taxValue+surchargeValue)))/packSize;
		price= roundPrice(sp);
	}
	public void calculateSPUserDefined(double sp) {
		double pp=addGst(purchase,taxValue,surchargeValue);
		if(sp<pp) {
			JOptionPane.showMessageDialog(null, "Amount must be greater than $" + pp);
			return;
		}
		sp=sp>=mrp?mrp:sp;
		sp=((sp*100)/(100+(taxValue+surchargeValue)))/packSize;
		price= roundPrice(sp);
	}
	public void calculateSellingPriceImplant() {
		price = purchase; 	
	}
	private double calculateMrpWithoutTax() {
		double tempVar1 = mrp / packSize;
		double tempVar2 = tempVar1 * ((taxValue + surchargeValue) / 100);
		return tempVar1 - tempVar2;
	}

	private double roundPrice(double price) {
		return Math.round(price * 100.0) / 100.0;
	}
	public double addGst(double amt,double cgst,double sgst) {
		double c = ((amt*(100+cgst))/100)-amt;
		double s = ((amt*(100+sgst))/100)-amt;
		taxAmountValue = c;
		surchargeAmountValue = s;
		return (amt+c+s);
	}
	public double getMrpCgstValue(double mrp,double cgst) {
		return roundPrice(mrp-((mrp*100)/(100+(cgst))));
	}
	public double getMrpSgstValue(double mrp,double sgst) {
		return roundPrice(mrp-((mrp*100)/(100+(sgst))));
	}
	//	public double getMrpPrice() {
	//		return roundPrice(((mrptotal*100)/(100+(taxValue+surchargeValue)))/packSize);
	//	}
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
		return roundPrice(taxAmountValue);
	}

	public double getSgstValue() {
		return roundPrice(surchargeAmountValue);
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
