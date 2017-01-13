package org.javaee8.jcache.configuration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class KeyValueServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(KeyValueService.class, KeyValueEntryRemovedListener.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private KeyValueService<String, String> service;

    @Test
    public void test() throws Exception {
        assertNull(service.get("JSR107"));
        assertNull(service.getRemoved("JSR107"));

        service.put("JSR107", "JCACHE");
        assertEquals("JCACHE", service.get("JSR107"));
        assertNull(service.getRemoved("JSR107"));

        service.remove("JSR107");
        assertNull(service.get("JSR107"));
        // should be available because of listener
        assertEquals("JCACHE", service.getRemoved("JSR107"));
    }
}
