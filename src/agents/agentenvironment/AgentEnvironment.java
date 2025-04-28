package agents.agentenvironment;

import OSPABA.*;
import OSPStat.Stat;
import common.Order;
import simulation.*;
import agents.agentenvironment.continualassistants.*;



//meta! id="6"
public class AgentEnvironment extends OSPABA.Agent
{
	private final Stat avgTimeOrderCompletion = new Stat();
	private  int ordersCompleted = 0;

	public AgentEnvironment(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		this.addOwnMessage(Mc.orderArrival);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.ordersCompleted = 0;
		this.avgTimeOrderCompletion.clear();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerEnvironment(Id.managerEnvironment, mySim(), this);
		new SchedulerOrderArrival(Id.schedulerOrderArrival, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.orderCompleted);
	}
	//meta! tag="end"

	public void updateStats(Order o) {
		this.ordersCompleted++;
		this.avgTimeOrderCompletion.addSample(o.getCompletedAt() - o.getCreatedAt());
	}

	public int getOrdersCompleted() {
		return this.ordersCompleted;
	}

	public Stat getAvgTimeOrderCompletion() {
		return avgTimeOrderCompletion;
	}
}