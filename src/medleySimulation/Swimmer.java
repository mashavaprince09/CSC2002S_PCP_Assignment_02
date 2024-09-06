//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swimmer swimming a race
//Swimmers have one of four possible swim strokes: backstroke, breaststroke, butterfly and freestyle
package medleySimulation;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Swimmer extends Thread {
	public static StadiumGrid stadium; //shared 
	private FinishCounter finish; //shared
		
	GridBlock currentBlock;
	private Random rand;
	private int movingSpeed;
	
	private PeopleLocation myLocation;
	private int ID; //thread ID 
	private int team; // team ID
	private GridBlock start;

	public enum SwimStroke { 
		Backstroke(0,2.5,Color.black),
		Breaststroke(1,2.1,new Color(255,102,0)),
		Butterfly(2,2.55,Color.magenta),
		Freestyle(3,2.8,Color.red);
			
		 private final double strokeTime;
		 private final int order;
		 private final Color colour;   
	
		 private SwimStroke( int order, double sT, Color c) {
				this.strokeTime = sT;
				this.order = order;
				this.colour = c;
			}
	  
			public int getOrder(){
				return order;
			}
	
			public  Color getColour() { 
				return colour; 
			}
	
			public double getstrokeTime(){
				return strokeTime;
			}
		
		}	
   
   private final SwimStroke swimStroke;
		
	private AtomicBoolean checker;//my lane checker
	
	//Constructor
	Swimmer( int ID, int t, PeopleLocation loc, FinishCounter f, int speed, SwimStroke s) {
		this.swimStroke = s;
		this.ID=ID;
		movingSpeed=speed; //range of speeds for swimmers
		this.myLocation = loc;
		this.team=t;
		start = stadium.returnStartingBlock(team);
		finish=f;
		rand=new Random();		
	}
	
	//getter
	public   int getX() { return currentBlock.getX();}	
	
	//getter
	public   int getY() {	return currentBlock.getY();	}
	
	//getter
	public   int getSpeed() { return movingSpeed; }

	
	public SwimStroke getSwimStroke() {
		return swimStroke;
	}

	//!!!You do not need to change the method below!!!
	//swimmer enters stadium area
	public void enterStadium() throws InterruptedException {
		currentBlock = stadium.enterStadium(myLocation);  //
		sleep(200);  //wait a bit at door, look around
	}
	
	//!!!You do not need to change the method below!!!
	//go to the starting blocks
	//printlns are left here for help in debugging
	public void goToStartingBlocks() throws InterruptedException {		
		int x_st= start.getX();
		int y_st= start.getY();
	//System.out.println("Thread "+this.ID + " has start position: " + x_st  + " " +y_st );
	 System.out.println("Thread "+this.ID + " at " + currentBlock.getX()  + " " +currentBlock.getY() );
	 while (currentBlock!=start) {
		//	System.out.println("Thread "+this.ID + " has starting position: " + x_st  + " " +y_st );
		//	System.out.println("Thread "+this.ID + " at position: " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep(movingSpeed*3);  //not rushing 
			currentBlock=stadium.moveTowards(currentBlock,x_st,y_st,myLocation); //head toward starting block
		//	System.out.println("Thread "+this.ID + " moved toward start to position: " + currentBlock.getX()  + " " +currentBlock.getY() );
		}
	//System.out.println("-----------Thread "+this.ID + " at start " + currentBlock.getX()  + " " +currentBlock.getY() );
	}
	
	//!!!You do not need to change the method below!!!
	//dive in to the pool
	private void dive() throws InterruptedException {
		int x= currentBlock.getX();
		int y= currentBlock.getY();
		currentBlock=stadium.jumpTo(currentBlock,x,y-2,myLocation);
	}
	
	//!!!You do not need to change the method below!!!
	//swim there and back
	private void swimRace() throws InterruptedException {
		int x= currentBlock.getX();
		while((boolean) ((currentBlock.getY())!=0)) {
			currentBlock=stadium.moveTowards(currentBlock,x,0,myLocation);
			//System.out.println("Thread "+this.ID + " swimming " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep((int) (movingSpeed*swimStroke.getstrokeTime())); //swim
			//System.out.println("Thread "+this.ID + " swimming  at speed" + movingSpeed );	
		}

		while((boolean) ((currentBlock.getY())!=(StadiumGrid.start_y-1))) {
			currentBlock=stadium.moveTowards(currentBlock,x,StadiumGrid.start_y,myLocation);
			//System.out.println("Thread "+this.ID + " swimming " + currentBlock.getX()  + " " +currentBlock.getY() );
			sleep((int) (movingSpeed*swimStroke.getstrokeTime()));  //swim
		}
		
	}
	
	//!!!You do not need to change the method below!!!
	//after finished the race
	public void exitPool() throws InterruptedException {		
		int bench=stadium.getMaxY()-swimStroke.getOrder(); 		//they line up
		int lane = currentBlock.getX()+1;//slightly offset
		currentBlock=stadium.moveTowards(currentBlock,lane,currentBlock.getY(),myLocation);
	   while (currentBlock.getY()!=bench) {
		 	currentBlock=stadium.moveTowards(currentBlock,lane,bench,myLocation);
			sleep(movingSpeed*3);  //not rushing 
		}
	}

	void setChecker(AtomicBoolean checker){
		this.checker = checker;
	}
	
	public void run() {
		try {
			sleep(movingSpeed+(rand.nextInt(10))); //arriving takes a while
			myLocation.setArrived();
			enterStadium();	//swimmer enters the stadium
			goToStartingBlocks(); // swimmer is at the starting block
			if (swimStroke.getOrder() == 0) { // if the swimmer is a backstroke swimmer
				MedleySimulation.startBarrier.await();	//cyclicbarrier decreases until all 10 backstroke swimmers have arrived
			}
			else {
				// if the swimmer is not a backstroke swimmer, then it has to wait for the previous swimmer to finish before it can dive into the pool
				synchronized (checker) { 
					while (!checker.get() ) { 
						checker.wait();
					}
				}
			}
			checker.set(false);
			dive();	
			swimRace();

			//when swimmers with the order, 3 are swimming then it means those are the final members for each team
			if(swimStroke.getOrder()==3) {
				finish.finishRace(ID, team); // finishline
			}
			else {
				//System.out.println("Thread "+this.ID + " done " + currentBlock.getX()  + " " +currentBlock.getY() );			
				synchronized (checker) {
					checker.set(true); // signal the next swimmer in their team that they have finished their part of the race
					checker.notifyAll(); // signals that the next swimmer can now start their swim.
				}
   			exitPool();//if not last swimmer leave pool
			}

		}  
		catch (BrokenBarrierException | InterruptedException e){
			e.printStackTrace();
		}
		
		}  	
	
}
