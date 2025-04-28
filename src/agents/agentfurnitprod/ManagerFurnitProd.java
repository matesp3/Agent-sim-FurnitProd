package agents.agentfurnitprod;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import common.Carpenter;
import common.Furniture;
import common.Order;
import simulation.*;

import static common.Furniture.TechStep.CARVING;
import static common.Furniture.TechStep.WOOD_PREPARATION;

//meta! id="24"
public class ManagerFurnitProd extends OSPABA.Manager
{
	private final AssignMessage assignMsg;
	private final TechStepMessage stepMsgPattern;
	private final OrderMessage orderMsgPattern;

	public ManagerFurnitProd(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
		this.assignMsg = new AssignMessage(this.mySim());
		this.stepMsgPattern = new TechStepMessage(this.mySim());

		this.orderMsgPattern = new OrderMessage(this.mySim());
		this.orderMsgPattern.setCode(Mc.orderProcessingEnd);
		this.orderMsgPattern.setAddressee(Id.agentModel);
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
	}

	//meta! sender="AgentTransfer", id="35", type="Response"
	public void processDeskTransfer(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="57", type="Response"
	public void processWoodPrep(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		this.executeEnd(tsMsg); // todo iba kontrola, riadok odstranit
//		tsMsg.getProduct().setStep(CARVING);
//		this.sendStorageTransferRequest(tsMsg);
	}

	//meta! sender="AgentTransfer", id="33", type="Response"
	public void processStorageTransfer(MessageForm message)
	{
		TechStepMessage tsMsg = (TechStepMessage) message;
		tsMsg.getCarpenter().setCurrentDeskID(
				tsMsg.getProduct().getStep() == WOOD_PREPARATION ? Carpenter.IN_STORAGE : tsMsg.getProduct().getDeskID()
		); // location after storage transfer is IN_STORAGE just for WOOD_PREP step, because other steps are done on desk places

		switch (tsMsg.getProduct().getStep()) {
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
						tsMsg.getCarpenter().getGroup() == Carpenter.GROUP.A ? Id.agentGroupA : Id.agentGroupC, tsMsg);
				break;
			default:
				throw new RuntimeException("Unknown tech step or null");
		}
	}

	//meta! sender="AgentGroupB", id="79", type="Response"
	public void processAssembling(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="54", type="Response"
	public void processAssignCarpenterA(MessageForm message)
	{
		// 2 possibilities: two types -> typecasting ; or additional attribute in one class {due to order BT | tech step)
		AssignMessage assignMsg = (AssignMessage) message;
		Carpenter carpenter = assignMsg.getCarpenter();
		if (assignMsg.getOrder() != null) { // 1. option - product beginning
			this.processAssignedCarpenterForNewFurniture(assignMsg);
		}
		else { // 2. option - tech step processing
			// todo tech step processing
		}
	}

	//meta! sender="AgentGroupB", id="77", type="Response"
	public void processAssignCarpenterB(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="88", type="Response"
	public void processAssignCarpenterC(MessageForm message)
	{
	}

	//meta! sender="AgentGroupC", id="90", type="Response"
	public void processStainingAndPaintcoat(MessageForm message)
	{
	}

	//meta! sender="AgentGroupA", id="59", type="Response"
	public void processFittingsInstallationAgentGroupA(MessageForm message)
	{
//		this.noticeIfCompleted(...);
	}

	//meta! sender="AgentGroupC", id="92", type="Response"
	public void processFittingsInstallationAgentGroupC(MessageForm message)
	{
//		this.noticeIfCompleted();...);
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
	}

	//meta! sender="AgentModel", id="158", type="Notice"
	public void processOrderProcessingStart(MessageForm message)
	{
		/* NOTES
			- if there's some free carpenter A, then there's no request for processing Fittings Installation (bcs then,
			  he would already work on it)
			- if no desk is free, then there's no reason to do request for carpenter A assign
		 */
		if (this.myAgent().getDeskManager().hasFreeDesk()) {
			this.sendAssignRequestForOrder( ((OrderMessage)message).getOrder() );
		}
		else { // this new order must wait as a whole, bcs there's no place where some product can be created
			this.myAgent().getQUnstarted().add( ((OrderMessage)message).getOrder() );
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
	 * Check order completion. If it's completed, notice message {@code Mc.orderProcessingEnd} is sent to parent agent
	 * @param o order
	 */
	private void noticeIfCompleted(Order o) {
//		if (o.isCompleted()) {
		if (true) {
			OrderMessage newOrderMsg = (OrderMessage) this.orderMsgPattern.createCopy();
			newOrderMsg.setOrder(o); // other params are set in pattern
			this.notice(newOrderMsg);
		}
	}

	private void processAssignedCarpenterForNewFurniture(AssignMessage msg) {
		Carpenter carpenter = msg.getCarpenter();
		if (carpenter == null) {
			if (!assignMsg.getOrder().isUnstarted())
				this.myAgent().getQStarted().add(assignMsg.getOrder());
			else
				this.myAgent().getQUnstarted().add(assignMsg.getOrder());
			return;
		}
		Furniture product = assignMsg.getOrder().assignUnstartedProduct();
		carpenter.receiveProduct(product, this.mySim().currentTime());
		product.setDeskID(this.myAgent().getDeskManager().occupyDesk(product));
		product.setStep(Furniture.TechStep.WOOD_PREPARATION);
		product.setProcessingBT(this.mySim().currentTime());
		if (carpenter.isInStorage())
			this.sendTechStepRequest(Mc.woodPrep, Id.agentGroupA, carpenter);
		else {
			this.sendStorageTransferRequest(carpenter);
		}
		// now I know, that I have free desk & free carpenter -> I can start working on new product
		// - - - - - -
		if (assignMsg.getOrder().hasUnassignedProduct() && this.myAgent().getDeskManager().hasFreeDesk()) {
			this.sendAssignRequestForOrder(assignMsg.getOrder());
		}
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
	 * Sends request (of provided TechStepMessage's instance) with specified params
	 * @param code
	 * @param addressee
	 * @param msgToReuse
	 */
	private void sendTechStepRequest(int code, int addressee, TechStepMessage msgToReuse) {
		msgToReuse.setCode(code);
		msgToReuse.setAddressee(addressee);
		this.request(msgToReuse);
	}

	private void sendAssignRequestForOrder(Order order) {
		// 1 instance of assignMessage is enough, bcs it's used only within messages, which are executed in the current time
		this.assignMsg.setCode(Mc.assignCarpenterA);
		this.assignMsg.setAddressee(Id.agentGroupA);
		this.assignMsg.setOrder(order);
		this.assignMsg.setProduct(null);
		this.request(this.assignMsg);

//		AssignMessage msg = (AssignMessage) this.assignMsg.createCopy();
//		msg.setCode(Mc.assignCarpenterA);
//		msg.setAddressee(Id.agentGroupA);
//		msg.setOrder(order);
//		msg.setProduct(null);
//		this.request(msg);
	}

	private void sendAssignRequestForProduct(int code, int agentGroup, Furniture product) {
		// 1 instance of assignMessage is enough, bcs it's used only within messages, which are executed in the current time
		this.assignMsg.setCode(code);
		this.assignMsg.setAddressee(agentGroup);
		this.assignMsg.setOrder(null);
		this.assignMsg.setProduct(product);
		this.request(this.assignMsg);
	}

	/**
	 * Creates new request for storage transfer with assigned {@code carpenter}
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
		tsMsg.setCode(Mc.storageTransfer);
		tsMsg.setAddressee(Id.agentTransfer);
		this.request(tsMsg);
	}

	/**
	 * Release of desk + carpenter -> then end notice
	 */
	private void executeEnd(TechStepMessage tsMsg) {
		Carpenter c = tsMsg.getCarpenter();
		Furniture f = c.returnProduct(this.mySim().currentTime());
		this.myAgent().getDeskManager().setDeskFree(f.getDeskID(), f);
		// todo update carpenter stats - asi si vytvorim SimPrQueue wrapper
		// todo dorob si statistiky na gui
		this.releaseCarpenter(Mc.releaseCarpenterA, Id.agentGroupA, tsMsg);
		this.noticeIfCompleted( f.getOrder() );
	}

	private void releaseCarpenter(int code, int agentId, TechStepMessage tsMsg) {
		tsMsg.setCode(code);
		tsMsg.setAddressee(agentId);
		this.notice(tsMsg);
	}
}