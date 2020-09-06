package midi;

import org.springframework.beans.factory.annotation.Autowired;
import tcp.client.RtMidiTransmitter;

import javax.sound.midi.*;
import java.util.List;
import java.util.Map;

public class SequencerRunnable implements Runnable{

    long songPositionMs = 0;
    private boolean exit;
    private boolean stopped = true;
    private float beatsPerMinute;
    private Synthesizer synth;

    SequencerContext sequencerContext;
    private volatile long iPlayTimeInTicks;

    public long getiPlayTimeInTicks(){
        return iPlayTimeInTicks;
    }



    enum MODE {PLAYBACK, RECORD}
    MODE mode;
    //long positionFromStart = 0;

    public SequencerRunnable(){}

    public void initSequence(){

        Map<Integer, TrackInfo> trackInfoMap = this.sequencerContext.trackInfoMap;

        for (Integer trackId : trackInfoMap.keySet()){
            TrackInfo trackInfo = trackInfoMap.get(trackId);
            Track track = trackInfo.getTrack();
            trackInfo.getInputPorts().add(new MidiPortInfo(MidiPortInfo.PortType.INPUT, 0, "DefaultInputPort"));
            trackInfo.getOutputPorts().add(new MidiPortInfo(MidiPortInfo.PortType.OUTPUT, 0, "DefaultOutputPort"));
            int eventCount = track.size();
            for(int i=0; i<eventCount; i++){
                MidiEvent ev = track.get(i);
                long tick = ev.getTick();
                //System.out.println(tick);
                //MidiUtils.tick2microsecond();
            }
        }
    }

    @Override
    public void run() {
        //This thread is running for the whole lifecycle of the application
        //only stops when the application is closed
        try {
            //TODO: we want to create our own receiver
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }


        while( ! this.exit) {
            //just loop and wait while we are not playing or recording
            while(this.stopped){
                TimeUtil.sleep(10);
            }

            long loopStartTime = System.currentTimeMillis() - songPositionMs;
            while( ! stopped) {
                playEvents();
                //Todo: we need a notification when BPM is modified at play/record time
                songPositionMs = System.currentTimeMillis() - loopStartTime;
                midi.TimeUtil.sleep(1);
            }

        }
    }

    public void exit() {
//        if(this.synthReceiver != null) {
//            this.synthReceiver.close();
//        }
        if(this.synth != null) {
            this.synth.close();
        }
        this.exit = true;
    }

    public void play() {
        this.mode = MODE.PLAYBACK;
        this.stopped = false;
        //this.notifyAll();
    }
    public void record() {
        this.mode = MODE.RECORD;
        this.stopped = false;
        //this.notifyAll();
    }
    public void pause() {
        this.stopped = true;
        //this.notifyAll();
    }
    public void stop() {
        this.stopped = true;
        /*
        this.songPositionMs = 0;
        for(int i=0; i<this.trackEventsCurrentIndexes.length; i++){
            this.trackEventsCurrentIndexes[i] = 0;
        }
        */

        //this.notifyAll();
    }

    private void playEvents() {
        try {


            //TODO: Pack all this info into one object. Also create a method that does these calcualtions
            long lengthMs = this.sequencerContext.sequence.getMicrosecondLength();
            float divisionType = this.sequencerContext.sequence.getDivisionType();
            int resolution = this.sequencerContext.sequence.getResolution();
            long tickLength = this.sequencerContext.sequence.getTickLength();
            double beatsPerSecond = (double)(beatsPerMinute) / 60.00;
            double ticksPerSecond = beatsPerSecond * resolution; //resolution is how many ticks we have in a single beat
            double singleTickTime = 1.00 / ticksPerSecond;
            double playTimeInTicks = (songPositionMs/1000.00) / singleTickTime;

            this.iPlayTimeInTicks = Math.round(playTimeInTicks);


            //System.out.printf("Time(ticks): %s%n", iPlayTimeInTicks);
            //System.out.printf("Time(Ticks) %s%n", iPlayTimeInTicks);
            //TODO: Now check which events should be played
            //we use the tick for timing. When event_tick == iPlayTimeInTicks => play the event
            Map<Integer, TrackInfo> trackInfoMap = this.sequencerContext.trackInfoMap;

            for (Integer trackId : trackInfoMap.keySet()){
                TrackInfo trackInfo = trackInfoMap.get(trackId);
                Track track = trackInfo.getTrack();

                while( ! this.stopped ){
                    int currentEventIndex = trackInfo.currentEventIndex;
                    if(currentEventIndex < track.size() ){
                        MidiEvent event = track.get(currentEventIndex);
                        long tick = event.getTick();
                        if(tick <= iPlayTimeInTicks){
                            //play the event
                            //System.out.printf("Time(ticks): %s, Track: %s, Message: %s%n", iPlayTimeInTicks, i, event.getMessage().getMessage());
                            List<MidiPortInfo> outputs = trackInfo.getOutputPorts();
                            for(MidiPortInfo port : outputs) {
                                Integer portId = port.portId;
                                RtMidiTransmitter tx = this.sequencerContext.transmitterMap.get(portId);
                                if(tx != null) {
                                    tx.send(event.getMessage(), 0);
                                }
                            }
                            trackInfo.currentEventIndex++;
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        break;
                    }
                }
            }



            TimeUtil.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //play the sequence then set the status to stopped
        //sequence.getTickLength();
        //sequence.getTracks();
        //sequence.getResolution();
        //if no more ticks then change the status to stopped
    }

    public void setBpm(float tempoInBPM) {
        this.beatsPerMinute = tempoInBPM;
    }

    public void setSongPositionTicks(long ticks) {
        int resolution = this.sequencerContext.sequence.getResolution();
        double beatsPerSecond = (double) (beatsPerMinute) / 60.00;
        double ticksPerSecond = beatsPerSecond * resolution; //resolution is how many ticks we have in a single beat
        double singleTickTime = 1.00 / ticksPerSecond;
        long songPositionMs = (long) (ticks * singleTickTime * 1000);
        this.iPlayTimeInTicks = ticks;
        setSongPositionMs(songPositionMs);
    }

    public void setSongPositionMs(long songPositionMs) {
        this.songPositionMs = songPositionMs;
        //TODO: Pack all this info into one object. Also create a method that does these calcualtions
        if(this.sequencerContext.sequence != null) {
            long lengthMs = this.sequencerContext.sequence.getMicrosecondLength();
            float divisionType = this.sequencerContext.sequence.getDivisionType();
            int resolution = this.sequencerContext.sequence.getResolution();
            long tickLength = this.sequencerContext.sequence.getTickLength();
            double beatsPerSecond = (double) (beatsPerMinute) / 60.00;
            double ticksPerSecond = beatsPerSecond * resolution; //resolution is how many ticks we have in a single beat
            double singleTickTime = 1.00 / ticksPerSecond;
            double playTimeInTicks = (songPositionMs / 1000.00) / singleTickTime;
            long iPlayTimeInTicks = Math.round(playTimeInTicks);

            Map<Integer, TrackInfo> trackInfoMap = this.sequencerContext.trackInfoMap;

            for (Integer trackId : trackInfoMap.keySet()){
                TrackInfo trackInfo = trackInfoMap.get(trackId);
                Track track = trackInfo.getTrack();
                for (int j = 1; j < track.size(); j++) {
                    MidiEvent event = track.get(j);
                    long tick = event.getTick();
                    if (tick <= iPlayTimeInTicks) {
                        trackInfo.currentEventIndex = j;
                    } else {
                        break;
                    }
                }
            }
        }
    }

}
