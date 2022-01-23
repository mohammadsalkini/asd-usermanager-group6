package utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

@Setter
@Getter
public class SessionTimer {
    private Timer timer;
    private boolean isSessionValid;

    public SessionTimer(int seconds) {
        isSessionValid = true;
        startTimer(seconds);
    }

    public void resetTimer(int seconds) {
        this.timer.cancel();
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
}
