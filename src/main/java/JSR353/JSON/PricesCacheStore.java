package JSR353.JSON;

import java.util.concurrent.ConcurrentHashMap;

public enum PricesCacheStore {

    INSTANCE;

    private volatile String lastSymbolUpdated;

    // will contain Symbols and prices stored and constantly updated
    private ConcurrentHashMap<String, Double> pricesCache = new ConcurrentHashMap<>();

    public void addPrice(String symbol, Double price) {
        if (symbol != null && price != null) {
            pricesCache.put(symbol, price);
            lastSymbolUpdated = symbol;
        }
    }

    public Double getPrice(String symbol) {
        if (symbol == null) {
            return 0d;
        }
        Double price = pricesCache.get(symbol);
        return price == null ? 0d : price;
    }

    // Object is threading is safe so can be returned as it is
    public ConcurrentHashMap<String, Double> getAllPrices() {
        return pricesCache;
    }

    public String getLastSymbolUpdated() {
        return new String(lastSymbolUpdated);
    }
}
