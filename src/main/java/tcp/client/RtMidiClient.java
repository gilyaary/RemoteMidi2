package tcp.client;

import javax.sound.midi.Receiver;
import java.io.IOException;

public class RtMidiClient {

    //these will be multiple receivers
    RtMidiReceiver receiver = new RtMidiReceiver();

    public Receiver getReceiver(Integer receiverId) {
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
