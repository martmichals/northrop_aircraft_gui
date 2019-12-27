package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**  Custom panel used in order to generate the attitude indicator
 *   Changes display based on the orientation data from the aircraft
 **/
public class AttitudeIndicator extends Panel{
    private BufferedImage attitudeIndicatorOverlay;
    private BufferedImage attitudeIndicatorArtificialHorizon;
    private double pitchRadians;
    private double rollRadians;
    
    private AttitudeIndicatorStabilization stabilizer;
    
    /**
     * Constructor for the class
     * @param id : string id, attribute of parent class
     * @param dimensions: pixel dimensions, attribute of parent class
     * @param overlayFileName: filename for image put on top of the artificial horizon
     * @param artificialHorizonFileName : filename for artificial horizon
     */
    public AttitudeIndicator(String id, int[] dimensions, String overlayFileName, String artificialHorizonFileName){
        super(id, dimensions);
        
        try{
            attitudeIndicatorArtificialHorizon = ImageIO.read(new File(artificialHorizonFileName));
            attitudeIndicatorOverlay = ImageIO.read(new File(overlayFileName));
        }catch(IOException ex){
            System.out.println("Error in obtaining attitude indicator images (IO Exception)");
        }
        
        pitchRadians = 0;
        rollRadians = 0;
        stabilizer = new AttitudeIndicatorStabilization();
    }
    
    // Function that returns how much the artificial horizon is to be shifted for a given pitch
    private double getPitchTranslation(){
        return 13 * (pitchRadians / 0.0872665);
    }
    
    // Function that paints the panel, creating the attitude indicator based on object attributes
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        double upTrans = getPitchTranslation();
        double rotation = rollRadians; 
        
        AffineTransform at = new AffineTransform();
        at.translate(this.getWidth() / 2, this.getHeight() / 2);
        at.rotate(rotation); 
        at.scale(0.95, 0.95); 
        at.translate(-attitudeIndicatorArtificialHorizon.getWidth()/2 + 5, -attitudeIndicatorArtificialHorizon.getHeight()/2 + upTrans);        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(attitudeIndicatorArtificialHorizon, at, null);
        
       AffineTransform at2 = new AffineTransform();
       at2.translate(this.getWidth() / 2, this.getHeight() / 2);
       at2.scale(0.4, 0.4);
       at2.translate(-attitudeIndicatorOverlay.getWidth()/2, -attitudeIndicatorOverlay.getHeight()/2);
       g2d.drawImage(attitudeIndicatorOverlay, at2, null);
    }
    
    // Method to update the panel display based on the datascraper's attributes
    @Override
    public void updatePanel(DataScraper dataScraper){
        
        double[] orientation  = new double[3];
        orientation[1] = dataScraper.getSystemPitchAngle();
        orientation[0] = dataScraper.getSystemRollAngle();
        orientation[2] = 0;
        this.stabilizer.updateValues(orientation);
        
        rollRadians = stabilizer.getSmoothedRoll();
        pitchRadians = stabilizer.getSmoothedPitch();
        
        this.repaint(); 
    }
    
    @Override
    public void initialize(){
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1]));
    }
}
