package agents.agentgroupc;

import OSPABA.*;
import OSPAnimator.IAnimator;
import OSPRNG.RNG;
import animation.FurnitureFactoryAnimation;
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
		new ProcessCheckPieces(Id.processCheckPieces, mySim(), this);
		new ProcessPaintcoat(Id.processPaintcoat, mySim(), this);
		new ProcessStaining(Id.processStaining, mySim(), this);
		addOwnMessage(Mc.releaseCarpenterC);
		addOwnMessage(Mc.checkPieces);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.stainingAndPaintcoat);
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
		this.allocator.initCarpenters(amount, this.mySim().animatorExists());
	}

	@Override
	public double getGroupUtilization() {
		return this.allocator.getGroupUtilization(this.mySim().currentTime());
	}

	@Override
	public void registerEntities() {
		IAnimator animator = this.mySim().animator();
		FurnitureFactoryAnimation animHandler = ((MySimulation)this.mySim()).getAnimationHandler();
		for (Carpenter c : this.allocator.getCarpenters()) {
			c.initAnimatedEntity().registerEntity(animator);
			if (c.isWorking()) {
				animHandler.placeCarpenterToDesk(c.getAssignedProduct().getDeskID(), c.getAnimatedEntity());
			}
			else {
				if (c.isInStorage())
					animHandler.placeCarpenterCToStorage(c.getAnimatedEntity());
				else
					animHandler.placeCarpenterToDesk(c.getCurrentDeskID(), c.getAnimatedEntity());
			}
		}
	}

	@Override
	public void unregisterEntities() {
		for (Carpenter c : this.allocator.getCarpenters()) {
			c.removeAnimatedEntity(this.mySim().animator());
//			c.getAnimatedEntity().unregisterEntity(this.mySim().animator());
		}
	}
}