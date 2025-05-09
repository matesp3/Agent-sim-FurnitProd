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
		message.setAddressee(this.myAgent().findAssistant(Id.processAssembling));
		this.startContinualAssistant(message);
	}

	//meta! sender="AgentFurnitProd", id="77", type="Request"
	public void processAssignCarpenterB(MessageForm message)
	{
		AssignMessage assignMsg = (AssignMessage) message;
		assignMsg.setCarpenter(this.myAgent().getAllocator().assignCarpenter()); // always needs to be updated
		this.response(assignMsg);
	}

	//meta! sender="ProcessAssembling", id="83", type="Finish"
	public void processFinish(MessageForm message)
	{
		message.setCode(Mc.assembling);
		this.response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentFurnitProd", id="145", type="Notice"
	public void processReleaseCarpenterB(MessageForm message)
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
			processFinish(message);
		break;

		case Mc.releaseCarpenterB:
			processReleaseCarpenterB(message);
		break;

		case Mc.assignCarpenterB:
			processAssignCarpenterB(message);
		break;

		case Mc.assembling:
			processAssembling(message);
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