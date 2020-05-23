package web;

import midi.LgSequencer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcp.client.RtMidiConnection;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

@RestController
public class SequencerController {

    @Autowired
    private LgSequencer sequencer;

    @Autowired
    private RtMidiConnection midiConnection;

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

    @PutMapping("/sequencer/play")
    @RequestMapping(value = "/sequencer/play", method = RequestMethod.PUT)
    public String play() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        String midi_file = "/home/gil/Music/gil_music.mid";
        Sequence sequence = MidiSystem.getSequence(new File(midi_file));
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(160);
        sequencer.setMicrosecondPosition(0);
        sequencer.start();
        return "started";
    }

    @PutMapping("/sequencer/resume")
    @RequestMapping(value = "/sequencer/resume", method = RequestMethod.PUT)
    public String resume() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencer.start();
        return "resumed";
    }

    @PutMapping("/sequencer/stop")
    @RequestMapping(value = "/sequencer/stop", method = RequestMethod.PUT)
    public String stop() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencer.stop();
        return "stopped";
    }

    //Ping it
    @GetMapping("/sequencer/test")
    @RequestMapping(value = "/sequencer/test", method = RequestMethod.GET)
    public String isAlive() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {

        return "Alive";
    }

    @GetMapping("/sequencer/position")
    @RequestMapping(value = "/sequencer/position", method = RequestMethod.GET)
    public Long getPosition() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        return sequencer.getMicrosecondPosition();
    }

    @PutMapping("/sequencer/position")
    @RequestMapping(value = "/sequencer/position", method = RequestMethod.PUT)
    public void setPosition(@RequestParam Long position) throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencer.setMicrosecondPosition(position);
    }
}

//TODO:
//1. Correctly start the sequencer
//2. set position
//3. get position