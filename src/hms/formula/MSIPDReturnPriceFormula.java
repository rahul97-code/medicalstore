package hms.formula;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableModel;

import hms.formula.manipulation.ReturnCharges;
import hms.main.GeneralDBConnection;
import hms.store.database.CancelRestockFeeDB;
import hms.store.database.ItemsDBConnection;

public class MSIPDReturnPriceFormula {

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
	private double serviceChargesPer;
	private double cuttingFee;

	public static void main(String[] args) {
		new MSIPDReturnPriceFormula().processSellingPrice(50,"2025-06-10");
	}

	public double processSellingPrice(double unitPrice,String issuedDate) {

		ReturnCharges returnCharges=new ReturnCharges();
	    serviceChargesPer=returnCharges.getReturnCharges(issuedDate, "IPD");
		double sp = unitPrice-(((unitPrice*(100+serviceChargesPer))/100)-unitPrice);
		price=roundPrice(unitPrice-sp);
		System.out.println("serviceChargesPer:"+serviceChargesPer);
		System.out.println("price:"+price);
		return price;

	}

	private void calculateSellingPriceCashIPD() {
		double sp=0;
		if (formulaActive.equals("1")) {
			sp = purchase;
		} else {
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
		}
		price = roundPrice(sp);
	}
	public void calculateSellingPricePanelIPD() {
		double sp=((mrptotal*100)/(100+(taxValue+surchargeValue)))/packSize;
		price= roundPrice(sp);
	}

	public void calculateSellingPriceImplant() {
		price = purchase; 	
	}
	
	public double addGst(double amt,double cgst,double sgst) {
		double c = ((amt*(100+cgst))/100)-amt;
		double s = ((amt*(100+sgst))/100)-amt;
		taxAmountValue = c;
		surchargeAmountValue = s;
		return (amt+c+s);
	}
	private double calculateMrpWithoutTax() {
		double tempVar1 = mrp / packSize;
		double tempVar2 = tempVar1 * ((taxValue + surchargeValue) / 100);
		return tempVar1 - tempVar2;
	}

	private double roundPrice(double price) {
		return Math.round(price * 100.0) / 100.0;
	}

	public double getMrpCgstValue(double mrp,double cgst) {
		return roundPrice(mrp-((mrp*100)/(100+(cgst))));
	}
	public double getMrpSgstValue(double mrp,double sgst) {
		return roundPrice(mrp-((mrp*100)/(100+(sgst))));
	}
	public double getMrpPrice() {
		return roundPrice(((mrptotal*100)/(100+(taxValue+surchargeValue)))/packSize);
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
