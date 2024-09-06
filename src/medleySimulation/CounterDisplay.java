package medleySimulation;

import java.awt.Color;
import javax.swing.JLabel;

public class CounterDisplay implements Runnable {
    private FinishCounter results;
    private JLabel win;

    CounterDisplay(JLabel w, FinishCounter score) {
        this.win = w;
        this.results = score;
    }

    public void run() {
        while (true) {
            if (results.isRaceWon()) {
                win.setForeground(Color.RED);
                win.setText("<html>" +
                    "First Place: " + results.getWinningTeam() + " 🥇<br>" +
                    "Second Place: " + results.getSecondPlaceTeam() + " 🥈<br>" +
                    "Third Place: " + results.getThirdPlaceTeam() + " 🥉<br>" +
                "</html>");
            } else {
                win.setForeground(Color.BLACK);
                win.setText("------");
            }
        }
    }
}
