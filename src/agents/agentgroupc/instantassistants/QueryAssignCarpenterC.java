package agents.agentgroupc.instantassistants;

import OSPABA.*;
import simulation.*;
import agents.agentgroupc.*;

//meta! id="156"
public class QueryAssignCarpenterC extends OSPABA.Query
{
	public QueryAssignCarpenterC(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentGroupC myAgent()
	{
		return (AgentGroupC)super.myAgent();
	}

}
