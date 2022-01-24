package samygo.infra;

public enum Mode {

    AIR("airChannels", (byte) 0x02, "Mode: map-AirD"),
    CABLE("cableChannels", (byte) 0x01, "Mode: map-CableD"),
    SAT("satChannels", (byte) 0x04, "Mode: map-SateD"),
    CLONE("cloneChannels", (byte) 0x08, "Mode: CLONE.BIN");

    private final String componentId;
    private final byte mask;
    private final String labelText;

    Mode(String componentId, byte mask, String labelText) {
        this.componentId = componentId;
        this.mask = mask;
        this.labelText = labelText;
    }

    public String componentId() {
        return componentId;
    }

    public byte mask() {
        return mask;
    }

    public String labelText() {
        return labelText;
    }
}
