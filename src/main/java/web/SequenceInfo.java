package web;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Patch;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class SequenceInfo {
    public final long tickLength;
    public final long microsecondLength;
    public final int trackCount;
    public final TrackInfo[] trackInfo;
    public final int resolution;

    public SequenceInfo(Sequence sequence) {
        Track[] tracks = sequence.getTracks();
        this.tickLength =sequence.getTickLength();
        Patch[] patches = sequence.getPatchList();
        this.microsecondLength =sequence.getMicrosecondLength();
        this.resolution = sequence.getResolution();
        float divisionType = sequence.getDivisionType();
        //this is How many Ticks in one Quarter Note (1 Beat)
        int resolution = sequence.getResolution();
        //Tempo is actually a Midi message.
        //The user can record tempo changes
        
        
        this.trackCount = tracks.length;
        this.trackInfo = new TrackInfo[trackCount];
        for(int i=0; i<trackCount; i++){
            Track track = tracks[i];
            TrackInfo ti = new TrackInfo(track);
            this.trackInfo[i] = ti;
        }
    }

    public static class TrackInfo {
        public final int eventCount;
        public final long ticks;
        public final MidiEvent[] events;

        public TrackInfo(Track track){
            this.eventCount = track.size();
            this.ticks = track.ticks();
            this.events = new MidiEvent[this.eventCount];
            for(int i=0; i<track.size(); i++){
                MidiEvent ev = track.get(i);
                this.events[i] = ev;
            }
        }
    }

}
