package common;

import utils.Formatter;

public class Order {
    private final int orderID;
    private final double createdAt;
    private double completedAt;
    private Furniture[] products;
    private int nextToBeProcessed;

    /**
     *
     * @param id unique ID among orders within system
     * @param createdAt time, when this order came to existence in the system
     */
    public Order(int id, double createdAt) {
        this(id, null, createdAt);
    }

    /**
     *
     * @param id unique ID among orders within system
     * @param products product of which this order consists
     * @param createdAt time, when this order came to existence in the system
     */
    public Order(int id, Furniture[] products, double createdAt) {
        this.orderID = id;
        this.createdAt = createdAt;
        this.products = products;
        this.nextToBeProcessed = 0;
    }

    /**
     * @param products ordered products of which order consists
     */
    public void setProducts(Furniture[] products) {
        this.products = products;
        this.nextToBeProcessed = 0;
    }

    public int getOrderID() {
        return this.orderID;
    }

    /**
     * @return furniture product whose creating hasn't started yet or {@code null} if all products processing already
     * started (or even ended).
     */
    public Furniture assignUnstartedProduct() {
        return (this.nextToBeProcessed < this.products.length) ? this.products[this.nextToBeProcessed++] : null;
    }

    /**
     * @return time of when this order was created
     */
    public double getCreatedAt() {
        return this.createdAt;
    }

    /**
     *
     * @return simulation time of whole order completion (completion time of last not completed product)
     */
    public double getCompletedAt() {
        return this.completedAt;
    }

    /**
     * After completing lastly not completed product, this setter should be called
     * @param completedAt simulation time of whole order completion
     */
    public void setCompletedAt(double completedAt) {
        this.completedAt = completedAt;
    }

    public Furniture[] getProducts() {
        return this.products;
    }

    /**
     * @return {@code true} if all furniture products, that it consists of, are completed, else {@code false}
     */
    public boolean isCompleted() {
        for (Furniture product : this.products) {
            if (!product.isCompleted())
                return false;
        }
        return true;
    }

    /**
     * @return {@code true} if at least one of order's products has not been assigned for work yet, else {@code false}
     */
    public boolean hasUnassignedProduct() {
        return this.nextToBeProcessed < this.products.length;
    }

    /**
     *
     * @return {@code true} if no product of this order has started its creating yet, else {@code false}
     */
    public boolean isUnstarted() {
        return this.nextToBeProcessed == 0;
    }

    /**
     * Sets waitingBT time to all its products, which are still unassigned
     * @param time begin time of waiting
     */
    public void setWaitingBT(double time) {
        for (int i = this.nextToBeProcessed; i < this.products.length; i++) {
            this.products[i].setWaitingBT(time);
        }
    }

    /**
     * @return amount of products, of which order consists
     */
    public int getProductsCount() {
        return this.products.length;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", createdAt=" + Formatter.getStrDateTime(createdAt, 8, 6) +
                ", nextToBeProcessed=" + nextToBeProcessed +
                '}';
    }

    public static void main(String[] args) {
        Order order = new Order(248, 2500710);
        Furniture[] products = new Furniture[]
                {
                    new Furniture(order, ("" + order.getOrderID() + "-" + 1), Furniture.Type.WARDROBE),
                    new Furniture(order, ("" + order.getOrderID() + "-" + 2), Furniture.Type.CHAIR),
                    new Furniture(order, ("" + order.getOrderID() + "-" + 3), Furniture.Type.CHAIR),
                    new Furniture(order, ("" + order.getOrderID() + "-" + 4), Furniture.Type.WARDROBE),
                    new Furniture(order, ("" + order.getOrderID() + "-" + 5), Furniture.Type.TABLE)
                };
        order.setProducts(products);
        System.out.println(order);
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        System.out.println(order.assignUnstartedProduct());
        // ok
    }
}
