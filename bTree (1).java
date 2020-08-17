
import java.awt.Color;
import acm.graphics.GLabel;
import acm.program.GraphicsProgram;

/**
* @param BallData aBall stores info for each node
* @param left Node links to the left branch
* @param right Node links to the right branch
* @param root Node points to the root of the tree
*/

public class bTree extends GraphicsProgram{
	private class Node{             //creates a node class
		aBall BallData;
		Node left;
		Node right;
	}
 
	private Node root = null;
	public static final double DELTASIZE = 0.1;
	boolean simRunning = false;
	
	/**
	* The makeNode method creates a new Node
	* @param aBall
	* @return Node
	*/
	
	private Node makeNode(aBall iBall) {        
		Node ThisNode = new Node();							
		ThisNode.BallData = iBall;				
		ThisNode.left = null;									
		ThisNode.right = null;								
		return ThisNode;									
	}
	
	public Node getRoot() {
		return root;
	}
	
	/**
	* The addNode method adds a new node to the binary tree
	* @param aBall
	* @return void
	*/
	
	public void addNode(aBall iBall) {         
		Node Current;
		if (root == null) {                    //if empty tree
			root = makeNode(iBall);
		} 
		else {
			Current = root;
			while (true) {
				
				if (iBall.getBallSize() < Current.BallData.getBallSize()) {                   // New data < data at node, branch left
					if (Current.left == null) {				// leaf node
						Current.left = makeNode(iBall);		// attach new node here
						break;
					}
					else {									// otherwise
						Current = Current.left;				// keep traversing
					}
				}
				
				else{                                        // New data >= data at node, branch right
					if (Current.right == null) {			// leaf node	
						Current.right = makeNode(iBall);		// attach new node here
						break;
					}
					else {									// otherwise 
						Current = Current.right;			// keep traversing
					}
				}
			}
		}
		
	}
	
	
	
	public static final int SCALE = 6;
	
	/**
	* The TraverseInOrder method traverses each node in the tree in ascending order
	* @param Node
	* @return void
	*/
	
	public void TraverseInOrder(Node Root) {    
		if(Root.left != null) TraverseInOrder(Root.left);     //follow the left
		if(Root.BallData.getBallState()) simRunning = true;   
		if(Root.right != null) TraverseInOrder(Root.right);   //follow the right branch
	}
	
	/**
	* The isRunning method calls the TraverseInOrder method and checks whether every ball thread in the tree has stopped moving
	* @param void
	* @return boolean
	*/
	
	public boolean isRunning() {               
		simRunning = false;
		TraverseInOrder(root);
		return simRunning;
	}
	
	/**
	* The stackBalls method stacks balls on top of each other according to ball size
	* @param Node
	* @return void
	*/
	double Xs = 5;
	double Ys = 600;
	double CurrentSize = 0;
	double LastSize = 0;
	Color CurrentColor = null;
	
	
	private void stackBalls(Node R) { 
		System.out.println("here");
		if (R.left != null) stackBalls(R.left);             //follow the left branch
	    
		CurrentSize = R.BallData.getBallSize();
		CurrentColor = R.BallData.getColor();
		if((CurrentSize - LastSize) > DELTASIZE) {   //start new stack
			Ys = 600 - (2*SCALE*CurrentSize);
			Xs = Xs + (2*SCALE*LastSize);
		    R.BallData.getBall().setLocation(Xs, Ys);   
		}
		else {    //Put current ball on top of last ball
			Ys = Ys - (2*SCALE*CurrentSize);
			R.BallData.getBall().setLocation(Xs, Ys);
		}
		LastSize = CurrentSize;
		
		if (R.right != null) stackBalls(R.right);          //follow the right branch
	}
	
	public void StackBalls(Node Root) {
		CurrentSize = 0;
		LastSize = 0;
		Xs = 0;
		Ys = 0;
		CurrentColor = null;
		removeAll();
		stackBalls(Root);
	}
	 
	

}
