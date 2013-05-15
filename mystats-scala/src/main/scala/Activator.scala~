package com.example.mystats
import org.osgi.framework.{ BundleActivator, BundleContext }

import org.opendaylight.controller.sal._
import org.opendaylight.controller.statisticsmanager.IStatisticsManager
import org.opendaylight.controller.switchmanager.ISwitchManager
import org.opendaylight.controller.sal.utils.ServiceHelper
import scala.collection.JavaConversions._

class Activator extends BundleActivator {
  override def start(context: BundleContext) {
    println("Start OSGi Bundle...")
    val mystats = new MyStats()
    mystats.start
  }

  override def stop(context: BundleContext) {
    println("Stop OSGi Bundle...")
  }
}

class MyStats {
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
    val nodes:scala.collection.mutable.Set[org.opendaylight.controller.sal.core.Node] = switchManager.getNodes()
    for (node <- nodes) {
      println("Node: " + node)
    }
  }
}

