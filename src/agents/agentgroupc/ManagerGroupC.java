package agents.agentgroupc;

import OSPABA.*;
import simulation.*;

//meta! id="83"
public class ManagerGroupC extends OSPABA.Manager
{
	public ManagerGroupC(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentFurnitProd", id="86", type="Request"
	public void processStaining(MessageForm message)
	{
	}

	//meta! sender="ProcessFitInst", id="129", type="Finish"
	public void processFinishProcessFitInst(MessageForm message)
	{
	}

	//meta! sender="ProcessPaintCoat", id="127", type="Finish"
	public void processFinishProcessPaintCoat(MessageForm message)
	{
	}

	//meta! sender="ProcessStaining", id="105", type="Finish"
	public void processFinishProcessStaining(MessageForm message)
	{
	}

	//meta! sender="AgentFurnitProd", id="136", type="Request"
	public void processAssignCarpenterC(MessageForm message)
	{
	}

	//meta! sender="AgentFurnitProd", id="88", type="Request"
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

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.fittingsInstallation:
			processFittingsInstallation(message);
		break;

		case Mc.staining:
			processStaining(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processFitInst:
				processFinishProcessFitInst(message);
			break;

			case Id.processPaintCoat:
				processFinishProcessPaintCoat(message);
			break;

			case Id.processStaining:
				processFinishProcessStaining(message);
			break;
			}
		break;

		case Mc.assignCarpenterC:
			processAssignCarpenterC(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroupC myAgent()
	{
		return (AgentGroupC)super.myAgent();
	}

}
