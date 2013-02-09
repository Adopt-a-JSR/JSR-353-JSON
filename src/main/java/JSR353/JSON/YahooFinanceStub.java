package JSR353.JSON;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		    // Uncomment the below to test this method - but yo 
			writeJSONObjectToConsoleOnlyOnce(symbol, price);
		    
		    //System.out.println(getJSONObjectFromValues(symbol, price));
			
			// Then pause for a bit...before starting all over again
			try {
				Thread.sleep(1000);
				
			} catch (InterruptedException ex) {
				throw new RuntimeException( ex );
			}
		}
	}
	
	// Implementation of JsonGenerator and Json in JSR-353
	private void writeJSONObjectToConsoleOnlyOnce(String symbol, Double price) {
		JsonGenerator generator = Json.createGenerator(System.out);
			generator
				.writeStartObject() 
					.write("symbol", symbol)
					.write("price",  price)
				.writeEnd();			
			
			generator.close();
	}
	
	// Implementation of JsonObject & JsonObjectBuilder in JSR-353
	private JsonObject getJSONObjectFromValues(String symbol, Double price) {
		JsonObject value = new JsonObjectBuilder()
			.add("symbol", symbol)
			.add("price", price)
			.build();
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