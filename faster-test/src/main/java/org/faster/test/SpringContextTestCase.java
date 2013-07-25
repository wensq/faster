package org.faster.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.util.Assert;

/**
 * @author sqwen
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public abstract class SpringContextTestCase extends Assert implements ApplicationContextAware {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ApplicationContext applicationContext;

    @Override
    public final void setApplicationContext(
            final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
