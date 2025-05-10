package agents.agentgroupc.continualassistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import animation.AnimatedEntity;
import common.Furniture;
import simulation.*;
import agents.agentgroupc.*;
import utils.SeedGen;

//meta! id="100"
public class ProcessPaintcoat extends OSPABA.Process
{
	private EmpiricRNG rndTableLacquering = new EmpiricRNG(SeedGen.getSeedRNG(),
			new EmpiricPair(new UniformContinuousRNG(50.0*60, 70.0*60, SeedGen.getSeedRNG()), 0.1),
			new EmpiricPair(new UniformContinuousRNG(70.0*60, 150.0*60, SeedGen.getSeedRNG()), 0.6),
			new EmpiricPair(new UniformContinuousRNG(150.0*60, 200.0*60, SeedGen.getSeedRNG()), 0.3)
	);
	private RNG<Double> rndChairLacquering = new UniformContinuousRNG(40.0*60.0, 200.0*60.0, SeedGen.getSeedRNG());
	private RNG<Double> rndWardrobeLacquering = new UniformContinuousRNG(250.0*60.0, 560.0*60.0, SeedGen.getSeedRNG());

	public ProcessPaintcoat(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupC", id="101", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProductToProcess().setStepBT(this.mySim().currentTime());
		tsMsg.getProductToProcess().setStep(Furniture.TechStep.LACQUERING);
		tsMsg.setCode(Mc.stainingAndPaintcoat);
		switch (tsMsg.getProductToProcess().getProductType()) {
			case TABLE		-> this.hold((double) this.rndTableLacquering.sample(), tsMsg);
			case CHAIR		-> this.hold(this.rndChairLacquering.sample(), tsMsg);
			case WARDROBE	-> this.hold(this.rndWardrobeLacquering.sample(), tsMsg);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.stainingAndPaintcoat -> {
				TechStepMessage tsMsg = (TechStepMessage) message;
				tsMsg.getProductToProcess().setStepET(this.mySim().currentTime());
				this.assistantFinished(tsMsg);
			}
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
	public AgentGroupC myAgent()
	{
		return (AgentGroupC)super.myAgent();
	}

}