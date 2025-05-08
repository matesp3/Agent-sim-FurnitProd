package common;

import OSPAnimator.IAnimator;
import OSPStat.WStat;
import animation.FurnitureFactoryAnimation;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class DeskAllocation {
    // - - - - logic attributes
    private final Furniture[] desks;
    private int firstFree;
    // - - - - simulation attributes
    private int usedDesks;
    private WStat statCount;

    public DeskAllocation(int amountOfDesks, WStat statCount) {
        this.desks = new Furniture[amountOfDesks];
        this.firstFree = 0;
        this.usedDesks = 0;
        this.statCount = statCount;
        this.statCount.clear();
    }

    public void setDeskFree(int deskId, Furniture userIdentity) {
        if (deskId < 0 || deskId >= this.desks.length)
            throw new IllegalArgumentException("Desk ID " + deskId + " does not exist");
        if (this.desks[deskId] != userIdentity)
            throw new IllegalArgumentException("Violation of desk freeing. This identity cannot free desk that doesn't"
                    + " belong to him ");
        this.desks[deskId] = null;
        this.usedDesks--;
        this.statCount.addSample(this.usedDesks);

        if (deskId < this.firstFree)
            this.firstFree = deskId;
    }

    public boolean hasFreeDesk() {
        return this.firstFree < this.desks.length;
    }

    /**
     * @return {@code ID} of assigned desk (strategy of assigning is an internal logic)
     * @throws NoSuchElementException when all desks are occupied
     */
    public int occupyDesk(Furniture applicantIdentity) {
        if (applicantIdentity == null)
            throw new NullPointerException("applicantIdentity is null");
        if (!this.hasFreeDesk()) {
            throw new NoSuchElementException("All desks are occupied");
        }

        int assigned = this.firstFree;
        this.desks[assigned] = applicantIdentity;
        this.usedDesks++;
        this.statCount.addSample(this.usedDesks);

        this.firstFree = this.desks.length;
        for (int i = assigned+1; i < this.desks.length; i++) {
            if (this.desks[i] == null) {
                this.firstFree = i;
                break;
            }
        }
        return assigned;
    }

    public void freeAllDesks() {
        Arrays.fill(this.desks, null);
        this.firstFree = 0;
        this.usedDesks = 0;
        this.statCount.clear();
    }

    /**
     * @return number of how many desks is currently being used.
     */
    public int getUsedDesksCount() {
        return this.usedDesks;
    }

    /**
     * @return is equal to maximum number of furniture products, that can be processed at the same time (one furniture
     * is processed on the same desk from the beginning to the end of its creating process)
     */
    public int getAllDesksCount() {
        return this.desks.length;
    }

    public WStat getStatUsedDesksCount() {
        return this.statCount;
    }

    public void registerDesks(FurnitureFactoryAnimation animHandler) {
        for (int i = 0; i < this.desks.length; i++) {
            if (this.desks[i] != null) {
                this.desks[i].initAnimatedEntity().registerEntity(animHandler.getAnimator());
                animHandler.placeFurnitureOnDesk(i, this.desks[i].getAnimatedEntity());
            }
        }
    }

    public void unregisterDesks(IAnimator animator) {
        for (int i = 0; i < this.desks.length; i++) {
            if (this.desks[i] != null) {
                this.desks[i].getAnimatedEntity().unregisterEntity(animator);
            }
        }
    }

    @Override
    public String toString() {
        return "DeskAllocation{" +
                "firstFree=" + firstFree +
                ", desks=" + Arrays.toString(this.desks) +
                '}';
    }

    public static void main(String[] args) {
//        Order order = new Order(1, 1586522);
//        DeskAllocation manager = new DeskAllocation(5, new WStat(MySimInstanceNeeded));
//        manager.occupyDesk(new Furniture(order, "1-A", Furniture.Type.CHAIR, true));
//        Furniture o = new Furniture(order, "1-B", Furniture.Type.TABLE, true);
//        int deskID = manager.occupyDesk(o);
//        manager.occupyDesk(new Furniture(order, "1-C", Furniture.Type.WARDROBE, true));
//        System.out.println(manager);
//        System.out.println("Removing "+o+" from desk["+deskID+"]");
//        manager.setDeskFree(deskID, o);
//        System.out.println(manager);
//        System.out.println("Freeing everything...");
//        manager.freeAllDesks();
//        System.out.println(manager);
        // ok
    }
}
