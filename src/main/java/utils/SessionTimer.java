package utils;

import java.util.Timer;
import java.util.TimerTask;

public class SessionTimer {
    private Timer timer;
    public boolean isSessionValid;

    public SessionTimer(int seconds) {
        isSessionValid = true;
        startTimer(seconds);
    }

    private void startTimer(int seconds) {
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isSessionValid = false;
            }
        }, seconds * 1000L);
    }

    public void resetTimer(int seconds) {
        this.timer.cancel();
        isSessionValid = true;
        startTimer(seconds);
    }
}
