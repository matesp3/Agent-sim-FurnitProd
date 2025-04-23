package agents.agentenvironment;

import OSPABA.*;
import simulation.*;

//meta! id="7"
public class ManagerEnvironment extends OSPABA.Manager
{
	public ManagerEnvironment(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentModel", id="9", type="Notice"
	public void processInit(MessageForm message)
	{
	}

	//meta! sender="SchedulerOrder", id="115", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="18", type="Notice"
	public void processOrderCompleted(MessageForm message)
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
		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.orderCompleted:
			processOrderCompleted(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentEnvironment myAgent()
	{
		return (AgentEnvironment)super.myAgent();
	}

}
