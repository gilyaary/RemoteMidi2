package tcp.client;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;

public class RtMidiReceiver implements Receiver {

    private Socket soc;
    private InputStream in;
    private OutputStream out;

    public void init() throws IOException {
        this.soc = new Socket("localhost", 8888);
        //this.soc.setSoTimeout(1000);
        //this.soc.setTcpNoDelay(false);
        //this.soc.setKeepAlive(false);
        //this.soc.setSoLinger(false, -1);
        //this.soc.setOOBInline(false);
        //this.soc.setPerformancePreferences();
        //this.soc.shutdownInput();
        //this.soc.setSoLinger();
        //this.soc.setPerformancePreferences();

        this.in = soc.getInputStream();
        this.out = soc.getOutputStream();
    }

    @Override
    public void send(MidiMessage midiMessage, long l) {


        try {
            sendMessage(midiMessage, "2#A.1");
            sendMessage(midiMessage, "2#A.2");
            sendMessage(midiMessage, "2#A.3");
            //sendMessage(midiMessage, "2#A.4");
            //sendMessage(midiMessage, "2#A.2");
            //sendMessage(midiMessage, "2#A.1");
            //sendMessage(midiMessage, "2#A.2");
            //sendMessage(midiMessage, "2#A.3");
            //sendMessage(midiMessage, "2#A.4");
            //sendMessage(midiMessage, "2#A.5");
            //sendMessage(midiMessage, "2#A.6");
            //sendMessage(midiMessage, "2#A.7");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    boolean lastWasOn = false;
    int lastNote = 0;

    private void sendMessage(MidiMessage midiMessage, String commandRouting) {

//        if(midiMessage instanceof ShortMessage){
//
//                try {
//                    if(lastWasOn) {
//                        midiMessage = new ShortMessage(ShortMessage.NOTE_OFF, 1, lastNote, 64);
//                        lastWasOn = false;
//                    }else{
//                        Random random = new Random();
//                        lastNote = 64;//random.nextInt()%36 + 20;
//                        lastWasOn = true;
//                        midiMessage = new ShortMessage(ShortMessage.NOTE_ON, 1, lastNote, 64);
//                    }
//                } catch (InvalidMidiDataException e) {
//                    e.printStackTrace();
//                    return;
//                }
//
//        }
//        else{
//            return;
//        }

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
