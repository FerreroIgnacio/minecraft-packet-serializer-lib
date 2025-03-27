import SerializationInfo.Refs.Components.FunctionComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.math.BigInteger;
import java.util.BitSet;

public enum Natives {

    VARINT("varint",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("writeVarint", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readVarint", Consts.BUFNAME.toString()))
            )
    ),
    VARLONG("varlong",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("writeVarlong", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readVarlong", Consts.BUFNAME.toString()))
            )
    ),
    OPTVARINT("optvarint",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("writeVarint", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readOptvarint", Consts.BUFNAME.toString()))
            )
    ),
    BOOL("bool",
            new SerializationInfo(
                    Boolean.class,
                    new SerializerRef(new FunctionComponent("writeBoolean", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readBoolean", Consts.BUFNAME.toString()))
            )
    ),
    U8("u8",
            new SerializationInfo(
                    Short.class,
                    new SerializerRef(new FunctionComponent("writeU8", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU8", Consts.BUFNAME.toString()))
            )
    ),
    U16("u16",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("writeU16", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU16", Consts.BUFNAME.toString()))
            )
    ),
    U32("u32",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("writeU32", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU32", Consts.BUFNAME.toString()))
            )
    ),
    U64("u64",
            new SerializationInfo(
                    BigInteger.class,
                    new SerializerRef(new FunctionComponent("writeU64", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readU64", Consts.BUFNAME.toString()))
            )
    ),
    I8("i8",
            new SerializationInfo(
                    Byte.class,
                    new SerializerRef(new FunctionComponent("writeI8", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI8", Consts.BUFNAME.toString()))
            )
    ),
    I16("i16",
            new SerializationInfo(
                    Short.class,
                    new SerializerRef(new FunctionComponent("writeI16", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI16", Consts.BUFNAME.toString()))
            )
    ),
    I32("i32",
            new SerializationInfo(
                    Integer.class,
                    new SerializerRef(new FunctionComponent("writeI32", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI32", Consts.BUFNAME.toString()))
            )
    ),
    I64("i64",
            new SerializationInfo(
                    Long.class,
                    new SerializerRef(new FunctionComponent("writeI64", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readI64", Consts.BUFNAME.toString()))
            )
    ),
    F32("f32",
            new SerializationInfo(
                    Float.class,
                    new SerializerRef(new FunctionComponent("writeFloat", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readFloat", Consts.BUFNAME.toString()))
            )
    ),
    F64("f64",
            new SerializationInfo(
                    Double.class,
                    new SerializerRef(new FunctionComponent("writeDouble", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readDouble", Consts.BUFNAME.toString()))
            )
    ),
    UUID("UUID",
            new SerializationInfo(
                    BigInteger.class,
                    new SerializerRef(new FunctionComponent("writeUUID", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readUUID", Consts.BUFNAME.toString()))
            )
    ),
    ANONYMOUSNBT("anonymousNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("writeAnonymousNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    ANONOPTIONALNBT("anonOptionalNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("writeAnonOptionalNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    OPTIONALNBT("optionalNbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("writeOptionalNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    BIT("bit",
            new SerializationInfo(
                    Boolean.class,
                    new SerializerRef(new FunctionComponent("writeBit", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readBit", Consts.BUFNAME.toString()))
            )
    ),
    NBT("nbt",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("writeNbt", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readNbt", Consts.BUFNAME.toString()))
            )
    ),
    STRING("string",
            new SerializationInfo(
                    String.class,
                    new SerializerRef(new FunctionComponent("writeString", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readString", Consts.BUFNAME.toString()))
            )
    ),
    VOID("void",
            new SerializationInfo(
                    Void.class,
                    new SerializerRef(new FunctionComponent("writeVoid", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readVoid", Consts.BUFNAME.toString()))
            )
    ),
    RESTBUFFER("restBuffer",
            new SerializationInfo(
                    BitSet.class,
                    new SerializerRef(new FunctionComponent("writeRestBuffer", Consts.BUFNAME.toString())),
                    new DeserializerRef(new FunctionComponent("readRestBuffer", Consts.BUFNAME.toString()))
            )
    );


    private final SerializationInfo serializationInfo;
    private final String nameInJson;


    Natives(String nameInJson, SerializationInfo serializationInfo){
        this.nameInJson = nameInJson;
        this.serializationInfo = serializationInfo;
    }
}
