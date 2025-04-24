package agents.agentenvironment.continualassistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import OSPRNG.RNG;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import common.Furniture;
import common.Order;
import simulation.*;
import agents.agentenvironment.*;
import utils.DoubleComp;
import utils.Formatter;
import utils.SeedGen;

//meta! id="41"
public class SchedulerOrderArrival extends OSPABA.Scheduler
{
	private static final double THRESHOLD_TABLE = 50;
	private static final double THRESHOLD_CHAIR = THRESHOLD_TABLE+15;

	private final RNG<Double> rndGapDuration;
	private final RNG<Double> rndOrderType;
	private final RNG<Integer> rndOrderedProductsCount;
	private int nextOrderID;

	public SchedulerOrderArrival(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		// 2 arrivals per 1 hour, 1 arrival each 1800[s]
		this.rndGapDuration = new ExponentialRNG(3600.0 / 2, SeedGen.getSeedRNG());
		this.rndOrderType = new UniformContinuousRNG(0.0, 100.0, SeedGen.getSeedRNG()); // generates percentages of probability of order's type
		this.rndOrderedProductsCount = new UniformDiscreteRNG(1, 5, SeedGen.getSeedRNG());
		this.nextOrderID = 1;
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.nextOrderID = 1;
	}

	//meta! sender="AgentEnvironment", id="42", type="Start"
	public void processStart(MessageForm message)
	{
		double gap = this.rndGapDuration.sample();
		message.setCode(Mc.orderArrival);
		((OrderMessage)message).setOrder(this.generateNewOrder(this.mySim().currentTime()+gap));
		this.hold(gap, message); // update of simTime + message to yourself
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.orderArrival:
				double gap = this.rndGapDuration.sample();
				OrderMessage copyMsg = (OrderMessage)message.createCopy();
				copyMsg.setOrder(this.generateNewOrder(this.mySim().currentTime()+gap));
				this.hold(gap, copyMsg); // update of simTime + message to yourself
//				System.out.println(" >>> NEXT = "+Formatter.getStrDateTime(this.mySim().currentTime()+gap,8,6));
				// - - - - - - - - - - - - - - - -
				message.setAddressee(this.myAgent());
				this.notice(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentEnvironment myAgent()
	{
		return (AgentEnvironment)super.myAgent();
	}

	public int getCreatedOrdersCount() {
		return this.nextOrderID-1;
	}

	/**
	 * @return generated new order with generated ordered furniture products
	 * @param createdAt time of order creating
	 */
	private Order generateNewOrder(double createdAt) {
		Order order = new Order(this.nextOrderID++, createdAt);
		Furniture[] orderedProducts = new Furniture[this.rndOrderedProductsCount.sample()];
		for (int i = 0; i < orderedProducts.length; i++) {
			orderedProducts[i] = new Furniture(order,
					String.format("%d-%c", order.getOrderID(), (char)(65+i)), this.nextProductType()
			);
		}
		order.setProducts(orderedProducts);
		return order;
	}

	/**
	 * @return generated product type
	 */
	private Furniture.Type nextProductType() {
		double generated = this.rndOrderType.sample();
		return  (DoubleComp.compare(generated, THRESHOLD_TABLE) == -1)
				? Furniture.Type.TABLE : ((DoubleComp.compare(generated, THRESHOLD_CHAIR) == -1)
				? Furniture.Type.CHAIR : Furniture.Type.WARDROBE);
	}
}