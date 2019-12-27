package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

/**
 * Panel class in order to house multiple panels, in this program houses:
 *      - Attitude Indicator
 *      - Groundspeed Indicator
 *      - Heading Indicator
 */
public class FlowPanel extends Panel{
    ArrayList<Panel> allPanels;
    //Make sure to initialize the panels once they are loaded in 
    public FlowPanel(){
        super();
        allPanels = null;
    }
    
    public FlowPanel(String id, int[] dimensions, ArrayList<Panel> allPanels){
        super(id, dimensions);
        this.allPanels = allPanels;
    }
    
    @Override
    public void updatePanel(DataScraper dataScraper){
        allPanels.forEach((p) -> {
            p.updatePanel(dataScraper);
        });
    }
    
    @Override
    public void initialize(){
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1])); 
        this.setBackground(new Color(216, 210, 158));
        allPanels.forEach((p) -> {
           p.initialize();
           this.add(p);
        });
    }
}
