package agents.agentgroupb.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.agentgroupb.*;
import OSPABA.Process;
import utils.SeedGen;

//meta! id="82"
public class ProcessAssembling extends OSPABA.Process
{
	private final RNG<Double> rndTableAssembling = new UniformContinuousRNG(30.0*60.0, 60.0*60.0, SeedGen.getSeedRNG());
	private final RNG<Double> rndChairAssembling = new UniformContinuousRNG(14.0*60.0, 24.0*60.0, SeedGen.getSeedRNG());
	private final RNG<Double> rndWardrobeAssembling = new UniformContinuousRNG(35.0*60.0, 75.0*60.0, SeedGen.getSeedRNG());

	public ProcessAssembling(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupB", id="83", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProductToProcess().setStepBT(this.mySim().currentTime());
		tsMsg.setCode(Mc.assembling);
		switch (tsMsg.getProductToProcess().getProductType()) {
			case TABLE		-> this.hold(this.rndTableAssembling.sample(), tsMsg);
			case CHAIR		-> this.hold(this.rndChairAssembling.sample(), tsMsg);
			case WARDROBE	-> this.hold(this.rndWardrobeAssembling.sample(), tsMsg);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.assembling -> {
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
	public AgentGroupB myAgent()
	{
		return (AgentGroupB)super.myAgent();
	}

}