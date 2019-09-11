package gui;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class MainDisplay{
    private Container container;
    private JFrame mainFrame;
    
    public MainDisplay(SystemConstants systemConstants, ArrayList<Panel> allPanels) throws IOException {        
        mainFrame = new JFrame(systemConstants.MAIN_FRAME_TITLE);
        System.out.println("System is now active");
        
        container = mainFrame.getContentPane();
        container.setLayout(new GridBagLayout());
        container.setBackground(new Color(systemConstants.BACKGROUND_RGB[0]
                                        , systemConstants.BACKGROUND_RGB[1]
                                        , systemConstants.BACKGROUND_RGB[2]));
        
        
        //Change if the layout changes to only two flow panels
        GridBagConstraints c = new GridBagConstraints();
        for(int i = 0; i < allPanels.size(); i++){
            switch(i){
                case 0:
                    c.gridx = 1;
                    c.gridy = 0;
                    break;
                case 1:
                    c.gridx = 0;
                    c.gridy = 1;
                    c.insets = new Insets(10, 0, 0, 0); //top is first
                    break;
                case 2:
                    c.gridx = 1;
                    c.gridy = 1;
                    break;
                case 3:
                    c.gridx = 2;
                    c.gridy = 1;
                    break;
            }
            container.add(allPanels.get(i), c);
        }
        
        //CustomMenu mainFrameMenu = new CustomMenu("idCustomMenu");
        //mainFrame.setJMenuBar((JMenuBar)mainFrameMenu);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(systemConstants.SCREEN_DIMENSIONS);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setMinimumSize(new Dimension(1115, 703));
        mainFrame.setVisible(true);
        
        System.out.println("Frame Created and Displayed");
    }
    
    public int[] getContainerDimensions(){
        int[] dims = {container.getWidth(), container.getHeight()};
        return dims;
    }
    
    public int[] getMainFrameDimensions(){
        int[] dims = {mainFrame.getWidth(), mainFrame.getHeight()};
        return dims;
    }
    
    public Container getContainer(){
        return container;
    }
    
    public void setContainer(Container container){
        this.container = container;
    }
}
