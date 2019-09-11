package gui;

import java.io.IOException;
import java.util.Date;

/**Project in order to read in from a Flight Log, and then write current data to the GUI txt file
 */
public class PlaybackRunner {
    public static final String ABS_DESTINATION = "C:\\Users\\martm\\Desktop\\NetBeans Projects\\REconnaissanceGUI_FINAL\\DataPacket.txt";   //Absolute file path for the destination file, GUI file
    
    public static boolean isPlayBackRunning;
    public static String text_to_scrape = "C:\\Users\\martm\\Desktop\\NetBeans Projects\\REconnaissanceGUI_FINAL\\FlightPacketHistory.txt";
    
    public static boolean continueRunning = true;
    public static long systemStartTime;
    public static long systemCurrentTime;
    
    public static void main(String[] args)throws IOException, InterruptedException{
        PlaybackReader reader = new PlaybackReader(text_to_scrape, ABS_DESTINATION);
        GyroStabilizer stab = new GyroStabilizer();
        systemStartTime = System.currentTimeMillis();
        systemCurrentTime = System.currentTimeMillis();
        System.out.println("Starting PlayBack Runner");
        
        boolean localContinueRun = reader.readLine();
        while(localContinueRun){
            while(localContinueRun && continueRunning){
                localContinueRun = reader.readLine();
                stab.updateValues(reader.getGyroValues());
                
                while((systemCurrentTime - systemStartTime) < reader.getRelativeTime())
                    systemCurrentTime = System.currentTimeMillis();
                
                
                reader.setSystemRollAngle(stab.getSmoothedRoll());
                reader.setSystemPitchAngle(stab.getSmoothedPitch());
                reader.setSystemYawAngle(stab.getSmoothedYaw());

                reader.writeToFile();
            }
        }
        System.out.println("Done running the playback");
        reader.closeReader();
    }
    
    public static void flipProgramLoopState(){
        if(continueRunning)
            continueRunning = false;
        else
            continueRunning = true;
        System.out.println("continueRunning is now: " + continueRunning);
    }
}