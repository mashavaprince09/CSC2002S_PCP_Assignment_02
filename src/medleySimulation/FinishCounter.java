package medleySimulation;

public class FinishCounter {
    private boolean firstAcrossLine; // flag to indicate if the race is won
    private int[] winners; // array to store the top 3 finishers
    private int[] winningTeams; // array to store the teams of the top 3 finishers

    // Constructor
    FinishCounter() {
        firstAcrossLine = true; // flag
        winners = new int[3]; // array for top 3 finishers
        winningTeams = new int[3]; // array for teams of the top 3 finishers
        for (int i = 0; i < 3; i++) {
            winners[i] = -1; // initialize with -1 (indicating no finisher)
            winningTeams[i] = -1; // initialize with -1 (indicating no team)
        }
    }

    // This is called by a swimmer when they touch the finish line
    public synchronized void finishRace(int swimmer, int team) {
        boolean added = false;
        for (int i = 0; i < 3; i++) {
            if (winners[i] == -1) {
                winners[i] = swimmer;
                winningTeams[i] = team;
                added = true;
                break;
            }
        }
        
        if (added && firstAcrossLine) {
            firstAcrossLine = false;
        }
    }

    // Has race been won?
    synchronized public boolean isRaceWon() {
        return !firstAcrossLine;
    }

    synchronized public int getWinner() {
        return winners[0];
    }
    
    synchronized public int getWinningTeam() {
        return winningTeams[0] + 1;
    }

    // Methods to get the places for 2nd, 3rd
    synchronized public int getSecondPlace() {
        return winners[1];
    }

    synchronized public int getSecondPlaceTeam() {
        return winningTeams[1] + 1;
    }

    synchronized public int getThirdPlace() {
        return winners[2];
    }

    synchronized public int getThirdPlaceTeam() {
        return winningTeams[2] + 1;
    }
}
