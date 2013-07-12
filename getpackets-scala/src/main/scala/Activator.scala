package com.example.getpackets
import org.osgi.framework.{ BundleActivator, BundleContext }

import org.opendaylight.controller.sal._
import org.opendaylight.controller.switchmanager.ISwitchManager
import org.opendaylight.controller.sal.utils.ServiceHelper
import scala.collection.JavaConversions._

class Activator extends BundleActivator {
  override def start(context: BundleContext) {
    println("Start OSGi Bundle...")
    val getpackets = new GetPackets()
    getpackets.start
  }

  override def stop(context: BundleContext) {
    println("Stop OSGi Bundle...")
  }
}

class GetPackets {
  def start() = {
    println("Start class")
    getFlowStatistics
  }
	def getFlowStatistics = {
		val containerName = "default"
		val statsManager:IStatisticsManager = org.opendaylight.controller.sal.utils.ServiceHelper.getInstance(classOf[IStatisticsManager], containerName, this).asInstanceOf[IStatisticsManager]
    val swmgrclass = classOf[ISwitchManager]
    println(swmgrclass)
		val switchManager:ISwitchManager = ServiceHelper.getInstance(classOf[ISwitchManager], containerName, this).asInstanceOf[ISwitchManager]
    switchManager.getNodes().map( x => println("Node: " + x))
    /* Replacing the following with the coe above to make it more functional
    val nodes:scala.collection.mutable.Set[org.opendaylight.controller.sal.core.Node] = switchManager.getNodes()

    for (node <- nodes) {
      println("Node: " + node)
    }
    */
  }
}

