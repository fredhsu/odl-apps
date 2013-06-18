package com.example.getpackets;

import org.opendaylight.controller.sal.packet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple bundle to grab some statistics
 * Fred Hsu
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
    System.out.println("recevieDataPacket");
        if (inPkt == null) {
            return PacketResult.IGNORED;
        }
        log.trace("Received a frame of size: {}",
                        inPkt.getPacketData().length);
        System.out.println("packet size " + inPkt.getPacketData().length);
        Packet formattedPak = this.dataPacketService.decodeDataPacket(inPkt);
        System.out.println("packet");
        System.out.println(formattedPak);
        if (formattedPak instanceof Ethernet) {
            System.out.println("Ethernet packet");
            System.out.println(formattedPak);
            Object nextPak = formattedPak.getPayload();
            if (nextPak instanceof ICMP) {
                System.out.println("ICMP");
                log.trace("Handled ICMP packet");
                System.out.println(nextPak);
            }
            if (nextPak instanceof IPv4) {
                System.out.println("IP");
                log.trace("Handled IP packet");
                System.out.println(((IPv4)nextPak).getSourceAddress());
            }
            if (nextPak instanceof ARP) {
                System.out.println("Arp");
                log.trace("Handled ARP packet");
                System.out.println("Getting protocol address of target:");
                System.out.println(((ARP)nextPak).getTargetProtocolAddress());
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
