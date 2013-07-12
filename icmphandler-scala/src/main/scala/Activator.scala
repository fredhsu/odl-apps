package com.example.icmphandler
import org.osgi.framework.{ BundleActivator, BundleContext }

import org.opendaylight.controller.sal._
import org.opendaylight.controller.sal.packet.IListenDataPacket
import org.opendaylight.controller.sal.packet._
import org.opendaylight.controller.switchmanager.ISwitchManager
import org.opendaylight.controller.sal.utils.ServiceHelper
import scala.collection.JavaConversions._

class Activator extends BundleActivator {
  override def start(context: BundleContext) {
    println("Start OSGi Bundle...")
    val icmphandler = new icmphandler()
    icmphandler.start
}

  override def stop(context: BundleContext) {
    println("Stop OSGi Bundle...")
  }
  }

class icmphandler extends IListenDataPacket{
    val containerName = "default"
    val dataPacketService:IDataPacketService =
      org.opendaylight.controller.sal.utils.ServiceHelper.getInstance(classOf[IDataPacketService], containerName,
        this).asInstanceOf[IDataPacketService]
  def start() = {
    println("Start class")
    getIcmpPackets
  }
  def getIcmpPackets = {
    val swmgrclass = classOf[ISwitchManager]
    println(swmgrclass)
    val switchManager:ISwitchManager = ServiceHelper.getInstance(classOf[ISwitchManager], containerName, this).asInstanceOf[ISwitchManager]
    switchManager.getNodes().map( x => println("Node: " + x))
  }
  override def receiveDataPacket(inPkt: RawPacket): PacketResult = {
    println("Got a packet")
    val formattedPak: Packet = dataPacketService.decodeDataPacket(inPkt)
    println("formattedPak")
    PacketResult.IGNORED
  }
}

