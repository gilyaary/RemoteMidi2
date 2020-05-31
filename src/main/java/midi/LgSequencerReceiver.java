package midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class LgSequencerReceiver implements Receiver {

    //each receiver should be associated with a track
    @Override
    public void send(MidiMessage message, long timeDiff){
        //take the message and do the following:
        //if the track is not muted send the event to the device mapped to the track
        //if the recording is on and track armed then ADD this event to the track
        //by the end of the recording we can save the sequence
    }
    @Override
    public void close(){

    }

}
