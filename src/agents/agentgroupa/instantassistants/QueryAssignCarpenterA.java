package agents.agentgroupa.instantassistants;

import OSPABA.*;
import simulation.*;
import agents.agentgroupa.*;

//meta! id="149"
public class QueryAssignCarpenterA extends OSPABA.Query
{
	public QueryAssignCarpenterA(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentGroupA myAgent()
	{
		return (AgentGroupA)super.myAgent();
	}

}
