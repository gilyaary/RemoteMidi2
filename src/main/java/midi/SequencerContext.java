package midi;

import org.codehaus.groovy.util.FastArray;
import org.springframework.stereotype.Component;
import tcp.client.RtMidiTransmitter;

import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SequencerContext {
    public Map<Integer, RtMidiTransmitter> transmitterMap = new HashMap<>();
    public Map<Integer, LgSequencerMidiEventReceiver> receiverMap = new HashMap<>();
    public Sequence sequence;
    public Map<Integer, TrackInfo> trackInfoMap = new HashMap<>();
    public AtomicInteger trackInfoIdGen = new AtomicInteger(1);
}
