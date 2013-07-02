name := "getpackets"

version := "1.0.0"

libraryDependencies ++= Seq(
	"org.osgi" % "org.osgi.core" % "4.3.0" % "provided",
	"org.apache.felix" % "org.apache.felix.framework" % "4.0.3" % "runtime",
	"org.opendaylight.controller" % "sal" % "0.5.0-SNAPSHOT",
	"org.opendaylight.controller" % "switchmanager" % "0.4.0-SNAPSHOT",
)

resolvers += "Local Maven Repository" at "file:///"+Path.userHome+"/.m2/repository"

osgiSettings

OsgiKeys.bundleActivator := Option("com.example.mystats.Activator")

OsgiKeys.importPackage := Seq(
	"org.opendaylight.controller.sal.core;version=0.4.0.SNAPSHOT",
	"org.opendaylight.controller.switchmanager;version=0.4.0.SNAPSHOT",
	"org.opendaylight.controller.sal.utils;version=0.4.0.SNAPSHOT",
	"org.opendaylight.controller.sal.packet;version=0.4.0.SNAPSHOT",
	"org.slf4j",
	"org.apache.felix.dm",
  "*"
)

OsgiKeys.exportPackage := Seq("com.example.getpackets")


OsgiKeys.additionalHeaders := Map(
    "Service-Component" -> "*",
      "Conditional-Package" -> "scala.*"
    )
