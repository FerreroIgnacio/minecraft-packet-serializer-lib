import SerializationInfo.Refs.Components.FunctionComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public enum Natives {

    VARINT("varint",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeVarint", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readVarint", Consts.BUFNAME.toString()))
            )
    ),
    VARLONG("varlong",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeVarlong", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readVarlong", Consts.BUFNAME.toString()))
            )
    ),
    OPTVARINT("optvarint",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeVarint", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readOptvarint", Consts.BUFNAME.toString()))
            )
    ),
    BOOL("bool",
            new SerializationInfo(
                    Boolean.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeBoolean", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readBoolean", Consts.BUFNAME.toString()))
            )
    ),
    U8("u8",
            new SerializationInfo(
                    Short.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeU8", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU8", Consts.BUFNAME.toString()))
            )
    ),
    U16("u16",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeU16", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU16", Consts.BUFNAME.toString()))
            )
    ),
    U32("u32",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeU32", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU32", Consts.BUFNAME.toString()))
            )
    ),
    U64("u64",
            new SerializationInfo(
                    BigInteger.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeU64", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU64", Consts.BUFNAME.toString()))
            )
    ),
    I8("i8",
            new SerializationInfo(
                    Byte.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeI8", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI8", Consts.BUFNAME.toString()))
            )
    ),
    I16("i16",
            new SerializationInfo(
                    Short.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeI16", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI16", Consts.BUFNAME.toString()))
            )
    ),
    I32("i32",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeI32", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI32", Consts.BUFNAME.toString()))
            )
    ),
    I64("i64",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeI64", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI64", Consts.BUFNAME.toString()))
            )
    ),
    F32("f32",
            new SerializationInfo(
                    Float.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeFloat", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readFloat", Consts.BUFNAME.toString()))
            )
    ),
    F64("f64",
            new SerializationInfo(
                    Double.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeDouble", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readDouble", Consts.BUFNAME.toString()))
            )
    ),
    UUID("UUID",
            new SerializationInfo(
                    BigInteger.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeUUID", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readUUID", Consts.BUFNAME.toString()))
            )
    ),
    ANONYMOUSNBT("anonymousNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeAnonymousNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    ANONOPTIONALNBT("anonOptionalNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeAnonOptionalNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    OPTIONALNBT("optionalNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeOptionalNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    BIT("bit",
            new SerializationInfo(
                    Boolean.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeBit", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readBit", Consts.BUFNAME.toString()))
            )
    ),
    NBT("nbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readNbt", Consts.BUFNAME.toString()))
            )
    ),
    STRING("string",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeString", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    VOID("void",
            new SerializationInfo(
                    Void.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeVoid", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readVoid", Consts.BUFNAME.toString()))
            )
    ),
    RESTBUFFER("restBuffer",
            new SerializationInfo(
                    BitSet.class,
                    new SerializerRef(new FunctionComponent("DeserializerFunctions.writeRestBuffer", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readRestBuffer", Consts.BUFNAME.toString()))
            )
    );


    private final SerializationInfo serializationInfo;
    private final String nameInJson;


    Natives(String nameInJson, SerializationInfo serializationInfo){
        this.nameInJson = nameInJson;
        this.serializationInfo = serializationInfo;
    }

    private enum DeserializerFunctions {
        ;
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
        public static Boolean readBool(ByteBuffer buffer) {
            return buffer.get() != 0;
        }
        public static String readString(ByteBuffer buffer) {
            // Read the length prefix as a VarInt
            Integer length = readVarInt(buffer);

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
    }



}
