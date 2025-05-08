package agents.agentmodel;

import OSPABA.*;
import simulation.*;



//meta! id="4"
public class AgentModel extends OSPABA.Agent
{
	public AgentModel(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.startReplication();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerModel(Id.managerModel, mySim(), this);
		addOwnMessage(Mc.orderArrival);
		addOwnMessage(Mc.orderProcessingEnd);
	}
	//meta! tag="end"

	private void startReplication() {
		OrderMessage msg = new OrderMessage(this.mySim());
		msg.setCode(Mc.init);
		msg.setAddressee(this);
		this.myManager().notice(msg);
	}
}