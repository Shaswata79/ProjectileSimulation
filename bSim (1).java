
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.tools.JavaFileObject;

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.program.Program;
import acm.util.RandomGenerator;

@SuppressWarnings("serial")
public class bSim extends GraphicsProgram {
	
	private static final int WIDTH = 1800; // n.b. screen coordinates
	private static final int HEIGHT = 800;
	private static final double SCALE = 6; // pixels per meter
	public int NUMBALLS;
	public double MINSIZE;
	public double MAXSIZE;
	public double MINVEL;
	public double MAXVEL;
	public double MINTHETA;
	public double MAXTHETA;
	public double MINLOSS;
	public double MAXLOSS;
	public static boolean TracePoint = false; 
	public static boolean SimPaused = false;
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	bTree MyTree = new bTree();                      //initializes a new binary tree
	//GLabel Prompt = new GLabel("", 600, 650);        //prompts to click the mouse
	
	
	JComboBox<String> bSimC = new JComboBox<String>();
	JPanel EastPanel = new JPanel();
	JTextField NumBalls = new JTextField(3);
	JTextField MinSize = new JTextField(3);
	JTextField MaxSize = new JTextField(3);
	JTextField MinAngle = new JTextField(3);
	JTextField MaxAngle = new JTextField(3);
	JTextField MinVel = new JTextField(3);
	JTextField MaxVel = new JTextField(3);
	JTextField MinLoss = new JTextField(3);
	JTextField MaxLoss = new JTextField(3);
	
	JToggleButton Trace = new JToggleButton("Trace", false);
	
	
	public void init() {
		this.resize(WIDTH, HEIGHT);
		
		GRect Ground = new GRect(0, 600, 1600, 3);  //creates the object ground
        Ground.setFilled(true);
        add(Ground) ;                               // adds the ground to the screen
		
        EastPanel.setLocation(1000, 0);
		EastPanel.setSize(2, 8);
		EastPanel.setLayout(new GridLayout(0,4));
		
		
		EastPanel.add(new JLabel ("NUMBALLS:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(NumBalls);
		EastPanel.add(new JLabel ("   100"));
		NumBalls.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MinSize:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(MinSize);
		EastPanel.add(new JLabel ("   25"));
		MinSize.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MaxSize:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(MaxSize);
		EastPanel.add(new JLabel ("   25"));
		MaxSize.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MinAngle:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(MinAngle);
		EastPanel.add(new JLabel ("   180"));
		MinAngle.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MaxAngle:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(MaxAngle);
		EastPanel.add(new JLabel ("   180"));
		MaxAngle.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MinVel:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(MinVel);
		EastPanel.add(new JLabel ("   200"));
		MinVel.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MaxVel:  "));
		EastPanel.add(new JLabel ("                   1"));
		EastPanel.add(MaxVel);
		EastPanel.add(new JLabel ("   200"));
		MaxVel.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MinLoss:  "));
		EastPanel.add(new JLabel ("                   0"));
		EastPanel.add(MinLoss);
		EastPanel.add(new JLabel ("   1"));
		MinLoss.addActionListener(this);
		
		
		EastPanel.add(new JLabel ("MaxLoss:  "));
		EastPanel.add(new JLabel ("                   0"));
		EastPanel.add(MaxLoss);
		EastPanel.add(new JLabel ("   1"));
		MaxLoss.addActionListener(this);
	
		add(EastPanel,EAST);
		
		bSimC.addItem("bSim");
		bSimC.addItem("Run");
		bSimC.addItem("Stack");
		bSimC.addItem("Stop/Resume");
		bSimC.addItem("Clear");
		bSimC.addItem("Exit");
		add(bSimC, NORTH);
		bSimC.addActionListener(this);
		
		add(Trace, SOUTH);
		Trace.addActionListener(this); 
	
		
	}
	
	public void actionPerformed(ActionEvent e) {
		    Object source = e.getSource();
		    if (source==bSimC) {
		    	if (bSimC.getSelectedItem().equals("Run")) {
		    		System.out.println("Starting simulation");
		        	doSim();
		    	}
		    	else if (bSimC.getSelectedItem().equals("Stack")) {
		        	doStack();
		    	}
		    	else if (bSimC.getSelectedItem().equals("Stop/Resume")) {
		        	StopSim(); 	
		    	}
		    	else if (bSimC.getSelectedItem().equals("Clear")) {
		        	ClearScreen();
		    	}
		    	else if (bSimC.getSelectedItem().equals("Exit")) {
		        	exitSim();
		    	}
		    }
		    else if(source == Trace) {
		    	if(Trace.isSelected()==true) {
		    		TracePoint = true;
		    	}
		    	else {
		    		TracePoint = false;
		    	}
		    }	
			
	}
	
	
	
	
	/**
	* The doSim method run a new simulation when called
	* @param void
	* @return void
	*/
	
			
	
	public void doSim() {
		SetParameters();
		rgen.setSeed((long) 424242);
		for(int i=0; i<NUMBALLS;i++) {
        	double iSize =  rgen.nextDouble(MINSIZE,MAXSIZE);           //generates random values
            Color iColor = rgen.nextColor();                            //for iSize, iColor, iLoss,
            double iLoss = rgen.nextDouble(MINLOSS,MAXLOSS);                  //iSpeed, iTheta
            double iSpeed = rgen.nextDouble(MINVEL,MAXVEL);  
            double iTheta = rgen.nextDouble(MINTHETA,MAXTHETA);
            
            aBall ThisBall = new aBall(800-(iSize*SCALE), (600-(iSize*2*SCALE)), iSpeed, iTheta, iSize, iColor,iLoss,this);  //initializes the object ball
            add(ThisBall.getBall());           //gets the ball color and positions
            ThisBall.start();                  //starts this thread
            MyTree.addNode(ThisBall);          //adds this ball to the binary tree
        }
		
	}
	
	/**
	* The doStack method stacks all the aBall object currently in the tree
	* @param void
	* @return void
	*/


	public void doStack() {
       	    if(!MyTree.isRunning()) {                           //if no ball is moving
       	    	System.out.println("Stacking balls");
            	MyTree.StackBalls(MyTree.getRoot());           //stacks balls according to size
       	    }
       	    else {
       	    	System.out.println("Simulation still running!!");
       	    }       	       
	}
	
	/**
	* The StopSim method pauses the current simulation when called and resumes the simulation when called again
	* @param void
	* @return void
	*/
	
	public void StopSim() {
		SimPaused = !SimPaused;
		System.out.println(SimPaused);
	}
	
	/**
	* The exitSim method exits the application
	* @param void
	* @return void
	*/
	
	public void exitSim() {
		System.exit(0);
	}
	
	/**
	* The ClearScreen method clears the screen 
	* @param void
	* @return void
	*/
	
	public void ClearScreen() {
		removeAll();
		GRect Ground = new GRect(0, 600, 1600, 3);  //creates the object ground
        Ground.setFilled(true);
        add(Ground) ;                               // adds the ground to the screen
		
	}
	
	/**
	* The SetParameters method assigns input data to variables and validates them 
	* @param void
	* @return void
	*/
	
	public void SetParameters() {
		
		if (Integer.parseInt(NumBalls.getText()) > 100) {
			NUMBALLS = 100;
		}
		else if(Integer.parseInt(NumBalls.getText()) < 0) {
			NUMBALLS = 0;
		}
		else {
			NUMBALLS = Integer.parseInt(NumBalls.getText());
		}
		
		
		if (Double.parseDouble(MinSize.getText()) > 25) {
			MINSIZE = 25;
		}
		else if(Double.parseDouble(MinSize.getText()) < 1) {
			MINSIZE = 1;
		}
		else {
			MINSIZE = Double.parseDouble(MinSize.getText());
		}
		
		
		if (Double.parseDouble(MaxSize.getText()) > 25) {
			MAXSIZE = 25;
		}
		else if(Double.parseDouble(MaxSize.getText()) < 1) {
			MAXSIZE = 1;
		}
		else {
			MAXSIZE = Double.parseDouble(MaxSize.getText());
		}
		
		
		if (Double.parseDouble(MinLoss.getText()) > 1) {
			MINLOSS = 1;
		}
		else if(Double.parseDouble(MinLoss.getText()) < 0) {
			MINLOSS = 0;
		}
		else {
			MINLOSS = Double.parseDouble(MinLoss.getText());
		}
		
		
		if (Double.parseDouble(MaxLoss.getText()) > 1) {
			MAXLOSS = 1;
		}
		else if(Double.parseDouble(MaxLoss.getText()) < 0) {
			MAXLOSS = 0;
		}
		else {
			MAXLOSS = Double.parseDouble(MaxLoss.getText());
		}
		
		
		if (Double.parseDouble(MinVel.getText()) > 200) {
			MINVEL = 200;
		}
		else if(Double.parseDouble(MinVel.getText()) < 1) {
			MINVEL = 1;
		}
		else {
			MINVEL = Double.parseDouble(MinVel.getText());
		}
		
		
		if (Double.parseDouble(MaxVel.getText()) > 200) {
			MAXVEL = 200;
		}
		else if(Double.parseDouble(MaxVel.getText()) < 1) {
			MAXVEL = 1;
		}
		else {
			MAXVEL = Double.parseDouble(MaxVel.getText());
		}
		
		
		if (Double.parseDouble(MinAngle.getText()) > 180) {
			MINTHETA = 180;
		}
		else if(Double.parseDouble(MinAngle.getText()) < 1) {
			MINTHETA = 1;
		}
		else {
			MINTHETA = Double.parseDouble(MinAngle.getText());
		}
		
		
		if (Double.parseDouble(MaxAngle.getText()) > 180) {
			MAXTHETA = 180;
		}
		else if(Double.parseDouble(MaxAngle.getText()) < 1) {
			MAXTHETA = 1;
		}
		else {
			MAXTHETA = Double.parseDouble(MaxAngle.getText());
		}
		
	}
	
}

        



