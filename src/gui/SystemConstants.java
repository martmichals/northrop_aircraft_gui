package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;

/**
 * Class that furnishes general constant values for the rest of the project
 * Gathers data from a constants text file
 */
public class SystemConstants {
    //Title of the the overall JFrame
    public final String MAIN_FRAME_TITLE;
    
    //Int arrays are {width, height}
    public final int[] IMAGE_DIMENSIONS;
    public final int[] ATTITUDE_DIMENSIONS;
    public final int[] GROUND_SPEED_DIMENSIONS;
    public final int[] HEADING_DIMENSIONS;
    public final int[] FLOW_DIMENSIONS;
    public final Dimension SCREEN_DIMENSIONS;
    
    //Array (R, G, B)
    public final int[] BACKGROUND_RGB;
    
    //Name of the text file to scrape data from
    public final String DATA_TXT_NAME;
    
    public SystemConstants(){
        MAIN_FRAME_TITLE  = "";
        IMAGE_DIMENSIONS = new int[2];
        ATTITUDE_DIMENSIONS = new int[2];
        GROUND_SPEED_DIMENSIONS = new int[2];
        HEADING_DIMENSIONS = new int[2];
        FLOW_DIMENSIONS = new int[2];
        BACKGROUND_RGB = new int[3];
        DATA_TXT_NAME = "";
        SCREEN_DIMENSIONS = null;
    }
    
    public SystemConstants(String txtFile) throws IOException{
        File constantsFile = new File(txtFile);
        BufferedReader constantsReader = null;
        
        try{
            constantsReader = new BufferedReader(new FileReader(constantsFile));
        }catch(FileNotFoundException e){
            System.out.println("The constants text file could not be found. Program start failure.");
        }
        
        MAIN_FRAME_TITLE  = constantsReader.readLine();
        IMAGE_DIMENSIONS = getNextNLines(constantsReader, 2);
        ATTITUDE_DIMENSIONS = getNextNLines(constantsReader, 2);
        GROUND_SPEED_DIMENSIONS = getNextNLines(constantsReader, 2);
        HEADING_DIMENSIONS = getNextNLines(constantsReader, 2);
        DATA_TXT_NAME = constantsReader.readLine();
        BACKGROUND_RGB = getNextNLines(constantsReader, 3);
        SCREEN_DIMENSIONS = Toolkit.getDefaultToolkit().getScreenSize();
        FLOW_DIMENSIONS = getNextNLines(constantsReader, 2);
        System.out.println("System Constants Imported Successfully");
    }
    
    private int[] getNextNLines(BufferedReader constantsReader, int num) throws IOException{
        int[] valueArray = new int[num];
        for(int i  = 0; i < num; i++){
            String str = constantsReader.readLine();
            valueArray[i] = Integer.parseInt(str);
        }
        return valueArray;
    }
    
    @Override
    public String toString(){
        String str = "";
        str += "MAIN_FRAME TITLE: " + MAIN_FRAME_TITLE + "\n";
        str += "IMAGE_WIDTH: " + IMAGE_DIMENSIONS[0] + "\n";
        str += "IMAGE_HEIGHT: " + IMAGE_DIMENSIONS[1] + "\n";
        str += "ATTITUDE_WIDTH: " + ATTITUDE_DIMENSIONS[0] + "\n";
        str += "ATTITUDE_HEIGHT: " + ATTITUDE_DIMENSIONS[1] + "\n";
        str += "GROUND_SPEED_WIDTH: " + GROUND_SPEED_DIMENSIONS[0] + "\n";
        str += "GROUND_SPEED_HEIGHT: " + GROUND_SPEED_DIMENSIONS[1] + "\n";
        str += "HEADING_WIDTH: " + HEADING_DIMENSIONS[0] + "\n";
        str += "HEADING_HEIGHT: " + HEADING_DIMENSIONS[1] + "\n";
        str += "DATA_TXT_NAME : " + DATA_TXT_NAME + "\n";
        str += "BACK_R: " + BACKGROUND_RGB[0] + "\n";
        str += "BACK_G: " + BACKGROUND_RGB[1] + "\n";
        str += "BACK_B: " + BACKGROUND_RGB[2] + "\n";
        str += "FLOW_WIDTH: " + FLOW_DIMENSIONS[0] + "\n";
        str += "FLOW_HEIGHT: " + FLOW_DIMENSIONS[1] + "\n";
        
        return str;
    }
}
