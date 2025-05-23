package agents.agenttransfer;

import OSPABA.*;
import simulation.*;

//meta! id="30"
public class ManagerTransfer extends OSPABA.Manager
{
	public ManagerTransfer(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentFurnitProd", id="35", type="Request"
	public void processDeskTransfer(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.processDeskTransfer));
		this.startContinualAssistant(message);
	}

	//meta! sender="AgentFurnitProd", id="33", type="Request"
	public void processStorageTransfer(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.processStorageTransfer));
		this.startContinualAssistant(message);
	}

	//meta! sender="ProcessStorageTransfer", id="48", type="Finish"
	public void processFinishProcessStorageTransfer(MessageForm message)
	{
		message.setCode(Mc.storageTransfer);
		this.response(message);
	}

	//meta! sender="ProcessDeskTransfer", id="46", type="Finish"
	public void processFinishProcessDeskTransfer(MessageForm message)
	{
		message.setCode(Mc.deskTransfer);
		this.response(message);
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
		case Mc.storageTransfer:
			processStorageTransfer(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processDeskTransfer:
				processFinishProcessDeskTransfer(message);
			break;

			case Id.processStorageTransfer:
				processFinishProcessStorageTransfer(message);
			break;
			}
		break;

		case Mc.deskTransfer:
			processDeskTransfer(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentTransfer myAgent()
	{
		return (AgentTransfer)super.myAgent();
	}

}