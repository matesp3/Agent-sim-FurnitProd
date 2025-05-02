package agents.agentgroupc.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.agentgroupc.*;
import utils.SeedGen;

//meta! id="98"
public class ProcessStaining extends OSPABA.Process
{
	private RNG<Double> rndTableStaining = new UniformContinuousRNG(100.0*60.0, 480.0*60.0, SeedGen.getSeedRNG());
	private RNG<Double> rndChairStaining = new UniformContinuousRNG(90.0*60.0, 400.0*60.0, SeedGen.getSeedRNG());
	private RNG<Double> rndWardrobeStaining = new UniformContinuousRNG(300.0*60.0, 600.0*60.0, SeedGen.getSeedRNG());

	public ProcessStaining(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupC", id="99", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProductToProcess().setStepBT(this.mySim().currentTime());
		tsMsg.setCode(Mc.stainingAndPaintcoat);
		switch (tsMsg.getProductToProcess().getProductType()) {
			case TABLE		-> this.hold(this.rndTableStaining.sample(), tsMsg);
			case CHAIR		-> this.hold(this.rndChairStaining.sample(), tsMsg);
			case WARDROBE	-> this.hold(this.rndWardrobeStaining.sample(), tsMsg);
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