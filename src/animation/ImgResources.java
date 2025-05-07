package animation;

import OSPAnimator.AnimImageItem;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImgResources {
    public static final String DIR_IMAGES = System.getProperty("user.dir") + "\\images\\";
    /** original: 251x643 px */
    public static final String IMG_PATH_CARPENTER_A = DIR_IMAGES + "stolar-A.png";
    public static final int WIDTH_CARPENTER = 80;
    public static final int HEIGHT_CARPENTER = 180;  // ratio 3.5
    /** original: 450x750 px */
    public static final String IMG_PATH_CARPENTER_B = DIR_IMAGES + "stolar-B.png";
    /** original: 450x750 px */
    public static final String IMG_PATH_CARPENTER_C = DIR_IMAGES + "stolar-C.png";
    /** original:  700x500 px */
    public static final String IMG_PATH_DESK = DIR_IMAGES + "pracovisko.png";
    public static final int WIDTH_DESK = 200; // ratio 3.5
    public static final int HEIGHT_DESK = 145; // ratio 3.5
    /** original: 532x356 px */
    public static final String IMG_PATH_TABLE = DIR_IMAGES + "order-stol.png";
    public static final int WIDTH_TABLE = 115;
    public static final int HEIGHT_TABLE = 75;
    /** original: 250x452 px */
    public static final String IMG_PATH_CHAIR = DIR_IMAGES + "order-stolicka.png";
    public static final int WIDTH_CHAIR = 30;
    public static final int HEIGHT_CHAIR = 95;
    /** original: 350x662 px */
    public static final String IMG_PATH_WARDROBE = DIR_IMAGES + "order-skrina.png";
    public static final int WIDTH_WARDROBE = 75;
    public static final int HEIGHT_WARDROBE = 140;
    /** original: 2000x1200 px */
    public static final String IMG_PATH_STORAGE = DIR_IMAGES + "sklad.png";
    public static final int WIDTH_STORAGE = 2220;
    public static final int HEIGHT_STORAGE = 1335;

    public static AnimImageItem createCarpenterA(double posX, double posY) {
        AnimImageItem ai = createCarpenterA();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createCarpenterB(double posX, double posY) {
        AnimImageItem ai = createCarpenterB();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createCarpenterC(double posX, double posY) {
        AnimImageItem ai = createCarpenterC();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createDesk(double posX, double posY) {
        AnimImageItem ai = createDesk();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createTable(double posX, double posY) {
        AnimImageItem ai = createTable();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createChair(double posX, double posY) {
        AnimImageItem ai = createChair();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createWardrobe(double posX, double posY) {
        AnimImageItem ai = createWardrobe();
        ai.setPosition(posX, posY);
        return ai;
    }

    public static AnimImageItem createCarpenterA() {
        return createCarpenter(IMG_PATH_CARPENTER_A);
    }

    public static AnimImageItem createCarpenterB() {
        return createCarpenter(IMG_PATH_CARPENTER_B);
    }

    public static AnimImageItem createCarpenterC() {
        return createCarpenter(IMG_PATH_CARPENTER_C);
    }

    public static AnimImageItem createDesk() {
        try {
            return new AnimImageItem(ImageIO.read(new File(IMG_PATH_DESK)), WIDTH_DESK, HEIGHT_DESK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AnimImageItem createTable() {
        try {
            return new AnimImageItem(ImageIO.read(new File(IMG_PATH_TABLE)), WIDTH_TABLE, HEIGHT_TABLE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AnimImageItem createChair() {
        try {
            return new AnimImageItem(ImageIO.read(new File(IMG_PATH_CHAIR)), WIDTH_CHAIR, HEIGHT_CHAIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AnimImageItem createWardrobe() {
        try {
            return new AnimImageItem(ImageIO.read(new File(IMG_PATH_WARDROBE)), WIDTH_WARDROBE, HEIGHT_WARDROBE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static AnimImageItem createCarpenter(String imageWithCarpType) {
        try {
            return new AnimImageItem(ImageIO.read(new File(imageWithCarpType)), WIDTH_CARPENTER, HEIGHT_CARPENTER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
