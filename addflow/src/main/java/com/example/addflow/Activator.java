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
 * To change this template use File | Settings | File Templates.
 */
public class Activator extends ComponentActivatorAbstractBase {

    @Override
    protected void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
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
