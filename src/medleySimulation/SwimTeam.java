//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.*;
import medleySimulation.Swimmer.SwimStroke;;

public class SwimTeam extends Thread {
	static CountDownLatch startSwimmers = new CountDownLatch(1);
	AtomicBoolean isPrince = new AtomicBoolean();

	public static StadiumGrid stadium; //shared 
	private Swimmer [] swimmers;
	private int teamNo; //team number 
	public static final int sizeOfTeam=4; // number of swimmers per team



	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr ) {
		this.teamNo=ID;
		
		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);

		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+20); //range of speeds 
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s]); //hardcoded speed for now
			swimmers[s].setPrince(isPrince);
		}
	}
		
    public void run() {
        try {			
			MedleySimulation.startLatch.await();
			swimmers[0].start();
			synchronized (this) {
				for (int s = 1; s < sizeOfTeam;) {
					//System.out.println("team "+teamNo+", swimmer "+s+" started");
					if (swimmers[s-1].currentBlock != null) {
						swimmers[s++].start(); // Start each swimmer thread	
					}
					
				}					
			}
						
        } catch (InterruptedException startLatchException) {
            startLatchException.printStackTrace();
        }
    }
}
	

