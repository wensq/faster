package org.faster.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author sqwen
 */
public class CollectionsTest {

    @Test
    public void testToArray() {
        List<Integer> list = java.util.Arrays.asList(100, 200);
        Integer[] arrays = Collections.toArray(list);
        assertNotNull(arrays);
        assertEquals(2, arrays.length);
        assertEquals(100, arrays[0].intValue());
        assertEquals(200, arrays[1].intValue());
    }

}
