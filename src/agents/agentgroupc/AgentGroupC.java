package agents.agentgroupc;

import OSPABA.*;
import simulation.*;
import agents.agentgroupc.continualassistants.*;
import agents.agentgroupc.instantassistants.*;

//meta! id="83"
public class AgentGroupC extends OSPABA.Agent
{
	public AgentGroupC(int id, Simulation mySim, Agent parent)
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
		new ManagerGroupC(Id.managerGroupC, mySim(), this);
		new QueryAssignCarpenterC(Id.queryAssignCarpenterC, mySim(), this);
		new ProcessFitInst(Id.processFitInst, mySim(), this);
		new ProcessStaining(Id.processStaining, mySim(), this);
		new ProcessPaintCoat(Id.processPaintCoat, mySim(), this);
		addOwnMessage(Mc.staining);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"
}
