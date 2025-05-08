package agents.agenttransfer.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.TriangularRNG;
import agents.agenttransfer.*;
import animation.FurnitureFactoryAnimation;
import common.Carpenter;
import common.Furniture;
import simulation.*;
import OSPABA.Process;
import utils.SeedGen;

//meta! id="47"
public class ProcessStorageTransfer extends OSPABA.Process
{
	private final RNG<Double> rndTransferDuration = new TriangularRNG(60.0, 120.0, 480.0, SeedGen.getSeedRNG());

	public ProcessStorageTransfer(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentTransfer", id="48", type="Start"
	public void processStart(MessageForm message)
	{
		double dur = this.rndTransferDuration.sample();
		Carpenter c = ((TechStepMessage)message).getCarpenter();
		if (this.mySim().animatorExists())
			transferAnimation(c, dur);
		c.setCurrentDeskID(Carpenter.TRANSFER_STORAGE);
		message.setCode(Mc.storageTransfer);
		this.hold(dur, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.storageTransfer:
				this.assistantFinished(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
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

	private void transferAnimation(Carpenter c, double dur) {
		FurnitureFactoryAnimation animHandler = ((MySimulation) this.mySim()).getAnimationHandler();
		Furniture f = c.getAssignedProduct();
		if (c.isInStorage()) {
			switch (c.getGroup()) {
				case A -> animHandler.moveCarpenterAToDesk(f.getDeskID(), c.getAnimatedEntity(), this.mySim().currentTime(), dur);
				case B -> animHandler.moveCarpenterBToDesk(f.getDeskID(), c.getAnimatedEntity(), this.mySim().currentTime(), dur);
				case C -> animHandler.moveCarpenterCToDesk(f.getDeskID(), c.getAnimatedEntity(), this.mySim().currentTime(), dur);
			}
		} else {
			animHandler.moveCarpenterAToStorage(c.getAnimatedEntity(), this.mySim().currentTime(), dur);
		}
	}
}