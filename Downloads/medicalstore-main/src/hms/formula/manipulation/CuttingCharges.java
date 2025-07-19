package hms.formula.manipulation;

import java.sql.ResultSet;
import java.sql.SQLException;

import hms.store.database.CancelRestockFeeDB;

public class CuttingCharges {
    private double cuttingPercentage1;
    private double cuttingPercentage2;
    private double cuttingPercentage3;
    private double cuttingPercentage;

    // Method to retrieve cutting charges from database
    public void getCuttingCharges(String type) {
        CancelRestockFeeDB db = new CancelRestockFeeDB();
        ResultSet rs = db.retrieveDataNew(type);
        try {
            if (rs.next()) cuttingPercentage3 = Double.parseDouble(rs.getString(2));
            if (rs.next()) cuttingPercentage2 = Double.parseDouble(rs.getString(2));
            if (rs.next()) cuttingPercentage1 = Double.parseDouble(rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    public void calculateCuttingCharge(double qtyIssued, int packSize) {
        cuttingPercentage=0; 
        if (packSize > 3) {
            int pack = packSize / 3;
            int remainder = (int) (qtyIssued % packSize);
            if (remainder > 0 && remainder <= pack) {
                cuttingPercentage = cuttingPercentage1;
            } else if (remainder > pack && remainder <= pack * 2) {
                cuttingPercentage = cuttingPercentage2;
            } else if (remainder > pack * 2) {
                cuttingPercentage = cuttingPercentage3;
            }
        }
    }

    public double getCuttingPercentage() {
        return cuttingPercentage;
    }
    
    public double addCuttingPercentage(double amt,double per) {
        return  (amt + (amt * (per / 100)));
    }
}
