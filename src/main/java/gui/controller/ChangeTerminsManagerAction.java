package gui.controller;

import gui.controller.popup.ChangeTerminsManagerBox;
import gui.controller.popup.ListTerminsBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ChangeTerminsManagerAction extends AbstractAction {
    public ChangeTerminsManagerAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        putValue(NAME,"List trainings for manager");
        putValue(SHORT_DESCRIPTION,"List trainings for manager");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ChangeTerminsManagerBox().display();
    }
}
