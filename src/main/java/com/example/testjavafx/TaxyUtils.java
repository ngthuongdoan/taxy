package com.example.testjavafx;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class TaxyUtils {
	// Custom Transferable implementation
	static class ImageTransferable implements Transferable {
		private final Image image;

		public ImageTransferable(Image image) {
			this.image = image;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
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
	}

	private static final int SIGNATURE_POSITION_X = 910;
	private static final int SIGNATURE_POSITION_Y = 550;

	private static void copySignature() throws IOException {
		try {
			String projectRootPath = System.getProperty("user.dir");
			String signaturePath = projectRootPath + File.separator + "signature.png";
			File signatureFile = new File(signaturePath);
			// Step 1: Read the image from the file
			BufferedImage image = ImageIO.read(signatureFile);

			// Step 2: Create an Image Transferable
			ImageTransferable transferable = new ImageTransferable(image);

			// Step 3: Copy to Clipboard
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
			System.out.println("Image copied to clipboard.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void pasteSignature(RobotManager robot) {
		robot.click(SIGNATURE_POSITION_X, SIGNATURE_POSITION_Y);
		robot.pasteFromClipboard();
	}

	private static void refineSignature(RobotManager robot) {
		robot.click(450, 550);
		robot.click(500, 550);
		robot.click(640, 630);
		robot.click(450, 633);
	}

	private static void dragSignature(RobotManager robot) {
		Robot _robot = robot.getRobot();
		_robot.mouseMove(450, 550);
		_robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		_robot.mouseMove(SIGNATURE_POSITION_X, SIGNATURE_POSITION_Y);
		_robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	private static int getTotalFilePages(String filePath) {
		// TODO: Implement the logic to get the total number of pages in the file
		return 0;
	}

	public File getFileFolder(Stage primaryStage, String initialFolderPath) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose folder");

		File defaultDirectory = new File(initialFolderPath);
		chooser.setInitialDirectory(defaultDirectory);

		File selectedDirectory = chooser.showDialog(primaryStage);

		return selectedDirectory;
	}

	public File[] getFilesInFolder(Stage primaryStage, File folder)
			throws IOException, NoPropertySetStreamException, UnexpectedPropertySetTypeException {
		File[] files = folder.listFiles();
		for (File file : files) {
			System.out.println("Files: " + file);
		}

		System.out.println("Done");
		return files;
	}

	public void run(File file) {

		Robot robot;
		try {
			robot = new Robot();

			robot.setAutoDelay(300);
			RobotManager robotUtils = new RobotManager(robot);
			// Execute the command to open the file
			if (file.exists()) {
				Desktop.getDesktop().open(file);
			} else {
				System.out.println("File does not exist: " + file);
			}
			// Wait for the file to open
			Thread.sleep(10000);

			int pageLimit = 10;
			for (int i = 0; i < pageLimit; i++) {
				pasteSignature(robotUtils);
				refineSignature(robotUtils);
				dragSignature(robotUtils);
				robot.keyPress(KeyEvent.VK_PAGE_DOWN);
				robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
			}

			robot.keyPress(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_S);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
