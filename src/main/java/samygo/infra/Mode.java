package samygo.infra;

public enum Mode {

    CABLE("cableChannels", (byte) 0x01, "map-CableD"),
    AIR("airChannels", (byte) 0x02, "map-AirD"),
    SAT("satChannels", (byte) 0x04, "map-SateD"),
    CLONE("cloneChannels", (byte) 0x08, "CLONE.BIN");

    private final String componentId;
    private final byte mask;
    private final String description;

    Mode(String componentId, byte mask, String description) {
        this.componentId = componentId;
        this.mask = mask;
        this.description = description;
    }

    public String componentId() {
        return componentId;
    }

    public byte mask() {
        return mask;
    }

    public String description() {
        return description;
    }
}
