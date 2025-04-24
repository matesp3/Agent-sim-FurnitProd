package agents.agentgroupa;

import OSPABA.*;
import simulation.*;

//meta! id="39"
public class ManagerGroupA extends OSPABA.Manager
{
	public ManagerGroupA(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentFurnitProd", id="57", type="Request"
	public void processWoodPrep(MessageForm message)
	{
	}

	//meta! sender="AgentFurnitProd", id="54", type="Request"
	public void processAssignCarpenterA(MessageForm message)
	{
	}

	//meta! sender="ProcessFitInstA", id="71", type="Finish"
	public void processFinishProcessFitInstA(MessageForm message)
	{
	}

	//meta! sender="ProcessWoodPrep", id="63", type="Finish"
	public void processFinishProcessWoodPrep(MessageForm message)
	{
	}

	//meta! sender="ProcessCarving", id="68", type="Finish"
	public void processFinishProcessCarving(MessageForm message)
	{
	}

	//meta! sender="AgentFurnitProd", id="59", type="Request"
	public void processFittingsInstallation(MessageForm message)
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
	public void processReleaseCarpenterA(MessageForm message)
	{
	}

	//meta! sender="AgentFurnitProd", id="130", type="Request"
	public void processCarving(MessageForm message)
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
			switch (message.sender().id())
			{
			case Id.processWoodPrep:
				processFinishProcessWoodPrep(message);
			break;

			case Id.processFitInstA:
				processFinishProcessFitInstA(message);
			break;

			case Id.processCarving:
				processFinishProcessCarving(message);
			break;
			}
		break;

		case Mc.fittingsInstallation:
			processFittingsInstallation(message);
		break;

		case Mc.carving:
			processCarving(message);
		break;

		case Mc.woodPrep:
			processWoodPrep(message);
		break;

		case Mc.assignCarpenterA:
			processAssignCarpenterA(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroupA myAgent()
	{
		return (AgentGroupA)super.myAgent();
	}

}