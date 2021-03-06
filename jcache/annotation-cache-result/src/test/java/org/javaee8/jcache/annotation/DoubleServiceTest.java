package org.javaee8.jcache.annotation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
public class DoubleServiceTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DoubleService.class)
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("jboss-deployment-structure.xml");
    }

    @Inject
    private DoubleService service;

    @Test
    public void test() throws Exception {
        assertEquals("Dude_1", service.getName("Dude"));
        assertEquals("Dude_data_1", service.getData("Dude"));
        assertEquals("Dude_1", service.getName("Dude"));
        assertEquals("Dude_data_1", service.getData("Dude"));

        assertEquals("Joe_2", service.getName("Joe"));
        assertEquals("Joe_data_2", service.getData("Joe"));
        assertEquals("Joe_2", service.getName("Joe"));
        assertEquals("Joe_data_2", service.getData("Joe"));
    }
}
