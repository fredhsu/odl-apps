package com.example.getpackets;

import org.opendaylight.controller.sal.packet.*;
import org.opendaylight.controller.sal.utils.NetUtils;
import org.opendaylight.controller.sal.utils.IPProtocols;
import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple bundle to grab some statistics
 * Fred Hsu
 * install file:///home/fhsu/code/odl-apps/getpackets/target/getpackets-1.4.0-SNAPSHOT.jar
 */

public class GetPackets implements IListenDataPacket {
    private static final Logger log = LoggerFactory
            .getLogger(GetPackets.class);
    private IDataPacketService dataPacketService = null;


    public GetPackets() {

    }

    void init() {
        log.debug("INIT called!");
        System.out.println("init for package");
    }

    void destroy() {
        log.debug("DESTROY called!");
    }

    void start() {
        log.debug("START called!");
    }

    void stop() {
        log.debug("STOP called!");
    }



    @Override
    public PacketResult receiveDataPacket(RawPacket inPkt) {
        if (inPkt == null) {
            return PacketResult.IGNORED;
        }
        log.trace("Received a frame of size: {}",
                        inPkt.getPacketData().length);
        Packet formattedPak = this.dataPacketService.decodeDataPacket(inPkt);
        System.out.println("packet");
        System.out.println(formattedPak);
        if (formattedPak instanceof Ethernet) {
            System.out.println(formattedPak);
            Object nextPak = formattedPak.getPayload();
            if (nextPak instanceof IPv4) {
                IPv4 ipPak = (IPv4)nextPak;
                System.out.println("IP");
                log.trace("Handled IP packet");
                int sipAddr = ipPak.getSourceAddress();
                InetAddress sip = NetUtils.getInetAddress(sipAddr);
                int dipAddr = ipPak.getDestinationAddress();
                InetAddress dip = NetUtils.getInetAddress(dipAddr);
                System.out.println("SRC IP:");
                System.out.println(sip);
                System.out.println("DST IP:");
                System.out.println(dip);
                Object frame = ipPak.getPayload();
                if (frame instanceof ICMP) {
                    System.out.println("ICMP from instance");
                }
                String protocol = IPProtocols.getProtocolName(ipPak.getProtocol());
                if (protocol == IPProtocols.ICMP.toString()) {
                    ICMP icmpPak = (ICMP)ipPak.getPayload();
                    System.out.println("ICMP from checking protocol");
                }
            }
            if (nextPak instanceof ARP) {
                //This is handled by the ARP Manager
            }
        }
        return PacketResult.IGNORED;
    }

    // Need these two methods to hook into the data path service
    // They are called from the activator

    void setDataPacketService(IDataPacketService s) {
        this.dataPacketService = s;
    }

    void unsetDataPacketService(IDataPacketService s) {
        if (this.dataPacketService == s) {
            this.dataPacketService = null;
        }
    }
}
