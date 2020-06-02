package midi;

public class MidiPortInfo {
    public static enum PortType {INPUT, OUTPUT}
    PortType portType;
    Integer portId;
    String portName;

    public MidiPortInfo(PortType portType, Integer portId, String portName) {
        this.portType = portType;
        this.portId = portId;
        this.portName = portName;
    }

    //we may add port configuration and such later.
    //this may be filters, settings, etc etc.
}
