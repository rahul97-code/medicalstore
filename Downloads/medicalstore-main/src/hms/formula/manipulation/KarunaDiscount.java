package hms.formula.manipulation;

public class KarunaDiscount {

    public double getKarunaRelief(double amt,double per) {
        return (amt - (amt * (per / 100)));
    }
    public double getKarunaDiscountAmt(double amt,double per) {
        return  (amt * (per / 100));
    }
}
