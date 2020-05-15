package midi;

//import com.sun.media.sound.RealTimeSequencerProvider;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {


    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {


        LgSequencer seq = new LgSequencer();
        seq.open();

        //String midi_file = "/home/gil/Music/bach2.mid";
        //String midi_file = "/home/gil/Music/mozk313c.mid";
        //Sequence sequence = MidiSystem.getSequence(new File(midi_file));
        Sequence sequence = generateMidi();

        seq.setSequence(sequence);
        seq.setTempoInBPM(10);
        long time = System.currentTimeMillis();
        seq.start();
        //seq.close();
    }



    public static void main2(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {

        LgSequencer seq = new LgSequencer();
        seq.setTempoInBPM(120);
        seq.open();

        Sequence sequence = MidiSystem.getSequence(new File("/home/gil/Music/test_midi.mid"));
        seq.setSequence(sequence);
        //long ticks = seq.getTickLength();
        //System.out.println(ticks);
        double tickPosition = seq.getMicrosecondPosition()/1000000D;
        //System.out.println(tickPosition);
        long time = System.currentTimeMillis();

        for(int i=0; i<20;i++) {
            printPosition(seq, time);
            seq.start();
            Thread.sleep(1000);
            seq.stop();
            //same position because we stopped
            printPosition(seq, time);
            Thread.sleep(1000);
        }

        seq.close();
    }

    private static Sequence generateMidi() throws InvalidMidiDataException {
        Sequence seq = new Sequence(0, 32);
        Track track = seq.createTrack();
        int j=0;
        for(int i=0; i<100000; i+=4, j++) {
            Random r = new Random();
            /*
            int note = r.nextInt()%6  + 60;
            ShortMessage msg1 = new ShortMessage(ShortMessage.NOTE_ON, 1, note, 100);
            MidiEvent event1 = new MidiEvent(msg1, i*20);
            track.add(event1);
            ShortMessage msg2 = new ShortMessage(ShortMessage.NOTE_OFF, 1, note, 0);
            MidiEvent event2 = new MidiEvent(msg2, (i+1)*20);
            track.add(event2);
            */
            int note = 60;// + j%4;
            playNote(track, i, note);

        }
        return seq;
    }

    private static void playNote(Track track, int i, int note) throws InvalidMidiDataException {
        ShortMessage msg1 = new ShortMessage(ShortMessage.NOTE_ON, note, 100);
        MidiEvent event1 = new MidiEvent(msg1, i*20);
        track.add(event1);
        ShortMessage msg2 = new ShortMessage(ShortMessage.NOTE_OFF, note, 0);
        MidiEvent event2 = new MidiEvent(msg2, (i+1)*20);
        track.add(event2);
    }

    //Use Calf synthesizer

    private static void printPosition(LgSequencer seq, long time) {
        double tickPosition;
        tickPosition = seq.getMicrosecondPosition() / 1000000D;
        System.out.println(tickPosition);
        //System.out.println(System.currentTimeMillis() - time);
        System.out.println("--------");
    }
}
