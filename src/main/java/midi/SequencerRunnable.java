package midi;

import tcp.client.RtMidiClient;

import javax.sound.midi.*;

public class SequencerRunnable implements Runnable{

    private Sequence sequence;
    long songPositionMs = 0;
    private boolean exit;
    private boolean stopped = true;
    private Track[] tracks;
    private float beatsPerMinute;
    private int[] trackEventsCurrentIndexes;
    private Synthesizer synth;
    private Receiver synthReceiver;
    RtMidiClient rtMidiClient = new RtMidiClient();


    enum MODE {PLAYBACK, RECORD}
    MODE mode;
    //long positionFromStart = 0;

    public SequencerRunnable(){}

    public void setSequence(Sequence sequence){
        this.sequence = sequence;
        this.tracks = this.sequence.getTracks();

        for (Track track : this.tracks){
            int eventCount = track.size();
            for(int i=0; i<eventCount; i++){
                MidiEvent ev = track.get(i);
                long tick = ev.getTick();
                //System.out.println(tick);
                //MidiUtils.tick2microsecond();
            }
        }

        this.trackEventsCurrentIndexes = new int[tracks.length];
    }

    @Override
    public void run() {
        //This thread is running for the whole lifecycle of the application
        //only stops when the application is closed
        try {
            //TODO: we want to create our own receiver
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();


            this.synthReceiver = rtMidiClient.getReceiver();
            //this.synthReceiver = synth.getReceiver();

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
        if(this.synthReceiver != null) {
            this.synthReceiver.close();
        }
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
            long lengthMs = this.sequence.getMicrosecondLength();
            float divisionType = this.sequence.getDivisionType();
            int resolution = this.sequence.getResolution();
            long tickLength = this.sequence.getTickLength();
            this.tracks = this.sequence.getTracks();
            double beatsPerSecond = (double)(beatsPerMinute) / 60.00;
            double ticksPerSecond = beatsPerSecond * resolution; //resolution is how many ticks we have in a single beat
            double singleTickTime = 1.00 / ticksPerSecond;
            double playTimeInTicks = (songPositionMs/1000.00) / singleTickTime;
            long iPlayTimeInTicks = Math.round(playTimeInTicks);


            //System.out.printf("Time(ticks): %s%n", iPlayTimeInTicks);
            //System.out.printf("Time(Ticks) %s%n", iPlayTimeInTicks);
            //TODO: Now check which events should be played
            //we use the tick for timing. When event_tick == iPlayTimeInTicks => play the event
            for(int i=0; i<this.trackEventsCurrentIndexes.length; i++){
                Track track = this.tracks[i];
                while( ! this.stopped ){
                    int currentEventIndex = trackEventsCurrentIndexes[i];
                    if(currentEventIndex < track.size() && i < 300){
                        MidiEvent event = track.get(currentEventIndex);
                        long tick = event.getTick();
                        if(tick <= iPlayTimeInTicks){
                            //play the event
                            //System.out.printf("Time(ticks): %s, Track: %s, Message: %s%n", iPlayTimeInTicks, i, event.getMessage().getMessage());
                            synthReceiver.send(event.getMessage(), 0);
                            trackEventsCurrentIndexes[i]++;
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

    public void setSongPositionMs(long songPositionMs) {
        this.songPositionMs = songPositionMs;
        //TODO: Pack all this info into one object. Also create a method that does these calcualtions
        if(this.sequence != null) {
            long lengthMs = this.sequence.getMicrosecondLength();
            float divisionType = this.sequence.getDivisionType();
            int resolution = this.sequence.getResolution();
            long tickLength = this.sequence.getTickLength();
            this.tracks = this.sequence.getTracks();
            double beatsPerSecond = (double) (beatsPerMinute) / 60.00;
            double ticksPerSecond = beatsPerSecond * resolution; //resolution is how many ticks we have in a single beat
            double singleTickTime = 1.00 / ticksPerSecond;
            double playTimeInTicks = (songPositionMs / 1000.00) / singleTickTime;
            long iPlayTimeInTicks = Math.round(playTimeInTicks);


            for (int i = 0; i < this.trackEventsCurrentIndexes.length; i++) {
                this.trackEventsCurrentIndexes[i] = 0;
                Track track = this.tracks[i];
                for (int j = 1; j < track.size(); j++) {
                    MidiEvent event = track.get(j);
                    long tick = event.getTick();
                    if (tick <= iPlayTimeInTicks) {
                        this.trackEventsCurrentIndexes[i] = j;
                    } else {
                        break;
                    }
                }
            }
        }
    }

}
