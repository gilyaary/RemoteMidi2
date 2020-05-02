import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException, InterruptedException {

        LgSequencer seq = new LgSequencer();
        seq.open();

        Sequence sequence = MidiSystem.getSequence(new File("/home/gil/Music/test_midi.mid"));
        seq.setSequence(sequence);
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

    private static void printPosition(LgSequencer seq, long time) {
        double tickPosition;
        tickPosition = seq.getMicrosecondPosition() / 1000000D;
        System.out.println(tickPosition);
        //System.out.println(System.currentTimeMillis() - time);
        System.out.println("--------");
    }
}
