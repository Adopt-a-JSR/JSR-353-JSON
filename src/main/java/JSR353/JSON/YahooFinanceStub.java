package JSR353.JSON;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.*;
import javax.json.stream.*;

public class YahooFinanceStub implements Runnable {

	private String[] symbols = new String[] {"GOOG", "MSFT", "YHOO", "IBM"};
	
	@Override
	public void run() {
		boolean stopRunning = false;
		// Poll yahoo - generate random price for the moment
		// <-code to poll Yahoo->

		// Simulate real-time prices
		while( !stopRunning ) {
			// Post results into cache store
			PricesCacheStore pricesCacheStore = PricesCacheStore.getInstance();
			pricesCacheStore.addPrice(getRandomSymbol(), getRandomPrice());
			
			// get the last symbol updated
			String symbol = pricesCacheStore.getLastSymbolUpdated();
			Double price = pricesCacheStore.getPrice(symbol);
			
			// print it to console			
		    //writeJSONObjectToConsoleOnlyOnce(symbol, price);
		    System.out.println(getJSONObjectFromValues(symbol, price));
			
			// Then pause for a bit...before starting all over again
			try {
				Thread.sleep(1000);
				
				// if user types a key in the console, then stop running!
				//if ( readUserInput() != "" ) stopRunning = true; 
			} catch (InterruptedException ex) {
				throw new RuntimeException( ex );
			}
		}
	}
	
	// Implementation of JsonGenerator and Json in JSR-353
	private void writeJSONObjectToConsoleOnlyOnce(String symbol, Double price) {
		JsonGenerator generator = Json.createGenerator(System.out);
		if (generator != null) {	
			generator
				.writeStartObject() 
					.write("symbol", symbol)
					.write("price",  price)
				.writeEnd();			
			
			generator.close();
		}
	}
	
	// Implementation of JsonGenerator and Json in JSR-353
	private JsonObject getJSONObjectFromValues(String symbol, Double price) {
		JsonObject value = new JsonObjectBuilder()
			.add("symbol", symbol)
			.add("price", price)
			.build();
		return value;
	}
	
	// Implementation of JsonObject & Json.createObjectBuilder in JSR-353
	private JsonObject getJSONObject(String symbol, Double price) {
	     /*	JsonObject value = Json.createObjectBuilder()
			//.add("quotes") 
				.add("symbol", symbol)
				.add("price", price)
			.build();
			*/
		JsonObject value = null;
		return value;
	}
		
	
	public String getRandomSymbol() {
		String symbol = "";
		if ((symbols != null) && (symbols.length > 0)) {
			Random randSeed = new Random();
			int symbolsUpperLimit= symbols.length;
			int randomIndex = Math.abs(randSeed.nextInt()) % symbolsUpperLimit;
			symbol = symbols[randomIndex];
		}
		return symbol;
	}
	public ConcurrentHashMap<String, Double> getAllPrices() {
		return PricesCacheStore.getInstance().getAllPrices();
	}
	
	public static void main(String[] args) {
		
		YahooFinanceStub yahooStub = new YahooFinanceStub();
		
		Thread yahooPricesThread = new Thread(yahooStub);
		yahooPricesThread.start();		
	}
 
	static private Double getRandomPrice() {
		Random rand = new Random();
		return rand.nextDouble() * 100;
	}
}