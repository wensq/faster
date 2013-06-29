package org.faster.cache;

import java.util.concurrent.Callable;

/**
 * @author sqwen
 */
public class SearchItem implements Callable {

    private final CacheMissHandler handler;

    public SearchItem(CacheMissHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object call() throws Exception {
        return handler.doFind();
    }

}
