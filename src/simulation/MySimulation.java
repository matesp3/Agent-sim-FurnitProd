package simulation;

import OSPABA.*;
import agents.agenttransfer.*;
import agents.agentmodel.*;
import agents.agentenvironment.*;
import agents.agentgroupb.*;
import agents.agentgroupc.*;
import agents.agentfurnitprod.*;

public class MySimulation extends OSPABA.Simulation
{
	public MySimulation()
	{
		init();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
	}

	@Override
	public void simulationFinished()
	{
		// Display simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModel(new AgentModel(Id.agentModel, this, null));
		setAgentEnvironment(new AgentEnvironment(Id.agentEnvironment, this, agentModel()));
		setAgentFurnitProd(new AgentFurnitProd(Id.agentFurnitProd, this, agentModel()));
		setAgentGroupB(new AgentGroupB(Id.agentGroupB, this, agentFurnitProd()));
		setAgentGroupC(new AgentGroupC(Id.agentGroupC, this, agentFurnitProd()));
		setAgentTransfer(new AgentTransfer(Id.agentTransfer, this, agentFurnitProd()));
	}

	private AgentModel _agentModel;

public AgentModel agentModel()
	{ return _agentModel; }

	public void setAgentModel(AgentModel agentModel)
	{_agentModel = agentModel; }

	private AgentEnvironment _agentEnvironment;

public AgentEnvironment agentEnvironment()
	{ return _agentEnvironment; }

	public void setAgentEnvironment(AgentEnvironment agentEnvironment)
	{_agentEnvironment = agentEnvironment; }

	private AgentFurnitProd _agentFurnitProd;

public AgentFurnitProd agentFurnitProd()
	{ return _agentFurnitProd; }

	public void setAgentFurnitProd(AgentFurnitProd agentFurnitProd)
	{_agentFurnitProd = agentFurnitProd; }

	private AgentGroupB _agentGroupB;

public AgentGroupB agentGroupB()
	{ return _agentGroupB; }

	public void setAgentGroupB(AgentGroupB agentGroupB)
	{_agentGroupB = agentGroupB; }

	private AgentGroupC _agentGroupC;

public AgentGroupC agentGroupC()
	{ return _agentGroupC; }

	public void setAgentGroupC(AgentGroupC agentGroupC)
	{_agentGroupC = agentGroupC; }

	private AgentTransfer _agentTransfer;

public AgentTransfer agentTransfer()
	{ return _agentTransfer; }

	public void setAgentTransfer(AgentTransfer agentTransfer)
	{_agentTransfer = agentTransfer; }
	//meta! tag="end"
}
