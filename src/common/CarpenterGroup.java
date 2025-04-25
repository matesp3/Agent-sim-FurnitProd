package common;


public class CarpenterGroup {
    private static int nextID = 1;
    private final Carpenter.GROUP groupID;
    private Carpenter[] carpenters;
    private int firstFree;

    public CarpenterGroup(Carpenter.GROUP groupID) {
        this.groupID = groupID;
        this.carpenters = null;
        this.firstFree = Integer.MAX_VALUE;
    }

    public void initCarpenters(int amount) {
        this.carpenters = new Carpenter[amount];
        for (int i = 0; i < amount; i++) {
            this.carpenters[i] = new Carpenter(this.groupID, nextID++);
        }
        this.firstFree = 0;
    }

    public boolean hasFreeCarpenter() {
        return this.firstFree < this.carpenters.length;
    }

    public Carpenter assignCarpenter() {
        if (!this.hasFreeCarpenter())
            return null;
        Carpenter assigned = this.carpenters[this.firstFree];
        for (this.firstFree = this.firstFree+1; this.firstFree < this.carpenters.length; this.firstFree++) {
            if (!this.carpenters[this.firstFree].isWorking())
                break; // firstFree updated
        }
        return assigned;
    }

    public void releaseCarpenter(Carpenter c) {
        if (c.isWorking())
            throw new RuntimeException("Carpenter is working on some furniture, therefore cannot be released");
        // update first free
        for (int i = 0; i < this.firstFree; i++) { // first free's maximum idx is the number of all carpenters
            if (this.carpenters[i].getCarpenterId() == c.getCarpenterId()) {
                this.firstFree = i;
                break;
            }
        }
    }

    public void resetCarpenters() {
        for (Carpenter carpenter : this.carpenters) {
            carpenter.reset();
        }
    }

    public Carpenter[] getCarpenters() {
        return this.carpenters;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CarpenterGroup[groupID=");
        sb.append(this.groupID);
        sb.append(", firstFree=");
        sb.append(this.firstFree < this.carpenters.length ? this.firstFree : "UNAVAILABLE");
        sb.append(", carpentersIDs=");
        for (int i = 0; i < this.carpenters.length-1; i++) {
            sb.append(this.carpenters[i].getCarpenterId());
            sb.append('(');
            sb.append(this.carpenters[i].isWorking());
            sb.append("), ");
        }
        sb.append(this.carpenters[this.carpenters.length-1].getCarpenterId());
        sb.append('(');
        sb.append(this.carpenters[this.carpenters.length-1].isWorking());
        sb.append(")]");
        return sb.toString();
    }

    public static void main(String[] args) {
        CarpenterGroup allocator = new CarpenterGroup(Carpenter.GROUP.A);
        allocator.initCarpenters(3);
        System.out.println("State:  "+allocator);
        Carpenter c1 = allocator.assignCarpenter();
        System.out.println(" > assigned: "+c1);
        System.out.println("State:  "+allocator);
        System.out.println(" > assigned: "+allocator.assignCarpenter());
        System.out.println("State:  "+allocator);
        Carpenter c2 = allocator.assignCarpenter();
        System.out.println(" > assigned: "+c2);
        System.out.println("State:  "+allocator);
        System.out.println(" > assigned: "+allocator.assignCarpenter());
        System.out.println("State:  "+allocator);
        System.out.println(" > assigned: "+allocator.assignCarpenter());
        System.out.println("State:  "+allocator);
        System.out.println(" *** releasing: "+c2);
        allocator.releaseCarpenter(c2);
        System.out.println("State:  "+allocator);
        System.out.println(" > assigned: "+allocator.assignCarpenter());
        System.out.println("State:  "+allocator);
        System.out.println(" *** releasing: "+c1);
        allocator.releaseCarpenter(c1);
        System.out.println("State:  "+allocator);
        // ok
    }
}
