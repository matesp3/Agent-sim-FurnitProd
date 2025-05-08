package agents.agenttransfer.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.TriangularRNG;
import agents.agenttransfer.*;
import animation.FurnitureFactoryAnimation;
import common.Carpenter;
import simulation.*;
import OSPABA.Process;
import utils.SeedGen;

//meta! id="45"
public class ProcessDeskTransfer extends OSPABA.Process
{
	private final RNG<Double> rndTransferDuration = new TriangularRNG(120.0, 150.0, 500.0, SeedGen.getSeedRNG());

	public ProcessDeskTransfer(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentTransfer", id="46", type="Start"
	public void processStart(MessageForm message)
	{
		double dur = this.rndTransferDuration.sample();
		TechStepMessage tsMsg = (TechStepMessage) message;
		if (this.mySim().animatorExists())
			this.transferAnimation(tsMsg.getCarpenter(), dur);
		message.setCode(Mc.deskTransfer);
		this.hold(dur, message);
	}

	private void transferAnimation(Carpenter c, double dur) {
		FurnitureFactoryAnimation animHandler = ((MySimulation) this.mySim()).getAnimationHandler();
		animHandler.moveCarpenterToOtherDesk(c.getAssignedProduct().getDeskID(), c.getAnimatedEntity(),
				this.mySim().currentTime(), dur);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.deskTransfer:
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

}