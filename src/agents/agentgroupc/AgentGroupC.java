package agents.agentgroupc;

import OSPABA.*;
import OSPAnimator.IAnimator;
import OSPRNG.RNG;
import common.Carpenter;
import common.CarpenterGroup;
import contracts.IAgentWithEntity;
import contracts.ICarpenterGroup;
import contracts.IFittingsInstaller;
import simulation.*;
import agents.agentgroupc.continualassistants.*;



//meta! id="85"
public class AgentGroupC extends OSPABA.Agent implements IFittingsInstaller, ICarpenterGroup, IAgentWithEntity
{
	private final CarpenterGroup allocator;

	public AgentGroupC(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		this.allocator = new CarpenterGroup(Carpenter.GROUP.C);
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
		new ManagerGroupC(Id.managerGroupC, mySim(), this);
		new ProcessFitInstC(Id.processFitInstC, mySim(), this);
		new ProcessStaining(Id.processStaining, mySim(), this);
		new ProcessPaintcoat(Id.processPaintcoat, mySim(), this);
		addOwnMessage(Mc.releaseCarpenterC);
		addOwnMessage(Mc.stainingAndPaintcoat);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"

	@Override
	public void setFitInstGenerator(RNG<Double> durationGenerator) {
		((ProcessFitInstC)this.findAssistant(Id.processFitInstC)).setFitInstGenerator(durationGenerator);
	}

	@Override
	public CarpenterGroup getAllocator() {
		return this.allocator;
	}

	@Override
	public void setAmountOfCarpenters(int amount) {
		this.allocator.initCarpenters(amount);
	}

	@Override
	public double getGroupUtilization() {
		return this.allocator.getGroupUtilization(this.mySim().currentTime());
	}

	@Override
	public void registerEntities() {
		IAnimator animator = this.mySim().animator();
		for (Carpenter c : this.allocator.getCarpenters()) {
			c.getAnimatedEntity().registerEntity(animator);
		}
	}
}