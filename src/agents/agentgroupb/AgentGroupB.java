package agents.agentgroupb;

import OSPABA.*;
import common.Carpenter;
import common.CarpenterGroup;
import contracts.ICarpenterGroup;
import simulation.*;
import agents.agentgroupb.continualassistants.*;



//meta! id="73"
public class AgentGroupB extends OSPABA.Agent implements ICarpenterGroup
{
	private final CarpenterGroup allocator;

	public AgentGroupB(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		this.allocator = new CarpenterGroup(Carpenter.GROUP.B);
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

	@Override
	public CarpenterGroup getAllocator() {
		return this.allocator;
	}

	@Override
	public void setAmountOfCarpenters(int amount) {
		this.allocator.initCarpenters(amount);
	}
	//meta! tag="end"
}