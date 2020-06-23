package midi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcp.client.RtMidiConnection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
public class LgSequencer implements Sequencer {


    @Autowired
    SequencerContext sequencerContext;

    Semaphore sequencePlayerSemaphore = new Semaphore(1);
    SequencerRunnable sequencerRunnable = new SequencerRunnable();
    Thread sequencerThread = null;
    long songPositionMs = 0;
    private float bpm = 60;
    private boolean running;

    @Autowired
    private RtMidiConnection midiConnection;
    private List<Receiver> receivers = new ArrayList<>();

    @PostConstruct
    public void init() throws Exception {
        this.open();
        //TODO: this is a connection to a single local machine. We may want to connect to multiple cards (machines)
        try {
            this.midiConnection.init();
        }catch(Exception ex){
            System.out.println("Could not connect to RTServer");
            //TODO: update sequencer status to not connected
        }
    }

    @PreDestroy
    public void cleanup(){
        this.close();
        this.midiConnection.destroy();
    }


    //should only happen once in the sequencer lifetime
    private void initialize(){
        sequencerRunnable.setBpm(this.bpm);
        sequencerRunnable.sequencerContext = sequencerContext;
        sequencerThread = new Thread(sequencerRunnable);
        sequencerThread.start();
    }

    private void destroy(){
        stop();
        sequencerRunnable.exit();
    }

    //We want to wrap the sequence class and its embedded tracks in our own
    //when we save we want to save as own own sequence class
    //This should be backward compatible. So generic formats could be read and wrapped with our
    //special classes.
    //Not doing so would mean that we are saving the info related to each track in a different file
    @Override
    public void setSequence(Sequence sequence){
        //this.sequence = sequence;
        this.sequencerContext.sequence = sequence;
        this.sequencerRunnable.initSequence();
        this.songPositionMs = 0;
    }

    @Override
    public void setSequence(InputStream stream) throws IOException, InvalidMidiDataException {
        //to do: read the stream and convert to a sequence object
    }

    @Override
    public Sequence getSequence() {
        /*
        Track[] tracks = sequence.getTracks();
        for(Track t : tracks){
            //record and event
            t.add(ev);
            //remove an event
            t.remove(ev);
            Track tt = sequence.createTrack();
        }
        */
        return this.sequencerContext.sequence;
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
        this.running = true;
        if(this.sequencerContext.sequence != null){
            sequencerRunnable.setBpm(this.getTempoInBPM());
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
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
        if(this.sequencerContext.sequence != null){
            //a mutex is a Semaphore that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
                    //start the thread. We want only one executing thread
                    sequencerRunnable.stop();
                } catch (Exception ex) {

                } finally {
                    sequencePlayerSemaphore.release();
                }
            }
        }
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void startRecording() {
        if(this.sequencerContext.sequence != null){
            //a mutex is a Semaphor that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    //sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
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
        if(this.sequencerContext.sequence != null){
            //a mutex is a Semaphor that allows only one thread at a time to access a protected method
            if(sequencePlayerSemaphore.availablePermits() > 0) {
                try {
                    //sequencePlayerSemaphore.tryAcquire(10, TimeUnit.MILLISECONDS);
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
        return bpm;
    }

    @Override
    public void setTempoInBPM(float bpm) {
        this.bpm = bpm;
        this.sequencerRunnable.setBpm(bpm);
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
        return sequencerRunnable.getiPlayTimeInTicks();
    }

    @Override
    public void setTickPosition(long tick) {
        //todo: let the runnable handle this
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
        this.initialize();
    }

    @Override
    public void close() {
        this.destroy();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public long getMicrosecondPosition() {
        //System.out.println(sequencerRunnable.songPositionMs);
        return sequencerRunnable.songPositionMs * 1000;
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
        //this should create a new receiver through which we send NEW midi events to the sequencer.
        //The sequencer should add these new events to their tracks
        LgSequencerReceiver receiver = new LgSequencerReceiver();
        this.receivers.add(receiver);
        return receiver;
    }

    @Override
    public List<Receiver> getReceivers() {
        return this.receivers;
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
    public void setMicrosecondPosition(long seconds) {
        this.songPositionMs = seconds * 1000;
        this.sequencerRunnable.setSongPositionMs(this.songPositionMs);
        //let the runnable handle this
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
