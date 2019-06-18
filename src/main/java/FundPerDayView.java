public class FundPerDayView {
    private String id;
    private String name;
    private int buyUnit;
    private int sellUnit;
    private int diffUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(int buyUnit) {
        this.buyUnit = buyUnit;
    }

    public int getSellUnit() {
        return sellUnit;
    }

    public void setSellUnit(int sellUnit) {
        this.sellUnit = sellUnit;
    }

    public int getDiffUnit() {
        return diffUnit;
    }

    public void setDiffUnit(int diffUnit) {
        this.diffUnit = diffUnit;
    }
}
