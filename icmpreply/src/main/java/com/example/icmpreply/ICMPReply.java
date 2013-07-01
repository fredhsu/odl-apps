package com.example.icmpreply;

import java.net.InetAddress;
import java.util.Arrays;

import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.ICMP;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.IPv4;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.utils.EtherTypes;
import org.opendaylight.controller.sal.utils.IPProtocols;
import org.opendaylight.controller.sal.utils.NetUtils;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.switchmanager.Subnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple bundle to grab some statistics
 * Fred Hsu
 * install file:///home/fhsu/code/odl-apps/getpackets/target/getpackets-1.4.0-SNAPSHOT.jar
 */

public class ICMPReply implements IListenDataPacket {
    private static final Logger log = LoggerFactory
            .getLogger(ICMPReply.class);
    private IDataPacketService dataPacketService = null;
    private ISwitchManager switchManager = null;

    public ICMPReply() {

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
                    // Read ICMP type if echo, then create echo reply
                    // handleICMPPacket((Ethernet) formattedPak, icmpPak, inPkt.getIncomingNodeConnector());

					// Send packet
                    // sendEchoReply(dip
                    // Check to see if the ping was sent to the controller

                }
            }
        }
        return PacketResult.IGNORED;
    }
    
    protected void handleICMPPacket(Ethernet eHeader, ICMP pkt, NodeConnector p) {
        IPv4 ipPak = (IPv4)pkt.getParent();
        InetAddress sourceIP = NetUtils.getInetAddress(ipPak.getSourceAddress());
        InetAddress targetIP = NetUtils.getInetAddress(ipPak.getDestinationAddress());

        //if (pkt.getType() == 0x8 && pkt.getCode() == 0x0) {
        if (true) {
            log.debug("Received ICMP ECHO REQUEST Packet from NodeConnector: {}",
                         p);
            Subnet subnet = null;
            if (switchManager != null) {
                subnet = switchManager.getSubnetByNetworkAddress(sourceIP);
            }
            byte[] targetMAC = eHeader.getDestinationMACAddress();
            byte[] sourceMAC = eHeader.getSourceMACAddress();

            if ((targetIP.equals(subnet.getNetworkAddress()))
                    && Arrays.equals(targetMAC, getControllerMAC())) {
                sendEchoReply(p, getControllerMAC(), targetIP, sourceMAC, sourceIP);
            }

            sendEchoReply(p, targetMAC, targetIP, 
            		sourceMAC, sourceIP);
        } 
    }
    
    protected void sendEchoReply(NodeConnector p, byte[] sMAC, InetAddress sIP,
            byte[] tMAC, InetAddress tIP) {
        ICMP reply = new ICMP(true);
        reply.setType((byte)0);
        reply.setCode((byte)0);
        IPv4 replyPkt = new IPv4(true);
        /*
         * The following are set in IPv4 packet:
         *       setVersion((byte) 4);
        setHeaderLength((byte) 5);
        setDiffServ((byte) 0);
        setECN((byte) 0);
        setIdentification(generateId());
        setFlags((byte) 2);
        setFragmentOffset((short) 0);
         */
        replyPkt.setSourceAddress(sIP).setDestinationAddress(tIP).setPayload(reply);
        replyPkt.setProtocol(IPProtocols.ICMP.byteValue());

        Ethernet ethernet = new Ethernet();
        ethernet.setSourceMACAddress(sMAC).setDestinationMACAddress(tMAC)
                .setEtherType(EtherTypes.IPv4.shortValue()).setPayload(replyPkt);

        RawPacket destPkt = this.dataPacketService.encodeDataPacket(ethernet);
        destPkt.setOutgoingNodeConnector(p);

        this.dataPacketService.transmitDataPacket(destPkt);
    }
    public byte[] getControllerMAC() {
        if (switchManager == null) {
            return null;
        }
        return switchManager.getControllerMAC();
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
    void setSwitchManager(ISwitchManager s) {
        log.debug("SwitchManager set");
        this.switchManager = s;
    }

    void unsetSwitchManager(ISwitchManager s) {
        if (this.switchManager == s) {
            log.debug("SwitchManager removed!");
            this.switchManager = null;
        }
    }

}
