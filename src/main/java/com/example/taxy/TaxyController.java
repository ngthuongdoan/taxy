package com.example.taxy;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaxyController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}