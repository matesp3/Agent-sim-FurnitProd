package agents.agentfurnitprod;

import OSPABA.*;
import OSPAnimator.IAnimator;
import OSPStat.Stat;
import OSPStat.WStat;
import animation.FurnitureFactoryAnimation;
import common.DeskAllocation;
import common.Furniture;
import common.Order;
import contracts.IAgentWithEntity;
import simulation.*;
import utils.DoubleComp;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;


//meta! id="24"
public class AgentFurnitProd extends OSPABA.Agent implements IAgentWithEntity
{
	private final Queue<Order> qUnstarted;
	private final Queue<Order> qStarted; // at least one furniture product is being processed
	private final Queue<Furniture> qStaining;
	private final Queue<Furniture> qAssembling;
	private final Queue<Furniture> qFittings;
	private final Queue<Furniture> qChecks;
	private DeskAllocation deskManager;
	// statistics: 'WT' - WaitingTime
	private final Stat statUnsOrdersWT = new Stat();
	private final Stat statUnsProductsWT = new Stat();
	private final Stat statStainingWT = new Stat();
	private final Stat statAssemblingWT = new Stat();
	private final Stat statFittingsWT = new Stat();
	private final Stat statChecksWT = new Stat();
	// statistics: 'QL' - QueueLength
	private final WStat statUnsOrdersQL = new WStat(this.mySim());
	private final WStat statUnsProductsQL = new WStat(this.mySim());
	private final WStat statStainingQL = new WStat(this.mySim());
	private final WStat statAssemblingQL = new WStat(this.mySim());
	private final WStat statFittingsQL = new WStat(this.mySim());
	private final WStat statChecksQL = new WStat(this.mySim());

	public AgentFurnitProd(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// orderCmp is used just for beginning of furniture creating process
		Comparator<Order> orderCmp = (o1, o2) -> DoubleComp.compare(o1.getCreatedAt(), o2.getCreatedAt());
		this.qUnstarted = new PriorityBlockingQueue<>(50, orderCmp);
		this.qStarted = new PriorityBlockingQueue<>(50, orderCmp);

		// furnitureCmp is used for products that have some tech step already executed -> in case of orderCreation equality, prev step ET is compared
		Comparator<Furniture> furnitureCmp = (f1, f2) -> {
            int cmp = DoubleComp.compare(f1.getMyOrderCreatedAt(), f2.getMyOrderCreatedAt());
            return cmp != 0 ? cmp : DoubleComp.compare(f1.getStepET(), f2.getStepET());
        };
		this.qStaining = new PriorityBlockingQueue<>(50, furnitureCmp);
		this.qAssembling = new PriorityBlockingQueue<>(50, furnitureCmp);
		this.qFittings = new PriorityBlockingQueue<>(50, furnitureCmp);
		this.qChecks = new PriorityBlockingQueue<>(50, furnitureCmp);
		this.deskManager = null;
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.qUnstarted.clear();
		this.qStarted.clear();
		this.qStaining.clear();
		this.qAssembling.clear();
		this.qFittings.clear();
		this.qChecks.clear();
		this.deskManager.freeAllDesks();
		// statistics: 'WT' - WaitingTime
		this.statUnsOrdersWT.clear();
		this.statUnsProductsWT.clear();
		this.statStainingWT.clear();
		this.statAssemblingWT.clear();
		this.statFittingsWT.clear();
		this.statChecksWT.clear();
		// statistics: 'QL' - QueueLength
		this.statUnsOrdersQL.clear();
		this.statUnsProductsQL.clear();
		this.statStainingQL.clear();
		this.statAssemblingQL.clear();
		this.statFittingsQL.clear();
		this.statChecksQL.clear();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerFurnitProd(Id.managerFurnitProd, mySim(), this);
		addOwnMessage(Mc.deskTransfer);
		addOwnMessage(Mc.woodPrep);
		addOwnMessage(Mc.storageTransfer);
		addOwnMessage(Mc.carving);
		addOwnMessage(Mc.checkPieces);
		addOwnMessage(Mc.assembling);
		addOwnMessage(Mc.assignCarpenterA);
		addOwnMessage(Mc.assignCarpenterB);
		addOwnMessage(Mc.orderProcessingStart);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.stainingAndPaintcoat);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"


	public Queue<Order> getQUnstarted() {
		return this.qUnstarted;
	}

	public Queue<Order> getQStarted() {
		return this.qStarted;
	}

	public Queue<Furniture> getQStaining() {
		return this.qStaining;
	}

	public Queue<Furniture> getQAssembling() {
		return this.qAssembling;
	}

	public Queue<Furniture> getQFittings() {
		return this.qFittings;
	}

	public Queue<Furniture> getQChecks() {
		return this.qChecks;
	}

	/**
	 * @return waiting time of unstarted orders
	 */
	public Stat getStatUnsOrdersWT() {
		return statUnsOrdersWT;
	}

	/**
	 * @return waiting time of unstarted products
	 */
	public Stat getStatUnsProductsWT() {
		return statUnsProductsWT;
	}

	/**
	 * @return waiting time for staining
	 */
	public Stat getStatStainingWT() {
		return statStainingWT;
	}

	/**
	 * @return waiting time for assembling
	 */
	public Stat getStatAssemblingWT() {
		return statAssemblingWT;
	}

	/**
	 * @return waiting time for fittings installation
	 */
	public Stat getStatFittingsWT() {
		return statFittingsWT;
	}

	public Stat getStatChecksWT() {
		return statChecksWT;
	}

	/**
	 * @return unstarted orders queue length
	 */
	public WStat getStatUnsOrdersQL() {
		return statUnsOrdersQL;
	}

	/**
	 * @return unstarted products queue length
	 */
	public WStat getStatUnsProductsQL() {
		return statUnsProductsQL;
	}

	/**
	 * @return waiting for staining queue length
	 */
	public WStat getStatStainingQL() {
		return statStainingQL;
	}

	/**
	 * @return waiting for assembling queue length
	 */
	public WStat getStatAssemblingQL() {
		return statAssemblingQL;
	}

	/**
	 * @return waiting for fittings installation queue length
	 */
	public WStat getStatFittingsQL() {
		return statFittingsQL;
	}

	public WStat getStatChecksQL() {
		return statChecksQL;
	}

	public DeskAllocation getDeskManager() {
		return this.deskManager;
	}

	/**
	 * Creates new working places and the number of these places is specified by param {@code amount}.
	 * @param amount amount of working places for carpenters
	 */
	public void setAmountOfDesks(int amount) {
		if (amount < 1)
			throw new IllegalArgumentException("Amount of desks must be at least 1.");
		this.deskManager = new DeskAllocation(amount, new WStat(this.mySim()));
	}

	/**
	 * @return number of how many desks is currently being used.
	 */
	public int getUsedDesksCount() {
		return this.deskManager.getUsedDesksCount();
	}

	public WStat getStatUsedDesksCount() {
		return this.deskManager.getStatUsedDesksCount();
	}

	@Override
	public void registerEntities() {
		IAnimator animator = this.mySim().animator();
		FurnitureFactoryAnimation animHandler = ((MySimulation)this.mySim()).getAnimationHandler();
		this.deskManager.registerDesks(animHandler);
		for (Order o : this.qStarted)
			o.registerUnstarted(animHandler);
		for (Order o : this.qUnstarted)
			o.registerUnstarted(animHandler);
	}

	@Override
	public void unregisterEntities() {
		this.deskManager.unregisterDesks(this.mySim().animator());
//		for (Order o : this.qStarted)
//			o.unregisterUnstarted(this.mySim().animator());
//		for (Order o : this.qUnstarted)
//			o.unregisterUnstarted(this.mySim().animator());
	}

}