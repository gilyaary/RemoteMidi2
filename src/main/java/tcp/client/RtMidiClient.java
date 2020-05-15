package tcp.client;

import javax.sound.midi.Receiver;
import java.io.IOException;

public class RtMidiClient {
    RtMidiReceiver receiver = new RtMidiReceiver();
    public Receiver getReceiver() {
        try {
            receiver.init();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return this.receiver;
    }
}
