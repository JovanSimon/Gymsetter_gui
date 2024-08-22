package gui.core;

public abstract class ApplicationFramework {
    protected Gui gui;

    public ApplicationFramework(){

    }

    public abstract void run();

    public void initialise(Gui gui){
        this.gui = gui;
    }
}
