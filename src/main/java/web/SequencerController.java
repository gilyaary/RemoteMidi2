package web;

import midi.LgSequencer;
import midi.LgSequencerManager;
import midi.TrackInfo;
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
    private LgSequencerManager sequencerManager;



    @GetMapping("/sequencer")
    @RequestMapping(value = "/sequencer", method = RequestMethod.GET)
    public String index() {
        return "Greetings from Sequencer!";
    }

    @GetMapping("/sequencer/ports")
    @RequestMapping(value = "/sequencer/ports", method = RequestMethod.GET)
    public String ports() {
        String response = sequencerManager.getPorts();
        return response;
    }

    @PutMapping("/sequencer/play")
    @RequestMapping(value = "/sequencer/play", method = RequestMethod.PUT)
    public String play() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        String midi_file = "/home/gil/Music/gil_music.mid";
        sequencerManager.loadSequenceFromFileSystem(midi_file);
        sequencerManager.setTempoInBPM(160);
        sequencerManager.setMicrosecondPosition(0);
        sequencerManager.start();
        return "started";
    }

    @PutMapping("/sequencer/resume")
    @RequestMapping(value = "/sequencer/resume", method = RequestMethod.PUT)
    public String resume() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencerManager.start();
        return "resumed";
    }

    @PutMapping("/sequencer/stop")
    @RequestMapping(value = "/sequencer/stop", method = RequestMethod.PUT)
    public String stop() throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencerManager.stop();
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
        return sequencerManager.getMicrosecondPosition();
    }

    @PutMapping("/sequencer/position")
    @RequestMapping(value = "/sequencer/position", method = RequestMethod.PUT)
    public void setPosition(@RequestParam Integer position) throws MidiUnavailableException, InvalidMidiDataException, IOException, InterruptedException {
        sequencerManager.setMicrosecondPosition(position);
    }

    @PostMapping("/sequencer/track")
    @RequestMapping(value = "/sequencer/track", method = RequestMethod.POST)
    public TrackInfo createTrack(@RequestParam String name) {
        return sequencerManager.createTrack(name);
    }

    @PostMapping("/sequencer/track")
    @RequestMapping(value = "/sequencer/track", method = RequestMethod.POST)
    public TrackInfo deleteTrack(@RequestParam Integer trackInfoId) {
        return sequencerManager.deleteTrack(trackInfoId);
    }



}

//TODO:
/*
1. Correctly start the sequencer: done
2. set position: done
3. get position: done
4. View MIDI files anywhere on the file system, possibly on the network, Open MIDI file
5. View the played MIDI score(Simple file transfer)
6  Clips: What is it exactly? We need to be able to define clips and edit them
7. Add/Remove Track - Added
8. Record
9. Change Device and Channel (MIDI message)

 */