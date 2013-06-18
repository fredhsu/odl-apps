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
        Packet formattedPak = this.dataPacketService.decodeDataPacket(inPkt);
        if (formattedPak instanceof Ethernet) {
            Object nextPak = formattedPak.getPayload();
            if (nextPak instanceof IPv4) {
                log.trace("Handled IP packet");
            }
            if (nextPak instanceof ARP) {
                log.trace("Handled ARP packet");
            }
        }
        return PacketResult.IGNORED;
    }
}
