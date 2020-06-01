package midi;

import org.springframework.stereotype.Component;

import javax.sound.midi.Sequence;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SequencerContext {
    Sequence sequence;
    Map<Integer, TrackInfo> trackInfoMap = new HashMap<>();
    AtomicInteger trackInfoIdGen = new AtomicInteger(1);
}
