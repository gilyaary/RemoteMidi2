package web;

import midi.LgSequencer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tcp.client.RtMidiClient;
import tcp.client.RtMidiConnection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

@RestController
public class SequencerController {

    private LgSequencer sequencer;

    @Autowired
    private RtMidiConnection midiConnection;

    @PostConstruct
    public void init() throws Exception {
        this.sequencer = new LgSequencer();
        this.sequencer.open();
        this.midiConnection.init();
    }

    @PreDestroy
    public void cleanup(){
        this.sequencer.close();
        this.midiConnection.close();
    }

    @GetMapping("/sequencer")
    @RequestMapping(value = "/sequencer", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Sequencer!";
    }

    @GetMapping("/sequencer/ports")
    @RequestMapping(value = "/sequencer/ports", method = RequestMethod.GET)
    public String ports() {
        String response = this.midiConnection.sendCommand("1#@");
        return response;
    }

    @GetMapping("/sequencer/play")
    @RequestMapping(value = "/sequencer/play", method = RequestMethod.GET)
    public String play() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        String midi_file = "/home/gil/Music/gil_music.mid";
        Sequence sequence = MidiSystem.getSequence(new File(midi_file));
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(160);
        long time = System.currentTimeMillis();
        sequencer.start();
        return "started";
    }

    @GetMapping("/sequencer/resume")
    @RequestMapping(value = "/sequencer/resume", method = RequestMethod.GET)
    public String resume() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencer.resume();
        return "resumed";
    }

    @GetMapping("/sequencer/stop")
    @RequestMapping(value = "/sequencer/stop", method = RequestMethod.GET)
    public String stop() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencer.stop();
        return "stopped";
    }

    @GetMapping("/sequencer/test")
    @RequestMapping(value = "/sequencer/test", method = RequestMethod.GET)
    public String test() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {

        return "TEST by gil";
    }

    @GetMapping("/sequencer/test2")
    @RequestMapping(value = "/sequencer/test2", method = RequestMethod.GET)
    public String test2() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {

        return "TEST2 Hahaha by gil";
    }


    public static void main(String[] args) throws Exception {
        SequencerController sc = new SequencerController();
        sc.midiConnection = new RtMidiConnection();
        sc.init();


        //call tested methods
        //String ports = sc.ports(); System.out.print(ports);
        sc.play();


        sc.cleanup();
    }


}
