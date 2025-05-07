package simulation;

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
import animation.FurnitureFactoryAnimation;
import common.Carpenter;
import common.Furniture;
import common.Order;
import results.FurnitProdRepStats;
import results.FurnitProdState;
import utils.SeedGen;

public class MySimulation extends OSPABA.Simulation
{
	public enum TIME_UNIT {
		SECONDS(1), MINUTES(60), HOURS(3600), DAYS(3600*8);
		private final double secs;

		TIME_UNIT(int secs) {
			this.secs = secs;
		}
	}
	private final Stat avgTimeOrderCompletion = new Stat();
	private final Stat avgCountOrdersCreated = new Stat();
	private final Stat avgCountOrdersCompleted = new Stat();
	private final Stat avgCountUsedDesks = new Stat();

	private final Stat avgUtilizationA  = new Stat();
	private final Stat avgUtilizationB  = new Stat();
	private final Stat avgUtilizationC  = new Stat();

	private final Stat avgCountUnsOrders = new Stat();
	private final Stat avgCountUnsProducts = new Stat();
	private final Stat avgCountStaining = new Stat();
	private final Stat avgCountAssembling = new Stat();
	private final Stat avgCountFitInstallation = new Stat();

	private final Stat avgTimeInUnsOrders = new Stat();
	private final Stat avgTimeInUnsProducts = new Stat();
	private final Stat avgTimeInStaining = new Stat();
	private final Stat avgTimeInAssembling = new Stat();
	private final Stat avgTimeInFitInstallation = new Stat();

	private final FurnitProdState simStateData = new FurnitProdState(0, 0);
	private final FurnitProdRepStats repResults = new FurnitProdRepStats(0);

	private FurnitureFactoryAnimation animationHandler = null;

	private TIME_UNIT timeUnit = TIME_UNIT.HOURS;

	public MySimulation()
	{
		init();
		// fit installation is one random phenomenon
		RNG<Double> rndFitInstDur = new UniformContinuousRNG(15.0, 25.0, SeedGen.getSeedRNG());
		this.agentGroupA().setFitInstGenerator(rndFitInstDur);
		this.agentGroupC().setFitInstGenerator(rndFitInstDur);

	}

	public TIME_UNIT getTimeUnit() {
		return this.timeUnit;
	}

	public void setTimeUnit(TIME_UNIT timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Override
	public void createAnimator() {
		super.createAnimator();
		if (!this.animatorExists())
			throw new RuntimeException("Animator should be already created, but isn't");
		// register all entities to animator here in one place
		this.animationHandler = new FurnitureFactoryAnimation(this.animator(), this.agentFurnitProd().getDeskManager().getAllDesksCount()); // desks are internally registered
		this.agentGroupC().registerEntities();
		this.agentGroupB().registerEntities();
		this.agentGroupA().registerEntities();
//		Carpenter c1 = new Carpenter(Carpenter.GROUP.A, 1, true);
//		Carpenter c2 = new Carpenter(Carpenter.GROUP.B, 2, true);
//		Carpenter c3 = new Carpenter(Carpenter.GROUP.C, 3, true);
//		c1.getAnimatedEntity().registerEntity(this.animator());
//		c2.getAnimatedEntity().registerEntity(this.animator());
//		c3.getAnimatedEntity().registerEntity(this.animator());
//		this.animationHandler.moveCarpenterToDesk(0, 0, c1.getAnimatedEntity());
//		this.animationHandler.moveCarpenterToDesk(6, 0, c2.getAnimatedEntity());
//		this.animationHandler.moveCarpenterToDesk(12, 0, c3.getAnimatedEntity());
//
//		Order o = new Order(24, 15475);
//		Furniture f1 = new Furniture(o, "1-A", Furniture.Type.WARDROBE, false, true);
//		Furniture f2 = new Furniture(o, "1-B", Furniture.Type.WARDROBE, false, true);
//		Furniture f3 = new Furniture(o, "1-C", Furniture.Type.TABLE, false, true);
//		this.animator().register(f1.getAnimatedEntity());
//		this.animator().register(f2.getAnimatedEntity());
//		this.animator().register(f3.getAnimatedEntity());
//		this.animationHandler.enqueueFurnitureInStorage(f1.getAnimatedEntity());
//		this.animationHandler.enqueueFurnitureInStorage(f2.getAnimatedEntity());
//		this.animationHandler.enqueueFurnitureInStorage(f3.getAnimatedEntity());
		
//		f1.setStep(Furniture.TechStep.WOOD_PREPARATION);
//		c1.receiveProduct(f1, 50);
//		f1.getAnimatedEntity().registerEntity(this.animator());
//		f2.getAnimatedEntity().registerEntity(this.animator());
//		f3.getAnimatedEntity().registerEntity(this.animator());
//		this.animationHandler.moveFurnitureOnDesk(0, 0, f1.getAnimatedEntity());
//		this.animationHandler.moveFurnitureOnDesk(6, 0, f2.getAnimatedEntity());
//		this.animationHandler.moveFurnitureOnDesk(12, 0, f3.getAnimatedEntity());
//		// - - - - - - - - - - - - - -- -
//		Carpenter c11 = new Carpenter(Carpenter.GROUP.A, 1, true);
//		Carpenter c12 = new Carpenter(Carpenter.GROUP.B, 2, true);
//		Carpenter c13 = new Carpenter(Carpenter.GROUP.C, 3, true);
//		Carpenter c14 = new Carpenter(Carpenter.GROUP.A, 1, true);
//		Carpenter c15 = new Carpenter(Carpenter.GROUP.B, 2, true);
//		Carpenter c16 = new Carpenter(Carpenter.GROUP.C, 3, true);
//		Carpenter c17 = new Carpenter(Carpenter.GROUP.A, 1, true);
//		Carpenter c18 = new Carpenter(Carpenter.GROUP.B, 2, true);
//		Carpenter c19 = new Carpenter(Carpenter.GROUP.C, 3, true);
//		Carpenter c20 = new Carpenter(Carpenter.GROUP.A, 1, true);
//		Carpenter c21 = new Carpenter(Carpenter.GROUP.B, 2, true);
//		Carpenter c22 = new Carpenter(Carpenter.GROUP.C, 3, true);
//		c11.getAnimatedEntity().registerEntity(this.animator());
//		c12.getAnimatedEntity().registerEntity(this.animator());
//		c13.getAnimatedEntity().registerEntity(this.animator());
//		c14.getAnimatedEntity().registerEntity(this.animator());
//		c15.getAnimatedEntity().registerEntity(this.animator());
//		c16.getAnimatedEntity().registerEntity(this.animator());
//		c17.getAnimatedEntity().registerEntity(this.animator());
//		c18.getAnimatedEntity().registerEntity(this.animator());
//		c19.getAnimatedEntity().registerEntity(this.animator());
//		c20.getAnimatedEntity().registerEntity(this.animator());
//		c21.getAnimatedEntity().registerEntity(this.animator());
//		c22.getAnimatedEntity().registerEntity(this.animator());
//		this.animationHandler.placeCarpenterAToStorage(c11.getAnimatedEntity());
//		this.animationHandler.placeCarpenterBToStorage(c12.getAnimatedEntity());
//		this.animationHandler.placeCarpenterCToStorage(c13.getAnimatedEntity());
//		this.animationHandler.placeCarpenterAToStorage(c14.getAnimatedEntity());
//		this.animationHandler.placeCarpenterBToStorage(c15.getAnimatedEntity());
//		this.animationHandler.placeCarpenterCToStorage(c16.getAnimatedEntity());
//		this.animationHandler.placeCarpenterAToStorage(c17.getAnimatedEntity());
//		this.animationHandler.placeCarpenterBToStorage(c18.getAnimatedEntity());
//		this.animationHandler.placeCarpenterCToStorage(c19.getAnimatedEntity());
//		this.animationHandler.placeCarpenterAToStorage(c20.getAnimatedEntity());
//		this.animationHandler.placeCarpenterBToStorage(c21.getAnimatedEntity());
//		this.animationHandler.placeCarpenterCToStorage(c22.getAnimatedEntity());
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
		this.avgTimeOrderCompletion.clear();
		this.avgCountOrdersCreated.clear();
		this.avgCountOrdersCompleted.clear();
		this.avgCountUsedDesks.clear();
		this.avgUtilizationA.clear();
		this.avgUtilizationB.clear();
		this.avgUtilizationC.clear();
		this.avgCountUnsOrders.clear();
		this.avgCountUnsProducts.clear();
		this.avgCountStaining.clear();
		this.avgCountAssembling.clear();
		this.avgCountFitInstallation.clear();
		this.avgTimeInUnsOrders.clear();
		this.avgTimeInUnsProducts.clear();
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
		this.avgCountOrdersCreated.addSample(this.agentEnvironment().getOrdersCreated());
		this.avgCountOrdersCompleted.addSample(this.agentEnvironment().getOrdersCompleted());
		this.avgCountUsedDesks.addSample(this.agentFurnitProd().getStatUsedDesksCount().mean());

		this.avgUtilizationA.addSample(this.agentGroupA().getGroupUtilization());
		this.avgUtilizationB.addSample(this.agentGroupB().getGroupUtilization());
		this.avgUtilizationC.addSample(this.agentGroupC().getGroupUtilization());

		this.avgCountUnsOrders.addSample(this.agentFurnitProd().getStatUnsOrdersQL().mean());
		this.avgCountUnsProducts.addSample(this.agentFurnitProd().getStatUnsProductsQL().mean());
		this.avgCountStaining.addSample(this.agentFurnitProd().getStatStainingQL().mean());
		this.avgCountAssembling.addSample(this.agentFurnitProd().getStatAssemblingQL().mean());
		this.avgCountFitInstallation.addSample(this.agentFurnitProd().getStatFittingsQL().mean());

		this.avgTimeInUnsOrders.addSample(this.agentFurnitProd().getStatUnsOrdersWT().mean());
		this.avgTimeInUnsProducts.addSample(this.agentFurnitProd().getStatUnsProductsWT().mean());
		this.avgTimeInStaining.addSample(this.agentFurnitProd().getStatStainingWT().mean());
		this.avgTimeInAssembling.addSample(this.agentFurnitProd().getStatAssemblingWT().mean());
		this.avgTimeInFitInstallation.addSample(this.agentFurnitProd().getStatFittingsWT().mean());
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

//	public Stat getStatCountOrdersCompleted() {
//		return avgCountOrdersCompleted;
//	}

	public Stat getStatUtilizationA() {
		return avgUtilizationA;
	}

	public Stat getStatUtilizationB() {
		return avgUtilizationB;
	}

	public Stat getStatUtilizationC() {
		return avgUtilizationC;
	}

	public Stat getStatCountUnsOrders() {
		return avgCountUnsOrders;
	}

	public Stat getStatCountUnstartedProducts() {
		return avgCountUnsProducts;
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

	public Stat getStatTimeInUnstartedOrders() {
		return avgTimeInUnsOrders;
	}

	public Stat getStatTimeInUnstartedProducts() {
		return avgTimeInUnsProducts;
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

	public FurnitProdRepStats getReplicationResults() {
		this.updateRepResults();
		return this.repResults;
	}

	public FurnitProdState getSimStateData() {
		this.updateSimStateModel();
		return this.simStateData;
	}

	public FurnitureFactoryAnimation getAnimationHandler() {
		return this.animationHandler;
	}

	private void updateRepResults() {
		this.repResults.setExperimentNum(this.currentReplication());

		this.repResults.setOrderTimeInSystem(this.avgTimeOrderCompletion);
		this.repResults.setAvgCreatedOrdersCount(this.avgCountOrdersCreated);
		this.repResults.setAvgCompletedOrdersCount(this.avgCountOrdersCompleted);
		this.repResults.setAvgUsedDesksCount(this.avgCountUsedDesks);

		this.repResults.setUtilizationGroupA(this.avgUtilizationA);
		this.repResults.setUtilizationGroupB(this.avgUtilizationB);
		this.repResults.setUtilizationGroupC(this.avgUtilizationC);
		this.repResults.setUnsOrdersCount(this.avgCountUnsOrders);
		this.repResults.setUnsProductsCount(this.avgCountUnsProducts);
		this.repResults.setStainingCount(this.avgCountStaining);
		this.repResults.setAssemblingCount(this.avgCountAssembling);
		this.repResults.setFittingsCount(this.avgCountFitInstallation);

		this.repResults.setUnsOrdersTime(this.avgTimeInUnsOrders);
		this.repResults.setUnsProductsTime(this.avgTimeInUnsProducts);
		this.repResults.setStainingTime(this.avgTimeInStaining);
		this.repResults.setAssemblingTime(this.avgTimeInAssembling);
		this.repResults.setFittingsTime(this.avgTimeInFitInstallation);
	}

	private void updateSimStateModel() {
		this.simStateData.setExperimentNum(this.currentReplication());
		this.simStateData.setSimTime(this.currentTime());

		this.simStateData.setCurrentlyCreatedOrders(this.agentEnvironment().getOrdersCreated());
		this.simStateData.setCurrentlyCompletedOrders(this.agentEnvironment().getOrdersCompleted());
		this.simStateData.setCurrentlyUsedDesks(this.agentFurnitProd().getUsedDesksCount());
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
		for (Furniture f : this.agentFurnitProd().getQStaining())
			this.simStateData.addToqStaining(f);
		for (Furniture f : this.agentFurnitProd().getQAssembling())
			this.simStateData.addToqAssembling(f);
		for (Furniture f : this.agentFurnitProd().getQFittings())
			this.simStateData.addToqFittings(f);
		// stats
		this.simStateData.setOrderTimeInSystem(this.agentEnvironment().getAvgTimeOrderCompletion().mean());
		this.simStateData.setUsedDesksCount(this.agentFurnitProd().getStatUsedDesksCount().mean());

		this.simStateData.setUnsOrdersCount(this.agentFurnitProd().getStatUnsOrdersQL().mean());
		this.simStateData.setUnsProductsCount(this.agentFurnitProd().getStatUnsProductsQL().mean());
		this.simStateData.setStainingCount(this.agentFurnitProd().getStatStainingQL().mean());
		this.simStateData.setAssemblingCount(this.agentFurnitProd().getStatAssemblingQL().mean());
		this.simStateData.setFittingsInstCount(this.agentFurnitProd().getStatFittingsQL().mean());

		this.simStateData.setUnsOrdersTime(this.agentFurnitProd().getStatUnsOrdersWT().mean());
		this.simStateData.setUnsProductsTime(this.agentFurnitProd().getStatUnsProductsWT().mean());
		this.simStateData.setStainingTime(this.agentFurnitProd().getStatStainingWT().mean());
		this.simStateData.setAssemblingTime(this.agentFurnitProd().getStatAssemblingWT().mean());
		this.simStateData.setFittingInstTime(this.agentFurnitProd().getStatFittingsWT().mean());

		this.simStateData.setUtilzA(this.agentGroupA().getGroupUtilization()*100);
		this.simStateData.setUtilzB(this.agentGroupB().getGroupUtilization()*100);
		this.simStateData.setUtilzC(this.agentGroupC().getGroupUtilization()*100);
	}
}