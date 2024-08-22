package gui.view.typesOfViews;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
@Getter
@Setter

public class WorkView extends JPanel {
    private static WorkView instance;
    private String token;
    private JToolBar toolBar;
    private WorkView(){

    }

    private void initialise(){
        initialiseGui();
    }

    private void initialiseGui() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setSize(screenWidth - 300 , screenHeight - 300 );
        toolBar = new Toolbar();
        add(toolBar, BorderLayout.NORTH);
    }

    public static WorkView getInstance(){
        if (instance == null) {
            instance = new WorkView();
            instance.initialise();
        }
        return instance;
    }
}
