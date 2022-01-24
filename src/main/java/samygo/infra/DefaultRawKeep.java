package samygo.infra;

import org.springframework.stereotype.Component;

@Component
public final class DefaultRawKeep implements RawKeep {

    private byte[] raw = new byte[0];

    @Override
    public byte[] get() {
        return raw;
    }

    @Override
    public void set(byte[] raw) {
        this.raw = raw;
    }
}
