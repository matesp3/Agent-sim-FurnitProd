package agents.agentgroupa.continualassistants;

import OSPABA.*;
import OSPRNG.RNG;
import contracts.IFittingsInstaller;
import simulation.*;
import agents.agentgroupa.*;

//meta! id="70"
public class ProcessFitInstA extends OSPABA.Process implements IFittingsInstaller
{
	private RNG<Double> rndFitInstA = null;

	public ProcessFitInstA(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroupA", id="71", type="Start"
	public void processStart(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProductToProcess().setStepBT(this.mySim().currentTime());
		tsMsg.setCode(Mc.fittingsInstallation);
		this.hold(this.rndFitInstA.sample(), tsMsg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.fittingsInstallation -> {
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
	public AgentGroupA myAgent()
	{
		return (AgentGroupA)super.myAgent();
	}

	@Override
	public void setFitInstGenerator(RNG<Double> durationGenerator) {
		this.rndFitInstA = durationGenerator;
	}
}