package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UserInputIndicator extends Panel{
    private String inputName;
    private BufferedImage gauge;
    private BufferedImage indicator;
    private BufferedImage tickMarks;
    private int arduinoValue;
    private final int inputType; //0-Throttle, 1-rudder, 2-elevator, 3-ailerons
    
    public UserInputIndicator(){
        super();
        inputName = "";
        gauge = null;
        indicator = null;
        tickMarks = null;
        arduinoValue = 0;
        inputType = -1;
    }
    
    public UserInputIndicator(String id, int[] dimensions, String inputName, String gaugeFileName, String indicatorFileName, String tickMarkFileName, int inputType){
        super(id, dimensions);
        this.inputName = inputName;
        
        try{
            gauge = ImageIO.read(new File(gaugeFileName));
            indicator = ImageIO.read(new File(indicatorFileName));
            tickMarks = ImageIO.read(new File(tickMarkFileName));
        }catch(IOException ex){
            System.out.println("Error in obtaining user input indicator images (IO Exception)");
        }
        
        this.arduinoValue = 0;
        this.inputType = inputType;
    }
    
   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        boolean flipper = false;
        if(inputType > 1)
            flipper = true;
        
        int textTranslation = 5;
        if(flipper)
            textTranslation = textTranslation * -1;
        double translation = 0; 
        //Range on the translation: -277 to 277 
        //Once arduino range is known debug and use the written mapValue() function to display in the GUI
        
        double scaleFactor = 0.35;
        AffineTransform at = new AffineTransform();
        at.translate(this.getWidth() / 2, this.getHeight() / 2);
        if(flipper)
            at.rotate(Math.PI);
        at.scale(scaleFactor, scaleFactor); 
        at.translate(-gauge.getWidth()/2, -gauge.getHeight()/2 + textTranslation);        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(gauge, at, null);
        
        AffineTransform at2 = new AffineTransform();
        at2.translate(this.getWidth() / 2, this.getHeight() / 2);
        if(flipper)
            at2.rotate(Math.PI);
        at2.scale(scaleFactor, scaleFactor);
        at2.translate(-indicator.getWidth()/2, -indicator.getHeight()/2 + translation + textTranslation);
        g2d.drawImage(indicator, at2, null);
        
        AffineTransform at3 = new AffineTransform();
        at3.translate(this.getWidth() / 2, this.getHeight() / 2);
        if(flipper)
            at3.rotate(Math.PI);
        at3.scale(scaleFactor, scaleFactor);
        at3.translate(-tickMarks.getWidth()/2, -tickMarks.getHeight()/2 + textTranslation);
        g2d.drawImage(tickMarks, at3, null);
        
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 15));
        if(!flipper)
            g2d.drawString(inputName, 1, 16);
        else if(inputType != 3)
            g2d.drawString(inputName, 3, 16);
        else
            g2d.drawString(inputName, 7, 16);
    } 
    
   @Override
   public void updatePanel(DataScraper dataScraper){
        arduinoValue = dataScraper.getUserInputs()[inputType];
        this.repaint();
   }
   
   @Override
    public void initialize(){
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1]));
        this.setBackground(new Color(216, 210, 158));
    } 
    
    public double mapValue(double inputRangeLow, double inputRangeHigh, double displayRangeLow, double displayRangeHigh, double value){
        double lengthInput = inputRangeHigh - inputRangeLow;
        double lengthDisplay  = displayRangeHigh - displayRangeLow; 
        double posInInputRange = value - inputRangeLow;
        double adjustedPos = posInInputRange * (lengthInput / lengthDisplay); 
                
        return displayRangeLow + adjustedPos;
    }
    
    public String getInputName(){
        return inputName;
    }
    
    public BufferedImage getGauge(){
        return gauge;
    }
    
    public BufferedImage getIndicator(){
        return indicator;
    }
    
    public BufferedImage getTickMarks(){
        return tickMarks;
    }
    
    public int getArduinoValue(){
        return arduinoValue;
    }

    public int getInputType(){
        return inputType;
    }
    
    public void setInputName(String inputName){
        this.inputName = inputName;
    }
    
    public void setGauge(BufferedImage gauge){
        this.gauge = gauge;
    }
    
    public void setIndicator(BufferedImage indicator){
        this.indicator = indicator;
    }
    
    public void setTickMarks(BufferedImage tickMarks){
        this.tickMarks = tickMarks;
    }
    
    public void setArduinoValue(int valueToDisplay){
        this.arduinoValue = valueToDisplay;
    }
}
