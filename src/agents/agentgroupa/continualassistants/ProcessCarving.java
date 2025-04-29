package agents.agentgroupa.continualassistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.agentgroupa.*;
import OSPABA.Process;
import utils.SeedGen;

//meta! id="67"
public class ProcessCarving extends OSPABA.Process
{
	private final EmpiricRNG rndCarvingTable = new EmpiricRNG(SeedGen.getSeedRNG(),
			new EmpiricPair(new UniformContinuousRNG(10.0*60, 25.0*60, SeedGen.getSeedRNG()), 0.6),
			new EmpiricPair(new UniformContinuousRNG(25.0*60, 50.0*60, SeedGen.getSeedRNG()), 0.4)
	);
	private final RNG<Double> rndCarvingChair = new UniformContinuousRNG(12.0*60, 16.0*60, SeedGen.getSeedRNG());
	private final RNG<Double> rndCarvingWardrobe = new UniformContinuousRNG(15.0*60, 80.0*60, SeedGen.getSeedRNG());

	public ProcessCarving(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupA", id="68", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProduct().setStepBT(this.mySim().currentTime());
		tsMsg.setCode(Mc.carving);
		switch (tsMsg.getProduct().getProductType()) {
			case TABLE		-> this.hold((Double) this.rndCarvingTable.sample(), tsMsg);
			case CHAIR		-> this.hold(this.rndCarvingChair.sample(), tsMsg);
			case WARDROBE	-> this.hold(this.rndCarvingWardrobe.sample(), tsMsg);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.carving -> {
				TechStepMessage tsMsg = (TechStepMessage) message;
				tsMsg.getProduct().setStepET(this.mySim().currentTime());
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
	public AgentGroupA myAgent()
	{
		return (AgentGroupA)super.myAgent();
	}

}