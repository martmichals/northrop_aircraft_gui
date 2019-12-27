package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PreliminaryDisplay implements ActionListener{
    public static final String FRAME_TITLE = "REconnaissance GUI - Startup Options";
    public static final String BUTTON_TITLE = "Start Program";
    public static final String FILE_ICON_IMAGE_PATH = "FileIcon.png";
    public static final String[] STARTUP_OPTIONS = {"From local records"};
    public static final String DEFAULT_HISTORY_FILE_PATH = "C:\\Users\\martm\\Desktop\\NetBeans Projects\\REconnaissanceGUI_FINAL\\SAMPLE-HISTORY.txt";
    public static final String LIVE_READ_BEGIN_CMD = "C:\\Users\\martm\\PycharmProjects\\liveSerialReader\\venv\\Scripts\\python.exe C:\\Users\\martm\\PycharmProjects\\liveSerialReader\\liveSerialReader.py";
    public static final int[] ICON_DIMENSIONS = {1024/45, 1024/45};
    
    private JFrame mainFrame;
    private Container container;
    
    private JButton startButton;
    private JComboBox<String> startUpOptionDropdown;
    private JButton startFileExplorerButton;
    private JCheckBox useDefaultsCheckbox;
    private JTextField userSelectedPathField;
    private JLabel userPathFieldLabel;
    private JLabel runnerLabel;
    private JPanel hideableElements;
    
    private int lastOptionDropdownIndex;
    private String filePath;
    private boolean isRunningLive;
    private boolean isReadyToSend;
    
    /** 
     * Class to handle events in the initial display
     */
    public PreliminaryDisplay(){
        lastOptionDropdownIndex = 0;
        filePath = "";
        isRunningLive = false;
        
        mainFrame = new JFrame(FRAME_TITLE);
        System.out.println("Preliminary Frame Instantiated");
        
        container = mainFrame.getContentPane();
        //Add layout type here if something other than default desired
        initializeAllComponents();
        drawAllComponents(true);
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        
        waitOnData();
    }
    
    private void waitOnData(){
        isReadyToSend = false;
        while(!isReadyToSend){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Error Code: 101");
            }
        }
    }
    
    private void initializeAllComponents(){
        //Defining all of the components, setting up the default settings
        startButton = new JButton(PreliminaryDisplay.BUTTON_TITLE);
        startUpOptionDropdown = new JComboBox<>(STARTUP_OPTIONS);
        startFileExplorerButton = new JButton();
        try{
            Image iconImage = ImageIO.read(new File(FILE_ICON_IMAGE_PATH));
            Image resizedIconImage = iconImage.getScaledInstance(ICON_DIMENSIONS[0], ICON_DIMENSIONS[1], Image.SCALE_DEFAULT);
            startFileExplorerButton.setIcon(new ImageIcon(resizedIconImage));
        }catch(Exception ex){
            System.out.println(ex);
        }
        int startFileExplorerButton_padding = 3;
        startFileExplorerButton.setPreferredSize(new Dimension(ICON_DIMENSIONS[0] + startFileExplorerButton_padding
                                                               , ICON_DIMENSIONS[1] + startFileExplorerButton_padding));
        
        useDefaultsCheckbox = new JCheckBox("Use Defaults");
        useDefaultsCheckbox.setSelected(false);
        userPathFieldLabel = new JLabel("Filepath of recording: ");
        runnerLabel = new JLabel("Run: ");
        userSelectedPathField = new JTextField(20);
        
        //Adding Listeners for desired components
        startUpOptionDropdown.addActionListener(this);
        startButton.addActionListener(this);
        startFileExplorerButton.addActionListener(this);
        useDefaultsCheckbox.addActionListener(this);
    }
    
    private void drawAllComponents(boolean areHideablesVisible){
        container.removeAll();
        
        //Adding the components defined into the container
        //Outer section upper portion
        JPanel temporaryPanel = new JPanel();
        temporaryPanel.add(runnerLabel);
        temporaryPanel.add(startUpOptionDropdown);
        container.add(temporaryPanel, BorderLayout.PAGE_START);
        
        displayScreenWithHideables(areHideablesVisible);
    }
    
    private void displayScreenWithHideables(boolean areHideablesVisible){
        if(areHideablesVisible){
            //Outer portion central component
            JPanel temporaryPanel = new JPanel();
            temporaryPanel.setLayout(new BorderLayout());
            
            hideableElements = new JPanel();
            hideableElements.add(userPathFieldLabel);
            hideableElements.add(userSelectedPathField);
            hideableElements.add(startFileExplorerButton);
            temporaryPanel.add(hideableElements, BorderLayout.CENTER);
        
            JPanel innerTemporaryPanel = new JPanel();
            innerTemporaryPanel.add(useDefaultsCheckbox, BorderLayout.PAGE_END);
            temporaryPanel.add(innerTemporaryPanel, BorderLayout.PAGE_END);
        
            container.add(temporaryPanel, BorderLayout.CENTER);
        
            //Outer section lower portion
            temporaryPanel = new JPanel();
            temporaryPanel.add(startButton);
            container.add(temporaryPanel, BorderLayout.PAGE_END);
            
            mainFrame.setSize(450, 175);
        }else{
            //Outer section center portion
            JPanel temporaryPanel = new JPanel();
            temporaryPanel = new JPanel();
            temporaryPanel.add(startButton);
            container.add(temporaryPanel, BorderLayout.CENTER);
            
            mainFrame.setSize(450, 125);
        }
    }
    
    private String getFilePathFromBrowser(){
        JFileChooser chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Find text file for playback");
        
        //Set the file browser to filter in only text files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        
        if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
            System.out.println("File Browser: Valid user choice based on filtration");
        }else{
            System.out.println("No Selection ");
        }
        
        return chooser.getSelectedFile().getPath();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object caller = e.getSource();
        
        if(caller instanceof JComboBox){
            JComboBox temp = (JComboBox)caller;
            String option = (String)temp.getSelectedItem();
            if(option.equals(STARTUP_OPTIONS[0]) && lastOptionDropdownIndex != 0){
                System.out.println("Drawing in frame with hideables visible");
                drawAllComponents(true);
                lastOptionDropdownIndex = 0;
            }
            // This code segment to be used if program is extended to support live data reading
            /*else if(option.equals(STARTUP_OPTIONS[1]) && lastOptionDropdownIndex != 1){
                System.out.println("Drawing in frame with hideables invisible");
                drawAllComponents(false);
                lastOptionDropdownIndex = 1;
            }*/else{
                System.out.println("Not redrawing, repeat option");
            }
            return;
        }
        
        if(caller instanceof JButton){
            JButton temp = (JButton)caller;
            String buttonText = temp.getText();
            if(buttonText.equals(BUTTON_TITLE)){
                String option = (String)startUpOptionDropdown.getSelectedItem();
                if(option.equals(STARTUP_OPTIONS[0])){
                    filePath = userSelectedPathField.getText();
                    isRunningLive = false;
                    if(!checkFilePath()){
                        userSelectedPathField.setBackground(new Color(244, 159, 159));
                    }else{
                        userSelectedPathField.setBackground(Color.WHITE);
                        isReadyToSend = true;
                        printClassState();
                    }
                }else{
                    isRunningLive = true;
                    printClassState();
                    beginLiveRead();
                    isReadyToSend = true;
                }
            }else{
                userSelectedPathField.setText(getFilePathFromBrowser());
            }
            return;
        }
        
        if(caller instanceof JCheckBox){
            boolean isBoxSelected = ((JCheckBox)caller).isSelected();
            if(isBoxSelected){
                userSelectedPathField.setText(DEFAULT_HISTORY_FILE_PATH);
            }else{
                userSelectedPathField.setText(filePath);
            }
        }
    }
    
    private boolean checkFilePath(){
        File runnerFile = new File(filePath);
        if(runnerFile.exists() && filePath.endsWith(".txt")){
            System.out.println("The path given is a valid file path");
            return true;
        }
        return false;
    }
    
    // Was in development at project end - code to launch live reading from a Xbee
    private void beginLiveRead(){
//        try {
//            Process process = Runtime.getRuntime().exec(this.LIVE_READ_BEGIN_CMD);
//            System.out.println("Python program started");
//        } catch (IOException ex) {
//            Logger.getLogger(PreliminaryDisplay.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Error caught");
//        }
//        Ask mentors, this simply does not allow for the full run cycle of the program 
    }
    
    public String getFilePath(){
        return filePath;
    }
    
    public boolean getIsRunningLive(){
        return isRunningLive;
    }
    
    public void printClassState(){
        System.out.print("The class will run ");
        if(isRunningLive)
            System.out.println("live");
        else
            System.out.println("from the file " + filePath);
    }
}
