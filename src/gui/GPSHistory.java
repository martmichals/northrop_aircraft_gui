package gui;

import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Class in order to track the history of the GPS Coordinates
 * Used to draw out the path of the aircraft as it is flying over the map
 */
public class GPSHistory {
    private ArrayList<GPSCoordinate> coordinateHistory;
    private ArrayList<double[]> pathPointHistory;
    private int PATH_SIZE = 50;
    private int MAX_SIZE = 1500;
    public final boolean DEBUG_PRINT = false; 
    private double headingRadians;
    
    // Default Constructor
    public GPSHistory(){
        coordinateHistory = new ArrayList();
        pathPointHistory = new ArrayList();
    }
    
    // Method to add a coordinate to history, left shift if max size is exceeded
    public void addGPSCoordinateToHistory(GPSCoordinate coordinate){
        if(coordinateHistory.size() == MAX_SIZE){
            ArrayList<GPSCoordinate> temp = new ArrayList();
            for(int i = 1; i < coordinateHistory.size(); i++){
                temp.add(coordinateHistory.get(i));
            }
            temp.add(coordinate);
            coordinateHistory = temp;
            
            if(DEBUG_PRINT){
                for(GPSCoordinate p: coordinateHistory){
                    System.out.println("****************");
                    System.out.println(p);
                }
            }
        }else{
            coordinateHistory.add(coordinate);
        }
    }
    
    // Method to add a coordinate to history, left shift if max size is exceeded
    // This adds to path history, which is the array to draw out the path of the aircraft
    public void addPathPointToHistory(double[] pathPoint){
        //Check for null condition
        if(pathPoint == null)
            return;
        
        //Makes sure that there are no duplicate points
        if(pathPointHistory.size() != 0){
            double xLast = pathPointHistory.get(pathPointHistory.size() - 1)[0];
            double yLast = pathPointHistory.get(pathPointHistory.size() - 1)[1];
            if(xLast == pathPoint[0] && yLast == pathPoint[1])
                return;
        }
        
        //Adds to the path array
        if(pathPointHistory.size() == PATH_SIZE){
            ArrayList<double[]> temp = new ArrayList();
            for(int i = 1; i < pathPointHistory.size(); i++){
                temp.add(pathPointHistory.get(i));
            }
            temp.add(pathPoint);
            pathPointHistory = temp;
        }else{
            pathPointHistory.add(pathPoint);
        }
    }
    
    public Line2D.Double[] getPath(){
        Line2D.Double[] path = null;
        
        if(pathPointHistory.size() > 1){
            path = new Line2D.Double[pathPointHistory.size() - 1];
            
            for(int i = 1; i < pathPointHistory.size(); i++){
                double[] point1 = pathPointHistory.get(i);
                double[] point2 = pathPointHistory.get(i - 1);
                
                path[i - 1] = new Line2D.Double(point1[0], point1[1], point2[0], point2[1]);
            }
        }
        
        return path;
    }
    
    //Get the heading in radians
    public double calculateHeadingRadians(){
        if(coordinateHistory.size() > 2){
            GPSCoordinate firstPoint = coordinateHistory.get(coordinateHistory.size() - 2);
            GPSCoordinate secondPoint = coordinateHistory.get(coordinateHistory.size() - 1);
            
            if(!firstPoint.compareTo(secondPoint)){
                double latA = firstPoint.getLatitude();
                double longA = firstPoint.getLongitude();
                double latB = secondPoint.getLatitude();
                double longB = secondPoint.getLongitude();

                double X = Math.cos(latB) * Math.sin(longB - longA);
                double Y = (Math.cos(latA) * Math.sin(latB)) - (Math.sin(latA) * Math.cos(latB) * Math.cos(longB - longA));

                headingRadians = - Math.atan2(X, Y);
                return headingRadians;
            }else{
                return headingRadians;
            }
        }else{
            return headingRadians;
        }
    }
    
    //Get the heading in degrees
    public double getHeadingInDegrees(){
        double headingRadians = calculateHeadingRadians();
        return headingRadians * 180 / Math.PI;
    }
}
