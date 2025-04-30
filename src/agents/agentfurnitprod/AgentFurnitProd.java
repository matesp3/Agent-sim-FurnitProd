package agents.agentfurnitprod;

import OSPABA.*;
import OSPStat.Stat;
import common.DeskAllocation;
import common.Order;
import simulation.*;
import utils.DoubleComp;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;


//meta! id="24"
public class AgentFurnitProd extends OSPABA.Agent
{
	private final Queue<Order> qUnstarted;
	private final Queue<Order> qStarted; // at least one furniture product is being processed
	private final Queue<TechStepMessage> qStaining;
	private final Queue<TechStepMessage> qAssembling;
	private final Queue<TechStepMessage> qFittings;
	private DeskAllocation deskManager;
	// statistics: 'WT' - WaitingTime
	private final Stat statProcBeginWT = new Stat();
	private final Stat statStainingWT = new Stat();
	private final Stat statAssemblingWT = new Stat();
	private final Stat statFittingsWT = new Stat();
	// statistics: 'QL' - QueueLength
	private final Stat statProcBeginQL = new Stat();
	private final Stat statStainingQL = new Stat();
	private final Stat statAssemblingQL = new Stat();
	private final Stat statFittingsQL = new Stat();

	public AgentFurnitProd(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// orderCmp is used just for beginning of furniture creating process
		Comparator<Order> orderCmp = (o1, o2) -> DoubleComp.compare(o1.getCreatedAt(), o2.getCreatedAt());
		this.qUnstarted = new PriorityBlockingQueue<>(50, orderCmp);
		this.qStarted = new PriorityBlockingQueue<>(50, orderCmp);

		// stepMsgCmp is used for products that have some tech step already executed -> in case of orderCreation equality, prev step ET is compared
		Comparator<TechStepMessage> stepMsgCmp = (o1, o2) -> {
            int cmp = DoubleComp.compare(o1.getProduct().getMyOrderCreatedAt(), o2.getProduct().getMyOrderCreatedAt());
            return cmp != 0 ? cmp : DoubleComp.compare(o1.getProduct().getStepET(), o2.getProduct().getStepET());
        };
		this.qStaining = new PriorityQueue<>(stepMsgCmp);
		this.qAssembling = new PriorityQueue<>(stepMsgCmp);
		this.qFittings = new PriorityQueue<>(stepMsgCmp);
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
		this.deskManager.freeAllDesks();
		// statistics: 'WT' - WaitingTime
		this.statProcBeginWT.clear();
		this.statStainingWT.clear();
		this.statAssemblingWT.clear();
		this.statFittingsWT.clear();
		// statistics: 'QL' - QueueLength
		this.statProcBeginQL.clear();
		this.statStainingQL.clear();
		this.statAssemblingQL.clear();
		this.statFittingsQL.clear();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerFurnitProd(Id.managerFurnitProd, mySim(), this);
		addOwnMessage(Mc.woodPrep);
		addOwnMessage(Mc.deskTransfer);
		addOwnMessage(Mc.storageTransfer);
		addOwnMessage(Mc.carving);
		addOwnMessage(Mc.assembling);
		addOwnMessage(Mc.assignCarpenterA);
		addOwnMessage(Mc.orderProcessingStart);
		addOwnMessage(Mc.assignCarpenterB);
		addOwnMessage(Mc.stainingAndPaintcoat);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"


	public Queue<Order> getQUnstarted() {
		return this.qUnstarted;
	}

	public Queue<Order> getQStarted() {
		return this.qStarted;
	}

	public Queue<TechStepMessage> getQStaining() {
		return this.qStaining;
	}

	public Queue<TechStepMessage> getQAssembling() {
		return this.qAssembling;
	}

	public Queue<TechStepMessage> getQFittings() {
		return this.qFittings;

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
		this.deskManager = new DeskAllocation(amount);
	}
}