package simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import common.Carpenter;
import common.Furniture;
import common.Order;

public class AssignMessage extends MessageForm {

    private Carpenter carpenter;
    private Furniture product;

    public AssignMessage(Simulation mySim) {
        super(mySim);
        this.carpenter = null;
        this.product = null;
    }

    protected AssignMessage(AssignMessage original) {
        super(original);
        this.carpenter = original.carpenter;
        this.product = original.product;
    }

    @Override
    public MessageForm createCopy() {
        return new AssignMessage(this);
    }

    @Override
    protected void copy(MessageForm message)
    {
        super.copy(message);
        AssignMessage original = (AssignMessage)message;
        // Copy attributes
        this.carpenter = original.carpenter;
        this.product = original.product;
    }

    public Carpenter getCarpenter() {
        return this.carpenter;
    }

    public void setCarpenter(Carpenter carpenter) {
        this.carpenter = carpenter;
    }

    public Furniture getProduct() {
        return this.product;
    }

    public void setProduct(Furniture product) {
        this.product = product;
    }
}
