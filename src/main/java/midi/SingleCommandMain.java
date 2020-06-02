package midi;

//import com.sun.media.sound.RealTimeSequencerProvider;

import tcp.client.RtMidiConnection;

import javax.sound.midi.*;
import java.io.IOException;

public class SingleCommandMain {

    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {
        RtMidiConnection tx = new RtMidiConnection();
        MidiMessage msg = null;
        tx.init();

        int channel = 0;

        for(int sound=0; sound<127; sound++) {
            ShortMessage pcMessage = new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, sound, 0);
            tx.sendMidiMessage(pcMessage, "2#A.2");
            msg = new ShortMessage(ShortMessage.NOTE_ON, 60, 127);
            tx.sendMidiMessage(msg, "2#A.2");
            Thread.sleep(1000);
            msg = new ShortMessage(ShortMessage.NOTE_OFF, 60, 127);
            tx.sendMidiMessage(msg, "2#A.2");
        }

        tx.close();
    }
}
