package org.faster.test;

import org.faster.orm.service.GenericService;
import org.junit.Test;

import java.util.Collection;

/**
 * @author sqwen
 */
public abstract class BatchServiceTestCase extends SpringContextTestCase {

    @SuppressWarnings("rawtypes")
    protected abstract Collection<? extends GenericService> getServices();

    @SuppressWarnings("rawtypes")
    @Test
    public void testCountAll() {
        for (GenericService service : getServices()) {
            System.out.printf("%s CountAll: %d\n", service.getClass().getSimpleName(), service.countAll());
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testFirstEntity() {
        for (GenericService service : getServices()) {
            System.out.println(service.getClass().getSimpleName() + " First Entity: " + service.findFirst());
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testFirstPage() {
        for (GenericService service : getServices()) {
            System.out.println(service.getClass().getSimpleName() + " First Page: " + service.findPage(1, 10));
        }
    }

}
