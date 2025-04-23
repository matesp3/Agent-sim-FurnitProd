package simulation;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import OSPStat.Stat;
import agents.agenttransfer.*;
import agents.agentmodel.*;
import agents.agentenvironment.*;
import agents.agentgroupa.*;
import agents.agentgroupb.*;
import agents.agentgroupc.*;
import agents.agentfurnitprod.*;
import utils.SeedGen;


public class MySimulation extends OSPABA.Simulation
{
	private final Stat avgTimeOrderCompletion = new Stat();
	private final Stat avgCountOrdersCompleted = new Stat();

	private final Stat avgUtilizationA  = new Stat();
	private final Stat avgUtilizationB  = new Stat();
	private final Stat avgUtilizationC  = new Stat();

	private final Stat avgCountNotStarted = new Stat();
	private final Stat avgCountPartiallyStarted = new Stat();
	private final Stat avgCountStaining = new Stat();
	private final Stat avgCountAssembling = new Stat();
	private final Stat avgCountFitInstallation = new Stat();

	private final Stat avgTimeInNotStarted = new Stat();
	private final Stat avgTimeInPartiallyStarted = new Stat();
	private final Stat avgTimeInStaining = new Stat();
	private final Stat avgTimeInAssembling = new Stat();
	private final Stat avgTimeInFitInstallation = new Stat();

	public MySimulation()
	{
		init();
		// fit installation is one random phenomenon
		RNG<Double> rndFitInstDur = new UniformContinuousRNG(15.0, 25.0, SeedGen.getSeedRNG());
		this.agentGroupA().setFitInstGenerator(rndFitInstDur);
		this.agentGroupC().setFitInstGenerator(rndFitInstDur);

	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
		this.avgTimeOrderCompletion.clear();
		this.avgCountOrdersCompleted.clear();
		this.avgUtilizationA.clear();
		this.avgUtilizationB.clear();
		this.avgUtilizationC.clear();
		this.avgCountNotStarted.clear();
		this.avgCountPartiallyStarted.clear();
		this.avgCountStaining.clear();
		this.avgCountAssembling.clear();
		this.avgCountFitInstallation.clear();
		this.avgTimeInNotStarted.clear();
		this.avgTimeInPartiallyStarted.clear();
		this.avgTimeInStaining.clear();
		this.avgTimeInAssembling.clear();
		this.avgTimeInFitInstallation.clear();
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
		setAgentTransfer(new AgentTransfer(Id.agentTransfer, this, agentFurnitProd()));
		setAgentGroupA(new AgentGroupA(Id.agentGroupA, this, agentFurnitProd()));
		setAgentGroupB(new AgentGroupB(Id.agentGroupB, this, agentFurnitProd()));
		setAgentGroupC(new AgentGroupC(Id.agentGroupC, this, agentFurnitProd()));
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

	private AgentTransfer _agentTransfer;

public AgentTransfer agentTransfer()
	{ return _agentTransfer; }

	public void setAgentTransfer(AgentTransfer agentTransfer)
	{_agentTransfer = agentTransfer; }

	private AgentGroupA _agentGroupA;

public AgentGroupA agentGroupA()
	{ return _agentGroupA; }

	public void setAgentGroupA(AgentGroupA agentGroupA)
	{_agentGroupA = agentGroupA; }

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
	//meta! tag="end"


	public Stat getStatTimeOrderCompletion() {
		return avgTimeOrderCompletion;
	}

	public Stat getStatCountOrdersCompleted() {
		return avgCountOrdersCompleted;
	}

	public Stat getStatUtilizationA() {
		return avgUtilizationA;
	}

	public Stat getStatUtilizationB() {
		return avgUtilizationB;
	}

	public Stat getStatUtilizationC() {
		return avgUtilizationC;
	}

	public Stat getStatCountNotStarted() {
		return avgCountNotStarted;
	}

	public Stat getStatCountPartiallyStarted() {
		return avgCountPartiallyStarted;
	}

	public Stat getStatCountStaining() {
		return avgCountStaining;
	}

	public Stat getStatCountAssembling() {
		return avgCountAssembling;
	}

	public Stat getStatCountFitInstallation() {
		return avgCountFitInstallation;
	}

	public Stat getStatTimeInNotStarted() {
		return avgTimeInNotStarted;
	}

	public Stat getStatTimeInPartiallyStarted() {
		return avgTimeInPartiallyStarted;
	}

	public Stat getStatTimeInStaining() {
		return avgTimeInStaining;
	}

	public Stat getStatTimeInAssembling() {
		return avgTimeInAssembling;
	}

	public Stat getStatTimeInFitInstallation() {
		return avgTimeInFitInstallation;
	}
}