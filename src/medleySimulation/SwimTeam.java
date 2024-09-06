//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import java.util.concurrent.atomic.*;
import medleySimulation.Swimmer.SwimStroke;

public class SwimTeam extends Thread {
;
	AtomicBoolean checker = new AtomicBoolean(true);

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
	      int speed=(int)(Math.random() * (3)+30); //range of speeds 
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s]); //hardcoded speed for now
			swimmers[s].setChecker(checker);
		}
	}
		
    public void run() {
        try {			
			MedleySimulation.startLatch.await(); //wait until the start button is pressed
			swimmers[0].start(); // start the black swimmer first 
			synchronized (this) {
				for (int j = 1; j < sizeOfTeam;) {
					System.out.println("team "+teamNo+", swimmer "+j+" started");
					// only create new swimmers when the previous swimmer has reached the starting block(currentBlock will no longer be null)
					if (swimmers[j-1].currentBlock != null)
						swimmers[j++].start();					
					
				}					
			}
						
        } catch (InterruptedException e) {
        }
    }
}