# Medley Simulation README

## Overview

The `medleySimulation` project simulates a swimming race involving swimmers using different strokes (backstroke, breaststroke, butterfly, and freestyle). The swimmers participate in a medley relay race, with each swimmer taking turns to swim a segment of the race. The simulation is designed using a multi-threaded approach to represent each swimmer as a separate thread, ensuring concurrent execution of multiple swimmers. The project also integrates synchronization mechanisms to control the flow of the race, ensuring that swimmers from the same team wait for their turn before diving into the pool.

## Key Classes and Components

### 1. **Swimmer Class**

The `Swimmer` class represents an individual swimmer participating in the race. It extends the `Thread` class to allow each swimmer to run in its own thread. The class models a swimmer's behavior, including entering the stadium, moving to the starting block, swimming the race, and exiting the pool.

#### Key Features:
- Each swimmer is assigned a unique ID and belongs to a team.
- Swimmers are assigned a swim stroke (backstroke, breaststroke, butterfly, or freestyle), each with its own speed, color, and stroke time.
- The swimmer’s progress is tracked using a shared `StadiumGrid` object, which models the stadium or pool.
- Swimmers move towards the starting block, dive into the pool, swim to the other end, and then exit the pool.
- Swimmers synchronize with other team members to ensure the relay race progresses in the correct order.

### 2. **Swim Stroke Enum**

The `SwimStroke` enum defines the different swim strokes used in the race. Each stroke has the following attributes:
- **`strokeTime`**: The time it takes to swim a certain distance using that stroke.
- **`order`**: The order in which the swimmer with that stroke participates in the race.
- **`colour`**: The color associated with that stroke.

### 3. **StadiumGrid**

The `StadiumGrid` represents the layout of the pool and the swimmer’s movement within it. The swimmers interact with the `StadiumGrid` to:
- Enter the stadium.
- Move towards their starting blocks.
- Dive into the pool and swim the race.
- Exit the pool after completing their segment.

### 4. **FinishCounter**

The `FinishCounter` is responsible for tracking when each swimmer finishes their segment of the race. It ensures that the race progresses in an orderly manner and that the last swimmer of each team can signal the end of the race.

### 5. **Synchronization**

- **CyclicBarrier**: A `CyclicBarrier` is used to ensure that all backstroke swimmers start their segment at the same time.
- **AtomicBoolean**: An `AtomicBoolean` is used to synchronize the swimming order for swimmers within the same team. Non-backstroke swimmers wait for the previous swimmer to finish before they can dive into the pool.

## Running the Simulation

To run the simulation, ensure you have a Java environment set up. The `Swimmer` class will create and run threads for each swimmer in the race. The main simulation runs as follows:

1. **Initialization**: 
   - Each swimmer is created with an ID, team number, starting location, speed, and swim stroke.
   - The stadium and race grid are initialized.
   
2. **Swimmer Actions**: 
   - Swimmers enter the stadium and move towards their starting blocks.
   - After reaching their starting blocks, swimmers wait for the synchronization mechanism (either a `CyclicBarrier` for backstroke swimmers or an `AtomicBoolean` for other swimmers) before diving into the pool.
   - They swim the race, moving along the grid, and after completing their segment, they exit the pool.
   
3. **Race Completion**: 
   - The race continues until the last swimmer completes their segment, signaling the end of the race.

## How to Modify the Simulation

You can modify the simulation by adjusting the following parameters:

- **Swimmer Speed**: Change the speed of swimmers by modifying the `movingSpeed` parameter.
- **Swim Strokes**: Add or modify swim strokes in the `SwimStroke` enum. Each stroke has a `strokeTime` (how long it takes to swim), `order` (when the swimmer participates in the race), and a `colour` (which can be used for visual representation).
- **Synchronization**: The synchronization mechanism can be modified to allow more complex behavior or adjust how swimmers wait for each other.
  
## Example Use Case

### Example Code for Creating and Running Swimmers:

```java
public class MedleySimulation {

    public static void main(String[] args) {
        // Initialize the shared components
        StadiumGrid stadium = new StadiumGrid();
        FinishCounter finishCounter = new FinishCounter();

        // Create swimmers for each team
        Swimmer swimmer1 = new Swimmer(1, 0, new PeopleLocation(0, 0), finishCounter, 5, Swimmer.SwimStroke.Backstroke);
        Swimmer swimmer2 = new Swimmer(2, 0, new PeopleLocation(0, 1), finishCounter, 4, Swimmer.SwimStroke.Breaststroke);
        Swimmer swimmer3 = new Swimmer(3, 0, new PeopleLocation(0, 2), finishCounter, 6, Swimmer.SwimStroke.Butterfly);
        Swimmer swimmer4 = new Swimmer(4, 0, new PeopleLocation(0, 3), finishCounter, 5, Swimmer.SwimStroke.Freestyle);
        
        // Start the swimmers
        swimmer1.start();
        swimmer2.start();
        swimmer3.start();
        swimmer4.start();
    }
}
```

### Example Output:

```
Thread 1 at position: 0 0
Thread 2 at position: 0 0
Thread 3 at position: 0 0
Thread 4 at position: 0 0
Thread 1 at starting position: 0 0
Thread 2 at starting position: 0 0
Thread 3 at starting position: 0 0
Thread 4 at starting position: 0 0
Thread 1 swimming 10 5
Thread 2 swimming 10 5
Thread 3 swimming 10 5
Thread 4 swimming 10 5
Thread 1 finished the race!
```

## Dependencies

This project is written in Java and does not have external dependencies beyond the standard Java libraries for concurrency and graphical elements.

- **Java 8 or higher** is required to run the simulation.
- **AWT (Abstract Window Toolkit)** is used for color representation.

## Conclusion

This project simulates a medley swimming race with synchronized swimmers using threads, shared components, and synchronization mechanisms. The `Swimmer` class represents each swimmer, while the simulation ensures that the race runs smoothly, with each swimmer waiting for their turn based on the relay race order. The use of threads allows for concurrent execution, making the simulation more realistic and dynamic.
