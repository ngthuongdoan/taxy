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
import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.ooxml.POIXMLProperties.ExtendedProperties;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
	public static void main(String[] args) throws AWTException, InterruptedException {
		launch(args);
	}

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

	@Override
	public void start(Stage primaryStage) throws AWTException, InterruptedException {

		primaryStage.setTitle("Taxy");
		Button btn = new Button();

		btn.setText("Run");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Robot robot = new Robot();
					robot.setAutoDelay(300);
					RobotManager robotUtils = new RobotManager(robot);
//					copySignature();
					String projectRootPath = System.getProperty("user.dir");
					String docxPath = projectRootPath + File.separator + "src\\main\\resources\\sample.docx";
					System.out.println("Docx: " + docxPath);
					/* Open the Word Document */

//					FileInputStream stream;
//
//					stream = new FileInputStream(new File(docxPath));
//					XWPFDocument docx = new XWPFDocument(stream);
//					ExtendedProperties props = docx.getProperties().getExtendedProperties();
//					System.out.println("a: "+ props.getPages());
//					stream.close();
					File docxFile = new File(docxPath);
					InputStream is = new FileInputStream(docxFile);
					POIFSFileSystem poifs = new POIFSFileSystem(is);
					is.close();
					/* Read the summary information. */
					DirectoryEntry dir = poifs.getRoot();
					SummaryInformation si;

					DocumentEntry siEntry = (DocumentEntry) dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
					DocumentInputStream dis = new DocumentInputStream(siEntry);
					PropertySet ps = new PropertySet(dis);
					dis.close();
					si = new SummaryInformation(ps);

					System.out.println("asd: "+si.getPageCount());
					// Execute the command to open the file
					if (docxFile.exists()) {
						Desktop.getDesktop().open(docxFile);
					} else {
						System.out.println("File does not exist: " + docxPath);
					}
//				    p.waitFor();
					System.out.println("Done");
					// Wait for the file to open
					Thread.sleep(10000);

					int totalPages = getTotalFilePages("assets/sample.docx");
					System.out.println("Total pages: " + totalPages);

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
				} catch (AWTException | IOException | InterruptedException | NoPropertySetStreamException | UnexpectedPropertySetTypeException e) {
					e.printStackTrace();
				}
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
	}
}