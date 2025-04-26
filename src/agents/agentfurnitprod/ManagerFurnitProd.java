package agents.agentfurnitprod;

import OSPABA.*;
import common.*;
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
	public void processWoodPrep(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="28", type="Request"
	public void processOrderProcessing(MessageForm message)
	{
		/* NOTES
			- if there's some free carpenter A, then there's no request for processing Fittings Installation (bcs then,
			  he would already work on it)
			- if no desk is free, then there's no reason to do request for carpenter A assign
		 */
		OrderMessage msg = (OrderMessage)message;
		DeskAllocation deskManager = this.myAgent().getDeskManager();

		if (deskManager.hasFreeDesk()) {
			TechStepMessage tsMsg = new TechStepMessage(this.mySim());
			tsMsg.setCode(Mc.assignCarpenterA);
			tsMsg.setAddressee(Id.agentGroupA);
			this.request(tsMsg);
			// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
			Carpenter carpenter = null;
			do {
				// try to assign carpenter A - request!
				if (carpenter == null)
					break;
				// start work with assigned carpenter A
//				if (!order.hasUnstartedProduct()) // nothing to process
					return;
//				carpenter = null; // for next product, next carpenter
			} while (deskManager.hasFreeDesk());
		}
		else { // this new order must wait as a whole, bcs there's no place where some product can be created
			this.myAgent().getQUnprocessed().add(msg);
		}

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		this.myAgent().getQUnprocessed().add(msg);

		Furniture f = msg.getOrder().assignUnstartedProduct();
		while (f != null) {
			System.out.println(f);
			f = msg.getOrder().assignUnstartedProduct();
		}
		System.out.println();
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
	//		this.makeResponseIfOrderCompleted(...);
	}

	//meta! sender="AgentGroupC", id="92", type="Response"
	public void processFittingsInstallationAgentGroupC(MessageForm message)
	{
//		this.makeResponseIfOrderCompleted(...);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentGroupA", id="130", type="Response"
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
		case Mc.assignCarpenterA:
			processAssignCarpenterA(message);
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

		case Mc.orderProcessing:
			processOrderProcessing(message);
		break;

		case Mc.storageTransfer:
			processStorageTransfer(message);
		break;

		case Mc.woodPrep:
			processWoodPrep(message);
		break;

		case Mc.deskTransfer:
			processDeskTransfer(message);
		break;

		case Mc.carving:
			processCarving(message);
		break;

		case Mc.assignCarpenterB:
			processAssignCarpenterB(message);
		break;

		case Mc.stainingAndPaintcoat:
			processStainingAndPaintcoat(message);
		break;

		case Mc.assembling:
			processAssembling(message);
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
	public AgentFurnitProd myAgent()
	{
		return (AgentFurnitProd)super.myAgent();
	}

	private void makeResponseIfOrderCompleted(OrderMessage msg) {
		if (msg.getOrder().isCompleted()) {
			msg.setCode(Mc.orderCompleted);
			this.response(msg);
		}
	}

}