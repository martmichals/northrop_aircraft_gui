package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GroundSpeedIndicator extends Panel{
    private BufferedImage groundSpeedSpinner;
    private BufferedImage groundSpeedGauge;
    private double speed; //mph
    
    public GroundSpeedIndicator(){
        super();
        groundSpeedSpinner = null;
        groundSpeedGauge = null; 
        speed = 0;
    }
    
    public GroundSpeedIndicator(String id, int[] dimensions, String groundSpeedSpinnerFileName, String groundSpeedGaugeFileName){
        super(id, dimensions);
       
        try{
            groundSpeedSpinner = ImageIO.read(new File(groundSpeedSpinnerFileName));
            groundSpeedGauge = ImageIO.read(new File(groundSpeedGaugeFileName));
        }catch(IOException ex){
            System.out.println("Error in obtaining ground speed indicator images (IO Exception)");
        }
        
        speed = 0;
    }
   
   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        double rotation = calculateRoll(); 
        
        AffineTransform at = new AffineTransform();
        at.translate(this.getWidth() / 2, this.getHeight() / 2);
        at.scale(0.40, 0.40); 
        at.translate(-groundSpeedGauge.getWidth()/2, -groundSpeedGauge.getHeight()/2);        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(groundSpeedGauge, at, null);
        
        AffineTransform at2 = new AffineTransform();
        at2.translate(this.getWidth() / 2, this.getHeight() / 2);
        at2.scale(0.4, 0.4);
        at2.rotate(rotation); 
        at2.translate(-groundSpeedSpinner.getWidth()/2, -groundSpeedSpinner.getHeight()/2);
        g2d.drawImage(groundSpeedSpinner, at2, null);
    } 
    
   @Override
    public void updatePanel(DataScraper dataScraper){
        speed = dataScraper.getSystemSpeed();
        
        this.repaint(); 
    } 
    
   @Override
    public void initialize(){
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1]));
    } 
    
    private double calculateRoll(){
        return (speed / 5.0) * 0.393;
    }
    
    public BufferedImage getGroundSpeedSpinner(){
        return groundSpeedSpinner;
    }
    
    public BufferedImage getGroundSpeedGauge(){
        return groundSpeedGauge;
    }
    
    public double getSpeed(){ 
       return speed;
    }
    
    public void setSpeed(double speed){
       this.speed = speed;
    }
    
    public void setGroundSpeedSpinner(BufferedImage groundSpeedSpinner){
        this.groundSpeedSpinner = groundSpeedSpinner;
    }
    
    public void setGroundSpeedGauge(BufferedImage groundSpeedGauge){
        this.groundSpeedGauge = groundSpeedGauge;
    }
} 
