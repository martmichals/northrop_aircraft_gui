package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HeadingIndicator extends Panel{
    private BufferedImage headingBackdrop;
    private BufferedImage headingSpinner;
    private double heading;                   //value ranging from 0-360, heading 
    private GPSHistory historyAssist;
    
    public HeadingIndicator(){
        super();
        headingBackdrop = null;
        headingSpinner = null; 
        historyAssist = null;
        heading = 0;
    }
    
    public HeadingIndicator(String id, int[] dimensions, String headingBackdropFileName, String headingSpinnerFileName){
        super(id, dimensions);
       
        try{
            headingBackdrop = ImageIO.read(new File(headingBackdropFileName));
            headingSpinner = ImageIO.read(new File(headingSpinnerFileName));
        }catch(IOException ex){
            System.out.println("Error in obtaining ground speed indicator images (IO Exception)");
        }
        
        historyAssist = new GPSHistory();
        heading = 0;
    }
   
   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        double rotation = (heading * Math.PI) / 180.0; 
        
        double scaleFactor = 0.845;
        AffineTransform at = new AffineTransform();
        at.translate(this.getWidth() / 2, this.getHeight() / 2);
        at.scale(scaleFactor, scaleFactor); 
        at.translate(-headingBackdrop.getWidth()/2, -headingBackdrop.getHeight()/2);        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(headingBackdrop, at, null);
        
        AffineTransform at2 = new AffineTransform();
        at2.translate(this.getWidth() / 2, this.getHeight() / 2);
        at2.scale(scaleFactor, scaleFactor);
        at2.rotate(rotation); 
        at2.translate(-headingSpinner.getWidth()/2, -headingSpinner.getHeight()/2);
        g2d.drawImage(headingSpinner, at2, null);
    } 
    
   @Override
    public void updatePanel(DataScraper dataScraper){
        // heading = dataScraper.getSystemHeading();
        GPSCoordinate point = new GPSCoordinate(dataScraper.getSystemLatitude(), 
                                                dataScraper.getSystemLongitude());
        historyAssist.addGPSCoordinateToHistory(point);
        heading = historyAssist.getHeadingInDegrees();
        this.repaint(); 
    } 
    
   @Override
    public void initialize(){
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1]));
    } 
    
    public BufferedImage getHeadingBackdrop(){
        return headingBackdrop;
    }
    
    public BufferedImage getHeadingSpinner(){
        return headingSpinner;
    }
    
    public double getHeading(){
        return heading;
    }
    
    public void setHeadingBackdrop(BufferedImage headingBackdrop){
        this.headingBackdrop = headingBackdrop;
    }
    
    public void setHeadingSpinner(BufferedImage headingSpinner){
        this.headingSpinner = headingSpinner;
    }
    
    public void setHeading(double heading){
        this.heading = heading;
    }
}
