public class DayPrice {
    public static void main(String[] args) {
        int count = 1;
        int buy = 11100;
        int sell = 11150;
        double discount = 1;

        double tax = 0.0015;
        double fee = 0.001425;

        double actualBuyFee = buy * count * fee;
        double actualSellFee = sell* count * fee;
        double actualTax = sell * count * tax;

        System.out.println("actualBuyFee = " + actualBuyFee);
        actualBuyFee = (actualBuyFee <=20) ? 20 : actualBuyFee * discount;
        System.out.println("actualBuyFee = " + actualBuyFee);

        System.out.println("actualSellFee = " + actualSellFee);
        actualSellFee = (actualSellFee <=20) ? 20 : actualSellFee * discount;
        System.out.println("actualSellFee = " + actualSellFee);

        System.out.println("actualTax = " + actualTax);
        double cost = actualTax + actualBuyFee + actualSellFee;
        System.out.println("cost = " + cost);
        double profitLoss = sell * count - (buy * count + cost);
        System.out.println("profitLoss = " + profitLoss);
    }
}
