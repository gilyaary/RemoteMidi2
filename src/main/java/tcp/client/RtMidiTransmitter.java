package tcp.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

//Transmits MIDI events to the RtMidi Server.
//@Component
public class RtMidiTransmitter implements Receiver {

    private final Integer portId;
    //TODO: when we start supporting connections to multiple nodes we may have
    //more than one instance of RtMidiConnection
    RtMidiConnection rtMidiConnection;

    String commandRouting;
    public static enum MidiInterface{ALSA, JACK};

    public RtMidiTransmitter(Integer portId, MidiInterface midiInterface, int portNumber, RtMidiConnection conn){
        this.portId = portId;
        //"2#A.1"
        String commandPrefix = "2#";
        String interfaceSymbol = midiInterface == MidiInterface.ALSA ? "A" : midiInterface == MidiInterface.JACK ? "J" : "A";
        commandRouting = String.format("%s%s.%s", commandPrefix, interfaceSymbol, portNumber);
        this.rtMidiConnection = conn;
    }

    @Override
    public void send(MidiMessage midiMessage, long l) {
        try {
            rtMidiConnection.sendMidiMessage(midiMessage, commandRouting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        //we do not close from here
    }

    boolean lastWasOn = false;
    int lastNote = 0;

}
