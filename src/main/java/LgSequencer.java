import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class LgSequencer implements Sequencer {
    private Sequence sequence;
    Semaphore sequencePlayerSemaphore = new Semaphore(1);
    SequencerRunnable sequencerRunnable = new SequencerRunnable();
    Thread sequencerThread = null;

    public void initialize(){
        sequencerThread = new Thread(sequencerRunnable);
        sequencerThread.start();
    }

    public void destroy(){
        stop();
        sequencerRunnable.exit();
    }

    @Override
    public void setSequence(Sequence sequence){
        this.sequence = sequence;
        this.sequencerRunnable.setSequence(sequence);
    }

    @Override
    public void setSequence(InputStream stream) throws IOException, InvalidMidiDataException {
        //to do: read the stream and convert to a sequence object
    }

    @Override
    public Sequence getSequence() {
        return sequence;
    }

    /*
        MidiUtils:
            public static long microsecond2tick(Sequence seq, long micros, TempoCache cache)
            static long tick2microsecond(Sequence seq, long tick, TempoCache cache) {
            Many more conversion methods
            Each track has it's own tick length.
     */


    @Override
    public void start() {
        if(this.sequence != null){
            //a mutex is a Semaphor that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
                    //start the thread. We want only one executing thread
                    sequencerRunnable.setSequence(this.sequence);
                    sequencerRunnable.play();

                } catch (Exception ex) {

                } finally {
                    sequencePlayerSemaphore.release();
                }
            }
        }
    }

    @Override
    public void stop() {
        if(this.sequence != null){
            //a mutex is a Semaphor that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
                    //start the thread. We want only one executing thread
                } catch (Exception ex) {

                } finally {
                    sequencePlayerSemaphore.release();
                }
            }
        }
    }


    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void startRecording() {
        if(this.sequence != null){
            //a mutex is a Semaphor that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
                    //start the thread. We want only one executing thread
                } catch (Exception ex) {

                } finally {
                    sequencePlayerSemaphore.release();
                }
            }
        }
    }

    @Override
    public void stopRecording() {
        if(this.sequence != null){
            //a mutex is a Semaphor that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
                    //start the thread. We want only one executing thread
                } catch (Exception ex) {

                } finally {
                    sequencePlayerSemaphore.release();
                }
            }
        }
    }

    @Override
    public boolean isRecording() {
        return false;
    }

    @Override
    public void recordEnable(Track track, int channel) {

    }

    @Override
    public void recordDisable(Track track) {

    }

    @Override
    public float getTempoInBPM() {
        return 0;
    }

    @Override
    public void setTempoInBPM(float bpm) {

    }

    @Override
    public float getTempoInMPQ() {
        return 0;
    }

    @Override
    public void setTempoInMPQ(float mpq) {

    }

    @Override
    public void setTempoFactor(float factor) {

    }

    @Override
    public float getTempoFactor() {
        return 0;
    }

    @Override
    public long getTickLength() {
        return 0;
    }

    @Override
    public long getTickPosition() {
        return 0;
    }

    @Override
    public void setTickPosition(long tick) {

    }

    @Override
    public long getMicrosecondLength() {
        return 0;
    }

    @Override
    public Info getDeviceInfo() {
        return null;
    }

    @Override
    public void open() throws MidiUnavailableException {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public int getMaxReceivers() {
        return 0;
    }

    @Override
    public int getMaxTransmitters() {
        return 0;
    }

    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        return null;
    }

    @Override
    public List<Receiver> getReceivers() {
        return null;
    }

    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        return null;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return null;
    }

    @Override
    public void setMicrosecondPosition(long microseconds) {

    }

    @Override
    public void setMasterSyncMode(SyncMode sync) {

    }

    @Override
    public SyncMode getMasterSyncMode() {
        return null;
    }

    @Override
    public SyncMode[] getMasterSyncModes() {
        return new SyncMode[0];
    }

    @Override
    public void setSlaveSyncMode(SyncMode sync) {

    }

    @Override
    public SyncMode getSlaveSyncMode() {
        return null;
    }

    @Override
    public SyncMode[] getSlaveSyncModes() {
        return new SyncMode[0];
    }

    @Override
    public void setTrackMute(int track, boolean mute) {

    }

    @Override
    public boolean getTrackMute(int track) {
        return false;
    }

    @Override
    public void setTrackSolo(int track, boolean solo) {

    }

    @Override
    public boolean getTrackSolo(int track) {
        return false;
    }

    @Override
    public boolean addMetaEventListener(MetaEventListener listener) {
        return false;
    }

    @Override
    public void removeMetaEventListener(MetaEventListener listener) {

    }

    @Override
    public int[] addControllerEventListener(ControllerEventListener listener, int[] controllers) {
        return new int[0];
    }

    @Override
    public int[] removeControllerEventListener(ControllerEventListener listener, int[] controllers) {
        return new int[0];
    }

    @Override
    public void setLoopStartPoint(long tick) {

    }

    @Override
    public long getLoopStartPoint() {
        return 0;
    }

    @Override
    public void setLoopEndPoint(long tick) {

    }

    @Override
    public long getLoopEndPoint() {
        return 0;
    }

    @Override
    public void setLoopCount(int count) {

    }

    @Override
    public int getLoopCount() {
        return 0;
    }
}
