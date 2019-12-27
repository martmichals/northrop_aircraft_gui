package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;


/** Custom panel used in order to generate a map and trace the path of the aircraft 
 *  during its flight
 */
public class GPSTrackingMap extends Panel{
    private static final String MAPS_API_KEY = "YOUR_STATIC_MAPS_KEY";
    private static final String URL_START = "https://maps.googleapis.com/maps/api/staticmap?";
    public static final String DEFAULT_ZOOM = "17";
    public static final double[] SCOPE_ZOOM_DEFAULT = {0.0036, 0.0064};
    
    public static final String ZOOM = "17";
    
    private String urlRequest;
    private BufferedImage mapImage;
    private BufferedImage gpsFrameImage;
    
    //Latitude, Longitude
    private double[] gpsMapCenter;
    private double[] GPSScope;
    private double[] currentGPSPoint;
    
    private GPSHistory pathAssist;
    
    /**
     * Constructor for this custom class
     * @param id : required for parent class
     * @param dimensions : required for parent class
     * @param gpsFrameImageName : relative path for image used as the frame for the map
     */
    public GPSTrackingMap(String id, int[] dimensions, String gpsFrameImageName){
        super(id, dimensions);
        urlRequest = "";
        mapImage = null;
        gpsMapCenter = null;
        GPSScope = null;
        currentGPSPoint = null;
        
        try{
            gpsFrameImage = ImageIO.read(new File(gpsFrameImageName));
        }catch(IOException ex){
            System.out.println("Error in obtaining gps frame image (IO Exception)");
        }
        
        pathAssist = new GPSHistory();
    }
    
    /**
     * Method to initialize the display based on datascraper's attributes
     * Fetches required image from Google's Static Maps API
     * @param dataScraper : DataScraper object with current data
     */
    public void initializeWithValidGPS(DataScraper dataScraper) throws IOException{
        while(!dataScraper.checkGPSReadiness()){
            dataScraper.scrapeForData();
            System.out.println("Initial GPS Point is {0, 0}, rechecking...");
        }
        
        gpsMapCenter = new double[2];
        gpsMapCenter[0] = dataScraper.getSystemLatitude();
        gpsMapCenter[1] = dataScraper.getSystemLongitude();
        System.out.println("Valid GPS point gathered: " + gpsMapCenter[0] + ", "
                            + gpsMapCenter[1]);
        currentGPSPoint = gpsMapCenter;
        
        this.packURL();
        URL url = new URL(urlRequest);
        mapImage = ImageIO.read(url);
        
        this.fillGPSScope();
    }
    
    // Method that fills the attribute "GPSScope"
    private void fillGPSScope(){
        int zoomDifference = Integer.parseInt(DEFAULT_ZOOM) - Integer.parseInt(ZOOM);
        double scaleFactor = Math.pow(2.0, zoomDifference);
        
        GPSScope = new double[2];
        GPSScope[0] = scaleFactor * SCOPE_ZOOM_DEFAULT[0];
        GPSScope[1] = scaleFactor * SCOPE_ZOOM_DEFAULT[1];
    }
    
    // Packs urlRequest with the proper url needed to fetch the map required to run the program
    private void packURL(){
        urlRequest = "";
        
        //Start of url, same for every url request
        urlRequest += URL_START;
        
        //Center of the plot, the gps point gathered as the center
        urlRequest += "center=" + Double.toString(gpsMapCenter[0]) 
                    + "," + Double.toString(gpsMapCenter[1]);
        
        //Sets the zoom level of the map
        urlRequest += "&zoom=" + ZOOM;
        
        //Sets the size of the map width(pp) x height(pp)
        urlRequest += "&size=" + Integer.toString(super.getDimensions()[0])
                   + "x" + Integer.toString(super.getDimensions()[1]);
        
        //Sets the map type of the map
        urlRequest += "&maptype=" + "hybrid";
        
        //Uses custom API key to link request to Google user
        urlRequest += "&key=" + MAPS_API_KEY;
        
        System.out.println("Sending image request to Google API");
    }
    
    // Converts GPS coordinates of the plane to plottable XY coordinates on the map
    private double[] convertGPSToPlottable(){
        //Initial point, current point
        double latNot, longNot;
        latNot = gpsMapCenter[0];
        longNot = gpsMapCenter[1];
        double currentLat, currentLong;
        currentLat = currentGPSPoint[0];
        currentLong = currentGPSPoint[1];
        
        //Getting the change from the initial GPS position
        double deltaLong = currentLong - longNot;
        double deltaLat = currentLat - latNot;
        
        //Checking in the GPS Scope to see if point falls in
        double halfLongScope = GPSScope[1] / 2;
        double halfLatScope = GPSScope[0] / 2;
        if((Math.abs(deltaLong)) >= halfLongScope)
            return null;
        if((Math.abs(deltaLat)) >= halfLatScope)
            return null;
        
        //Converting deltaGPS to deltaXY
        double halfXScope, halfYScope;
        halfXScope = super.getDimensions()[0] / 2.0;
        halfYScope = super.getDimensions()[1] / 2.0;
        
        double deltaX, deltaY;
        deltaX = halfXScope * (deltaLong / halfLongScope);
        deltaY = halfYScope * (deltaLat / halfLatScope);
        
        //Using deltaX and deltaY in order generate a point on the image
        double xVal, yVal;
        xVal = halfXScope + deltaX;
        yVal = halfYScope - deltaY;
        double[] pointOnImage = {xVal,  yVal};
        
        pathAssist.addPathPointToHistory(pointOnImage);
        return pointOnImage;
    }
    
    // Method to properly paint all components onto the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        double scaleFactor = 1.0;
        AffineTransform at = new AffineTransform();
        at.translate(this.getWidth() / 2, this.getHeight() / 2);
        at.scale(scaleFactor, scaleFactor); 
        at.translate(-mapImage.getWidth()/2, -mapImage.getHeight()/2);        
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(mapImage, at, null);
        
        
        // Code that plots the path of the aircraft
        double[] point = convertGPSToPlottable();
        Line2D.Double[] path = pathAssist.getPath();
        
        if(point != null && path != null){
            g2d.setPaint(Color.RED);
            Ellipse2D currentPoint = new Ellipse2D.Double(point[0] - 2.5, point[1] - 2.5, 5, 5);
            for(Line2D.Double line: path)
                g2d.draw(line);
            g2d.fill(currentPoint);
        }
        
        AffineTransform at2 = new AffineTransform();
        at.translate(this.getWidth() / 2, this.getHeight() / 2);
        g2d.drawImage(gpsFrameImage, at2, null);
    }
    
    // Initializes the panel
    @Override
    public void initialize(){
        this.setPreferredSize(new Dimension(super.getDimensions()[0], super.getDimensions()[1]));
    }
    
    // Updates the panel with new data inside dataScraper
    @Override
    public void updatePanel(DataScraper dataScraper){
        currentGPSPoint = new double[2];
        currentGPSPoint[0] = dataScraper.getSystemLatitude();
        currentGPSPoint[1] = dataScraper.getSystemLongitude();
        
        this.repaint();  
    } 
}
