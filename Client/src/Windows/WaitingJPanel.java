package Windows;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javafx.scene.shape.Circle;
import javax.swing.JPanel;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class WaitingJPanel extends JPanel
{

    ////////////////////////////////////////////////////////////////////////////
    // Constances
    ////////////////////////////////////////////////////////////////////////////
    private final static String FILE_NAME = "WaitingJPanel";
    
    private final static int normalCircleSize = 10;
    private final static int XLCircleSize = 20;
    
    private Runnable circleAnimation;
    private Circle[] kreise;
    private int[] xCoordinaten = {100,130,160,190};
    private int currentCircle = 0; 
    private int currentPlayers =0;
    
    private boolean isAnimationRunning;
    
    

    ////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////
    
   /**
    * Default-Constructor
    */
    public WaitingJPanel(int width,int height) 
    {   
        this.setSize(width, height);
        kreise = new Circle[xCoordinaten.length];
        fillKreisArray();
        startAnimation();
        circleAnimation = new Runnable()
        {
            @Override
            public void run()
            {
                while(isAnimationRunning)
                {
                    if(currentCircle>xCoordinaten.length-1)
                        currentCircle=0;
                    
                    kreise[currentCircle].setRadius(XLCircleSize);
                    for(int i = 0;i<xCoordinaten.length;i++)
                    {
                        if(i!=currentCircle)
                        {
                            kreise[i].setRadius(normalCircleSize);
                        }
                    }
                    currentCircle++;
                    repaint();
                    try
                    {
                        Thread.sleep(400);
                    }
                    catch(InterruptedException ex)
                    {
                       ex.printStackTrace();
                    }
                }
            }
        };
        new Thread(circleAnimation).start();
    }

  

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void paintComponent( Graphics g)
    {
        super.paintComponent(g); 
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
     
        
        g2d.drawString("Waiting for Players ("+String.valueOf(currentPlayers)+"/5)", 100  , 100);
        for(int i =0;i<xCoordinaten.length;i++)
        {
            g2d.fillOval(xCoordinaten[i], (400-(int)kreise[i].getRadius())/2 , (int)kreise[i].getRadius(), (int)kreise[i].getRadius());
        }
    }
    private void fillKreisArray()
    {
        for(int i = 0;i<xCoordinaten.length;i++)
        {
            kreise[i] = new Circle(normalCircleSize);
        }
    }
    public void startAnimation()
    {
        isAnimationRunning = true;
    }
    public void stopAnimation()
    {
        isAnimationRunning = false;
    }

    public void updatePlayerCount(int size)
    {
        currentPlayers = size+1;
        repaint();
    }
}
