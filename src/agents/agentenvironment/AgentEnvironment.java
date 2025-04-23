package agents.agentenvironment;

import OSPABA.*;
import OSPStat.Stat;
import common.Furniture;
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

	public void orderCompleted(Furniture o) {
		this.ordersCompleted++;
		this.avgTimeOrderCompletion.addSample(o.getTimeCompleted()-o.getProcessingBT());
	}

	public int getOrdersCompleted() {
		return ordersCompleted;
	}

	public Stat getAvgTimeOrderCompletion() {
		return avgTimeOrderCompletion;
	}
}