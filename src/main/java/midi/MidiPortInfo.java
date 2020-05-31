package midi;

public class MidiPortInfo {
    enum PortType {INPUT, OUTPUT}
    PortType portType;
    Integer portId;
    String portName;
    //we may add port configuration and such later.
    //this may be filters, settings, etc etc.
}
