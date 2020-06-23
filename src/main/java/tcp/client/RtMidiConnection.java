package tcp.client;

import midi.LgSequencer;
import midi.SequencerContext;
import midi.TrackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class RtMidiConnection {

    @Autowired
    LgSequencer sequencer;

    private Socket soc;
    private InputStream in;
    private OutputStream out;
    private InputStreamReader reader;
    ArrayBlockingQueue<String> commandResponseInputQueue = new ArrayBlockingQueue<>(100);
    ArrayBlockingQueue<String> midiEventsInputQueue = new ArrayBlockingQueue<>(100);
    InputRunnable inputRunnable = new InputRunnable();
    Thread t = new Thread(inputRunnable);

    @Autowired
    SequencerContext sequencerContext;


    public void init(){
        t.start();
        try{
            openConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void openConnection() throws IOException {
        this.soc = new Socket("localhost", 8888);
        this.in = soc.getInputStream();
        this.out = soc.getOutputStream();
        this.reader = new InputStreamReader(in);

    }

    public void closeConnection() {
        try {
            if(in != null)
                this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(out != null)
                this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(soc != null)
                this.soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void reconnect(){
        try {
            closeConnection();
            Thread.sleep(1000);
            openConnection();
        }catch(Exception ex){

        }
    }

    public void destroy() {
        this.inputRunnable.stopped = true;
    }


    private class InputRunnable implements Runnable {
        boolean stopped = false;

        public void start() {
            this.stopped = false;
        }

        @Override
        public void run() {
            while (!stopped) {
                try {
                    if(soc == null || soc.isClosed()){
                        reconnect();
                    }
                    //System.out.println(cmdString);
                    BufferedReader br = new BufferedReader(reader);
                    StringBuffer lines = new StringBuffer();

                    while (!stopped) {
                        String response = br.readLine();
                        //we are expecting command responses to follow the format:
                        //{line}\n{line}\n...{line}\n   @@@\n
                        if (response != null && response.trim().equals("@@@")) {
                            String responseLines = lines.toString();
                            //check if this was a midi event or command response
                            if (responseLines.startsWith("1#")) {
                                System.out.println("Ping Event: " + responseLines);
                            } else if (responseLines.startsWith("2#")) {


                                //2#VIRTUAL_IN_PORT_Track 1.144.63.127.
                                int i1 = responseLines.indexOf("#");
                                int i2 = responseLines.indexOf(".");
                                System.out.println("Midi In Event: " + responseLines);
                                responseLines = responseLines.trim();
                                if(i1 >= 0 && i2 >= 0){
                                    final String trackName = responseLines.substring(i1+1, i2);
                                    final String midiCommandString = responseLines.substring(i2+1);
                                    final String[] midiCommandSegments = midiCommandString.split("\\.");
                                    System.out.println("Track Name: " + trackName);
                                    System.out.println("Midi Command Segments: " + Arrays.toString(midiCommandSegments));
                                    //TODO: find the track
                                    Map<Integer, TrackInfo> trackInfoMap = sequencerContext.trackInfoMap;
                                    for(Integer trackId : trackInfoMap.keySet()){
                                        final TrackInfo trackInfo = trackInfoMap.get(trackId);
                                        final String currentTrackName = trackInfo.getName();
                                        if(trackName.indexOf(currentTrackName) != -1){
                                            int [] values = new int[midiCommandSegments.length];
                                            for(int i=0; i<midiCommandSegments.length;i++){
                                                values[i] = Integer.parseInt(midiCommandSegments[i]);
                                            }
                                            MidiMessage midiMessage = new ShortMessage(values[0],values[1],values[2]);
                                            MidiEvent event = new MidiEvent(midiMessage, sequencer.getTickPosition());
                                            trackInfo.getTrack().add(event);
                                            //TODO: the sent event may be either realtime or at a certain time index. We should know which case it is.
                                            //for real time the player must be in play mode
                                        }
                                    }
                                }
                                //midiEventsInputQueue.add(responseLines);




                            } else {
                                commandResponseInputQueue.add(responseLines);
                                System.out.println("Command response: " + responseLines);
                            }
                            lines.delete(0, lines.length());
                        } else {
                            if(response!=null) {
                                lines.append(response);
                                lines.append("\n");
                            }
                            else{
                                closeConnection();
                                break;
                            }
                        }
                    }

                } catch (Exception ex) {
                    //System.out.println("Exiting read loop");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public synchronized void sendMidiMessage(MidiMessage midiMessage, long l) {
        try {
            sendMidiMessage(midiMessage, "2#A.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean lastWasOn = false;
    int lastNote = 0;



    public void sendMidiMessage(MidiMessage midiMessage, String commandRouting) {

        byte[] bytes = midiMessage.getMessage();
        String str = new String(bytes);
        StringBuffer cmd = new StringBuffer(commandRouting);
        if(true || bytes.length == 3) {
            for (byte b : bytes) {
                int value = ((int)b)<0 ? 256+((int)b) : ((int)b);
                cmd.append('.');
                cmd.append(value);
            }
            cmd.append("@");


            String cmdString = cmd.toString();
            try {
                if ( out !=null ) {
                    out.write(cmdString.getBytes());
                    out.flush();
                }
                else{
                    //reconnect();
                    closeConnection();
                }
                System.out.println(cmdString);
            } catch (Exception e) {
                //reconnect();
                closeConnection();
            }
        }
    }

    public String sendCommand(String commandRouting) {

        StringBuffer cmd = new StringBuffer(commandRouting);
        cmd.append("@");
        String cmdString = cmd.toString();
        try {
            if(out !=null) {
                out.write(cmdString.getBytes());
                out.flush();
                String read = commandResponseInputQueue.take();
                return read;
            }
        } catch (Exception e) {
            reconnect();
        }
        return "";
    }



}
