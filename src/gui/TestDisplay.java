package gui;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class TestDisplay{
    private Container container;
    private JFrame mainFrame;
    
    public TestDisplay() throws IOException{
        mainFrame = new JFrame("Testing Frame for New Panels");
        System.out.println("Testing Frame is Created");
        
        container = mainFrame.getContentPane();
        container.setLayout(new FlowLayout());
        container.setBackground(new Color(216, 210, 158));
        
        int[] indicatorDimensions = {36, 205};
        ArrayList<Panel> testPanels = new ArrayList<>();
        testPanels.add(new UserInputIndicator("idUserInputIndicator", indicatorDimensions, "THR", "InputIndicatorGauge.png", "InputIndicatorIndicator.png", "InputIndicatorTickMarks.png", -1));
        testPanels.add(new UserInputIndicator("idUserInputIndicator", indicatorDimensions, "THR", "InputIndicatorGauge.png", "InputIndicatorIndicator.png", "InputIndicatorTickMarks.png", 2));
        //testPanels.add()
        //testPanels.add()
        System.out.println("Elements now Added to the Testing Frame");
        
        for(Panel p: testPanels){
            p.initialize();
            container.add(p);
        }    
        
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        System.out.println("Testing Frame is Now on Display");
    }
}
