package agents.agentfurnitprod;

import OSPABA.*;
import common.Order;
import simulation.*;

import java.util.LinkedList;
import java.util.Queue;


//meta! id="24"
public class AgentFurnitProd extends OSPABA.Agent
{
	private Queue<OrderMessage> queueA;

	public AgentFurnitProd(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		this.queueA = new LinkedList<OrderMessage>();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerFurnitProd(Id.managerFurnitProd, mySim(), this);
		addOwnMessage(Mc.deskTransfer);
		addOwnMessage(Mc.prepAndCarving);
		addOwnMessage(Mc.orderProcessing);
		addOwnMessage(Mc.storageTransfer);
		addOwnMessage(Mc.assembling);
		addOwnMessage(Mc.assignCarpenterA);
		addOwnMessage(Mc.assignCarpenterB);
		addOwnMessage(Mc.assignCarpenterC);
		addOwnMessage(Mc.stainingAndPaintcoat);
		addOwnMessage(Mc.fittingsInstallation);
	}
	//meta! tag="end"


	public Queue<OrderMessage> getQueueA() {
		return this.queueA;
	}
}