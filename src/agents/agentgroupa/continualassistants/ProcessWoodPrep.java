package agents.agentgroupa.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.TriangularRNG;
import common.Furniture;
import simulation.*;
import agents.agentgroupa.*;
import OSPABA.Process;
import utils.SeedGen;

//meta! id="62"
public class ProcessWoodPrep extends OSPABA.Process
{
	private final RNG<Double> rndPrepDuration = new TriangularRNG(300.0, 500.0, 900.0, SeedGen.getSeedRNG());

	public ProcessWoodPrep(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupA", id="63", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProduct().setStepBT(this.mySim().currentTime());
		tsMsg.setCode(Mc.woodPrep);
		this.hold(this.rndPrepDuration.sample(), tsMsg); // addressee is set automatically to itself
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.woodPrep:
				TechStepMessage tsMsg = (TechStepMessage) message;
				tsMsg.getProduct().setStepET(this.mySim().currentTime());
				this.assistantFinished(tsMsg);
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
	public AgentGroupA myAgent()
	{
		return (AgentGroupA)super.myAgent();
	}

}