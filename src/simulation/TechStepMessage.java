package simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import common.Carpenter;
import common.Furniture;

public class TechStepMessage extends OSPABA.MessageForm {

    private Carpenter carpenter;
    private Furniture product;

    public TechStepMessage(Simulation mySim) {
        super(mySim);
        this.carpenter = null;
        this.product = null;
    }

    protected TechStepMessage(TechStepMessage original) {
        super(original);
        this.carpenter = original.carpenter;
        this.product = original.product;
    }

    @Override
    public MessageForm createCopy() {
        return new TechStepMessage(this);
    }

    @Override
    protected void copy(MessageForm message)
    {
        super.copy(message);
        TechStepMessage original = (TechStepMessage)message;
        // Copy attributes
        this.carpenter = original.carpenter;
        this.product = original.product;
    }

    public Carpenter getCarpenter() {
        return carpenter;
    }

    public void setCarpenter(Carpenter carpenter) {
        this.carpenter = carpenter;
    }

    public Furniture getProduct() {
        return product;
    }

    public void setProduct(Furniture product) {
        this.product = product;
    }
}
