package com.example.taxy;

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
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;

import javafx.application.Application;
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
	}
}