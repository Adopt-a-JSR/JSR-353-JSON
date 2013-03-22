package JSR353.JSON;

import java.util.concurrent.ConcurrentHashMap;

<<<<<<< HEAD
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
=======
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
>>>>>>> 6c725f7e39326df8e821fa77cf2b6da70ad80f99
	}
}