package hms.formula.manipulation;

public class OnlineDiscount {
	private double onlineDiscount=0.01;// one percentage discount for online
	public double getOnlineDiscount(double amt) {
        return (amt - (amt *onlineDiscount));
		// TODO Auto-generated constructor stub
	}

}
