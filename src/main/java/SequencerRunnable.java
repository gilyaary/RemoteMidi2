import javax.sound.midi.Sequence;
import java.util.concurrent.atomic.AtomicInteger;

public class SequencerRunnable implements Runnable{

    private Sequence sequence;

    private long startTime;
    private boolean exit;
    private boolean stopped;
    enum MODE {PLAYBACK, RECORD}
    MODE mode;
    long positionFromStart = 0;

    public SequencerRunnable(){}

    public void setSequence(Sequence sequence){
        this.sequence = sequence;
    }

    @Override
    public void run() {
        //This thread is running for the whole lifecycle of the application
        //only stops when the application is closed
        while( ! this.exit) {
            //just loop and wait while we are not playing or recording
            while(this.stopped){
                try {
                    wait(100);
                } catch (InterruptedException e) {

                }
            }
            while( ! stopped) {
                //play the sequence then set the status to stopped
                sequence.getTickLength();
                sequence.getTracks();
                sequence.getResolution();
                //play the ticks
                //if no more ticks then change the status to stopped
            }
        }
    }

    public void exit() {
        this.exit = true;
    }

    public void play() {
        this.mode = MODE.PLAYBACK;
        this.stopped = false;
        this.notifyAll();
    }
    public void record() {
        this.mode = MODE.RECORD;
        this.stopped = false;
        this.notifyAll();
    }
    public void pause() {
        this.stopped = true;
        this.notifyAll();
    }
    public void stop() {
        this.pause();
        this.positionFromStart = 0;
    }

}
