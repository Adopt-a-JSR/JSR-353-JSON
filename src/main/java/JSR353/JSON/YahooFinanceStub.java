/*
  The GNU General Public License (GPL)

  Version 2, June 1991

  Copyright (C) 1989, 1991 Free Software Foundation, Inc.
  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  Everyone is permitted to copy and distribute verbatim copies of this license
  document, but changing it is not allowed.

  Copyright (c) 2013, Mani Sarkar <sadhak001@gmail.com> All rights reserved.

  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

  This code is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License version 2 only, as
  published by the Free Software Foundation.  Oracle designates this
  particular file as subject to the "Classpath" exception as provided
  by Oracle in the LICENSE file that accompanied this code.

  This code is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
  version 2 for more details (a copy is included in the LICENSE file that
  accompanied this code).

  You should have received a copy of the GNU General Public License version
  2 along with this work; if not, write to the Free Software Foundation,
  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package JSR353.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;

public class YahooFinanceStub implements Runnable {

    static final Logger logger = Logger.getLogger(YahooFinanceStub.class.getName());

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
            PricesCacheStore.INSTANCE.addPrice(getRandomSymbol(), getRandomPrice());

            // get the last symbol updated
            String symbol = PricesCacheStore.INSTANCE.getLastSymbolUpdated();
            Double price = PricesCacheStore.INSTANCE.getPrice(symbol);

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
            logger.log(Level.INFO, "JsonObject: {0}", getJSONObjectFromValues(symbol, price));

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
        return PricesCacheStore.INSTANCE.getAllPrices();
    }

    public static void main(String[] args) {

        // Java 7 SimpleFormatter.
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tH:%1$tM:%1$tS %5$s%6$s%n");

        YahooFinanceStub yahooStub = new YahooFinanceStub();

        Thread yahooPricesThread = new Thread(yahooStub);
        yahooPricesThread.start();
    }

    /*
     * Function to return a randomized price.
     */
    static private Double getRandomPrice() {
        return new Random().nextDouble() * 100;
    }


    private static FileOutputStream outputStream = null;
    private static FileInputStream inputStream = null;

    /*
     * Method to get output stream for a file.
     */
    public static FileOutputStream getFileOutputStream() {
        if (outputStream == null) {
            try {
                outputStream = new FileOutputStream(new File("jsr353.txt"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(YahooFinanceStub.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return outputStream;
        }
        return outputStream;
    }

    /*
     * Method to get input stream for a file.
     */
    public static FileInputStream getFileInputStream() {

        if (inputStream == null) {
            try {
                inputStream = new FileInputStream(new File("jsr353.txt"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(YahooFinanceStub.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return inputStream;
        }

        return inputStream;
    }

    /*
     * Method to write JsonObj to a given output stream.
     */
    public static void writeJsonObj(JsonObject jsonObject) {
         JsonWriter jsonWriter = new JsonWriter(getFileOutputStream());
         logger.log(Level.INFO, "JSR 353 - Write Object: ");

         jsonWriter.writeObject(jsonObject);

         logger.log(Level.INFO, "The Object : {0}", jsonObject);

         jsonWriter.close();
    }

    /*
     * Method to write JsonObj from a given output stream.
     */
    public static void readJsonObj(FileInputStream stream) {
        JsonReader jsonReader = new JsonReader(stream);

        logger.log(Level.INFO, "JSR 353 - Read Object: ");

        JsonObject jsonObject = jsonReader.readObject();

        logger.log(Level.INFO, "The Object : {0}", jsonObject);
        jsonReader.close();
    }
}

