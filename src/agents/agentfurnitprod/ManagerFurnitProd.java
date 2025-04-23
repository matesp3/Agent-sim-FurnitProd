package agents.agentfurnitprod;

import OSPABA.*;
import common.Furniture;
import simulation.*;

//meta! id="24"
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

	//meta! sender="AgentTransfer", id="35", type="Response"
	public void processDeskTransfer(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="57", type="Response"
	public void processPrepAndCarving(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="28", type="Request"
	public void processOrderProcessing(MessageForm message)
	{
		OrderMessage msg = (OrderMessage)message;
		this.myAgent().getQueueA().add(msg);
//		// print received order
//		System.out.println(msg.getOrder());
//		Furniture f = msg.getOrder().assignUnprocessedProduct();
//		while (f != null) {
//			System.out.println(f);
//			f = msg.getOrder().assignUnprocessedProduct();
//		}
//		System.out.println();
//		// return it back to test communication through AgentModel
		msg.setCode(Mc.orderProcessing);
		this.response(message);
//		// ok...
	}

	//meta! sender="AgentTransfer", id="33", type="Response"
	public void processStorageTransfer(MessageForm message)
	{
	}

	//meta! sender="AgentGroupB", id="79", type="Response"
	public void processAssembling(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="54", type="Response"
	public void processAssignCarpenterA(MessageForm message)
	{
	}

	//meta! sender="AgentGroupB", id="77", type="Response"
	public void processAssignCarpenterB(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="88", type="Response"
	public void processAssignCarpenterC(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="90", type="Response"
	public void processStainingAndPaintcoat(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="59", type="Response"
	public void processFittingsInstallationAgentGroupA(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="92", type="Response"
	public void processFittingsInstallationAgentGroupC(MessageForm message)
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

		case Mc.prepAndCarving:
			processPrepAndCarving(message);
		break;

		case Mc.assembling:
			processAssembling(message);
		break;

		case Mc.fittingsInstallation:
			switch (message.sender().id())
			{
			case Id.agentGroupA:
				processFittingsInstallationAgentGroupA(message);
			break;

			case Id.agentGroupC:
				processFittingsInstallationAgentGroupC(message);
			break;
			}
		break;

		case Mc.assignCarpenterA:
			processAssignCarpenterA(message);
		break;

		case Mc.storageTransfer:
			processStorageTransfer(message);
		break;

		case Mc.assignCarpenterB:
			processAssignCarpenterB(message);
		break;

		case Mc.assignCarpenterC:
			processAssignCarpenterC(message);
		break;

		case Mc.deskTransfer:
			processDeskTransfer(message);
		break;

		case Mc.stainingAndPaintcoat:
			processStainingAndPaintcoat(message);
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