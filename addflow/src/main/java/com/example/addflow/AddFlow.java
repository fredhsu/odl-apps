package com.example.addflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.forwardingrulesmanager.*;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.core.NodeConnector.NodeConnectorIDType;
import org.opendaylight.controller.sal.core.Edge;
import org.opendaylight.controller.sal.utils.*;
import org.opendaylight.controller.sal.action.Action;
import org.opendaylight.controller.sal.match.*;
import org.opendaylight.controller.sal.match.MatchType;
import org.opendaylight.controller.sal.utils.EtherTypes;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.action.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
/**
mvn archetype:generate -DgroupId=com.example -DartifactId=addflow -DarchetypeArtifactId=maven-archetype-quickstart -Dpackage=com.example.addflow -DinteractiveMode=false
 * 
 */
public class AddFlow
{
  private static short DEFAULT_IPSWITCH_PRIORITY = 1;

  private static final Logger log = LoggerFactory
    .getLogger(AddFlow.class);

  private IForwardingRulesManager frm;

  public void setForwardingRulesManager(IForwardingRulesManager forwardingRulesManager) {
    log.debug("Setting ForwardingRulesManager");
    this.frm = forwardingRulesManager;
  }

  void start() {
    log.debug("START called!");
    pushFlows();
  }

  void stop() {
    log.debug("STOP called!");
  }

  void addStaticFlow() {
    frm.addStaticFlow()
  }

  void buildNodeConnectors() {

    //Get a list of nodeconnectors for the links we care about
    //I can get a list of all the nodeconnectors for a given node
    //Then pick out the ones I care about
    this.switchmanager.getNode(); // Get a partiular node
    ports = switchManager.getUpNodeConnectors(currNode);

  }
  void buildEdges() {
    // Create edges for the links we care about by linking nodeconnectors
    //
  }
  void pushFlows() {
    // Could send a List of edges, which have nodeconnectors and nodes.  
    // then go through each edge and generate a match, action, flow, flowentry
    // Pass it in as a map of edges -> flow entries
    // for this edge I want to match this type of traffic, and perform this
    // action
    ArrayList<Node> nodes = new ArrayList<Node>();
    // Nodes = switches
    // Node + flow entries per node
    // First we get a list of the switches we want to put flows on
    Node switch1 = Node.fromString(Node.NodeIDType.OPENFLOW, "00:00:00:00:00:00:00:01");
    Node switch2 = Node.fromString(Node.NodeIDType.OPENFLOW, "00:00:00:00:00:00:00:02");
    Node switch3 = Node.fromString(Node.NodeIDType.OPENFLOW, "00:00:00:00:00:00:00:03");
    Node switch4 = Node.fromString(Node.NodeIDType.OPENFLOW, "00:00:00:00:00:00:00:04");
    //key = Host Node Pair;
    //Might want to create another Map that links nodes to pos entries
    //private ConcurrentMap<HostNodePair, HashMap<NodeConnector, FlowEntry>> rulesDB;
    //NodeConnectors have a Node, ConnectorID, and Type
    //Edges have NodeConnectors
    Map<Node, HashMap<NodeConnector, FlowEntry>> rules;
    //
    //HashMap<NodeConnector, FlowEntry> pos = this.rulesDB.get(key);
    //Create a hashmap that maps ports to flow entries
    //

    // Get NodeConnectors (ports) that will be involved, type and ID
    HashSet<NodeConnector> ports = new HashSet<NodeConnector>();

    NodeConnector nc1;

    ports.add(NodeConnectorCreator.createNodeConnector(
          NodeConnectorIDType.ALL, NodeConnector.SPECIALNODECONNECTORID,
          switch1));
    // Maybe iterate over a set of links instead?
    for (NodeConnector inPort : ports) {
      // For each inport we create a flow entry specifying 
      // a match, and an action
      Match match = new Match();
      List<Action> actions = new ArrayList<Action>();
      // Create matches
      match.setField(MatchType.DL_TYPE, EtherTypes.IPv4.shortValue());
      match.setField(MatchType.NW_DST, "dst ip Address");// Figure out address
      match.setField(MatchType.NW_SRC, "src ip address");
      //Might be able to skip setting L2 if all on same subnet
      //actions.add(new SetDlDst(host.getDataLayerAddressBytes())); //Set l2 addr
      NodeConnector outPort = null;
      // Edges connect NodeConnectors = links, have a head and tail
      actions.add(new Output(outPort));

      // Create flows from matches
      Flow flow = new Flow(match, actions);
      flow.setIdleTimeout((short) 0);
      flow.setHardTimeout((short) 0);
      flow.setPriority(DEFAULT_IPSWITCH_PRIORITY);
      String policyName = "My Policy";
      String flowName = "My Flow";
      //flow entry vs flow?  Flow entry seems to be an annotated flow
      //Flow Entry is what needs to be pushed to the switch
      FlowEntry po = new FlowEntry(policyName, flowName, flow, switch1);
    }
    // Next we want to create the flow entries for each switch
    //
    // Finally push the flow
    // use FRM
    //installFlows(nodes);
  }
  private int installFlows(Set<Node> switchesToProgram, Map<Node, Map<NodeConnector, FlowEntry>> rules) {
    Map<NodeConnector, FlowEntry> pos;
    FlowEntry po;
    int retCode = 0;
    // Now program every single switch
    for (Node swId : switchesToProgram) {
      Node key = swId;
      pos = rules.get(key);
      for (Map.Entry<NodeConnector, FlowEntry> e : pos.entrySet()) {
        po = e.getValue();
        if (po != null) {
          // Populate the Policy field now
          // Push the flowEntry
          Status poStatus = this.frm.installFlowEntry(po);
          if (!poStatus.isSuccess()) {
            log.error("Failed to install policy: "
                + po.getGroupName() + " (" 
                + poStatus.getDescription() + ")");

            retCode = 1;
          } else {
            log.debug("Successfully installed policy "
                + po.toString() + " on switch " + swId);
          }
        }
      }
    }
    return retCode;
  }

}
