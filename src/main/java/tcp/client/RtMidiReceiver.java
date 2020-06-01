package tcp.client;

import org.springframework.stereotype.Component;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Component
public class RtMidiReceiver implements Receiver {

    private Socket soc;
    private InputStream in;
    private OutputStream out;

    String commandRouting;
    enum MidiInterface{ALSA, JACK};

    public RtMidiReceiver(MidiInterface midiInterface, int portNumber){
        //"2#A.1"
        String commandPrefix = "2#";
        String interfaceSymbol = midiInterface == MidiInterface.ALSA ? "A" : midiInterface == MidiInterface.JACK ? "J" : "A";
        commandRouting = String.format("%s%s%s", commandPrefix, interfaceSymbol, portNumber);
    }

    public void init() throws IOException {
        this.soc = new Socket("localhost", 8888);
        this.in = soc.getInputStream();
        this.out = soc.getOutputStream();
    }

    @Override
    public void send(MidiMessage midiMessage, long l) {
        try {
            sendMessage(midiMessage, commandRouting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean lastWasOn = false;
    int lastNote = 0;

    public void sendMessage(MidiMessage midiMessage, String commandRouting) {

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
                out.write(cmdString.getBytes());
                out.flush();
                System.out.println(cmdString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        try {
            this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
