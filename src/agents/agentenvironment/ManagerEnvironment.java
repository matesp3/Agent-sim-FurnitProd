package agents.agentenvironment;

import OSPABA.*;
import simulation.*;

//meta! id="6"
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

	//meta! sender="AgentModel", id="11", type="Notice"
	public void processInit(MessageForm message)
	{
		message.setAddressee(this.myAgent().findAssistant(Id.schedulerOrderArrival));
		this.startContinualAssistant(message);
	}

	//meta! sender="SchedulerOrderArrival", id="42", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="13", type="Notice"
	public void processOrderCompleted(MessageForm message)
	{
		this.myAgent().updateStats(
				((OrderMessage)message).getOrder()
		);
//		System.out.println("jre");
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.orderArrival:
				message.setAddressee(this.myAgent().parent());
				this.notice(message); // notify boss about new order arrival
				break;
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
		case Mc.orderCompleted:
			processOrderCompleted(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			processFinish(message);
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