package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class PlaybackReader {
    public final String DATA_START = "|S|";
    public final String DATA_SEPARATOR = "|";
    public final String DATA_END = "|E|";
    
    private File sourceFile; 
    private String destinationFile;
    private BufferedReader reader;
    
    private String nextLine;
    private String currentLine;
    private boolean firstLine;
    private long waitTime;
    
    //Arrays for the information to be stored, 0 is tbr and 1 is future, to be shifted
    private boolean isSystemStarted;
    private boolean isNewDataPacket;
    private long systemStartTime;
    private long currentSystemTime;
    private double systemRollAngle;
    private double systemPitchAngle;
    private double systemYawAngle;
    private double longitude;
    private double latitude;
    private double heading;
    private double groundSpeed;
    private double satellites;
    private double userInputThrottle;
    private double userInputRudder;
    private double userInputElevator;
    private double userInputAilerons;
    
    private long relativeTime;
    
    public PlaybackReader(){
        this.sourceFile = null;
        this.destinationFile = null;
        
        isSystemStarted = false;
        isNewDataPacket = false;
        systemStartTime = 0;
        currentSystemTime = 0;
        systemRollAngle = 0;
        systemPitchAngle = 0;
        systemYawAngle = 0;
        longitude = 0;
        latitude = 0;
        heading = 0;
        groundSpeed = 0;
        satellites = 0;
        userInputThrottle = 0;
        userInputRudder = 0;
        userInputElevator = 0;
        userInputAilerons = 0;
        
        relativeTime = 0;
        
        firstLine = false;
        nextLine = "";
        currentLine = "";
        waitTime = 0;
    }
    
    public PlaybackReader(String absoluteSource, String absoluteDestination){
        sourceFile = new File(absoluteSource);
        destinationFile = absoluteDestination;
        
        isSystemStarted = false;
        isNewDataPacket = false;
        systemStartTime = 0;
        currentSystemTime = 0;
        systemRollAngle = 0;
        systemPitchAngle = 0;
        systemYawAngle = 0;
        longitude = 0;
        latitude = 0;
        heading = 0;
        groundSpeed = 0;
        satellites = 0;
        userInputThrottle = 0;
        userInputRudder = 0;
        userInputElevator = 0;
        userInputAilerons = 0;
        
        relativeTime = 0;
        
        try{
            reader = new BufferedReader(new FileReader(sourceFile));
        }catch(FileNotFoundException e){
            System.out.println("Error in getting flight log text file");
        }
        firstLine = true;
        nextLine = "";
        currentLine = "";
        waitTime = 0;
    }
    
    public boolean readLine()throws IOException{
        if(firstLine){
            currentLine = reader.readLine();
            nextLine = reader.readLine();
            parseData(currentLine);
            parseWaitTime(nextLine);
            
            firstLine = false;
        }else{
            currentLine = nextLine;
            nextLine = reader.readLine();
            if(nextLine == null)
                return false;
            
            parseData(currentLine);
            parseWaitTime(nextLine);
        }
        return true;
    }
    
    public void closeReader()throws IOException{
        reader.close();
    }
   
    @Override
    public String toString(){
        String str = "isSystemStarted: " + isSystemStarted + "\n";
        str += "isNewDataPacket: " + isNewDataPacket + "\n";
        str += "systemStartTime: " + systemStartTime + "\n";
        str += "currentSystemTime: " + currentSystemTime + "\n";
        str += "systemRollAngle: " + systemRollAngle + "\n";
        str += "systemPitchAngle: " + systemPitchAngle + "\n";
        str += "systemYawAngle: " + systemYawAngle + "\n";
        str += "longitude: " + longitude + "\n";
        str += "latitude: " + latitude + "\n";
        str += "heading: " + heading + "\n";
        str += "groundSpeed: " + groundSpeed + "\n";
        str += "satellites: " + satellites + "\n";
        str += "userInputThrottle: " + userInputThrottle + "\n";
        str += "userInputRudder: " + userInputRudder + "\n";
        str += "userInputElevator: " + userInputElevator + "\n";
        str += "userInputAilerons: " + userInputAilerons + "\n";
        str += "waitTime: " + waitTime + "\n";
        str += "currentLine: " + currentLine + "\n";
        str += "nextLine: " + nextLine + "\n";
        str += "relativeTime: " + relativeTime + "\n";
        return str;
    }
    
    public void writeToFile() throws UnsupportedEncodingException{
        PrintWriter dataWriter;
        try{
            dataWriter = new PrintWriter(destinationFile, "UTF-8");
        }catch(FileNotFoundException e){
            System.out.println("File to write to was not found");
            return;
        }
        
        dataWriter.println(isSystemStarted);
        dataWriter.println(isNewDataPacket);
        dataWriter.println(systemStartTime);
        dataWriter.println(currentSystemTime);
        dataWriter.println(systemRollAngle);
        dataWriter.println(systemPitchAngle);
        dataWriter.println(systemYawAngle);
        dataWriter.println(longitude);
        dataWriter.println(latitude);
        dataWriter.println(heading);
        dataWriter.println(groundSpeed);
        dataWriter.println((int)satellites);
        dataWriter.println((int)userInputThrottle);
        dataWriter.println((int)userInputRudder);
        dataWriter.println((int)userInputElevator);
        dataWriter.println((int)userInputAilerons);
        
        dataWriter.close();
    }
    
    private void parseData(String str){
        int startIndex = findSeq(str, DATA_START);
        if(startIndex != -1){
            str = str.substring(DATA_START.length(), str.length());
            isSystemStarted = true;
            isNewDataPacket = true;
            long filler = Long.parseLong(getNextDataBit(str));
            str = str.substring(findSeq(str, DATA_SEPARATOR) + 1);
            currentSystemTime = Long.parseLong(getNextDataBit(str));
            
            //Comment out if erronous
            if(firstLine){
                systemStartTime = currentSystemTime - 200;
                System.out.println("Start time: " + systemStartTime);
            }
            
            str = str.substring(findSeq(str, DATA_SEPARATOR) + 1);
            int iterations = 0;
            while(!checkEnd(str)){
                str = fillDoubles(str, iterations);
                iterations++;
            }
        }
    }
            
    private void parseWaitTime(String str){
        int startIndex = findSeq(str, DATA_START);
        long nextPacketTime;
        if(startIndex != -1){
            str = str.substring(DATA_START.length(), str.length());
            str = str.substring(findSeq(str, DATA_SEPARATOR) + 1);
            nextPacketTime = Long.parseLong(getNextDataBit(str));
        }else{
            nextPacketTime = currentSystemTime;
        }
        waitTime = nextPacketTime - currentSystemTime;
        relativeTime = nextPacketTime - systemStartTime;
    }
    
    private int findSeq(String str, String sub){
        for(int i = 0; i <= str.length() - sub.length(); i++){
            if(str.substring(i, i + sub.length()).equals(sub)){
                return i;
            }
        }
        return -1;
    }
    
    private String getNextDataBit(String str){
        int endIndex = findSeq(str, DATA_SEPARATOR);
        if(endIndex != -1){
            return str.substring(0, endIndex);
        }else{
            return "";
        }
    }
    
    private boolean checkEnd(String str){
        if(findSeq(str, DATA_END) == 0){
            return true;
        }else{
            return false;
        }
    }
    
    private String fillDoubles(String str, int iteration){
        switch(iteration){
            case(0):
                systemRollAngle = Double.parseDouble(getNextDataBit(str));
                break;
            case(1):
                systemPitchAngle = Double.parseDouble(getNextDataBit(str));
                break;
            case(2):
                systemYawAngle = Double.parseDouble(getNextDataBit(str));
                break;
            case(3):
                longitude = Double.parseDouble(getNextDataBit(str));
                break;
            case(4):
                latitude = Double.parseDouble(getNextDataBit(str));
                break;
            case(5):
                heading = Double.parseDouble(getNextDataBit(str));
                break;
            case(6):
                groundSpeed = Double.parseDouble(getNextDataBit(str));
                break;
            case(7):
                satellites = Double.parseDouble(getNextDataBit(str));
                break;
            case(8):
                userInputThrottle = Double.parseDouble(getNextDataBit(str));
                break;
            case(9):
                userInputRudder = Double.parseDouble(getNextDataBit(str));
                break;
            case(10):
                userInputElevator = Double.parseDouble(getNextDataBit(str));
                break;
            case(11):
                userInputAilerons = Double.parseDouble(getNextDataBit(str));
                break;
            default:
                break;
        }
        if(!checkEnd(str.substring(findSeq(str, DATA_SEPARATOR))))
            return str.substring(findSeq(str, DATA_SEPARATOR) + 1);
        else
            return str.substring(findSeq(str, DATA_SEPARATOR));
    }
    
    public long getWaitTime(){return waitTime;}
    
    public double[] getGyroValues(){
        double[] gyroValues = {systemRollAngle, systemPitchAngle, systemYawAngle};
        return gyroValues;
    }
    
    public void setSystemRollAngle(double systemRollAngle){
        this.systemRollAngle = systemRollAngle;
    }
    
    public void setSystemPitchAngle(double systemPitchAngle){
        this.systemPitchAngle = systemPitchAngle;
    }
    
    public void setSystemYawAngle(double systemYawAngle){
        this.systemYawAngle = systemYawAngle;
    }
    
    public long getRelativeTime(){
        return relativeTime;
    }
}