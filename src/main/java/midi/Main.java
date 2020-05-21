package midi;

//import com.sun.media.sound.RealTimeSequencerProvider;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {
        LgSequencer seq = new LgSequencer();
        seq.open();
        //String midi_file = "/home/gil/Music/bach2.mid";
        //String midi_file = "/home/gil/Music/mozk313c.mid";
        String midi_file = "/home/gil/Music/gil_music.mid";
        Sequence sequence = MidiSystem.getSequence(new File(midi_file));
        seq.setSequence(sequence);
        seq.setTempoInBPM(160);
        long time = System.currentTimeMillis();
        seq.start();
    }
}
