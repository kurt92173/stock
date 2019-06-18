import java.math.BigDecimal;

public class StockPriceView {
    private String id;       //代號
    private String date;     //日期
    private int volume;  //成交股數
    private double amount;  //成交金額
    private double open;    //開盤價
    private double high;     //最高價
    private double low;     //最低價
    private double price;   //收盤價
    private double diff;    //漲跌價差
    private double percentage; //漲跌價差
    private int count;   //成交筆數

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPercentage() {
        if (diff == 0) return  0;

        double f = diff / (price - diff) * 100;
        BigDecimal b = new BigDecimal(f);

        return b.setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
//        return Math.floor((diff / (price - diff)))*100;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String toString() {
        return "id="+id+", date="+date+", volume="+volume+", amount="+amount+", open="+open+", high="+high+", low="+low+", price="+price+", diff="+diff+", percentage="+percentage+", count="+count;
    }
}