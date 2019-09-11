package gui;

//Class that represents a GPS coordinate
public class GPSCoordinate {
    private double latitude;
    private double longitude;
    
    public GPSCoordinate(){
        latitude = 0;
        longitude = 0;
    }
    
    public GPSCoordinate(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    
    public double getLatitude(){
        return latitude;
    }
    
    public double getLongitude(){
        return longitude;
    }
    
    public boolean compareTo(GPSCoordinate coordinate){
        if(coordinate.getLatitude() == latitude && coordinate.getLongitude() == longitude){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public String toString(){
        return "{" + latitude + ", " + longitude + "}";
    }
}
