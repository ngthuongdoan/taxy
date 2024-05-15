package com.example.taxy;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

	public File getSignaturePath(Stage primaryStage, String initialSignaturePath) {
		// Create a FileChooser
		FileChooser fileChooser = new FileChooser();

// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

// Show open file dialog
		Stage stage = new Stage(); // Use your actual Stage here
		return fileChooser.showOpenDialog(stage);
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

	public void run(File file, File signature) {

		Robot robot;
		try {
			robot = new Robot();

			robot.setAutoDelay(200);
			RobotManager robotUtils = new RobotManager(robot);
			// Execute the command to open the file
			if (file.exists()) {
				Desktop.getDesktop().open(file);
			} else {
				System.out.println("File does not exist: " + file);
			}
			// Wait for the file to open
			robot.delay(10000);
			int pageLimit = 10;
			for (int i = 0; i < pageLimit; i++) {
				robotUtils.copyToClipboard(signature.getAbsolutePath());
				pasteSignature(robotUtils);
				refineSignature(robotUtils);
				dragSignature(robotUtils);
				robot.keyPress(KeyEvent.VK_PAGE_DOWN);
				robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
			}
			// Simulate F12 key press
			robot.keyPress(KeyEvent.VK_F12);
			// Release keys
			robot.keyRelease(KeyEvent.VK_F12);
			// Wait for the save dialog to open
			robot.delay(2000);
			// Get the file name
			String fileName = file.getName();

			// Get the name without extension
			String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));

			// Append "_signed" to the name
			String newName = nameWithoutExtension + "_signed";

			// Get the extension
			String extension = fileName.substring(fileName.lastIndexOf('.'));

			// Combine the new name with the extension
			String newFileName = newName + extension;
			for (char c : newFileName.toCharArray()) {
				if (c == '_') {
					// Special case for underscore
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_MINUS);
					robot.keyRelease(KeyEvent.VK_MINUS);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
					if (KeyEvent.CHAR_UNDEFINED == keyCode) {
						throw new RuntimeException("Key code not found for character '" + c + "'");
					}
					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
				}
			}
			// Press Enter to save the file
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(2000);

			robot.keyPress(KeyEvent.VK_F4);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_F4);
			robot.keyRelease(KeyEvent.VK_ALT);

		} catch (AWTException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
