package agents.agentgroupc;

import OSPABA.*;
import simulation.*;

//meta! id="85"
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

	//meta! sender="AgentFurnitProd", id="88", type="Request"
	public void processAssignCarpenterC(MessageForm message)
	{
		AssignMessage assignMsg = (AssignMessage) message;
		assignMsg.setCarpenter(this.myAgent().getAllocator().assignCarpenter()); // always needs to be updated
		this.response(assignMsg);
	}

	//meta! sender="ProcessStaining", id="99", type="Finish"
	public void processFinishProcessStaining(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		if (tsMsg.getProductToProcess().isLacqueringRequired()) {
			tsMsg.setAddressee(this.myAgent().findAssistant(Id.processPaintcoat));
			this.startContinualAssistant(tsMsg);
			return;
		}
		// else -> end of this process
		tsMsg.setCode(Mc.stainingAndPaintcoat);
		this.response(tsMsg);
	}

	//meta! sender="ProcessFitInstC", id="103", type="Finish"
	public void processFinishProcessFitInstC(MessageForm message)
	{
		message.setCode(Mc.fittingsInstallation);
		this.response(message);
	}

	//meta! sender="ProcessPaintcoat", id="101", type="Finish"
	public void processFinishProcessPaintcoat(MessageForm message)
	{
		message.setCode(Mc.stainingAndPaintcoat);
		this.response(message);
	}

	//meta! sender="AgentFurnitProd", id="90", type="Request"
	public void processStainingAndPaintcoat(MessageForm message)
	{
		// start staining
		message.setAddressee(this.myAgent().findAssistant(Id.processStaining));
		this.startContinualAssistant(message);
	}

	//meta! sender="AgentFurnitProd", id="92", type="Request"
	public void processFittingsInstallation(MessageForm message)
	{
		message.setAddressee(this.myAgent().findAssistant(Id.processFitInstC));
		this.startContinualAssistant(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentFurnitProd", id="147", type="Notice"
	public void processReleaseCarpenterC(MessageForm message)
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processStaining:
				processFinishProcessStaining(message);
			break;

			case Id.processPaintcoat:
				processFinishProcessPaintcoat(message);
			break;

			case Id.processFitInstC:
				processFinishProcessFitInstC(message);
			break;
			}
		break;

		case Mc.stainingAndPaintcoat:
			processStainingAndPaintcoat(message);
		break;

		case Mc.fittingsInstallation:
			processFittingsInstallation(message);
		break;

		case Mc.assignCarpenterC:
			processAssignCarpenterC(message);
		break;

		case Mc.releaseCarpenterC:
			processReleaseCarpenterC(message);
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