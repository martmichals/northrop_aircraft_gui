package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @title       REconnaissance GUI to display aircraft telemetry
 * @description This program displays telemetry from the aircraft. Displays indicators
 *              which show the aircraft position, orientation, speed, and heading.
 *              Currently runs only from history files stored locally on the machine
 * @author      Martin Michalski
 */

public class MainFrameDriver {
    private static SystemConstants systemConstants;
    private static DataScraper dataScraper;
    private static ArrayList<Panel> allPanels;
    private static MainDisplay mainDisplay;
    // private static TestDisplay testDisplay;
    
    public static void main(String[] args) throws IOException{
        PreliminaryDisplay preliminaryDisplay = new PreliminaryDisplay();
        if(!preliminaryDisplay.getIsRunningLive())
            runConcurrently(preliminaryDisplay);
        else
            runMainProgram();
    }
    
    private static void updatePanels(){
        for(int i = 0; i < allPanels.size(); i++){
            allPanels.get(i).updatePanel(dataScraper);
        }
    }
    
    private static void initializePanels(){
        for(int i = 0; i < allPanels.size(); i++)
            allPanels.get(i).initialize();
    }
    
    private static void runMainProgram() throws IOException{
        try{
            systemConstants = new SystemConstants("SystemConstants.txt");
        }catch(IOException e){
            System.out.println("IOException, call from MainDisplay");
        }
        
        dataScraper = new DataScraper(systemConstants.DATA_TXT_NAME);
        int[] indicatorDimensions = {36, 205};
        int[] mapDimensions = {600, 450};
        
        ArrayList<Panel> flowPanels = new ArrayList<>();
        //flowPanels.add(new UserInputIndicator("idUserInputIndicator", indicatorDimensions, "THR", "InputIndicatorGauge.png", "InputIndicatorIndicator.png", "InputIndicatorTickMarks.png", 0));
        //flowPanels.add(new UserInputIndicator("idUserInputIndicator", indicatorDimensions, "RUD", "InputIndicatorGauge.png", "InputIndicatorIndicator.png", "InputIndicatorTickMarks.png", 1));
        //flowPanels.add(new RecentImage("idImage", systemConstants.IMAGE_DIMENSIONS, null)); 
        
        flowPanels.add(new GPSTrackingMap("idTrackerMap", mapDimensions, "GPSImageBorder.png"));
        ((GPSTrackingMap)flowPanels.get(flowPanels.size() - 1)).initializeWithValidGPS(dataScraper);
        
        //flowPanels.add(new UserInputIndicator("idUserInputIndicator", indicatorDimensions, "ELE", "InputIndicatorGauge.png", "InputIndicatorIndicator.png", "InputIndicatorTickMarks.png", 2));
        //flowPanels.add(new UserInputIndicator("idUserInputIndicator", indicatorDimensions, "AIL", "InputIndicatorGauge.png", "InputIndicatorIndicator.png", "InputIndicatorTickMarks.png", 3));
        
        allPanels = new ArrayList<>(); 
        allPanels.add(new FlowPanel("idFlowPanelNum0", systemConstants.FLOW_DIMENSIONS, flowPanels));
        allPanels.add(new GroundSpeedIndicator("idGroundSpeedIndicator", systemConstants.GROUND_SPEED_DIMENSIONS,
                      "GroundSpeedSpinner.png", "GroundSpeedGauge.png"));
        allPanels.add(new AttitudeIndicator("idAttitudeIndicator", systemConstants.ATTITUDE_DIMENSIONS, 
                      "AttitudeIndicatorOverlay.png", "AttitudeIndicatorArtificialHorizon.png"));
        allPanels.add(new HeadingIndicator("idHeadingIndicator", systemConstants.HEADING_DIMENSIONS,
                      "HeadingIndicatorGauge.png", "HeadingIndicatorSpinner.png"));
         
        initializePanels();  
        while(!dataScraper.getIsSystemStarted())
            dataScraper.scrapeForData();
       
        mainDisplay = new MainDisplay(systemConstants, allPanels);
        
        while(dataScraper.getIsSystemStarted()){
            dataScraper.scrapeForData();
            updatePanels();
        }
        System.out.println("End of system lifecycle");
    }
    
    private static void runConcurrently(PreliminaryDisplay display){
        String[] temp = {""};
        new Thread(() -> {
            try {
                System.out.println("Launching the PlaybackRunner on a new thread");
                PlaybackRunner.text_to_scrape = display.getFilePath();
                PlaybackRunner.main(temp);
            } catch (IOException ex) {
                Logger.getLogger(MainFrameDriver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrameDriver.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }).start();
        
        new Thread(() -> {
            try {
                runMainProgram();
            } catch (IOException ex) {
                Logger.getLogger(MainFrameDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
