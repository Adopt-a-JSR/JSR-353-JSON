package JSR353.JSON;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PricesCacheStoreTest {

    private Double emptyDouble;

    @Before
    public void setUp() {
        emptyDouble = new Double(0d);
        PricesCacheStore.INSTANCE.addPrice("", null);
        PricesCacheStore.INSTANCE.addPrice("", 0d);
        PricesCacheStore.INSTANCE.addPrice("a", 0d);
        PricesCacheStore.INSTANCE.addPrice(null, 0d);
        PricesCacheStore.INSTANCE.addPrice("3d", 3d);
    }

    @Test
    public void getPrice() {
        Assert.assertEquals(emptyDouble, PricesCacheStore.INSTANCE.getPrice(""));
        Assert.assertEquals(emptyDouble, PricesCacheStore.INSTANCE.getPrice(null));
        Assert.assertEquals(emptyDouble, PricesCacheStore.INSTANCE.getPrice("a"));
        Assert.assertEquals(new Double(3d), PricesCacheStore.INSTANCE.getPrice("3d"));
    }

}
