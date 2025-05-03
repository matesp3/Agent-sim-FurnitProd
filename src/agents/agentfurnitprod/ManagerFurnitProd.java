package agents.agentfurnitprod;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import common.Carpenter;
import common.Furniture;
import common.Order;
import simulation.*;

import java.util.Queue;

import static common.Carpenter.GROUP.A;
import static common.Furniture.TechStep.*;

//meta! id="24"
public class ManagerFurnitProd extends OSPABA.Manager
{
	private final AssignMessage assignMsgPattern;
	private final TechStepMessage stepMsgPattern;
	private final OrderMessage orderMsgPattern;

	private AssignMessage freeAssignMsgInstance;
	private TechStepMessage freeStepMsgInstance;
	private OrderMessage freeOrderMsgInstance;

	private int unsProductsCount;

	public ManagerFurnitProd(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
		this.assignMsgPattern = new AssignMessage(this.mySim());
		this.stepMsgPattern = new TechStepMessage(this.mySim());

		this.orderMsgPattern = new OrderMessage(this.mySim());
		this.orderMsgPattern.setCode(Mc.orderProcessingEnd);
		this.orderMsgPattern.setAddressee(Id.agentModel);
		this.unsProductsCount = 0;
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
		this.unsProductsCount = 0;
	}

	//meta! sender="AgentTransfer", id="35", type="Response"
	public void processDeskTransfer(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		switch (tsMsg.getProductToProcess().getStep()) {
			case STAINING			-> this.sendTechStepRequest(Mc.stainingAndPaintcoat, Id.agentGroupC, tsMsg);
			case ASSEMBLING			-> this.sendTechStepRequest(Mc.assembling, Id.agentGroupB, tsMsg);
			case FIT_INSTALLATION	-> this.sendTechStepRequest(Mc.fittingsInstallation,
					tsMsg.getCarpenter().getGroup() == A ? Id.agentGroupA : Id.agentGroupC, tsMsg);
			default					-> throw new RuntimeException("Invalid tech step or null");
		}
	}

	//meta! sender="AgentGroupA", id="57", type="Response"
	public void processWoodPrep(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getProductToProcess().setStep(CARVING);
		this.sendStorageTransferRequest(tsMsg);
	}

	//meta! sender="AgentTransfer", id="33", type="Response"
	public void processStorageTransfer(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		switch (tsMsg.getProductToProcess().getStep()) {
			case WOOD_PREPARATION:
				this.sendTechStepRequest(Mc.woodPrep, Id.agentGroupA, tsMsg);
				break;
			case CARVING:
				this.sendTechStepRequest(Mc.carving, Id.agentGroupA, tsMsg);
				break;
			case STAINING:
				this.sendTechStepRequest(Mc.stainingAndPaintcoat, Id.agentGroupC, tsMsg);
				break;
			case ASSEMBLING:
				this.sendTechStepRequest(Mc.assembling, Id.agentGroupB, tsMsg);
				break;
			case FIT_INSTALLATION:
				this.sendTechStepRequest(Mc.fittingsInstallation,
						tsMsg.getCarpenter().getGroup() == A ? Id.agentGroupA : Id.agentGroupC, tsMsg);
				break;
			default:
				throw new RuntimeException("Unknown tech step or null");
		}
	}

	//meta! sender="AgentGroupB", id="79", type="Response"
	public void processAssembling(MessageForm message)
	{
		// CONTINUE WORKING ON CURRENT PRODUCT WITH NEXT STEP (FITTINGS INST.) OR END OF CREATING
		TechStepMessage tsMsg = (TechStepMessage) message;
		Furniture f = tsMsg.getCarpenter().returnProduct(this.mySim().currentTime());
		if (f.getProductType() == Furniture.Type.WARDROBE) {
			f.setStep(FIT_INSTALLATION);
			this.sendAssignRequestForProduct(Mc.assignCarpenterA, Id.agentGroupA, f); // carpenter A has priority
		}
		else {
			this.noticeIfCompleted(f);
		}
		// PLAN NEXT JOB FOR CARPENTER 'B'
		this.tryAssignNextWorkForCarpenterB(tsMsg);
	}

	//meta! sender="AgentGroupA", id="54", type="Response"
	public void processAssignCarpenterA(MessageForm message)
	{
		// 2 possibilities: two types -> typecasting ; or additional attribute in one class {due to order BT | tech step)
		AssignMessage assignMsg = (AssignMessage) message;
		if (assignMsg.getProduct() == null) { // 1. option - product beginning - need to assign product to carpenter
			this.processAssignedCarpenterForNewFurniture(assignMsg);
			return;
		}
		// 2. option - step == FIT_INST must be true
		if (assignMsg.getCarpenter() == null) {
			// now, try to assign carpenter C
			AssignMessage newAssignMsg = (AssignMessage) assignMsg.createCopy();
			newAssignMsg.setCode(Mc.assignCarpenterC);
			newAssignMsg.setAddressee(Id.agentGroupC);
			this.request(newAssignMsg);
			return;
		}
		this.myAgent().getStatFittingsWT().addSample(0);
		this.startNextStep(assignMsg.getCarpenter(), assignMsg.getProduct(), Mc.fittingsInstallation, Id.agentGroupA);
	}

	//meta! sender="AgentGroupB", id="77", type="Response"
	public void processAssignCarpenterB(MessageForm message)
	{
		AssignMessage asgMsg = (AssignMessage) message;
		if (asgMsg.getCarpenter() == null) { // ENQUEUE
			this.enqueueProduct(asgMsg.getProduct(), this.myAgent().getQAssembling(), this.myAgent().getStatAssemblingQL());
			return;
		}
		// START WORK
		this.myAgent().getStatAssemblingWT().addSample(0);
		this.startNextStep(asgMsg.getCarpenter(), asgMsg.getProduct(), Mc.assembling, Id.agentGroupB);
	}

	//meta! sender="AgentGroupC", id="88", type="Response"
	public void processAssignCarpenterC(MessageForm message)
	{
		AssignMessage asgMsg = (AssignMessage) message;
		if (asgMsg.getCarpenter() == null) { // ENQUEUE
			if (asgMsg.getProduct().getStep() == STAINING)
				this.enqueueProduct(asgMsg.getProduct(), this.myAgent().getQStaining(), this.myAgent().getStatStainingQL());
			else 						//	  == FIT_INST
				this.enqueueProduct(asgMsg.getProduct(), this.myAgent().getQFittings(), this.myAgent().getStatFittingsQL());
			return;
		}
		// START WORK
		if (asgMsg.getProduct().getStep() == STAINING) {
			this.myAgent().getStatStainingWT().addSample(0);
			this.startNextStep(asgMsg.getCarpenter(), asgMsg.getProduct(), Mc.stainingAndPaintcoat, Id.agentGroupC);
		}
		else {                        //	  == FIT_INST
			this.myAgent().getStatFittingsWT().addSample(0);
			this.startNextStep(asgMsg.getCarpenter(), asgMsg.getProduct(), Mc.fittingsInstallation, Id.agentGroupC);
		}
	}

	//meta! sender="AgentGroupC", id="90", type="Response"
	public void processStainingAndPaintcoat(MessageForm message)
	{
		// CONTINUE WORKING ON CURRENT PRODUCT WITH NEXT STEP (ASSEMBLING)
		TechStepMessage tsMsg = (TechStepMessage) message;
		Furniture f = tsMsg.getCarpenter().returnProduct(this.mySim().currentTime());
		f.setStep(ASSEMBLING);
		this.sendAssignRequestForProduct(Mc.assignCarpenterB, Id.agentGroupB, f);

		// PLAN NEXT JOB FOR CARPENTER 'C'
		this.tryAssignNextWorkForCarpenterC(tsMsg);
	}

	//meta! sender="AgentGroupA", id="59", type="Response"
	public void processFittingsInstallationAgentGroupA(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		Furniture f = tsMsg.getCarpenter().returnProduct(this.mySim().currentTime());
		this.noticeIfCompleted(f);

		// PLAN NEXT JOB FOR CARPENTER 'A'
		this.tryAssignNextWorkForCarpenterA(tsMsg);
	}

	//meta! sender="AgentGroupC", id="92", type="Response"
	public void processFittingsInstallationAgentGroupC(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		Furniture f = tsMsg.getCarpenter().returnProduct(this.mySim().currentTime());
		this.noticeIfCompleted(f);

		// PLAN NEXT JOB FOR CARPENTER 'C'
		this.tryAssignNextWorkForCarpenterC(tsMsg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentGroupA", id="130", type="Response"
	public void processCarving(MessageForm message)
	{
		/* notes:
			+ CONTINUE WORKING ON CURRENT PRODUCT WITH NEXT STEP (STAINING)
			+ PLAN NEXT JOB FOR CARPENTER 'A'
				1. check at first qStarted, because date of order's creation here must be older than in qUnstarted
				2. check if there's some product to be processed in one of these queues
			  	2.1 if yes, start transfer of carpenter or staining process
			  	2.2 else, release carpenter A
		 */
		// CONTINUE WORKING ON CURRENT PRODUCT WITH NEXT STEP (STAINING)
		TechStepMessage tsMsg = (TechStepMessage) message;
		Furniture f = tsMsg.getCarpenter().returnProduct(this.mySim().currentTime());
		f.setStep(STAINING);
		this.sendAssignRequestForProduct(Mc.assignCarpenterC, Id.agentGroupC, f);

		// PLAN NEXT JOB FOR CARPENTER 'A'
		this.tryAssignNextWorkForCarpenterA(tsMsg);
	}

	//meta! sender="AgentModel", id="158", type="Notice"
	public void processOrderProcessingStart(MessageForm message)
	{	/* NOTES
			- if there's some free carpenter A, then there's no request for processing Fittings Installation (bcs then,
			  he would already work on it)
			- if no desk is free, then there's no reason to do request for carpenter A assign
		 */
		Order o = ((OrderMessage)message).getOrder();
		this.updateWStatUnsProductsCount(o.getProductsCount());
		o.setWaitingBT(this.mySim().currentTime()); /* sets waitingBT to all its products, bcs order is new. Must be set
		 here, bcs carpenter may not be assigned to product, even if some desk is free */
		this.addToQUnstartedOrders(o);
		if (this.myAgent().getDeskManager().hasFreeDesk()) {
			this.sendAssignRequestForProduct(Mc.assignCarpenterA, Id.agentGroupA, null);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.assignCarpenterA:
			processAssignCarpenterA(message);
		break;

		case Mc.fittingsInstallation:
			switch (message.sender().id())
			{
			case Id.agentGroupA:
				processFittingsInstallationAgentGroupA(message);
			break;

			case Id.agentGroupC:
				processFittingsInstallationAgentGroupC(message);
			break;
			}
		break;

		case Mc.storageTransfer:
			processStorageTransfer(message);
		break;

		case Mc.orderProcessingStart:
			processOrderProcessingStart(message);
		break;

		case Mc.woodPrep:
			processWoodPrep(message);
		break;

		case Mc.deskTransfer:
			processDeskTransfer(message);
		break;

		case Mc.carving:
			processCarving(message);
		break;

		case Mc.assignCarpenterB:
			processAssignCarpenterB(message);
		break;

		case Mc.stainingAndPaintcoat:
			processStainingAndPaintcoat(message);
		break;

		case Mc.assembling:
			processAssembling(message);
		break;

		case Mc.assignCarpenterC:
			processAssignCarpenterC(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentFurnitProd myAgent()
	{
		return (AgentFurnitProd)super.myAgent();
	}

	/**
	 * Sets completion time for specified {@code f}. Checks furniture's order, to which belongs, its completion.
	 * If it's completed, notice message {@code Mc.orderProcessingEnd} is sent to parent agent.
	 * @param f furniture
	 */
	private void noticeIfCompleted(Furniture f) {
		f.setTimeCompleted(this.mySim().currentTime());
		this.myAgent().getDeskManager().setDeskFree(f.getDeskID(), f); // release desk
		if (f.getMyOrder().isCompleted()) { // if all furniture products of this order are completed
			f.getMyOrder().setCompletedAt(this.mySim().currentTime());
			OrderMessage newOrderMsg = (OrderMessage) this.orderMsgPattern.createCopy();
			newOrderMsg.setOrder(f.getMyOrder()); // other params are set in pattern
			this.notice(newOrderMsg);
		}
	}

	// comes here --v from order arrival || from this method itself
	/** Must be ensured, that conditions for fittings installation or new furniture are met. */
	private void processAssignedCarpenterForNewFurniture(AssignMessage msg) {
		if (msg.getCarpenter() == null) {
			return; // CARPENTER WASN'T ASSIGNED
		}
		// - - - - - - START WORKING
//		if (!tryToStartFittingsInstallation(msg.getCarpenter(), Id.agentGroupA)) { /* fittings priority - this should
//						not be possible state, bcs then, we wouldn't have assigned carpenter here */
		Furniture product = this.getUnstartedProduct();
		if (product != null) {
			this.startCreatingNextFurniture(msg.getCarpenter(), product);
		}
//		}
		// - - - - - - NEXT WORK PLANNING
		if (!this.myAgent().getQFittings().isEmpty() ||
			(this.myAgent().getDeskManager().hasFreeDesk() && this.existsUnstartedProduct())) {
			this.sendAssignRequestForProduct(Mc.assignCarpenterA, Id.agentGroupA, null);
		}
	}

	private boolean existsUnstartedProduct() {
		return !this.myAgent().getQStarted().isEmpty() || !myAgent().getQUnstarted().isEmpty();
	}

	/**
	 * Sends request (of copied TechStepMessage's instance) with specified params
	 * @param code
	 * @param addressee
	 * @param carpenter
	 */
	private void sendTechStepRequest(int code, int addressee, Carpenter carpenter) {
		TechStepMessage stepMsg = (TechStepMessage) this.stepMsgPattern.createCopy(); // need copy, bcs more request can be created in one sim time
		stepMsg.setCarpenter(carpenter);
		this.sendTechStepRequest(code, addressee, stepMsg);
	}

	/**
	 * Sends request (of provided TechStepMessage's instance) with specified params. This should be called, when carpenter
	 * is located on place, where he is processing some furniture assigned to him.
	 * THIS METHOD SETS APPROPRIATE LOCATION FOR CARPENTER REGARDING PRODUCT'S STEP & DESK_ID.
	 * @param code
	 * @param addressee
	 * @param msgToReuse
	 */
	private void sendTechStepRequest(int code, int addressee, TechStepMessage msgToReuse) {
		msgToReuse.getCarpenter().setCurrentDeskID(msgToReuse.getProductToProcess().getStep() != WOOD_PREPARATION
				? msgToReuse.getProductToProcess().getDeskID() : Carpenter.IN_STORAGE
		); // location for WOOD_PREP, is not on desk but IN_STORAGE
		msgToReuse.setCode(code);
		msgToReuse.setAddressee(addressee);
		this.request(msgToReuse);
	}

	/**
	 *
	 * @param code valid options: {@code Mc.assignCarpenterA}, {@code Mc.assignCarpenterB}, {@code Mc.assignCarpenterC}
	 * @param agentGroup valid options: {@code Id.agentGroupA}, {@code Id.agentGroupB}, {@code Id.agentGroupC},
	 * @param product furniture that is to be processed with potentially assigned carpenter of group {@code agentGroup}
	 */
	private void sendAssignRequestForProduct(int code, int agentGroup, Furniture product) {
		AssignMessage assignMsg = (AssignMessage) this.assignMsgPattern.createCopy();
		assignMsg.setCode(code);
		assignMsg.setAddressee(agentGroup);
		assignMsg.setProduct(product);
		this.request(assignMsg);
	}

	/**
	 * Creates new request for storage transfer with specified {@code carpenter} and assigned furniture product
	 * @param carpenter
	 */
	private void sendStorageTransferRequest(Carpenter carpenter) {
		TechStepMessage tsMsg = (TechStepMessage) this.stepMsgPattern.createCopy();
		tsMsg.setCarpenter(carpenter);
		this.sendStorageTransferRequest(tsMsg);
	}

	/**
	 * Creates new request for storage transfer with already assigned {@code carpenter}
	 */
	private void sendStorageTransferRequest(TechStepMessage tsMsg) {
		tsMsg.getCarpenter().setCurrentDeskID(Carpenter.TRANSFER_STORAGE);
		tsMsg.setCode(Mc.storageTransfer);
		tsMsg.setAddressee(Id.agentTransfer);
		this.request(tsMsg);
	}

	/**
	 * Creates new request for desks transfer with specified {@code carpenter} and assigned furniture product
	 * @param carpenter
	 */
	private void sendDeskTransferRequest(Carpenter carpenter) {
		TechStepMessage tsMsg = (TechStepMessage) this.stepMsgPattern.createCopy();
		tsMsg.setCarpenter(carpenter);
		this.sendDeskTransferRequest(tsMsg);
	}

	/**
	 * Creates new request for desks transfer with already assigned {@code carpenter}
	 */
	private void sendDeskTransferRequest(TechStepMessage tsMsg) {
		tsMsg.getCarpenter().setCurrentDeskID(Carpenter.TRANSFER_DESKS);
		tsMsg.setCode(Mc.deskTransfer);
		tsMsg.setAddressee(Id.agentTransfer);
		this.request(tsMsg);
	}

	private void releaseCarpenter(int code, int agentId, TechStepMessage tsMsg) {
		tsMsg.setCode(code);
		tsMsg.setAddressee(agentId);
		this.notice(tsMsg);
	}

	private void startCreatingNextFurniture(Carpenter carpenter, Furniture product) {
		TechStepMessage tsMsg = (TechStepMessage) this.stepMsgPattern.createCopy(); // need copy, bcs more request can be created in one sim time
		tsMsg.setCarpenter(carpenter);
		this.startCreatingNextFurniture(tsMsg, product);
	}

	/**
	 * Carpenter A must be already set in {@code tsMsg}
	 * @param tsMsg
	 * @param product
	 */
	private void startCreatingNextFurniture(TechStepMessage tsMsg, Furniture product) {
		tsMsg.getCarpenter().receiveProduct(product, this.mySim().currentTime());
		product.setDeskID(this.myAgent().getDeskManager().occupyDesk(product));
		product.setStep(Furniture.TechStep.WOOD_PREPARATION);
		product.setProcessingBT(this.mySim().currentTime());
		if (tsMsg.getCarpenter().isInStorage()) {
			this.sendTechStepRequest(Mc.woodPrep, Id.agentGroupA, tsMsg);
		}
		else {
			this.sendStorageTransferRequest(tsMsg);
		}
	}

	/**
	 * Starts some work with fittings installation or creating new furniture if such exists, else releases carpenter A
	 * assigned in param {@code tsMsg}.
	 * @param tsMsg msg to be reused with set carpenter
	 */
	private void tryAssignNextWorkForCarpenterA(TechStepMessage tsMsg) {
		if (this.tryToStartFittingsInstallation(tsMsg.getCarpenter(), Id.agentGroupA)) {
			return;
		}
		else { // no fittings montage work -> try to start creating new furniture product
			if (this.myAgent().getDeskManager().hasFreeDesk()) {
				Furniture f = this.getUnstartedProduct();
				if (f != null) {
					this.startCreatingNextFurniture(tsMsg, f);
					return;
				}
			}
		}
		// carpenter A has no potential work
		this.releaseCarpenter(Mc.releaseCarpenterA, Id.agentGroupA, tsMsg);
	}

	/**
	 * Starts some work with assembling if such exists, else releases carpenter B assigned in param {@code tsMsg}.
	 * @param tsMsg msg to be reused with set carpenter
	 */
	private void tryAssignNextWorkForCarpenterB(TechStepMessage tsMsg) {
		Furniture f = this.myAgent().getQAssembling().poll();
		if (f != null) {
			this.myAgent().getStatAssemblingQL().addSample( this.myAgent().getQAssembling().size() );
			this.updateStatWaitingTime(f, this.myAgent().getStatAssemblingWT());
			this.startNextStep(tsMsg.getCarpenter(), f, Mc.assembling, Id.agentGroupB);
			return;
		}
		// carpenter B has no potential work
		this.releaseCarpenter(Mc.releaseCarpenterB, Id.agentGroupB, tsMsg);
	}

	/**
	 * Starts some work with fittings installation or staining (with optional lacquering) if such exists, else releases
	 * carpenter C assigned in param {@code tsMsg}.
	 * @param tsMsg msg to be reused with set carpenter
	 */
	private void tryAssignNextWorkForCarpenterC(TechStepMessage tsMsg) {
		if (this.tryToStartFittingsInstallation(tsMsg.getCarpenter(), Id.agentGroupC)) {
			return;
		}
		else { // no fittings montage work -> check if some furniture is waiting for staining (with optional lacquering)
			Furniture f = this.myAgent().getQStaining().poll();
			if (f != null) {
				this.myAgent().getStatStainingQL().addSample( this.myAgent().getQStaining().size() );
				this.updateStatWaitingTime(f, this.myAgent().getStatStainingWT());
				this.startNextStep(tsMsg.getCarpenter(), f, Mc.stainingAndPaintcoat, Id.agentGroupC);
				return;
			}
		}
		// carpenter C has no potential work
		this.releaseCarpenter(Mc.releaseCarpenterC, Id.agentGroupC, tsMsg);
	}

	/**
	 * If exists some furniture waiting for fittings installation, then it starts this process with provided {@code c}.
	 * @param c carpenter of group {@code Carpenter.GROUP.A} or {@code Carpenter.GROUP.C}
	 * @param carpenterGroupID options: {@code Id.agentGroupA}, {@code Id.agentGroupC}
	 * @return {@code true}, if he did start fittings installation
	 */
	private boolean tryToStartFittingsInstallation(Carpenter c, int carpenterGroupID) {
		Furniture f = this.myAgent().getQFittings().poll();
		if (f == null)
			return false;
		this.myAgent().getStatFittingsQL().addSample( this.myAgent().getQFittings().size() );
		this.updateStatWaitingTime(f, this.myAgent().getStatFittingsWT());
		this.startNextStep(c, f, Mc.fittingsInstallation, carpenterGroupID);
		return true;
	}

	private void startNextStep(Carpenter c, Furniture f, int code, int agentGroup) {
		c.receiveProduct(f, this.mySim().currentTime());
		if (c.getCurrentDeskID() == f.getDeskID()) { // same desk
			this.sendTechStepRequest(code, agentGroup, c);
		}
		else if (c.getCurrentDeskID() != Carpenter.IN_STORAGE) { // other desk
			this.sendDeskTransferRequest(c);
		}
		else { // he's in storage - hasn't worked yet
			this.sendStorageTransferRequest(c);
		}
	}

	private void enqueueProduct(Furniture f, Queue<Furniture> q, WStat statQueueLen) {
		q.add(f);
		f.setWaitingBT(this.mySim().currentTime());
		statQueueLen.addSample(q.size());
	}

	/**
	 * WARNING!!! THIS METHOD SHOULD BE CALLED ONLY, WHEN THESE CONDITIONS ARE TRUE:
	 * 1. Some desk is free and therefore could be assigned.
	 * 2. Carpenter is present and ready to receive new order.
	 * Tries to assign the oldest product of received order. If such doesn't exist, then tries to assign the oldest
	 * product from completely unstarted order. It automatically handles queues 'qUnstarted' and 'qStarted'.
	 * @return product instance of the oldest order or {@code null}.
	 */
	private Furniture getUnstartedProduct() {
		Order o = this.myAgent().getQStarted().peek(); // here are the oldest orders with some unstarted products
		Furniture f = null;
		if (o != null) {
			f = o.assignUnstartedProduct();
			if (!o.hasUnassignedProduct()) // this was the last unstarted product in its order
				this.myAgent().getQStarted().remove(); // removes the oldest order
		}
		else { // nothing in qStarted, trying to get something from qUnstarted
			o = this.pollFromQUnstartedOrders(); // always removed from qUnstarted, because it is not unstarted from now
			if (o != null) {
				f = o.assignUnstartedProduct();
				if (o.hasUnassignedProduct())
					this.myAgent().getQStarted().add(o); // something remains unstarted in order, so it is enqueued for remaining products
			}
		}
		if (f != null) {
			this.updateWStatUnsProductsCount(-1);
			this.updateStatWaitingTime(f, this.myAgent().getStatUnsProductsWT());
		}
		return f;
	}

//	/**
//	 * WARNING!!! THIS METHOD DOES NOT MODIFY QUEUES qUnstarted and qStarted!
//	 * @return product instance of the oldest order (order with the highest priority to process) or {@code null}.
//	 */
//	private Order getOrderToProcess() {
//		Order o = this.myAgent().getQStarted().peek(); // here are the oldest orders with some unstarted products
//		return (o != null) ? o : this.myAgent().getQUnstarted().peek();
//	}
//	-	-	-	-	-	-	-	S T A T S   M A N A G E M E N T 	-	-	-	-	-	-	-	-
	/**
	 * Should be called in moment of next technological step start.
	 * It adds new sample to stat and invalidates product's waitingBT.
	 * @param f product, whose waitingBT is observed
	 * @param stat stat, to which new sample will be added
	 */
	private void updateStatWaitingTime(Furniture f, Stat stat) {
		stat.addSample(this.mySim().currentTime() - f.getWaitingBT());
		f.setWaitingBT(-1);
	}

	/**
	 * Updates wstat for unstarted products count.
	 * @param change amount that will be added to {@code this.unsProductsCount}
	 */
	private void updateWStatUnsProductsCount(int change) {
		this.unsProductsCount += change;
		this.myAgent().getStatUnsProductsQL().addSample(this.unsProductsCount);
	}
	// qUnstarted
	private void addToQUnstartedOrders(Order o) {
		this.myAgent().getQUnstarted().add(o);
		this.myAgent().getStatUnsOrdersQL().addSample( this.myAgent().getQUnstarted().size() );
	}
	/**
	 * Polls Order with the highest priority and updates wstat of queue length and stat of waiting in queue.
	 * This method is recommended to use instead of 'remove'.
	 * @return polled order or {@code null}, if empty
	 */
	private Order pollFromQUnstartedOrders() {
		Order o = this.myAgent().getQUnstarted().poll();
		if (o != null) {
			this.myAgent().getStatUnsOrdersQL().addSample( this.myAgent().getQUnstarted().size() );
			this.myAgent().getStatUnsOrdersWT().addSample( this.mySim().currentTime() - o.getCreatedAt() );
		}
		return o;
	}

	private AssignMessage getFreeAssignMessageInstance() {
		if (this.freeAssignMsgInstance == null)
			return (AssignMessage) this.assignMsgPattern.createCopy();
		AssignMessage m = this.freeAssignMsgInstance;
		this.freeAssignMsgInstance = null;
		return m;
	}

	private TechStepMessage getFreeTechStepMessageInstance() {
		if (this.freeStepMsgInstance == null)
			return (TechStepMessage) this.stepMsgPattern.createCopy();
		TechStepMessage m = this.freeStepMsgInstance;
		this.freeStepMsgInstance = null;
		return m;
	}
}