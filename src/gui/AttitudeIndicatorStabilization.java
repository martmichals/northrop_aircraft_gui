package gui;

import java.util.ArrayList;

/**
 * Class that averages incoming values displayed by the attitude indicator
 * Meant to smooth the rather shaky data from the gyroscope
 */
public class AttitudeIndicatorStabilization {
    // Number of values to average
    public static final int MAX_SIZE = 10;
    
    ArrayList<Double> roll;
    ArrayList<Double> pitch;
    ArrayList<Double> yaw;
    
    public AttitudeIndicatorStabilization(){
        roll = new ArrayList();
        pitch = new ArrayList();
        yaw = new ArrayList();
    }
    
    // Updates the values in the arrays, logical left shift once the arrays are 
    // at the desired size
    public void updateValues(double[]  newValues){
        this.roll.add(newValues[0]);
        this.pitch.add(newValues[1]);
        this.yaw.add(newValues[2]);
        
        if(roll.size() == (MAX_SIZE + 1)){
            roll.remove(0);
            pitch.remove(0);
            yaw.remove(0);
        }
    }
    
    // Checks whether the class is ready to begin outputting values
    public boolean getReadiness(){
        if(roll.size() == MAX_SIZE && pitch.size() == MAX_SIZE && yaw.size() == MAX_SIZE)
            return true;
        return false;
    }
    
    // Gets the average of the pitch value array
    public double getSmoothedPitch(){
        if(getReadiness()){
            double sum = 0;
            for(int i = 0; i < MAX_SIZE; i++)
                sum+= pitch.get(i);
            
            return sum / MAX_SIZE;
        }
        return pitch.get(pitch.size() - 1);
    }
    
    // Gets the average of the roll value array
    public double getSmoothedRoll(){
        if(getReadiness()){
            double sum = 0;
            for(int i = 0; i < MAX_SIZE; i++)
                sum+= roll.get(i);
            
            return sum / MAX_SIZE;
        }
        return roll.get(roll.size() - 1);
    }
    
    // Gets the average of the yaw value array
    public double getSmoothedYaw(){
        if(getReadiness()){
            double sum = 0;
            for(int i = 0; i < MAX_SIZE; i++)
                sum+= roll.get(i);
            
            return sum / MAX_SIZE;
        }
        return yaw.get(yaw.size() - 1);
    }
}
