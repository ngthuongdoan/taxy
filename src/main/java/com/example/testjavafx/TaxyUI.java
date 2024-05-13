package com.example.testjavafx;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;

import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TaxyUI {
	Stage primaryStage;

	public TaxyUI(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@SuppressWarnings("unchecked")
	public void createTable(TableView<Item> tblFiles, TaxyUtils taxyUtils, FolderWrapper folderWrapper)
			throws NoPropertySetStreamException, UnexpectedPropertySetTypeException, IOException {
		File[] files = taxyUtils.getFilesInFolder(this.primaryStage, folderWrapper.getFolder());
		TableColumn<Item, String> colFile = new TableColumn<Item, String>("File");
		TableColumn<Item, String> actionCol = new TableColumn<Item, String>("Action");

		for (int i = 0; i < files.length; i++) {
			String filePath = files[i].getAbsolutePath();
			if (filePath.endsWith(".doc") || filePath.endsWith(".docx")) {
				tblFiles.getItems().addAll(new Item(filePath));
				actionCol.setCellValueFactory(new PropertyValueFactory<Item, String>("action"));

				Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory = //
						new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {
							public TableCell<Item, String> call(final TableColumn<Item, String> param) {
								final TableCell<Item, String> cell = new TableCell<Item, String>() {

									final Button btn = new Button("Chạy");

									@Override
									public void updateItem(String item, boolean empty) {
										super.updateItem(item, empty);
										if (empty) {
											setGraphic(null);
											setText(null);
										} else {
											btn.setOnAction(event -> {
												Item file = getTableView().getItems().get(getIndex());
												taxyUtils.run(new File(file.getFilePath()));
											});
											setGraphic(btn);
											setText(null);
										}
									}
								};
								return cell;
							}
						};

				actionCol.setCellFactory(cellFactory);
			}
		}

		colFile.setCellValueFactory(new PropertyValueFactory<Item, String>("filePath"));
		tblFiles.getColumns().addAll(colFile, actionCol);
	}

	public void createUI() throws AWTException, InterruptedException, NoPropertySetStreamException,
			UnexpectedPropertySetTypeException, IOException {
		VBox vbox = new VBox();

		String projectRootPath = System.getProperty("user.dir");
		String initialFolderPath = projectRootPath + File.separator + "src\\main\\resources\\";
		TaxyUtils taxyUtils = new TaxyUtils();
		HBox topControls = new HBox();
		topControls.setPadding(new Insets(12));
		Button browseFolderBtn = new Button("Thư mục");
		topControls.getChildren().add(browseFolderBtn);
		FolderWrapper folderWrapper = new FolderWrapper(new File(initialFolderPath));
		TableView<Item> tblFiles = new TableView<Item>();

		browseFolderBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				folderWrapper.setFolder(taxyUtils.getFileFolder(primaryStage, initialFolderPath));
				// Refresh the table view with the new files
				try {
					refreshTableView(tblFiles, taxyUtils, folderWrapper);
				} catch (NoPropertySetStreamException | UnexpectedPropertySetTypeException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Separator sep = new Separator();
		this.createTable(tblFiles, taxyUtils, folderWrapper);
		// Create a Text node with the specified text

		Label paddedText = new Label("Hãy copy chữ ký của bạn trước khi khởi chạy");
		paddedText.setPadding(new Insets(12)); // Set the font style to italic
		Font font = Font.font("Inter", FontWeight.NORMAL, FontPosture.ITALIC, 14);
		paddedText.setFont(font);
		vbox.getChildren().addAll(topControls, tblFiles, sep, paddedText);

		// Create a scene and set it on the stage
		Scene scene = new Scene(vbox, 800, 400);
		this.primaryStage.setTitle("Taxy");
		this.primaryStage.setScene(scene);
	}

	private void refreshTableView(TableView<Item> tblFiles, TaxyUtils taxyUtils, FolderWrapper folderWrapper)
			throws NoPropertySetStreamException, UnexpectedPropertySetTypeException, IOException {
		// Clear the current items in the table view
		tblFiles.getItems().clear();

		// Get the new files from the updated folder
		File[] newFiles = taxyUtils.getFilesInFolder(this.primaryStage, folderWrapper.getFolder());

		// Add the new files to the table view
		for (int i = 0; i < newFiles.length; i++) {
			String filePath = newFiles[i].getAbsolutePath();
			tblFiles.getItems().addAll(new Item(filePath));
		}
	}

	public void run() {
		this.primaryStage.show();
	}
}
