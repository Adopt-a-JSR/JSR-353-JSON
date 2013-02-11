package JSR353.JSON;

import java.util.concurrent.ConcurrentHashMap;

public class PricesCacheStore {

	private final static PricesCacheStore INSTANCE = new PricesCacheStore();	
	
	private PricesCacheStore() {}
	private volatile String lastSymboleUpdated;
	
	// will contain Symbols and prices stored and constantly updated	
	private volatile ConcurrentHashMap<String, Double> pricesCache = new ConcurrentHashMap<>(); 
	
	public static PricesCacheStore getInstance() {
		return INSTANCE; 
	}
	
	public void addPrice(String symbol, Double price) {
		pricesCache.put(symbol, price);
		lastSymboleUpdated = symbol; 
	}
	
	public Double getPrice(String symbol) {
		Double price = pricesCache.get(symbol);
		return price == null ? 0 : price;
	}
	
	// Keep in mind immutability - return a copy of the object
	// does not matter if it is static or volatile
	// or if its threading is safe
	public ConcurrentHashMap<String, Double> getAllPrices() {		
		return new ConcurrentHashMap<String, Double>(pricesCache);
	}
	
	public String getLastSymbolUpdated() {
		return new String(lastSymboleUpdated);
	}
}