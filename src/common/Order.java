package common;

import utils.Formatter;

import java.util.Arrays;

public class Order {
    private final int orderID;
    private final double createdAt;
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

    public void setProducts(Furniture[] products) {
        this.products = products;
        this.nextToBeProcessed = 0;
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

    public int getOrderID() {
        return this.orderID;
    }

    /**
     * @return furniture product whose creating hasn't started yet or {@code null} if all products processing already
     * started (or even ended).
     */
    public Furniture assignUnprocessedProduct() {
        return (this.nextToBeProcessed < this.products.length) ? this.products[this.nextToBeProcessed++] : null;
    }

    /**
     * @return time of when this order was created
     */
    public double getCreatedAt() {
        return this.createdAt;
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
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        System.out.println(order.assignUnprocessedProduct());
        // ok
    }
}
