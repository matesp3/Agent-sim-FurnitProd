package agents.agentgroupb;

import OSPABA.*;
import simulation.*;
import agents.agentgroupb.continualassistants.*;

//meta! id="73"
public class AgentGroupB extends OSPABA.Agent
{
	public AgentGroupB(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroupB(Id.managerGroupB, mySim(), this);
		new ProcessAssembling(Id.processAssembling, mySim(), this);
		addOwnMessage(Mc.assembling);
		addOwnMessage(Mc.assignCarpenterB);
	}
	//meta! tag="end"
}
