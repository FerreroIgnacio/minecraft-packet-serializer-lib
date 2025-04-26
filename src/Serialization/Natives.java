package Serialization;

import Generic.Consts;
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
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeVarInt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readVarInt", Consts.BUFNAME.toString()))
            )
    ),
    VARLONG("varlong",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeVarLong", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readVarLong", Consts.BUFNAME.toString()))
            )
    ),
    OPTVARINT("optvarint",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeVarint", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readOptvarint", Consts.BUFNAME.toString()))
            )
    ),
    BOOL("bool",
            new SerializationInfo(
                    Boolean.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeBool", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readBool", Consts.BUFNAME.toString()))
            )
    ),
    U8("u8",
            new SerializationInfo(
                    Short.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeU8", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readU8", Consts.BUFNAME.toString()))
            )
    ),
    U16("u16",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeU16", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readU16", Consts.BUFNAME.toString()))
            )
    ),
    U32("u32",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeU32", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readU32", Consts.BUFNAME.toString()))
            )
    ),
    U64("u64",
            new SerializationInfo(
                    BigInteger.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeU64", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readU64", Consts.BUFNAME.toString()))
            )
    ),
    I8("i8",
            new SerializationInfo(
                    Byte.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeI8", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readI8", Consts.BUFNAME.toString()))
            )
    ),
    I16("i16",
            new SerializationInfo(
                    Short.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeI16", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readI16", Consts.BUFNAME.toString()))
            )
    ),
    I32("i32",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeI32", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readI32", Consts.BUFNAME.toString()))
            )
    ),
    I64("i64",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeI64", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readI64", Consts.BUFNAME.toString()))
            )
    ),
    F32("f32",
            new SerializationInfo(
                    Float.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeFloat", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readFloat", Consts.BUFNAME.toString()))
            )
    ),
    F64("f64",
            new SerializationInfo(
                    Double.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeDouble", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readDouble", Consts.BUFNAME.toString()))
            )
    ),
    UUID("UUID",
            new SerializationInfo(
                    BigInteger.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeUUID", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readUUID", Consts.BUFNAME.toString()))
            )
    ),
    ANONYMOUSNBT("anonymousNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeAnonymousNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readAnonymousNbt", Consts.BUFNAME.toString()))
            )
    ),
    ANONOPTIONALNBT("anonOptionalNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeAnonOptionalNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readAnonOptionalNbt", Consts.BUFNAME.toString()))
            )
    ),
    OPTIONALNBT("optionalNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeOptionalNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readOptionalNbt", Consts.BUFNAME.toString()))
            )
    ),
    BIT("bit",
            new SerializationInfo(
                    Boolean.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeBit", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readBit", Consts.BUFNAME.toString()))
            )
    ),
    NBT("nbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readNbt", Consts.BUFNAME.toString()))
            )
    ),
    STRING("string",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeString", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readString", Consts.BUFNAME.toString()))
            )
    ),
    VOID("void",
            new SerializationInfo(
                    Void.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeVoid", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readVoid", Consts.BUFNAME.toString()))
            )
    ),
    RESTBUFFER("restBuffer",
            new SerializationInfo(
                    BitSet.class,
                    new SerializerRef(new FunctionComponent("SerializerFunctions.writeRestBuffer", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("DeserializerFunctions.readRestBuffer", Consts.BUFNAME.toString()))
            )
    );

    private final SerializationInfo serializationInfo;
    private final String nameInJson;


    Natives(String nameInJson, SerializationInfo serializationInfo){
        this.nameInJson = nameInJson;
        this.serializationInfo = serializationInfo;
    }

    public SerializationInfo getSerializationInfo() {
        return serializationInfo;
    }

    public String getNameInJson() {
        return nameInJson;
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
