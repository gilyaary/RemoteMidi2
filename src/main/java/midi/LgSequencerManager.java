package midi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcp.client.RtMidiConnection;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//Since Sequence is a concrete class there is some issue with extending it.
//A client that has a Sequence instance may alwats create tracks without the manager knowing about it
//We will have to think how to resolve this
@Component
public class LgSequencerManager {

    @Autowired
    private LgSequencer sequencer;

    @Autowired
    private RtMidiConnection midiConnection;

    @Autowired
    SequencerContext sequencerContext;

    //TODO:we need more than one receiver. And eachreceiver should have an ID
    //Each receiver is associated with a keyboard ot another available controller
    //controllers can also include software and hardware. Drum sequencer, software or hardware patch controller etc etc.
    private LgMidiReceiver lgMidiReceiver;


    public TrackInfo createTrack(String name) {
        if(sequencer != null && sequencer.getSequence() != null) {
            Track track = sequencer.getSequence().createTrack();
            Integer trackInfoId = sequencerContext.trackInfoIdGen.getAndIncrement();
            TrackInfo trackInfo = new TrackInfo(trackInfoId, track);
            trackInfo.setName(name);
            sequencerContext.trackInfoMap.put(trackInfoId, trackInfo);
            return trackInfo;
        }
        return null;
    }

    public TrackInfo deleteTrack(Integer trackInfoId) {
        if(sequencer != null && sequencer.getSequence() != null) {
            TrackInfo trackInfo = sequencerContext.trackInfoMap.get(trackInfoId);
            if(trackInfo != null) {
                boolean deleted = sequencer.getSequence().deleteTrack(trackInfo.getTrack());
                if (deleted) {
                    sequencerContext.trackInfoMap.remove(trackInfo);
                }
                return trackInfo;
            }
        }
        return null;
    }

    /*
    TODO: We need to find a way to save this info in the midi file. As all tracks have information that is not part of the default implementation of javax.midi.Track
    For now just setting default names etc.
     */
    public void loadSequenceFromFileSystem(String midi_file) throws InvalidMidiDataException, IOException {
        sequencerContext.sequence = MidiSystem.getSequence(new File(midi_file));
        this.sequencer.setSequence(this.sequencerContext.sequence);
        sequencerContext.trackInfoMap.clear();
        sequencerContext.trackInfoIdGen.set(1);
        for (Track track : this.sequencerContext.sequence.getTracks()){
            Integer trackInfoId = sequencerContext.trackInfoIdGen.getAndIncrement();
            TrackInfo trackInfo = new TrackInfo(trackInfoId, track);
            //set a default name
            trackInfo.setName(String.format("Track %s", trackInfoId));
            sequencerContext.trackInfoMap.put(trackInfoId, trackInfo);
        }
    }


    public void setTempoInBPM(int i) {
        this.sequencer.setTempoInBPM(i);
    }

    public void setMicrosecondPosition(int i) {
        this.sequencer.setMicrosecondPosition(i);
    }

    public void start() {
        this.sequencer.start();
    }

    public void stop() {
        this.sequencer.stop();
    }

    public Long getMicrosecondPosition() {
        return this.sequencer.getMicrosecondPosition();
    }








    //TODO: We must implement all the methods below to be able to remord MIDI
    //TODO: we need to have IDs for these ports
    //Also we want to also list Java.midi input ports
    public String getPorts() {
        String response = this.midiConnection.sendCommand("1#@");
        return response;
    }
    public void getInputDevicesFromJavaMidi(){

    }
    public void addPortToTrack(MidiPortInfo portInfo, Integer trackInfoId) throws MidiUnavailableException {
        Transmitter transmitter = MidiSystem.getTransmitter();
        transmitter.setReceiver(lgMidiReceiver);

    }


}
