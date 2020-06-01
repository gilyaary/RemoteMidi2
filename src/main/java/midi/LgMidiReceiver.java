package midi;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;

public class LgMidiReceiver implements Receiver {


    //TODO: add a constructor that gets a PortId for this Receiver
    Integer portId;
    List<TrackInfo> associatedTracks = new ArrayList<>();

    @Override
    public void send(MidiMessage midiMessage, long l) {
        //TODO: we need to process these events (which come from a keyboard or hardware controller)
        //First thing is to add these to the tracks that have this receiver as an input device.
        for(TrackInfo trackInfo : associatedTracks){
            Track track = trackInfo.getTrack();
            TrackStatus trackStatus = trackInfo.getTrackStatus();
            /*
            List<MidiPortInfo> inputs = trackInfo.getInputPorts();
            for(MidiPortInfo portInfo : inputs){
                Integer portId = portInfo.portId;
                //TODO: rethink ...
            }
            */
            if(trackStatus.armed){
                //this effectively records the event. What is left undone is sending the modified sequence to a file when recording is done.
                //Also, we may want to keep a history of midi sequence changes and diff
                MidiEvent ev = new MidiEvent(midiMessage, l);
                track.add(ev);
            }
            if( ! trackStatus.muted){
                //TODO: we need to send this REALTIME event to all of the OUTPUT devices associated with this track.
                //Alternatively, the sequencer may be able to monitor this track and discover a new un-played event and send to the output
            }

        }
    }

    @Override
    public void close() {

    }
}
