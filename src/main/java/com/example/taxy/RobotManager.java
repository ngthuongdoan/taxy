package com.example.taxy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public record RobotManager(Robot robot) {

    public void pasteFromClipboard() {
        this.robot.keyPress(KeyEvent.VK_CONTROL);
        this.robot.keyPress(KeyEvent.VK_V);
        this.robot.keyRelease(KeyEvent.VK_CONTROL);
        this.robot.keyRelease(KeyEvent.VK_V);
    }

    public void focus() {
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void runCurrentProgram() {
        this.robot.keyPress(KeyEvent.VK_SHIFT);
        this.robot.keyPress(KeyEvent.VK_F10);
        this.robot.keyRelease(KeyEvent.VK_SHIFT);
        this.robot.keyRelease(KeyEvent.VK_F10);
    }

    public void click(int x, int y) {
        this.robot.mouseMove(x, y);
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void copyToClipboard(String imagePath) {
        try {
            // Read the image from the file
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            // Resize the image
            int width = 600;
            int height = 368;
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            // Convert the scaled image back to a BufferedImage
            BufferedImage image = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = image.createGraphics();
            bGr.drawImage(scaledImage, 0, 0, null);
            bGr.dispose();

            // Remove the background
            int colorToRemove = image.getRGB(0, 0); // Assuming the top-left pixel is the background color
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (image.getRGB(x, y) == colorToRemove) {
                        image.setRGB(x, y, 0x00ffffff); // Set to transparent
                    }
                }
            }

            // Create a Transferable implementation that contains the image
            Transferable imageTransferable = new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataFlavor.imageFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.imageFlavor.equals(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (!isDataFlavorSupported(flavor)) {
                        throw new UnsupportedFlavorException(flavor);
                    }
                    return image;
                }
            };

            // Get the system clipboard and set its contents to the image
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(imageTransferable, null);
        } catch (IOException e) {
            System.out.println("Error copying image to clipboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Robot getRobot() {
        return this.robot;
    }
}