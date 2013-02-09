package JSR353.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;

public class YahooFinanceStub implements Runnable {

	private String[] symbols = new String[] {"GOOG", "MSFT", "YHOO", "IBM"};
	
	boolean stopRunning = false;
	boolean doNotRunWriteJSONObjectToStreamOnlyOnce = true; // enable this flag to execute this implementation!
	boolean doNotRunWriteJSONObjectToConsoleOnlyOnce = true; // enable this flag to execute this implementation!
	// enabling the above will prevent other implementations that write to stream/console multiple times from working! 
	
	@Override
	public void run() {
		// Poll yahoo - generate random price for the moment
		// ToDO: <-code to poll Yahoo->

		// Simulate real-time prices
		while( !stopRunning ) {
			// Post results into cache store
			PricesCacheStore pricesCacheStore = PricesCacheStore.getInstance();
			pricesCacheStore.addPrice(getRandomSymbol(), getRandomPrice());
			
			// get the last symbol updated
			String symbol = pricesCacheStore.getLastSymbolUpdated();
			Double price = pricesCacheStore.getPrice(symbol);
			
			// print it to console				
			// Implementation of JsonGenerator and Json in JSR-353
		    // Note that you can write to a stream only once - hence running only once! 
			writeJSONObjectToConsoleOnlyOnce(symbol, price);
						
			// Implementation of JsonWriter & JsonReader in JSR-353
			/*
			 * Writes/Reads the specified JSON object or array to the output source.
			 * This method needs to be called only once for a reader/writer instance - hence running only once.
			 */
			writeJSONObjectToStreamOnlyOnce(symbol, price);

			// Implementation of JsonObject & JsonObjectBuilder in JSR-353
			// Returns a Json object with a symbol and price - continuously
		    System.out.println(getJSONObjectFromValues(symbol, price));
			
			// Then pause for a bit...before starting all over again
			pauseForABit(1000);
		}
	}
	
	public void pauseForABit(int milliSecsToWait) {
		try {
			Thread.sleep(milliSecsToWait);
			
		} catch (InterruptedException ex) {
			throw new RuntimeException( ex );
		}	
	}
	
	/*
	 * Method to write to console only once.
	 */
	private void writeJSONObjectToConsoleOnlyOnce(String symbol, Double price) {
		if (doNotRunWriteJSONObjectToConsoleOnlyOnce) return;
		doNotRunWriteJSONObjectToConsoleOnlyOnce = true;
		
		pauseForABit(1000);
		
		JsonGenerator generator = Json.createGenerator(System.out);
			generator
				.writeStartObject() 
					.write("symbol", symbol)
					.write("price",  price)
				.writeEnd();			
			
			generator.close();
	}
	
	/*
	 * Method to write to stream only once.
	 */
	private void writeJSONObjectToStreamOnlyOnce(String symbol, Double price) {
		if (doNotRunWriteJSONObjectToStreamOnlyOnce) return;
		doNotRunWriteJSONObjectToStreamOnlyOnce = true;

		pauseForABit(1000);

		writeJsonObj(getJSONObjectFromValues(symbol, price));
		readJsonObj(getFileInputStream());
	}
		
	/*
	 * Function to return a JsonObject
	 */
	private JsonObject getJSONObjectFromValues(String symbol, Double price) {
		JsonObject value = new JsonObjectBuilder()
			.add("symbol", symbol)
			.add("price", price)
			.build();
		return value;
	}
	
	/*
	 * Method to get output stream for a file.
	 */
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
	
	/*
	 * Function to return all prices stored in PricesCachseStore - immutable, singleton 
	 */	
	public ConcurrentHashMap<String, Double> getAllPrices() {
		return PricesCacheStore.getInstance().getAllPrices();
	}
	
	public static void main(String[] args) {
		
		YahooFinanceStub yahooStub = new YahooFinanceStub();
		
		Thread yahooPricesThread = new Thread(yahooStub);
		yahooPricesThread.start();		
	}
 
	/*
	 * Function to return a randomized price.
	 */
	static private Double getRandomPrice() {
		Random rand = new Random();
		return rand.nextDouble() * 100;
	}
	

	private static FileOutputStream outputStream = null;
	private static FileInputStream inputStream = null;
	
	/*
	 * Method to get output stream for a file.
	 */
	public static FileOutputStream getFileOutputStream() {
		try {
			if (outputStream == null) {
				outputStream =  new FileOutputStream(new File("jsr353.txt"));
			} else {
				return outputStream;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputStream;
	}

	/*
	 * Method to get input stream for a file.
	 */
	public static FileInputStream getFileInputStream() {
		try {
			if (inputStream == null) {
				inputStream =  new FileInputStream(new File("jsr353.txt"));
			} else {
				return inputStream;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	/*
	 * Method to write JsonObj to a given output stream.
	 */
	public static void writeJsonObj(JsonObject jsonObject) {
		 JsonWriter jsonWriter = new JsonWriter(getFileOutputStream());
		 System.out.println("JSR 353 - Write Object: ");
		 jsonWriter.writeObject(jsonObject);
		 System.out.println(jsonObject);
		 jsonWriter.close();
	}
	
	/*
	 * Method to write JsonObj from a given output stream.
	 */
	public static void readJsonObj(FileInputStream stream) {
		JsonReader jsonReader = new JsonReader(stream);
		System.out.println("JSR 353 - Read Object: ");
		JsonObject jsonObject = jsonReader.readObject();
		System.out.println(jsonObject);
	}
}