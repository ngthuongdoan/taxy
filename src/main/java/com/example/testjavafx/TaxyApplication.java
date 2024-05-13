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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TaxyApplication extends Application {
	public static void main(String[] args) throws AWTException, InterruptedException {
		launch(args);
	}



	@Override
	public void start(Stage primaryStage) throws AWTException, InterruptedException {
		TaxyUI UI = new TaxyUI(primaryStage);
		try {
			UI.createUI();
			UI.run();
		} catch (NoPropertySetStreamException | UnexpectedPropertySetTypeException | AWTException | InterruptedException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		primaryStage.setTitle("Taxy");
//
//		StackPane card = new StackPane();
//
//
//        // Add a Button for running the file
//        Button runButton = new Button("Run");
//        runButton.setPadding(new Insets(10)); // Adjust padding as needed
//        card.add
//		runButton.setOnAction(new EventHandler<ActionEvent>() {
//			// Create the main HBox for the card layout
//
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					Robot robot = new Robot();
//					robot.setAutoDelay(300);
//					RobotManager robotUtils = new RobotManager(robot);
//
//					String projectRootPath = System.getProperty("user.dir");
//					String docxPath = projectRootPath + File.separator + "src\\main\\resources\\sample.docx";
//					System.out.println("Docx: " + docxPath);
//
//					File docxFile = new File(docxPath);
//					InputStream is = new FileInputStream(docxFile);
//					POIFSFileSystem poifs = new POIFSFileSystem(is);
//					is.close();
//					/* Read the summary information. */
//					DirectoryEntry dir = poifs.getRoot();
//					SummaryInformation si;
//
//					DocumentEntry siEntry = (DocumentEntry) dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
//					DocumentInputStream dis = new DocumentInputStream(siEntry);
//					PropertySet ps = new PropertySet(dis);
//					dis.close();
//					si = new SummaryInformation(ps);
//
//					System.out.println("asd: " + si.getPageCount());
//					// Execute the command to open the file
//					if (docxFile.exists()) {
//						Desktop.getDesktop().open(docxFile);
//					} else {
//						System.out.println("File does not exist: " + docxPath);
//					}
////				    p.waitFor();
//					System.out.println("Done");
//					// Wait for the file to open
//					Thread.sleep(10000);
//
//					int totalPages = getTotalFilePages("assets/sample.docx");
//					System.out.println("Total pages: " + totalPages);
//
//					int pageLimit = 10;
//					for (int i = 0; i < pageLimit; i++) {
//						pasteSignature(robotUtils);
//						refineSignature(robotUtils);
//						dragSignature(robotUtils);
//						robot.keyPress(KeyEvent.VK_PAGE_DOWN);
//						robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
//					}
//
//					robot.keyPress(KeyEvent.VK_S);
//					robot.keyRelease(KeyEvent.VK_S);
//					robot.keyPress(KeyEvent.VK_CONTROL);
//					robot.keyPress(KeyEvent.VK_W);
//					robot.keyRelease(KeyEvent.VK_W);
//					robot.keyRelease(KeyEvent.VK_CONTROL);
//				} catch (AWTException | IOException | InterruptedException | NoPropertySetStreamException
//						| UnexpectedPropertySetTypeException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		Scene scene = new Scene(card, 400, 200);
//		primaryStage.setScene(scene);
//		primaryStage.show();

	}
}