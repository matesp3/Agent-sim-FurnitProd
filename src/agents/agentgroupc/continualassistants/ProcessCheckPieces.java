package agents.agentgroupc.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.TriangularRNG;
import simulation.*;
import agents.agentgroupc.*;
import OSPABA.Process;
import utils.SeedGen;

//meta! id="169"
public class ProcessCheckPieces extends OSPABA.Process
{
	private RNG<Double> rngCheckDur = new TriangularRNG(10*60.0, 25*60.0, 40*60.0, SeedGen.getSeedRNG());

	public ProcessCheckPieces(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupC", id="170", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProductToProcess().setStepBT(this.mySim().currentTime());
		tsMsg.setCode(Mc.checkPieces);
		this.hold(this.rngCheckDur.sample(), tsMsg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.checkPieces -> {
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
