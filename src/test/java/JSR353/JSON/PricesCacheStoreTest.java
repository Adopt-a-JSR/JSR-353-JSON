package JSR353.JSON;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class PricesCacheStoreTest {

	static final Logger logger = Logger.getLogger(PricesCacheStoreTest.class.getName());

	@Before
	public void setUp() {
		PricesCacheStore.INSTANCE.addPrice("", null);
    	PricesCacheStore.INSTANCE.addPrice("", 0d);
    	PricesCacheStore.INSTANCE.addPrice("a", 0d);
    	PricesCacheStore.INSTANCE.addPrice(null, 0d);
    	PricesCacheStore.INSTANCE.addPrice("3d", 3d);
	}

	@Test
	public void getPrice() {

		logger.log(Level.INFO, "The symbol:  the price: {0}", PricesCacheStore.INSTANCE.getPrice(""));

		logger.log(Level.INFO, "The symbol: null the price: {0}", PricesCacheStore.INSTANCE.getPrice(null));

		logger.log(Level.INFO, "The symbol: a the price: {0}", PricesCacheStore.INSTANCE.getPrice("a"));

		logger.log(Level.INFO, "The symbol: 3d the price: {0}", PricesCacheStore.INSTANCE.getPrice("3d"));
	}

	@Test
	public void getLastSymbolUpdated() {
		PricesCacheStore.INSTANCE.addPrice("", null);
		logger.log(Level.INFO, "The last symbol updated: {0}", PricesCacheStore.INSTANCE.getLastSymbolUpdated());

		PricesCacheStore.INSTANCE.addPrice("", 0d);
		logger.log(Level.INFO, "The last symbol updated: {0}", PricesCacheStore.INSTANCE.getLastSymbolUpdated());

		PricesCacheStore.INSTANCE.addPrice("a", 0d);
    	logger.log(Level.INFO, "The last symbol updated: {0}", PricesCacheStore.INSTANCE.getLastSymbolUpdated());

    	PricesCacheStore.INSTANCE.addPrice(null, 0d);
    	logger.log(Level.INFO, "The last symbol updated: {0}", PricesCacheStore.INSTANCE.getLastSymbolUpdated());

    	PricesCacheStore.INSTANCE.addPrice("3d", 3d);
    	logger.log(Level.INFO, "The last symbol updated: {0}", PricesCacheStore.INSTANCE.getLastSymbolUpdated());

	}

}
