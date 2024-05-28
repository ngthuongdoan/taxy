package com.example.taxy;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TaxyApplication extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws AWTException, InterruptedException {
		TaxyUI UI = new TaxyUI(primaryStage);
		try {
			String projectRootPath = System.getProperty("user.dir");
			String wordPath = projectRootPath + "/src/main/resources/sample.pdf";
			getTotalPages(wordPath);
			UI.createUI();
			UI.run();
		} catch (NoPropertySetStreamException | UnexpectedPropertySetTypeException | AWTException | InterruptedException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getTotalPages(String filePath) {
		File file = new File(filePath);

		try (PDDocument document = PDDocument.load(file)) {
			int totalPages = document.getNumberOfPages();
			System.out.println("Total pages: " + totalPages);
			return totalPages;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}