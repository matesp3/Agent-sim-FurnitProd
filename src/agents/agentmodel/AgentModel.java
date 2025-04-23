package agents.agentmodel;

import OSPABA.*;
import simulation.*;

//meta! id="3"
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
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerModel(Id.managerModel, mySim(), this);
		addOwnMessage(Mc.orderProcessing);
		addOwnMessage(Mc.orderArrival);
	}
	//meta! tag="end"
}
