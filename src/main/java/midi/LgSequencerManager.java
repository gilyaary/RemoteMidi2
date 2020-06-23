package midi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcp.client.RtMidiConnection;
import tcp.client.RtMidiTransmitter;

import javax.annotation.PostConstruct;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Since Sequence is a concrete class there is some issue with extending it.
A client that has a Sequence instance may alwats create tracks without the manager knowing about it
We will have to think how to resolve this

TODO:
This class is the center of the system.
Provides a facade for all operations.
One additional duty is Discovery
We must discover any input output devices on the local system as well as other nodes.
We need to also maintain status of each device and a unique ID
Doing so will allow:
Associating each TRACK with multiple input/output devices. Some of which are not on the local machine
This allows forming and MANAGING a NETWORK of midi devices.
*/
@Component
public class LgSequencerManager {

    @Autowired
    private LgSequencer sequencer;

    @Autowired
    private RtMidiConnection localHostRtMidiServerConnection;

    @Autowired
    SequencerContext sequencerContext;

    @PostConstruct
    public void init(){

        Integer DEFAULT_TX_PORT_ID = 1;
        Integer DEFAULT_RCV_PORT_ID = 1;

        sequencerContext.transmitterMap.put(DEFAULT_TX_PORT_ID, new RtMidiTransmitter(DEFAULT_TX_PORT_ID, RtMidiTransmitter.MidiInterface.ALSA, 0, localHostRtMidiServerConnection));
        try {
            //TODO:we need more than one receiver. And each receiver should have an ID
            //Each receiver is associated with a keyboard ot another available controller
            //controllers can also include software and hardware. Drum sequencer, software or hardware patch controller etc etc.
            LgSequencerMidiEventReceiver lgMidiReceiver = new LgSequencerMidiEventReceiver(DEFAULT_RCV_PORT_ID, sequencerContext);

            try {
                Transmitter transmitter = MidiSystem.getTransmitter();
                transmitter.setReceiver(lgMidiReceiver);
                sequencerContext.receiverMap.put(DEFAULT_RCV_PORT_ID, lgMidiReceiver);
            }catch (Exception ex){

            }
            //TODO: This is a default, add more receiver as discovered
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public TrackInfo createTrack(String name) {
        if(sequencer != null && sequencer.getSequence() != null) {
            Track track = sequencer.getSequence().createTrack();
            Integer trackInfoId = sequencerContext.trackInfoIdGen.getAndIncrement();
            TrackInfo trackInfo = new TrackInfo(trackInfoId, track);
            trackInfo.getInputPorts().add(new MidiPortInfo(MidiPortInfo.PortType.INPUT, 0, "DefaultInputPort"));
            trackInfo.getOutputPorts().add(new MidiPortInfo(MidiPortInfo.PortType.OUTPUT, 0, "DefaultOutputPort"));
            trackInfo.setName(name);
            sequencerContext.trackInfoMap.put(trackInfoId, trackInfo);
            return trackInfo;
        }
        notifyTrackInputsOutputs();
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
        notifyTrackInputsOutputs();
        return null;
    }

    //TODO: This may be a listener for track config changes event. For now it is just a simple call
    public void notifyTrackInputsOutputs(){
        for (Integer trackInfoId : sequencerContext.trackInfoMap.keySet()){
            TrackInfo trackInfo = sequencerContext.trackInfoMap.get(trackInfoId);
            //TODO:for now set default inputs. Later we MAY allow setting these in the TrackInfo
            //TODO: Also we may be stopped and still accept input events from MIDI controllers and send them to a track
            //The event should then be sent to all outputs associated with this track
            Integer trackId = trackInfo.getId();
            String trackName = trackInfo.getName();
            String command = String.format("3#I.A.V.%s.VIRTUAL_IN_PORT_%s.", trackId, trackName);
            this.localHostRtMidiServerConnection.sendCommand(command);
        }
    }

    public Set<String> getMidiFiles(){
        Set<String> filePaths = Collections.EMPTY_SET;
        try {
            filePaths = listFilesUsingFileWalk("/home/gil/Music", 5);
        }catch(IOException iox){

        }
        return filePaths;
    }

    public Set<String> listFilesUsingFileWalk(String dir, int depth) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.endsWith(".mid") || file.endsWith(".midi"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    /*
    TODO: We need to find a way to save this info in the midi file. As all tracks have information that is not part of the default implementation of javax.midi.Track
    For now just setting default names etc.
     */
    public void loadSequenceFromFileSystem(String midi_file) throws InvalidMidiDataException, IOException {

        //sequencerContext.sequence = MidiSystem.getSequence(new File(midi_file));
        sequencerContext.sequence = new Sequence(0, 32);
        sequencerContext.sequence.createTrack();


        this.sequencer.setSequence(this.sequencerContext.sequence);
        sequencerContext.trackInfoMap.clear();
        sequencerContext.trackInfoIdGen.set(1);
        //set tracks,
        Track[] tracks = this.sequencerContext.sequence.getTracks();
        for (int i=0; i<tracks.length; i++) {
            Track track = tracks[i];
            Integer trackInfoId = sequencerContext.trackInfoIdGen.getAndIncrement();
            TrackInfo trackInfo = new TrackInfo(trackInfoId, track);
            //set a default name
            trackInfo.setName(String.format("Track %s", trackInfoId));
            trackInfo.getInputPorts().add(new MidiPortInfo(MidiPortInfo.PortType.INPUT, 1, "DefaultInputPort"));
            trackInfo.getOutputPorts().add(new MidiPortInfo(MidiPortInfo.PortType.OUTPUT, 1, "DefaultOutputPort"));
            sequencerContext.trackInfoMap.put(trackInfoId, trackInfo);
        }
        notifyTrackInputsOutputs();
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
        String response = this.localHostRtMidiServerConnection.sendCommand("1#@");
        return response;
    }
    public void getInputDevicesFromJavaMidi(){

    }
    public void addPortToTrack(MidiPortInfo portInfo, Integer trackInfoId) throws MidiUnavailableException {


    }


}
