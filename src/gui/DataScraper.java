package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class gathers data from a "data packet" text file. This class holds all
 * the data gathered from each data packet, and is used by all the Panels in the
 * GUI in order to generate proper displays
 */
public class DataScraper {
    //All angles are in radians
    private String txtFile;
    private boolean isSystemStarted;
    private boolean isNewDataPacket;
    private long systemStartTime;
    private long currentPacketTime;
    private double systemRollAngle;
    private double systemPitchAngle;
    private double systemSpeed; 
    private double systemHeading; 
    private double systemYawValue;
    private double systemLongitude; 
    private double systemLatitude;
    private int satellitesConnected;
    private int[] userInputs; //{throttle, rudder, elevator, aileron} STD notation
    
    /**
     * Constructor for the data scraper
     * @param txtFile : the data packet text file path
     *                  a template for this file is in the project files
     */
    public DataScraper(String txtFile)throws IOException{
        this.txtFile = txtFile;
        
        systemStartTime = 0;
        currentPacketTime = 0;
        
        systemSpeed = 0;
        systemHeading = 0;
        systemYawValue = 0;
        systemLongitude = 0;
        systemLatitude = 0;
        userInputs = new int[4];
        satellitesConnected = 0;
        
        scrapeForData();
    }
    
    /** Method that pulls new data from the data packet text file
     */
    public void scrapeForData() throws IOException, FileNotFoundException {
        File dataFile = new File(txtFile);
        BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
        
        String[] boolStrings = processLines(2, dataReader);
        if(boolStrings == null)
            return;
        
        isSystemStarted = Boolean.parseBoolean(boolStrings[0]);
        isNewDataPacket = Boolean.parseBoolean(boolStrings[1]);
        
        if(isNewDataPacket && isSystemStarted){
           String[] dataPacket = processLines(10, dataReader);
            if(dataPacket == null){
                System.out.println("Failure at Telemetry read");
                return;
            }
            
            try{
                systemStartTime = Long.parseLong(dataPacket[0]);
                currentPacketTime = Long.parseLong(dataPacket[1]);
                systemRollAngle = Double.parseDouble(dataPacket[2]);
                systemPitchAngle = Double.parseDouble(dataPacket[3]);
                systemYawValue = Double.parseDouble(dataPacket[4]);
                systemLongitude = Double.parseDouble(dataPacket[5]);
                systemLatitude = Double.parseDouble(dataPacket[6]);
                systemHeading = Double.parseDouble(dataPacket[7]);
                systemSpeed = Double.parseDouble(dataPacket[8]);
                satellitesConnected = Integer.parseInt(dataPacket[9]);
            }catch(NumberFormatException e){
                System.out.println("Exception encountered while scraping data, re-reading");
            }
            
            dataPacket = processLines(4, dataReader);
            if(dataPacket == null){
                System.out.println("Failure at User Input read");
                return;
            }
            
            for(int i = 0; i < dataPacket.length; i++){
                userInputs[i] = Integer.parseInt(dataPacket[i]);
            }
   
            overWriteFile(txtFile);
        }
        dataReader.close();
    }
    
    // Method to overwrite the text file with the proper parameters
    private void overWriteFile(String txtFile){
        try{
            PrintWriter dataWriter = new PrintWriter(txtFile, "UTF-8");
            dataWriter.println(isSystemStarted);
            dataWriter.println("false");
            dataWriter.close();
        }catch(FileNotFoundException e){
            System.out.println("Problem editing " + txtFile); 
        }catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Method to return an array of strings, with each being a line read through the object dataReader
    private String[] processLines(int numLines, BufferedReader dataReader) throws IOException{
        String[] stringArray = new String[numLines];
        for(int i = 0; i < numLines; i++)
            stringArray[i] = dataReader.readLine();
        if(checkNull(stringArray))
            return null;
        else
            return stringArray;
    }
    
    // Checks if any array values are null
    private boolean checkNull(String[] stringArray){
        for(int i = 0; i < stringArray.length; i++)
            if(stringArray[i] == null)
                return true;
        return false;
    }
    
    public String toString(){
        String str = "isSystemStarted: " + isSystemStarted + "\n";
        str+= "isNewDataPacket: " + isNewDataPacket + "\n";
        str+= "systemStartTime: " + systemStartTime + "\n";
        str+= "currentPacketTime: " + currentPacketTime + "\n";
        str+= "systemRollAngle: " + systemRollAngle + "\n";
        str+= "systemPitchAngle: " + systemPitchAngle + "\n";
        str+= "systemSpeed: " + systemSpeed + "\n";
        str+= "systemHeading: " + systemHeading + "\n";
        str+= "systemYawValue: " + systemYawValue + "\n";
        str+= "systemLongitude: " + systemLongitude + "\n";
        str+= "systemLatitude: " + systemLatitude + "\n";
        str+= "satellitesConnected: " + satellitesConnected + "\n";
        str+= "throttleInput: " + userInputs[0] + "\n";
        str+= "rudderInput: " + userInputs[1] + "\n";
        str+= "elevatorInput: " + userInputs[2] + "\n";
        str+= "aileronInput: " + userInputs[3] + "\n";
        return str;
    }
    
    /** Getter methods for all the data values gathered from the data packets, 
     *  used by each panel to get relevant data to display
     */
    public boolean checkGPSReadiness(){
        if(systemLatitude + systemLongitude == 0)
            return false;
        return true;
    }
    
    public String getTxtFile(){
        return txtFile;
    }
    
    public boolean getIsSystemStarted(){
        return isSystemStarted;
    }
    
    public boolean getIsNewDataPacket(){
        return isNewDataPacket;
    }
    
    public Long getSystemStartTime(){
        return systemStartTime;
    }
    
    public Long getCurrentPacketTime(){
        return currentPacketTime;
    }
    
    public double getSystemRollAngle(){
        return systemRollAngle;
    }
    
    public double getSystemPitchAngle(){
        return systemPitchAngle;
    }
    
    public double getSystemSpeed(){
        return systemSpeed;
    }
    
    public double getSystemHeading(){
        return systemHeading;
    }
    
    public double getSystemYawValue(){
        return systemYawValue;
    }
    
    public double getSystemLongitude(){
        return systemLongitude;
    }
    
    public double getSystemLatitude(){
        return systemLatitude;
    }
    
    public int[] getUserInputs(){
        return userInputs;
    }
    
    public int getSatellitesConneceted(){
        return satellitesConnected;
    }
}
