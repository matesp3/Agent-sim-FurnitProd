package agents.agenttransfer;

import OSPABA.*;
import simulation.*;
import agents.agenttransfer.continualassistants.*;

//meta! id="30"
public class AgentTransfer extends OSPABA.Agent
{
	public AgentTransfer(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerTransfer(Id.managerTransfer, mySim(), this);
		new ProcessDeskTransfer(Id.processDeskTransfer, mySim(), this);
		new ProcessStorageTransfer(Id.processStorageTransfer, mySim(), this);
		addOwnMessage(Mc.deskTransfer);
		addOwnMessage(Mc.storageTransfer);
	}
	//meta! tag="end"
}
