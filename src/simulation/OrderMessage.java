package simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import common.Furniture;

public class OrderMessage extends OSPABA.MessageForm {

    private Furniture order;

    public OrderMessage(Simulation mySim) {
        super(mySim);
        this.order = null;
    }

    protected OrderMessage(OrderMessage original) {
        super(original);
        this.order = original.order;
    }

    @Override
    public MessageForm createCopy() {;
        return new OrderMessage(this);
    }

    @Override
    protected void copy(MessageForm message)
    {
        super.copy(message);
        OrderMessage original = (OrderMessage)message;
        // Copy attributes
        this.order = original.order;
    }

    public Furniture getOrder() {
        return this.order;
    }

    public void setOrder(Furniture order) {
        this.order = order;
    }
}
