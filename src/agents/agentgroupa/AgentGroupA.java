package agents.agentgroupa;

import OSPABA.*;
import OSPRNG.RNG;
import common.Carpenter;
import common.CarpenterGroup;
import contracts.IAgentWithEntity;
import contracts.ICarpenterGroup;
import contracts.IFittingsInstaller;
import simulation.*;
import agents.agentgroupa.continualassistants.*;



//meta! id="39"
public class AgentGroupA extends OSPABA.Agent implements IFittingsInstaller, ICarpenterGroup, IAgentWithEntity
{
	private final CarpenterGroup allocator;

	public AgentGroupA(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		this.allocator = new CarpenterGroup(Carpenter.GROUP.A);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.allocator.resetCarpenters();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroupA(Id.managerGroupA, mySim(), this);
		new ProcessCarving(Id.processCarving, mySim(), this);
		new ProcessFitInstA(Id.processFitInstA, mySim(), this);
		new ProcessWoodPrep(Id.processWoodPrep, mySim(), this);
		addOwnMessage(Mc.woodPrep);
		addOwnMessage(Mc.releaseCarpenterA);
		addOwnMessage(Mc.carving);
		addOwnMessage(Mc.assignCarpenterA);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"

	@Override
	public void setFitInstGenerator(RNG<Double> durationGenerator) {
		((ProcessFitInstA)this.findAssistant(Id.processFitInstA)).setFitInstGenerator(durationGenerator);
	}

	@Override
	public CarpenterGroup getAllocator() {
		return this.allocator;
	}

	@Override
	public void setAmountOfCarpenters(int amount) {
		this.allocator.initCarpenters(amount);
	}

	public double getGroupUtilization() {
		return this.allocator.getGroupUtilization(this.mySim().currentTime());
	}

	@Override
	public void registerEntities() {
		for (Carpenter c : this.allocator.getCarpenters()) {
			this.mySim().animator().register(c.getAnimatedEntity());
		}
	}
}