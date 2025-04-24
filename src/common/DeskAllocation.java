package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class DeskAllocation {
    private final Furniture[] desks;
    private int firstFree;

    public DeskAllocation(int amountOfDesks) {
        this.desks = new Furniture[amountOfDesks];
        this.firstFree = 0;
    }

    public void setDeskFree(int deskId, Furniture userIdentity) {
        if (deskId < 0 || deskId >= this.desks.length)
            throw new IllegalArgumentException("Desk ID " + deskId + " does not exist");
        if (this.desks[deskId] != userIdentity)
            throw new IllegalArgumentException("Violation of desk freeing. This identity cannot free desk that doesn't"
                    + " belong to him ");
        this.desks[deskId] = null;
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
    }

    @Override
    public String toString() {
        return "DeskAllocation{" +
                "firstFree=" + firstFree +
                ", desks=" + Arrays.toString(this.desks) +
                '}';
    }

    public static void main(String[] args) {
        Order order = new Order(1, 1586522);
        DeskAllocation manager = new DeskAllocation(5);
        manager.occupyDesk(new Furniture(order, "1-A", Furniture.Type.CHAIR));
        Furniture o = new Furniture(order, "1-B", Furniture.Type.TABLE);
        int deskID = manager.occupyDesk(o);
        manager.occupyDesk(new Furniture(order, "1-C", Furniture.Type.WARDROBE));
        System.out.println(manager);
        System.out.println("Removing "+o+" from desk["+deskID+"]");
        manager.setDeskFree(deskID, o);
        System.out.println(manager);
        System.out.println("Freeing everything...");
        manager.freeAllDesks();
        System.out.println(manager);
        // ok
    }
}
