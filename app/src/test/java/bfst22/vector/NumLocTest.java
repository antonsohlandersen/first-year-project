package bfst22.vector;

import bfst22.vector.Collections.AddressMap;
import bfst22.vector.Data.NumLocation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumLocTest {

    @Test
    void AddressMapTest(){
        NumLocation numloc1 = new NumLocation("10", 1, 2);
        NumLocation numloc2 = new NumLocation("11", 3, 4);
        NumLocation numloc3 = new NumLocation("12", 5, 6);
        NumLocation numloc4 = new NumLocation("13", 7, 8);
        NumLocation numloc5 = new NumLocation("14", 9, 10);
        AddressMap map = new AddressMap();
        map.add(numloc1);
        map.add(numloc2);
        map.add(numloc3);
        map.add(numloc4);
        map.add(numloc5);
        float[] result = map.get("11");
        assertEquals(result[0], 3);
        assertEquals(result[1], 4);

        result = map.get("10");
        assertEquals(result[0], 1);
        assertEquals(result[1], 2);

        result = map.get("12");
        assertEquals(result[0], 5);
        assertEquals(result[1], 6);

        result = map.get("20");
        assertEquals(result, null);

    }
}
