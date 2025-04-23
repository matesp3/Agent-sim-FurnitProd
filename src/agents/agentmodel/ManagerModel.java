package agents.agentmodel;

import OSPABA.*;
import simulation.*;

//meta! id="4"
public class ManagerModel extends OSPABA.Manager
{
	public ManagerModel(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentFurnitProd", id="28", type="Response"
	public void processOrderProcessing(MessageForm message)
	{
	}

	//meta! sender="AgentEnvironment", id="15", type="Notice"
	public void processOrderArrival(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.orderProcessing:
			processOrderProcessing(message);
		break;

		case Mc.orderArrival:
			processOrderArrival(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentModel myAgent()
	{
		return (AgentModel)super.myAgent();
	}

}
