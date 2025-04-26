import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class DeserializerFunctions {
    public static Long readVarLong(ByteBuffer buffer) {
        long result = 0;
        int shift = 0;
        byte currentByte;
        do {
            currentByte = buffer.get();
            result |= (long) (currentByte & 0x7F) << shift;
            shift += 7;
            if (shift > 70) {
                throw new IllegalArgumentException("VarLong is too big.");
            }
        } while ((currentByte & 0x80) != 0);
        return result;
    }
    public static Integer readVarInt(ByteBuffer buffer) {
        int result = 0;
        int shift = 0;
        byte currentByte;
        do {
            currentByte = buffer.get();
            result |= (currentByte & 0x7F) << shift;
            shift += 7;
            if (shift > 35) {
                throw new IllegalArgumentException("VarInt is too big.");
            }
        } while ((currentByte & 0x80) != 0);
        return result;
    }
    public static BigInteger readUUID(ByteBuffer buffer) {
        byte[] bytes = new byte[16];
        buffer.get(bytes);
        return new BigInteger(1, bytes);
    }
    public static BigInteger readU64(ByteBuffer buffer) {
        long longValue = buffer.getLong();
        return (longValue >= 0 ? BigInteger.valueOf(longValue) : BigInteger.valueOf(longValue).add(BigInteger.ONE.shiftLeft(64)));
    }
    public static Long readU32(ByteBuffer buffer) {
        return (buffer.getInt() & 0xFFFFFFFFL);
    }
    public static Integer readU16(ByteBuffer buffer) {
        return (buffer.getShort() & 0xFFFF);
    }
    public static Short readU8(ByteBuffer buffer) {
        return (short)(buffer.get() & 0xFF);
    }
    public static boolean readBool(ByteBuffer buffer) {
        return buffer.get() != 0;
    }
    public static String readString(ByteBuffer buffer) {
        // Read the length prefix as a VarInt
        int length = readVarInt(buffer);

        // Read the string bytes
        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes);

        // Convert the bytes to a string
        String value = new String(stringBytes, StandardCharsets.UTF_8);
        return value;
    }
    public static Long readI64(ByteBuffer buffer) {
        return (buffer.getLong());
    }
    public static Integer readI32(ByteBuffer buffer) {
        return (buffer.getInt());
    }
    public static Short readI16(ByteBuffer buffer) {
        return (buffer.getShort());
    }
    public static Byte readI8(ByteBuffer buffer) {
        return (buffer.get());
    }
    public static Double readF64(ByteBuffer buffer) {
        return (buffer.getDouble());
    }
    public static Float readF32(ByteBuffer buffer) {
        return (buffer.getFloat());
    }

    public static BitSet readBuffer(ByteBuffer buffer){
        throw new RuntimeException("Not implemented yet");
    }
}
