package com.example.getpackets;

import java.util.Hashtable;
import java.util.Dictionary;
import org.apache.felix.dm.Component;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends ComponentActivatorAbstractBase {
    protected static final Logger logger = LoggerFactory
            .getLogger(Activator.class);

    public void init() {
    }

    public void destroy() {
    }

    public Object[] getImplementations() {
        Object[] res = { GetPackets.class };
        return res;
    }

    public void configureInstance(Component c, Object imp, String containerName) {
        if (imp.equals(GetPackets.class)) {
            // export the service
            Dictionary<String, String> props = new Hashtable<String, String>();
            props.put("salListenerName", "getpackets");
            c.setInterface(new String[] { GetPackets.class.getName(),
                IListenDataPacket.class.getName() }, props);
            c.add(createContainerServiceDependency(containerName).setService(
                    IDataPacketService.class).setCallbacks(
                    "setDataPacketService", "unsetDataPacketService")
                    .setRequired(true));
            c.add(createContainerServiceDependency(containerName).setService(
                    ISwitchManager.class).setCallbacks("setSwitchManager",
                    "unsetSwitchManager").setRequired(true));
        }
    }
}
