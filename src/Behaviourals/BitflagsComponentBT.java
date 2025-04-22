package Behaviourals;

import Serialization.PacketField;

import java.util.List;

public class BitflagsComponentBT extends AbstractBehavioural{
    private final int index;
    public BitflagsComponentBT(int index) {
        this.index = index;
    }

    @Override
    public List<PacketField> asPacketFields() {
        throw new UnsupportedOperationException("Calling asPacketFields() on BitflagsComponentBT");
    }

    @Override
    protected String getName() {
        if(getFather() == null)
            return "BitflagsComponentBT with no father";
        return getFather().getName();
    }
}
