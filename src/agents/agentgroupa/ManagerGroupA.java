package agents.agentgroupa;

import OSPABA.*;
import common.CarpenterGroup;
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
		message.setAddressee(myAgent().findAssistant(Id.processWoodPrep));
		this.startContinualAssistant(message);
	}

	//meta! sender="AgentFurnitProd", id="54", type="Request"
	public void processAssignCarpenterA(MessageForm message)
	{
		AssignMessage assignMsg = (AssignMessage) message;
		assignMsg.setCarpenter(this.myAgent().getAllocator().assignCarpenter()); // always needs to be updated
		this.response(assignMsg);
	}

	//meta! sender="ProcessFitInstA", id="71", type="Finish"
	public void processFinishProcessFitInstA(MessageForm message)
	{
		message.setCode(Mc.fittingsInstallation);
		this.response(message);
	}

	//meta! sender="ProcessWoodPrep", id="63", type="Finish"
	public void processFinishProcessWoodPrep(MessageForm message)
	{
		message.setCode(Mc.woodPrep);
		this.response(message);
	}

	//meta! sender="ProcessCarving", id="68", type="Finish"
	public void processFinishProcessCarving(MessageForm message)
	{
		message.setCode(Mc.carving);
		this.response(message);
	}

	//meta! sender="AgentFurnitProd", id="59", type="Request"
	public void processFittingsInstallation(MessageForm message)
	{
		message.setAddressee(this.myAgent().findAssistant(Id.processFitInstA));
		this.startContinualAssistant(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentFurnitProd", id="130", type="Request"
	public void processCarving(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.processCarving));
		this.startContinualAssistant(message);
	}

	//meta! sender="AgentFurnitProd", id="143", type="Notice"
	public void processReleaseCarpenterA(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		this.myAgent().getAllocator().releaseCarpenter(tsMsg.getCarpenter());
		tsMsg.setCarpenter(null);
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
			processFittingsInstallation(message);
		break;

		case Mc.carving:
			processCarving(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processFitInstA:
				processFinishProcessFitInstA(message);
			break;

			case Id.processCarving:
				processFinishProcessCarving(message);
			break;

			case Id.processWoodPrep:
				processFinishProcessWoodPrep(message);
			break;
			}
		break;

		case Mc.releaseCarpenterA:
			processReleaseCarpenterA(message);
		break;

		case Mc.woodPrep:
			processWoodPrep(message);
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