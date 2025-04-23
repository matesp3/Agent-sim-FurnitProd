package agents.agentgroupb.instantassistants;

import OSPABA.*;
import simulation.*;
import agents.agentgroupb.*;

//meta! id="151"
public class QueryAssignCarpenterB extends OSPABA.Query
{
	public QueryAssignCarpenterB(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentGroupB myAgent()
	{
		return (AgentGroupB)super.myAgent();
	}

}
