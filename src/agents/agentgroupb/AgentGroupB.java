package agents.agentgroupb;

import OSPABA.*;
import common.Carpenter;
import common.CarpenterGroup;
import contracts.IAgentWithEntity;
import contracts.ICarpenterGroup;
import simulation.*;
import agents.agentgroupb.continualassistants.*;



//meta! id="73"
public class AgentGroupB extends OSPABA.Agent implements ICarpenterGroup, IAgentWithEntity
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
		this.allocator.resetCarpenters();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroupB(Id.managerGroupB, mySim(), this);
		new ProcessAssembling(Id.processAssembling, mySim(), this);
		addOwnMessage(Mc.releaseCarpenterB);
		addOwnMessage(Mc.assembling);
		addOwnMessage(Mc.assignCarpenterB);
	}
	//meta! tag="end"

	@Override
	public CarpenterGroup getAllocator() {
		return this.allocator;
	}

	@Override
	public void setAmountOfCarpenters(int amount) {
		this.allocator.initCarpenters(amount);
	}

	@Override
	public double getGroupUtilization() {
		return this.allocator.getGroupUtilization(this.mySim().currentTime());
	}

	@Override
	public void registerEntities() {
		for (Carpenter c : this.allocator.getCarpenters()) {
			this.mySim().animator().register(c.getAnimatedEntity());
		}
	}
}