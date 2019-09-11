package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;

public class RecentImage extends Panel{
    private BufferedImage recentImage;
    
    public RecentImage(){
        recentImage = null;
    }
    
    public RecentImage(String id, int[] dimensions, BufferedImage recentImage){
        super(id, dimensions);
        this.recentImage = recentImage;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        /**Paint the desired image here once the image file and procedure is known
         * Also paint the time that the image was received at on top of the image (top right)
         */
        
    }
    
    @Override
    public void initialize(){
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1]));
        //Remove once the actual image and image dimensions are known (below)
        this.setBorder(BorderFactory.createLineBorder(Color.black));//Remove the uneeded class imports when unneccessary as well (Color & BorderFactory)
    }
    
    @Override
    public void updatePanel(DataScraper dataScraper){/*Fill code to fill image here*/}
    
    public BufferedImage getRecentImage(){
        return recentImage;
    }
    
    public void setRecentImage(BufferedImage recentImage){
        this.recentImage = recentImage;
    }
}
