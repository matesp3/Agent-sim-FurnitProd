package agents.agentfurnitprod;

import OSPABA.*;
import common.DeskAllocation;
import simulation.*;
import utils.DoubleComp;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


//meta! id="24"
public class AgentFurnitProd extends OSPABA.Agent
{
	private final Queue<OrderMessage> qUnprocessed;
	private final Queue<OrderMessage> qReceived; // at least one furniture product is being processed
	private final Queue<TechStepMessage> qStaining;
	private final Queue<TechStepMessage> qAssembling;
	private final Queue<TechStepMessage> qFittings;
	private DeskAllocation deskManager;

	public AgentFurnitProd(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// orderMsgCmp is used just for beginning of furniture creating process
		Comparator<OrderMessage> orderMsgCmp = (o1, o2) -> DoubleComp.compare(o1.getOrder().getCreatedAt(), o2.getOrder().getCreatedAt());
		this.qUnprocessed = new PriorityQueue<>(orderMsgCmp);
		this.qReceived = new PriorityQueue<>(orderMsgCmp);

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
		this.qUnprocessed.clear();
		this.qReceived.clear();
		this.qStaining.clear();
		this.qAssembling.clear();
		this.qFittings.clear();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerFurnitProd(Id.managerFurnitProd, mySim(), this);
		addOwnMessage(Mc.woodPrep);
		addOwnMessage(Mc.deskTransfer);
		addOwnMessage(Mc.orderProcessing);
		addOwnMessage(Mc.storageTransfer);
		addOwnMessage(Mc.carving);
		addOwnMessage(Mc.assembling);
		addOwnMessage(Mc.assignCarpenterA);
		addOwnMessage(Mc.assignCarpenterB);
		addOwnMessage(Mc.stainingAndPaintcoat);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"


	public Queue<OrderMessage> getqUnprocessed() {
		return this.qUnprocessed;
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