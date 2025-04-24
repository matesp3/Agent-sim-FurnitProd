package agents.agentgroupc;

import OSPABA.*;
import OSPRNG.RNG;
import contracts.IFittingsInstaller;
import simulation.*;
import agents.agentgroupc.continualassistants.*;



//meta! id="85"
public class AgentGroupC extends OSPABA.Agent implements IFittingsInstaller
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
		new ProcessStaining(Id.processStaining, mySim(), this);
		new ProcessPaintcoat(Id.processPaintcoat, mySim(), this);
		new ProcessFitInstC(Id.processFitInstC, mySim(), this);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.stainingAndPaintcoat);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"

	@Override
	public void setFitInstGenerator(RNG<Double> durationGenerator) {
		((ProcessFitInstC)this.findAssistant(Id.processFitInstC)).setFitInstGenerator(durationGenerator);
	}
}