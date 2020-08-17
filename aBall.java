
import java.awt.Color;

import acm.graphics.*;
import acm.program.*;

public class aBall extends Thread{
	/**
	* The constructor specifies the parameters for simulation. They are
	*
	* @param Xi double The initial X position of the center of the ball
	* @param Yi double The initial Y position of the center of the ball
	* @param Vo double The initial velocity of the ball at launch
	* @param Theta double Launch angle (with the horizontal plane)
	* @param BSize double The radius of the ball in simulation units
	* @param BColor Color The initial color of the ball
	* @param BLoss double Fraction [0,1] of the energy lost on each bounce
	*/
	
	private double Xi;
    private double Yi;
    private double Vo;
    private double Theta;
    private double BSize;
    private Color BColor;
    private double Loss;
    private GOval MyBall;
    boolean running;
    private bSim Link;

    private static final double g = 9.8; // MKS gravitational constant 9.8 m/s^2
    private static final double Pi = 3.141592654; // To convert degrees to radians
    private static final double TICK = 0.01; // Clock tick duration (sec)
    private static final double ETHR = 0.001; // If either Vx or Vy < ETHR STOP
    private static final double k = 0.0001;
    private static final double SCALE = 6;
    
    double KEx = 0;
    double KEy = 0;
	
	public aBall(double Xi, double Yi, double Vo, double theta, double Size, Color Bcolor, double Loss, bSim link) {
		this.Xi = Xi;
        this.Yi = Yi;
        this.Vo = Vo;
        this.Theta = theta;
        this.BSize = Size;
        this.BColor = Bcolor;
        this.Loss = Loss;
        this.Link = link;
		
		MyBall = new GOval(this.Xi, this.Yi, BSize*2*SCALE, BSize*2*SCALE);
		MyBall.setFilled(true);
		MyBall.setFillColor(BColor);
		
	}
	
	double Energy = 0;
	
	public GOval getBall() {     //returns the object MyBall
		return MyBall;
	}
	
	public double getBallSize() {      //returns the ball size
		return (this.BSize);
	}
	
	public double getBallEnergy() {        //returns the KE of the ball
		System.out.println(Energy);
		return Energy;
	}
	
	public Color getColor() {           //returns the ball colour
		return (this.BColor);
	}
	
	public boolean getBallState() {      //returns true if ball is moving
		return running;
	}
	
	
	
	/**
	* The run method implements the simulation loop from Assignment 1.
	* Once the start method is called on the aBall instance, the
	* code in the run method is executed concurrently with the main
	* program.
	* @param void
	* @return void
	*/
    
	public void run() {
		
		running = true;              //indicates whether a ball is moving(whether a thread is running)

        double Time = 0;
        double X = 0;
        double Xo = 0;
        double Y = 0;
        double XLast = 0;
        double YLast = BSize;
        int ScrX = 0;
        int ScrY = 0;
        double Vx = 0;
        double Vy = 0;
        double KEx = ETHR;
        double KEy = ETHR;
        boolean hasEnoughEnergy = true;

        double Vt = g / (4 * Pi * BSize * BSize * k); // Terminal velocity
        double Vox= Vo * Math.cos(Theta * Pi/180); // X component of initial velocity
        double Voy= Vo * Math.sin(Theta * Pi/180); // Y component of initial velocity
        double ELast = 0.5*Vo*Vo;           //KE of the previous trajectory
        
        Link.pause(200);
        while (hasEnoughEnergy) {
           System.out.println("");
           if (bSim.SimPaused == false) {
        	   System.out.println("");
        	   X = Math.abs((Vox * Vt/g * (1-Math.exp(-g*Time/Vt))) + Xo); // X position
               Y = ((Vt/g) * (Voy+Vt) * (1 - Math.exp(-g*Time/Vt)) - (Vt * Time)); // Y position
               Vx = (X - XLast) / TICK;       // Estimate Vx from difference
               Vy = (Y - YLast) / TICK;         // Estimate Vy from difference
               XLast = X;         //remembers current value of X
               YLast = Y;  //remembers current value of Y
               
               if (bSim.TracePoint == true) {
               	   GOval Trace = new GOval((ScrX+(BSize* SCALE)), (ScrY+(BSize*SCALE)), (0.01 * SCALE), (0.01 * SCALE));
                   Trace.setFilled(true);
                   Trace.setColor(BColor);
                   Link.add(Trace);
               }
               
               if ((ScrY>=(600-(SCALE*2*BSize)) && (Vy < 0))) {
               	
               	KEx = Vx*0.5*Vx * (1 - Loss);   //calculates KE after collision with ground
                   KEy = 0.5*Vy*Vy* (1 - Loss);
                   Vox = Math.sqrt(2 * KEx);       // calculates horizontal velocity after collision
                   Voy = Math.sqrt(2 * KEy);       // calculates vertical velocity after collision
                   if(((KEx+KEy) < ETHR) || ((KEx+KEy) >= ELast)) {
                   	running = false;
                   	hasEnoughEnergy = false;
                   }
                   else {
                   	ELast = KEx + KEy;
                   	Xo = Math.abs(X);  //remembers the current value of X  
                       Time = 0;         // sets time to 0 at the start of each parabolic path
                       YLast = Y;
                       XLast = X;
                       System.out.println("Bounce");
                   }
                   
                       
                   
               }
               
               if(Theta>90) {
                   ScrX = (int) (Xi - (X * SCALE));       //converts real-world x coordinates to screen coordinates
               }
               else {
                   ScrX = (int) (Xi + (X * SCALE));       //converts real-world x coordinates to screen coordinates
               }
               ScrY = (int) (Yi - (Y * SCALE));     //converts real-world y coordinates to screen coordinates
               MyBall.setLocation(ScrX, ScrY);  // sets new location for the ball
               
               try {                             // pause for 50 milliseconds
                   Thread.sleep(2);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               Time = Time + TICK;  //increments time
           }
        }
    
    }

}

