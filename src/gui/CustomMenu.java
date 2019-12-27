package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/** Class used to create a menu at the top of the GUI
 *  Not in use currently. Can be used in order to add "pause" functionality
 */
public class CustomMenu extends JMenuBar implements ActionListener{
    private String id;
    private JMenu fileMenu;
    
    public CustomMenu(){
        super();
        id = null;
        fileMenu = null;
    }
    
    public CustomMenu(String id){
        super();
        this.id = id;
        fileMenu = new JMenu("Options");
        
        fillFileMenu();
        this.add(fileMenu);
        
        System.out.println("Menus created and added");
    }
    
    private void fillFileMenu(){
        //Creates and sets a Mneumotic
        JMenuItem menuItem = new JMenuItem("Pause program");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);
    }
    
    public String getId(){
        return id;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        System.out.println("Action heard and action executed");
        PlaybackRunner.flipProgramLoopState();
    }
}
