package gui.view;

import gui.view.typesOfViews.LoginView;
import gui.view.typesOfViews.WorkView;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
@Setter
@Getter
public class MainFrame extends JFrame {
    private static MainFrame instance;
    private JPanel currentPanel;

    private MainFrame(){

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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("GymSetter");
        currentPanel = LoginView.getInstance();

        this.add(currentPanel);
    }

    public void changePanel(JPanel panelToSet){
        remove(currentPanel);
        currentPanel = panelToSet;
        add(currentPanel);
        revalidate();
        repaint();
    }

    public static MainFrame getInstance(){
        if(instance == null) {
            instance = new MainFrame();
            instance.initialise();
        }
        return instance;
    }

}
