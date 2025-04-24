package agents.agentgroupb;

import OSPABA.*;
import simulation.*;

//meta! id="73"
public class ManagerGroupB extends OSPABA.Manager
{
	public ManagerGroupB(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentFurnitProd", id="79", type="Request"
	public void processAssembling(MessageForm message)
	{
	}

	//meta! sender="AgentFurnitProd", id="77", type="Request"
	public void processAssignCarpenterB(MessageForm message)
	{
	}

	//meta! sender="ProcessAssembling", id="83", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Removed from model"
	public void processReleaseCarpenterB(MessageForm message)
	{
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.assembling:
			processAssembling(message);
		break;

		case Mc.assignCarpenterB:
			processAssignCarpenterB(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroupB myAgent()
	{
		return (AgentGroupB)super.myAgent();
	}

}