package gui;

import gui.core.ApplicationFramework;
import gui.core.Gui;
import gui.view.SwingGui;

public class AppCore extends ApplicationFramework {
    private static AppCore instance;

    private AppCore(){

    }

    public static AppCore getInstance(){
        if(instance == null)
            instance = new AppCore();
        return instance;
    }

    @Override
    public void run() {
        this.gui.start();
    }

    public static void main(String[] args) {
        ApplicationFramework appCore = AppCore.getInstance();
        Gui gui = new SwingGui();

        appCore.initialise(gui);
        appCore.run();
    }
}
