package midi;

import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;

public class TrackInfo {
    private final Track track;
    private final Integer id;
    public int currentEventIndex;
    private String name;

    private TrackStatus trackStatus = new TrackStatus();

    public List<MidiPortInfo> getInputPorts() {
        return inputPorts;
    }

    public void setInputPorts(List<MidiPortInfo> inputPorts) {
        this.inputPorts = inputPorts;
    }

    public List<MidiPortInfo> getOutputPorts() {
        return outputPorts;
    }

    public void setOutputPorts(List<MidiPortInfo> outputPorts) {
        this.outputPorts = outputPorts;
    }

    //we can have some config sepecific info here
    //for example what device (Port) is associated with this track
    //this may be for a duration of a runtime or maybe saved to a file in the future
    List<MidiPortInfo> inputPorts = new ArrayList();
    List<MidiPortInfo> outputPorts = new ArrayList();

    public TrackInfo(Integer id, Track track) {
        this.track = track;
        this.id = id;
    }

    public Track getTrack(){
        return track;
    }
    public Integer getId(){return this.id;}


    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public TrackStatus getTrackStatus() {
        return trackStatus;
    }

}
