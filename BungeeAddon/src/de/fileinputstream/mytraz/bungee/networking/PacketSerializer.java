package de.fileinputstream.mytraz.bungee.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.UUID;

public class PacketSerializer extends ByteBuf {

    private final ByteBuf buffer;

    private Charset charset = Charset.forName("UTF-8");

    public PacketSerializer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    /**
     * Calculates the number of bytes required to fit the supplied int (0-5) if
     * it were to be read/written using readVarInt or writeVarInt
     */

    public static int getVarIntSize(int value) {
        if ((value & -128) == 0) {
            return 1;
        } else if ((value & -16384) == 0) {
            return 2;
        } else if ((value & -2097152) == 0) {
            return 3;
        } else if ((value & -268435456) == 0) {
            return 4;
        }
        return 5;
    }

    public int readVarInt() {
        int number = 0;
        int round = 0;
        byte currentByte;

        do {
            currentByte = this.readByte();
            number |= (currentByte & 127) << round++ * 7;

            if (round > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((currentByte & 128) == 128);

        return number;
    }

    public void writeVarInt(int number) {
        while ((number & -128) != 0) {
            this.writeByte(number & 127 | 128);
            number >>>= 7;
        }

        this.writeByte(number);
    }

    public String readString() {
        final byte[] bytes = new byte[readUnsignedShort()];
        readBytes(bytes);
        return new String(bytes, charset);
    }

    public void writeString(String value) {
        if (value == null)
            value = "null";

        final byte[] bytes = value.getBytes(charset);
        writeShort(bytes.length);
        writeBytes(bytes);
    }

    public UUID readUniqueId() {
        return UUID.fromString(readString());
    }

    public void writeUniqueId(UUID uniqueId) {
        writeString(uniqueId.toString());
    }

    public Object readObject() {
        switch (readString()) {
            case "Integer":
                return readInt();
            case "Double":
                return readDouble();
            case "Long":
                return readLong();
            case "Short":
                return readShort();
            case "Byte":
                return readByte();
            case "Boolean":
                return readBoolean();
            case "Float":
                return readFloat();
            case "String":
                return readString();
        }

        return null;
    }

    public void writeObject(Object object) {
        if (object == null)
            object = "null";

        writeString(object.getClass().getSimpleName());

        switch (object.getClass().getSimpleName()) {
            case "Integer":
                writeInt(Integer.parseInt(object.toString()));
                break;
            case "Double":
                writeDouble(Double.parseDouble(object.toString()));
                break;
            case "Long":
                writeLong(Long.parseLong(object.toString()));
                break;
            case "Short":
                writeShort(Short.parseShort(object.toString()));
                break;
            case "Byte":
                writeByte(Byte.parseByte(object.toString()));
                break;
            case "Boolean":
                writeBoolean(Boolean.parseBoolean(object.toString()));
                break;
            case "Float":
                writeFloat(Float.parseFloat(object.toString()));
                break;
            case "String":
                writeString(object.toString());
                break;
        }
    }

    @Override
    public int capacity() {
        return this.buffer.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        return this.buffer.capacity(newCapacity);

    }

    @Override
    public int maxCapacity() {
        return this.buffer.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override
    public ByteOrder order() {
        return this.buffer.order();
    }


    @Override
    public ByteBuf unwrap() {
        return this.buffer.unwrap();

    }

    @Override
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override
    public int readerIndex() {
        return this.buffer.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int readerIndex) {
        return this.buffer.readerIndex(readerIndex);
    }

    @Override
    public int writerIndex() {
        return this.buffer.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        return this.buffer.writerIndex(writerIndex);
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return this.buffer.setIndex(readerIndex, writerIndex);
    }

    @Override
    public int readableBytes() {
        return this.buffer.readableBytes();
    }

    @Override
    public int writableBytes() {
        return this.buffer.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return this.buffer.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return this.buffer.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return this.buffer.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return this.buffer.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return this.buffer.isWritable(size);
    }

    @Override
    public ByteBuf clear() {
        return this.buffer.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return this.buffer.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return this.buffer.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return this.buffer.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return this.buffer.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return this.buffer.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.buffer.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        return this.buffer.ensureWritable(minWritableBytes);

    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return this.buffer.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return this.buffer.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return this.buffer.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return this.buffer.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return this.buffer.getShort(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return this.buffer.getUnsignedShort(index);
    }

    @Override
    public int getMedium(int index) {
        return this.buffer.getMedium(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return this.buffer.getUnsignedMedium(index);
    }

    @Override
    public int getInt(int index) {
        return this.buffer.getInt(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return this.buffer.getUnsignedInt(index);
    }

    @Override
    public long getLong(int index) {
        return this.buffer.getLong(index);
    }

    @Override
    public char getChar(int index) {
        return this.buffer.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return this.buffer.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return this.buffer.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return this.buffer.getBytes(index, dst);

    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return this.buffer.getBytes(index, dst, length);

    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return this.buffer.getBytes(index, dst, dstIndex, length);

    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        return this.buffer.getBytes(index, dst);

    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return this.buffer.getBytes(index, dst, dstIndex, length);

    }


    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return this.buffer.setBoolean(index, value);

    }

    @Override
    public ByteBuf setByte(int index, int value) {
        return this.buffer.setByte(index, value);

    }

    @Override
    public ByteBuf setShort(int index, int value) {
        return this.buffer.setShort(index, value);

    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        return this.buffer.setMedium(index, value);

    }

    @Override
    public ByteBuf setInt(int index, int value) {
        return this.buffer.setInt(index, value);

    }

    @Override
    public ByteBuf setLong(int index, long value) {
        return this.buffer.setLong(index, value);

    }

    @Override
    public ByteBuf setChar(int index, int value) {
        return this.buffer.setChar(index, value);

    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        return this.buffer.setFloat(index, value);

    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        return this.buffer.setDouble(index, value);

    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        return this.buffer.setBytes(index, src);

    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return this.buffer.setBytes(index, src, length);

    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return this.buffer.setBytes(index, src, srcIndex, length);

    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        return this.buffer.setBytes(index, src);

    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return this.buffer.setBytes(index, src, srcIndex, length);

    }


    @Override
    public ByteBuf setZero(int index, int length) {
        return this.buffer.setZero(index, length);
    }

    @Override
    public boolean readBoolean() {
        return this.buffer.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.buffer.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.buffer.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.buffer.readShort();
    }

    @Override
    public int readUnsignedShort() {
        return this.buffer.readUnsignedShort();
    }

    @Override
    public int readMedium() {
        return this.buffer.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return this.buffer.readUnsignedMedium();
    }

    @Override
    public int readInt() {
        return this.buffer.readInt();
    }

    @Override
    public long readUnsignedInt() {
        return this.buffer.readUnsignedInt();
    }

    @Override
    public long readLong() {
        return this.buffer.readLong();
    }

    @Override
    public char readChar() {
        return this.buffer.readChar();
    }

    @Override
    public float readFloat() {
        return this.buffer.readFloat();
    }

    @Override
    public double readDouble() {
        return this.buffer.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return this.buffer.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return this.buffer.readSlice(length);

    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        return this.buffer.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return this.buffer.readBytes(dst, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return this.buffer.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        return this.buffer.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return this.buffer.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        return this.buffer.skipBytes(length);

    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        return this.buffer.writeBoolean(value);

    }

    @Override
    public ByteBuf writeByte(int value) {
        return this.buffer.writeByte(value);

    }

    @Override
    public ByteBuf writeShort(int value) {
        return this.buffer.writeShort(value);
    }

    @Override
    public ByteBuf writeMedium(int value) {
        return this.buffer.writeMedium(value);
    }

    @Override
    public ByteBuf writeInt(int value) {
        return this.buffer.writeInt(value);
    }

    @Override
    public ByteBuf writeLong(long value) {
        return this.buffer.writeLong(value);
    }

    @Override
    public ByteBuf writeChar(int value) {
        return this.buffer.writeChar(value);
    }

    @Override
    public ByteBuf writeFloat(float value) {
        return this.buffer.writeFloat(value);
    }

    @Override
    public ByteBuf writeDouble(double value) {
        return this.buffer.writeDouble(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        return this.buffer.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return this.buffer.writeBytes(src, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return this.buffer.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        return this.buffer.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return this.buffer.writeBytes(src, srcIndex, length);
    }


    @Override
    public ByteBuf writeZero(int length) {
        return this.buffer.writeZero(length);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return this.buffer.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return this.buffer.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return this.buffer.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return this.buffer.bytesBefore(index, length, value);
    }

    @Override
    public ByteBuf copy() {
        return this.buffer.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return this.buffer.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return this.buffer.slice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return this.buffer.slice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return this.buffer.duplicate();
    }

    @Override
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.buffer.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return this.buffer.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return this.buffer.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.buffer.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return this.buffer.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    @Override
    public byte[] array() {
        return this.buffer.array();
    }

    @Override
    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.buffer.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.buffer.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return this.buffer.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return this.buffer.toString(index, length, charset);
    }

    @Override
    public int hashCode() {
        return this.buffer.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.buffer.equals(obj);
    }

    @Override
    public int compareTo(ByteBuf buffer) {
        return this.buffer.compareTo(buffer);
    }

    @Override
    public String toString() {
        return this.buffer.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return this.buffer.retain(increment);
    }

    @Override
    public ByteBuf retain() {
        return this.buffer.retain();
    }

    @Override
    public int refCnt() {
        return this.buffer.refCnt();
    }

    @Override
    public boolean release() {
        return this.buffer.release();
    }

    @Override
    public boolean release(int decrement) {
        return this.buffer.release(decrement);
    }


    @Override
    public ByteBuf touch() {
        return this.buffer.touch();
    }

    @Override
    public ByteBuf touch(Object arg0) {
        return this.buffer.touch(arg0);
    }


    @Override
    public ByteBuf getBytes(int arg0, ByteBuffer arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf getBytes(int arg0, OutputStream arg1, int arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getBytes(int arg0, GatheringByteChannel arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf order(ByteOrder arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf readBytes(ByteBuffer arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf readBytes(OutputStream arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int readBytes(GatheringByteChannel arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf setBytes(int arg0, ByteBuffer arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int setBytes(int arg0, InputStream arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int setBytes(int arg0, ScatteringByteChannel arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int writeBytes(InputStream arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int writeBytes(ScatteringByteChannel arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf asReadOnly() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int forEachByte(ByteProcessor arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int forEachByte(int arg0, int arg1, ByteProcessor arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int forEachByteDesc(ByteProcessor arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int forEachByteDesc(int arg0, int arg1, ByteProcessor arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBytes(int arg0, FileChannel arg1, long arg2, int arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public CharSequence getCharSequence(int arg0, int arg1, Charset arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIntLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMediumLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public short getShortLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getUnsignedIntLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedMediumLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getUnsignedShortLE(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int readBytes(FileChannel arg0, long arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public CharSequence readCharSequence(int arg0, Charset arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int readIntLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long readLongLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readMediumLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf readRetainedSlice(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short readShortLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long readUnsignedIntLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readUnsignedMediumLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readUnsignedShortLE() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf retainedDuplicate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf retainedSlice() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf retainedSlice(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int setBytes(int arg0, FileChannel arg1, long arg2, int arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int setCharSequence(int arg0, CharSequence arg1, Charset arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf setIntLE(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf setLongLE(int arg0, long arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf setMediumLE(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf setShortLE(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int writeBytes(FileChannel arg0, long arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int writeCharSequence(CharSequence arg0, Charset arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ByteBuf writeIntLE(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf writeLongLE(long arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf writeMediumLE(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ByteBuf writeShortLE(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
