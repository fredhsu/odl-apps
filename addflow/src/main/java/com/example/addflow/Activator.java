package com.example.addflow;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: fhsu
 * Date: 5/24/13
 * Time: 9:13 AM
 */
public class Activator extends ComponentActivatorAbstractBase {

    @Override
    protected void init() {
    }

    @Override
    protected void destroy() {
    }
    public Object[] getImplementations() {
        Object[] res = { AddFlow.class };
        return res;
    }
    public void configureInstance(Component c, Object imp, String containerName) {
        if (imp.equals(AddFlow.class)) {
            // export the service
            c.setInterface(new String[] { AddFlow.class.getName() },
                    null);

        }
    }

}
