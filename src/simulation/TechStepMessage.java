package simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import common.Carpenter;
import common.Furniture;

public class TechStepMessage extends OSPABA.MessageForm {

    private Carpenter carpenter;

    public TechStepMessage(Simulation mySim) {
        super(mySim);
        this.carpenter = null;
    }

    protected TechStepMessage(TechStepMessage original) {
        super(original);
        this.carpenter = original.carpenter;
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
    }

    public Carpenter getCarpenter() {
        return carpenter;
    }

    public void setCarpenter(Carpenter carpenter) {
        this.carpenter = carpenter;
    }

    /**
     * @return furniture product that is assigned to the carpenter that is part of this message's instance
     */
    public Furniture getProduct() {
        return this.carpenter.getAssignedProduct();
    }
}
