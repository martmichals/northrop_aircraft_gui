package gui;

import javax.swing.JPanel;

public class Panel extends JPanel{
    private String id;
    private int[] dimensions;
    
    public Panel(){
        id = "";
        dimensions = new int[2];
    }
    
    public Panel(String id, int[] dimensions){
        this.id = id;
        this.dimensions = dimensions;
    }
    
    //Methods to have custom implementation in each child class
    public void updatePanel(DataScraper dataScraper){}
    public void initialize(){}
    
    public String getId(){
        return id;
    }
    
    public int[] getDimensions(){
        return dimensions;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public void setDimensions(int[] dimensions){
        this.dimensions = dimensions;
    }
}
