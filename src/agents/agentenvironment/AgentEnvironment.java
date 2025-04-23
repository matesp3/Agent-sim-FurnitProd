package agents.agentenvironment;

import OSPABA.*;
import simulation.*;
import agents.agentenvironment.continualassistants.*;

//meta! id="7"
public class AgentEnvironment extends OSPABA.Agent
{
	public AgentEnvironment(int id, Simulation mySim, Agent parent)
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
		new ManagerEnvironment(Id.managerEnvironment, mySim(), this);
		new SchedulerOrder(Id.schedulerOrder, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.orderCompleted);
	}
	//meta! tag="end"
}
