package agents.agentgroupa;

import OSPABA.*;
import OSPRNG.RNG;
import contracts.IFittingsInstaller;
import simulation.*;
import agents.agentgroupa.continualassistants.*;



//meta! id="39"
public class AgentGroupA extends OSPABA.Agent implements IFittingsInstaller
{
	public AgentGroupA(int id, Simulation mySim, Agent parent)
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
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessCarving(Id.processCarving, mySim(), this);
		new ProcessWoodPrep(Id.processWoodPrep, mySim(), this);
		new ProcessFitInstA(Id.processFitInstA, mySim(), this);
		addOwnMessage(Mc.prepAndCarving);
		addOwnMessage(Mc.assignCarpenterA);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"

	@Override
	public void setFitInstGenerator(RNG<Double> durationGenerator) {
		((ProcessFitInstA)this.findAssistant(Id.processFitInstA)).setFitInstGenerator(durationGenerator);
	}
}