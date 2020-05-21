package tcp.client;

import org.springframework.stereotype.Component;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import java.io.*;
import java.net.Socket;

@Component
public class RtMidiConnection {

    private Socket soc;
    private InputStream in;
    private OutputStream out;
    private InputStreamReader reader;

    public void init() throws IOException {
        this.soc = new Socket("localhost", 8888);
        this.in = soc.getInputStream();
        this.out = soc.getOutputStream();
        this.reader = new InputStreamReader(in);
    }

    public void sendMidiMessage(MidiMessage midiMessage, long l) {
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
                out.write(cmdString.getBytes());
                out.flush();
                System.out.println(cmdString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String sendCommand(String commandRouting) {

        StringBuffer cmd = new StringBuffer(commandRouting);
        cmd.append("@QQQ");
        String cmdString = cmd.toString();
        try {
            out.write(cmdString.getBytes());
            out.flush();
            //System.out.println(cmdString);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer lines = new StringBuffer();
            while(true) {
                String response = br.readLine();
                if(response == null || response.equals("@@@")){
                    return lines.toString();
                }
                else{
                    lines.append(response);
                    lines.append("\n");int i=9;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


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
