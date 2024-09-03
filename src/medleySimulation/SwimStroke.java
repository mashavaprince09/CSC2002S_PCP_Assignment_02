package medleySimulation;
import java.awt.Color;
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