package agents.agentfurnitprod;

import OSPABA.*;
import simulation.*;

//meta! id="8"
public class ManagerFurnitProd extends OSPABA.Manager
{
	public ManagerFurnitProd(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentTransfer", id="36", type="Response"
	public void processDeskTransfer(MessageForm message)
	{
	}

	//meta! sender="AgentTransfer", id="32", type="Response"
	public void processStorageTransfer(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="26", type="Request"
	public void processOrderProcessing(MessageForm message)
	{
	}

	//meta! sender="AgentGroupB", id="80", type="Response"
	public void processAssembling(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="86", type="Response"
	public void processStaining(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="132", type="Response"
	public void processAssignCarpenterA(MessageForm message)
	{
	}

	//meta! sender="AgentGroupB", id="134", type="Response"
	public void processAssignCarpenterB(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="136", type="Response"
	public void processAssignCarpenterC(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="88", type="Response"
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
		case Mc.deskTransfer:
			processDeskTransfer(message);
		break;

		case Mc.fittingsInstallation:
			processFittingsInstallation(message);
		break;

		case Mc.storageTransfer:
			processStorageTransfer(message);
		break;

		case Mc.assignCarpenterC:
			processAssignCarpenterC(message);
		break;

		case Mc.orderProcessing:
			processOrderProcessing(message);
		break;

		case Mc.staining:
			processStaining(message);
		break;

		case Mc.assignCarpenterB:
			processAssignCarpenterB(message);
		break;

		case Mc.assembling:
			processAssembling(message);
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
	public AgentFurnitProd myAgent()
	{
		return (AgentFurnitProd)super.myAgent();
	}

}
