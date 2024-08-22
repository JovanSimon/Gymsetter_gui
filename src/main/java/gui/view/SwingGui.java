package gui.view;

import gui.core.Gui;

public class SwingGui implements Gui {
    @Override
    public void start() {
        MainFrame.getInstance().setVisible(true);
        MainFrame.getInstance().setLocationRelativeTo(null);
    }
}
