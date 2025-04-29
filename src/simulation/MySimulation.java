package simulation;

import OSPABA.*;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import OSPStat.Stat;
import agents.agentenvironment.continualassistants.SchedulerOrderArrival;
import agents.agenttransfer.*;
import agents.agentmodel.*;
import agents.agentenvironment.*;
import agents.agentgroupa.*;
import agents.agentgroupb.*;
import agents.agentgroupc.*;
import agents.agentfurnitprod.*;
import common.Furniture;
import common.Order;
import results.FurnitProdState;
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

	private final FurnitProdState simStateData = new FurnitProdState(0, 0);

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
		this.avgTimeOrderCompletion.addSample(this.agentEnvironment().getAvgTimeOrderCompletion().mean());
		this.avgCountOrdersCompleted.addSample(this.agentEnvironment().getOrdersCompleted());
//		 todo remaining stats updating
//		this.avgUtilizationA.addSample();
//		this.avgUtilizationB.addSample();
//		this.avgUtilizationC.addSample();
//		this.avgCountNotStarted.addSample();
//		this.avgCountPartiallyStarted.addSample();
//		this.avgCountStaining.addSample();
//		this.avgCountAssembling.addSample();
//		this.avgCountFitInstallation.addSample();
//		this.avgTimeInNotStarted.addSample();
//		this.avgTimeInPartiallyStarted.addSample();
//		this.avgTimeInStaining.addSample();
//		this.avgTimeInAssembling.addSample();
//		this.avgTimeInFitInstallation.addSample();

		SchedulerOrderArrival sch = (SchedulerOrderArrival) agentEnvironment().findAssistant(Id.schedulerOrderArrival);
		System.out.println("created: "+sch.getCreatedOrdersCount());
		System.out.println("completed: "+agentEnvironment().getOrdersCompleted());
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

	/**
	 * Creates new working places and the number of these places is specified by param {@code amount}.
	 * @param amount amount of working places for carpenters
	 */
	public void setAmountOfDesks(int amount) {
		this.agentFurnitProd().setAmountOfDesks(amount);
	}

	/**
	 * Creates {@code amountGroupA} carpenters for group A, {@code amountGroupB} carpenters for group B and
	 * {@code amountGroupC} carpenters for group C.
	 */
	public void setAmountOfCarpenters(int amountGroupA, int amountGroupB, int amountGroupC) {
		this.agentGroupA().setAmountOfCarpenters(amountGroupA);
		this.agentGroupB().setAmountOfCarpenters(amountGroupB);
		this.agentGroupC().setAmountOfCarpenters(amountGroupC);
		// for results --v
		this.simStateData.carpentersAllocation(amountGroupA, amountGroupB, amountGroupC);
	}

	public FurnitProdState getSimStateData() {
		this.updateSimStateModel();
		return this.simStateData;
	}

	private void updateSimStateModel() {
		this.simStateData.setExperimentNum(this.currentReplication());
		this.simStateData.setSimTime(this.currentTime());
		// carpenters
		this.simStateData.setModelsCarpentersA(this.agentGroupA().getAllocator().getCarpenters());
		this.simStateData.setModelsCarpentersB(this.agentGroupB().getAllocator().getCarpenters());
		this.simStateData.setModelsCarpentersC(this.agentGroupC().getAllocator().getCarpenters());
		// orders
		this.simStateData.clearProducts();
		for (Order o : this.agentFurnitProd().getQUnstarted()) {
			for (Furniture f : o.getProducts())
				this.simStateData.addToqUnstarted(f);
		}
		for (Order o : this.agentFurnitProd().getQStarted()) {
			for (Furniture f : o.getProducts())
				if (f.isUnstarted()) // order processing started, but some products are still unstarted
					this.simStateData.addToqStarted(f);
		}
		for (TechStepMessage ts : this.agentFurnitProd().getQStaining())
			this.simStateData.addToqStaining(ts.getProduct());
		for (TechStepMessage ts : this.agentFurnitProd().getQAssembling())
			this.simStateData.addToqAssembling(ts.getProduct());
		for (TechStepMessage ts : this.agentFurnitProd().getQFittings())
			this.simStateData.addToqFittings(ts.getProduct());
		// stats
		this.simStateData.setOrderTimeInSystem(this.getStatTimeOrderCompletion().mean());

		this.simStateData.setUnstartedCount(this.getStatCountNotStarted().mean());
		this.simStateData.setStartedCount(this.getStatCountPartiallyStarted().mean());
		this.simStateData.setStainingCount(this.getStatCountStaining().mean());
		this.simStateData.setAssemblingCount(this.getStatCountAssembling().mean());
		this.simStateData.setFittingsInstCount(this.getStatCountFitInstallation().mean());

		this.simStateData.setUnstartedTime(this.getStatTimeInNotStarted().mean());
		this.simStateData.setStartedTime(this.getStatTimeInPartiallyStarted().mean());
		this.simStateData.setStainingTime(this.getStatTimeInStaining().mean());
		this.simStateData.setAssemblingTime(this.getStatTimeInAssembling().mean());
		this.simStateData.setFittingInstTime(this.getStatTimeInFitInstallation().mean());
	}
}