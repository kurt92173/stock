public enum EStockId {
    STK3056("3056","14"),
//    STK2367("2367","28"),
//    STK6191("6191","28"),
//    STK6168("6168","26"),
    ; // semicolon needed when fields / methods follow

    private final String id;
    private final String type;

    EStockId(String id, String type) {

        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public static String[] getAllId() {
        String[] stocks = new String[values().length];
        int index = 0;
        for (EStockId stockId : values()) {
            stocks[index] = stockId.getId();
            index++;
        }
        return stocks;
    }
}
