package de.fileinputstream.redisbuilder.network;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class DataSerializer extends ByteBuf {
    private final ByteBuf buf;

    public DataSerializer(final ByteBuf buf) {
        this.buf = buf;
    }

    public void writeString(final String value) {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        this.writeVarInt(bytes.length);
        this.writeBytes(bytes);
    }

    public String readString() {
        final int length = this.readVarInt();
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; ++i) {
            bytes[i] = this.readByte();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void writeVarInt(int value) {
        byte part = 0;
        do {
            part = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0) {
                part |= (byte) 128;
            }
            this.buf.writeByte((int) part);
        } while (value != 0);
    }

    public int readVarInt() {
        int out = 0;
        byte bytes = 0;
        byte in;
        do {
            in = this.buf.readByte();
            final int n = out;
            final byte b = (byte) (in & 0x7F);
            final byte b2 = bytes;
            bytes = (byte) (b2 + 1);
            out = (n | b << b2 * 7);
            if (bytes > 5) {
                throw new DecoderException("Attempt to read int bigger than allowed for a varint!");
            }
        } while ((in & 0x80) == 0x80);
        return out;
    }

    public int capacity() {
        return this.buf.capacity();
    }

    public ByteBuf capacity(final int i) {
        return this.buf.capacity(i);
    }

    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    public ByteOrder order() {
        return this.buf.order();
    }

    public ByteBuf order(final ByteOrder byteorder) {
        return this.buf.order(byteorder);
    }

    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }

    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public ByteBuf asReadOnly() {
        return null;
    }

    public int readerIndex() {
        return this.buf.readerIndex();
    }

    public ByteBuf readerIndex(final int i) {
        return this.buf.readerIndex(i);
    }

    public int writerIndex() {
        return this.buf.writerIndex();
    }

    public ByteBuf writerIndex(final int i) {
        return this.buf.writerIndex(i);
    }

    public ByteBuf setIndex(final int i, final int j) {
        return this.buf.setIndex(i, j);
    }

    public int readableBytes() {
        return this.buf.readableBytes();
    }

    public int writableBytes() {
        return this.buf.writableBytes();
    }

    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.buf.isReadable();
    }

    public boolean isReadable(final int i) {
        return this.buf.isReadable(i);
    }

    public boolean isWritable() {
        return this.buf.isWritable();
    }

    public boolean isWritable(final int i) {
        return this.buf.isWritable(i);
    }

    public ByteBuf clear() {
        return this.buf.clear();
    }

    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(final int i) {
        return this.buf.ensureWritable(i);
    }

    public int ensureWritable(final int i, final boolean flag) {
        return this.buf.ensureWritable(i, flag);
    }

    public boolean getBoolean(final int i) {
        return this.buf.getBoolean(i);
    }

    public byte getByte(final int i) {
        return this.buf.getByte(i);
    }

    public short getUnsignedByte(final int i) {
        return this.buf.getUnsignedByte(i);
    }

    public short getShort(final int i) {
        return this.buf.getShort(i);
    }

    @Override
    public short getShortLE(int i) {
        return 0;
    }

    public int getUnsignedShort(final int i) {
        return this.buf.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int i) {
        return 0;
    }

    public int getMedium(final int i) {
        return this.buf.getMedium(i);
    }

    @Override
    public int getMediumLE(int i) {
        return 0;
    }

    public int getUnsignedMedium(final int i) {
        return this.buf.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int i) {
        return 0;
    }

    public int getInt(final int i) {
        return this.buf.getInt(i);
    }

    @Override
    public int getIntLE(int i) {
        return 0;
    }

    public long getUnsignedInt(final int i) {
        return this.buf.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int i) {
        return 0;
    }

    public long getLong(final int i) {
        return this.buf.getLong(i);
    }

    @Override
    public long getLongLE(int i) {
        return 0;
    }

    public char getChar(final int i) {
        return this.buf.getChar(i);
    }

    public float getFloat(final int i) {
        return this.buf.getFloat(i);
    }

    public double getDouble(final int i) {
        return this.buf.getDouble(i);
    }

    public ByteBuf getBytes(final int i, final ByteBuf bytebuf) {
        return this.buf.getBytes(i, bytebuf);
    }

    public ByteBuf getBytes(final int i, final ByteBuf bytebuf, final int j) {
        return this.buf.getBytes(i, bytebuf, j);
    }

    public ByteBuf getBytes(final int i, final ByteBuf bytebuf, final int j, final int k) {
        return this.buf.getBytes(i, bytebuf, j, k);
    }

    public ByteBuf getBytes(final int i, final byte[] abyte) {
        return this.buf.getBytes(i, abyte);
    }

    public ByteBuf getBytes(final int i, final byte[] abyte, final int j, final int k) {
        return this.buf.getBytes(i, abyte, j, k);
    }

    public ByteBuf getBytes(final int i, final ByteBuffer bytebuffer) {
        return this.buf.getBytes(i, bytebuffer);
    }

    public ByteBuf getBytes(final int i, final OutputStream outputstream, final int j) throws IOException {
        return this.buf.getBytes(i, outputstream, j);
    }

    public int getBytes(final int i, final GatheringByteChannel gatheringbytechannel, final int j) throws IOException {
        return this.buf.getBytes(i, gatheringbytechannel, j);
    }

    @Override
    public int getBytes(int i, FileChannel fileChannel, long l, int i1) {
        return 0;
    }

    @Override
    public CharSequence getCharSequence(int i, int i1, Charset charset) {
        return null;
    }

    public ByteBuf setBoolean(final int i, final boolean flag) {
        return this.buf.setBoolean(i, flag);
    }

    public ByteBuf setByte(final int i, final int j) {
        return this.buf.setByte(i, j);
    }

    public ByteBuf setShort(final int i, final int j) {
        return this.buf.setShort(i, j);
    }

    @Override
    public ByteBuf setShortLE(int i, int i1) {
        return null;
    }

    public ByteBuf setMedium(final int i, final int j) {
        return this.buf.setMedium(i, j);
    }

    @Override
    public ByteBuf setMediumLE(int i, int i1) {
        return null;
    }

    public ByteBuf setInt(final int i, final int j) {
        return this.buf.setInt(i, j);
    }

    @Override
    public ByteBuf setIntLE(int i, int i1) {
        return null;
    }

    public ByteBuf setLong(final int i, final long j) {
        return this.buf.setLong(i, j);
    }

    @Override
    public ByteBuf setLongLE(int i, long l) {
        return null;
    }

    public ByteBuf setChar(final int i, final int j) {
        return this.buf.setChar(i, j);
    }

    public ByteBuf setFloat(final int i, final float f) {
        return this.buf.setFloat(i, f);
    }

    public ByteBuf setDouble(final int i, final double d0) {
        return this.buf.setDouble(i, d0);
    }

    public ByteBuf setBytes(final int i, final ByteBuf bytebuf) {
        return this.buf.setBytes(i, bytebuf);
    }

    public ByteBuf setBytes(final int i, final ByteBuf bytebuf, final int j) {
        return this.buf.setBytes(i, bytebuf, j);
    }

    public ByteBuf setBytes(final int i, final ByteBuf bytebuf, final int j, final int k) {
        return this.buf.setBytes(i, bytebuf, j, k);
    }

    public ByteBuf setBytes(final int i, final byte[] abyte) {
        return this.buf.setBytes(i, abyte);
    }

    public ByteBuf setBytes(final int i, final byte[] abyte, final int j, final int k) {
        return this.buf.setBytes(i, abyte, j, k);
    }

    public ByteBuf setBytes(final int i, final ByteBuffer bytebuffer) {
        return this.buf.setBytes(i, bytebuffer);
    }

    public int setBytes(final int i, final InputStream inputstream, final int j) throws IOException {
        return this.buf.setBytes(i, inputstream, j);
    }

    public int setBytes(final int i, final ScatteringByteChannel scatteringbytechannel, final int j) throws IOException {
        return this.buf.setBytes(i, scatteringbytechannel, j);
    }

    @Override
    public int setBytes(int i, FileChannel fileChannel, long l, int i1) {
        return 0;
    }

    public ByteBuf setZero(final int i, final int j) {
        return this.buf.setZero(i, j);
    }

    @Override
    public int setCharSequence(int i, CharSequence charSequence, Charset charset) {
        return 0;
    }

    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    public byte readByte() {
        return this.buf.readByte();
    }

    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    public short readShort() {
        return this.buf.readShort();
    }

    @Override
    public short readShortLE() {
        return 0;
    }

    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return 0;
    }

    public int readMedium() {
        return this.buf.readMedium();
    }

    @Override
    public int readMediumLE() {
        return 0;
    }

    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return 0;
    }

    public int readInt() {
        return this.buf.readInt();
    }

    @Override
    public int readIntLE() {
        return 0;
    }

    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return 0;
    }

    public long readLong() {
        return this.buf.readLong();
    }

    @Override
    public long readLongLE() {
        return 0;
    }

    public char readChar() {
        return this.buf.readChar();
    }

    public float readFloat() {
        return this.buf.readFloat();
    }

    public double readDouble() {
        return this.buf.readDouble();
    }

    public ByteBuf readBytes(final int i) {
        return this.buf.readBytes(i);
    }

    public ByteBuf readSlice(final int i) {
        return this.buf.readSlice(i);
    }

    @Override
    public ByteBuf readRetainedSlice(int i) {
        return null;
    }

    public ByteBuf readBytes(final ByteBuf bytebuf) {
        return this.buf.readBytes(bytebuf);
    }

    public ByteBuf readBytes(final ByteBuf bytebuf, final int i) {
        return this.buf.readBytes(bytebuf, i);
    }

    public ByteBuf readBytes(final ByteBuf bytebuf, final int i, final int j) {
        return this.buf.readBytes(bytebuf, i, j);
    }

    public ByteBuf readBytes(final byte[] abyte) {
        return this.buf.readBytes(abyte);
    }

    public ByteBuf readBytes(final byte[] abyte, final int i, final int j) {
        return this.buf.readBytes(abyte, i, j);
    }

    public ByteBuf readBytes(final ByteBuffer bytebuffer) {
        return this.buf.readBytes(bytebuffer);
    }

    public ByteBuf readBytes(final OutputStream outputstream, final int i) throws IOException {
        return this.buf.readBytes(outputstream, i);
    }

    public int readBytes(final GatheringByteChannel gatheringbytechannel, final int i) throws IOException {
        return this.buf.readBytes(gatheringbytechannel, i);
    }

    @Override
    public CharSequence readCharSequence(int i, Charset charset) {
        return null;
    }

    @Override
    public int readBytes(FileChannel fileChannel, long l, int i) {
        return 0;
    }

    public ByteBuf skipBytes(final int i) {
        return this.buf.skipBytes(i);
    }

    public ByteBuf writeBoolean(final boolean flag) {
        return this.buf.writeBoolean(flag);
    }

    public ByteBuf writeByte(final int i) {
        return this.buf.writeByte(i);
    }

    public ByteBuf writeShort(final int i) {
        return this.buf.writeShort(i);
    }

    @Override
    public ByteBuf writeShortLE(int i) {
        return null;
    }

    public ByteBuf writeMedium(final int i) {
        return this.buf.writeMedium(i);
    }

    @Override
    public ByteBuf writeMediumLE(int i) {
        return null;
    }

    public ByteBuf writeInt(final int i) {
        return this.buf.writeInt(i);
    }

    @Override
    public ByteBuf writeIntLE(int i) {
        return null;
    }

    public ByteBuf writeLong(final long i) {
        return this.buf.writeLong(i);
    }

    @Override
    public ByteBuf writeLongLE(long l) {
        return null;
    }

    public ByteBuf writeChar(final int i) {
        return this.buf.writeChar(i);
    }

    public ByteBuf writeFloat(final float f) {
        return this.buf.writeFloat(f);
    }

    public ByteBuf writeDouble(final double d0) {
        return this.buf.writeDouble(d0);
    }

    public ByteBuf writeBytes(final ByteBuf bytebuf) {
        return this.buf.writeBytes(bytebuf);
    }

    public ByteBuf writeBytes(final ByteBuf bytebuf, final int i) {
        return this.buf.writeBytes(bytebuf, i);
    }

    public ByteBuf writeBytes(final ByteBuf bytebuf, final int i, final int j) {
        return this.buf.writeBytes(bytebuf, i, j);
    }

    public ByteBuf writeBytes(final byte[] abyte) {
        return this.buf.writeBytes(abyte);
    }

    public ByteBuf writeBytes(final byte[] abyte, final int i, final int j) {
        return this.buf.writeBytes(abyte, i, j);
    }

    public ByteBuf writeBytes(final ByteBuffer bytebuffer) {
        return this.buf.writeBytes(bytebuffer);
    }

    public int writeBytes(final InputStream inputstream, final int i) throws IOException {
        return this.buf.writeBytes(inputstream, i);
    }

    public int writeBytes(final ScatteringByteChannel scatteringbytechannel, final int i) throws IOException {
        return this.buf.writeBytes(scatteringbytechannel, i);
    }

    @Override
    public int writeBytes(FileChannel fileChannel, long l, int i) {
        return 0;
    }

    public ByteBuf writeZero(final int i) {
        return this.buf.writeZero(i);
    }

    @Override
    public int writeCharSequence(CharSequence charSequence, Charset charset) {
        return 0;
    }

    public int indexOf(final int i, final int j, final byte b0) {
        return this.buf.indexOf(i, j, b0);
    }

    public int bytesBefore(final byte b0) {
        return this.buf.bytesBefore(b0);
    }

    public int bytesBefore(final int i, final byte b0) {
        return this.buf.bytesBefore(i, b0);
    }

    public int bytesBefore(final int i, final int j, final byte b0) {
        return this.buf.bytesBefore(i, j, b0);
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public int forEachByte(int i, int i1, ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public int forEachByteDesc(ByteProcessor byteProcessor) {
        return 0;
    }

    @Override
    public int forEachByteDesc(int i, int i1, ByteProcessor byteProcessor) {
        return 0;
    }

    public int forEachByte(final ByteBufProcessor bytebufprocessor) {
        return this.buf.forEachByte(bytebufprocessor);
    }

    public int forEachByte(final int i, final int j, final ByteBufProcessor bytebufprocessor) {
        return this.buf.forEachByte(i, j, bytebufprocessor);
    }

    public int forEachByteDesc(final ByteBufProcessor bytebufprocessor) {
        return this.buf.forEachByteDesc(bytebufprocessor);
    }

    public int forEachByteDesc(final int i, final int j, final ByteBufProcessor bytebufprocessor) {
        return this.buf.forEachByteDesc(i, j, bytebufprocessor);
    }

    public ByteBuf copy() {
        return this.buf.copy();
    }

    public ByteBuf copy(final int i, final int j) {
        return this.buf.copy(i, j);
    }

    public ByteBuf slice() {
        return this.buf.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return null;
    }

    public ByteBuf slice(final int i, final int j) {
        return this.buf.slice(i, j);
    }

    @Override
    public ByteBuf retainedSlice(int i, int i1) {
        return null;
    }

    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return null;
    }

    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }

    public ByteBuffer nioBuffer(final int i, final int j) {
        return this.buf.nioBuffer(i, j);
    }

    public ByteBuffer internalNioBuffer(final int i, final int j) {
        return this.buf.internalNioBuffer(i, j);
    }

    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(final int i, final int j) {
        return this.buf.nioBuffers(i, j);
    }

    public boolean hasArray() {
        return this.buf.hasArray();
    }

    public byte[] array() {
        return this.buf.array();
    }

    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    public String toString(final Charset charset) {
        return this.buf.toString(charset);
    }

    public String toString(final int i, final int j, final Charset charset) {
        return this.buf.toString(i, j, charset);
    }

    public int hashCode() {
        return this.buf.hashCode();
    }

    public boolean equals(final Object object) {
        return this.buf.equals(object);
    }

    public int compareTo(final ByteBuf bytebuf) {
        return this.buf.compareTo(bytebuf);
    }

    public String toString() {
        return this.buf.toString();
    }

    public ByteBuf retain(final int i) {
        return this.buf.retain(i);
    }

    public ByteBuf retain() {
        return this.buf.retain();
    }

    @Override
    public ByteBuf touch() {
        return null;
    }

    @Override
    public ByteBuf touch(Object o) {
        return null;
    }

    public int refCnt() {
        return this.buf.refCnt();
    }

    public boolean release() {
        return this.buf.release();
    }

    public boolean release(final int i) {
        return this.buf.release(i);
    }
}