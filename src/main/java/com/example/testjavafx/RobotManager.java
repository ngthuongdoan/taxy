package com.example.testjavafx;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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

    public void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
    }

    public Robot getRobot() {
        return this.robot;
    }
}