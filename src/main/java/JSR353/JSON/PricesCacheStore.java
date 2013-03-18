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

	// Keep in mind immutability - return a copy of the object
	// does not matter if it is static or volatile
	// or if its threading is safe
	public ConcurrentHashMap<String, Double> getAllPrices() {
		return new ConcurrentHashMap<String, Double>(pricesCache);
	}

	public String getLastSymbolUpdated() {
		return new String(lastSymbolUpdated);
	}
}