package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 文件修改请求
 * @author tobato
 * Update by sudot on 2017-04-19 0019.
 */
public class StorageModifyRequest extends FdfsRequest {

    /** 文件路径长度 */
    @FdfsColumn(index = 0)
    private long pathSize;
    /** 开始位置 */
    @FdfsColumn(index = 1)
    private long fileOffset;
    /** 发送文件长度 */
    @FdfsColumn(index = 2)
    private long fileSize;
    /** 文件路径 */
    @FdfsColumn(index = 3, dynamicField = DynamicFieldType.allRestByte)
    private String path;

    /**
     * 构造函数
     * @param inputStream 文件输入流
     * @param fileSize    文件大小
     * @param path        文件路径(不含组名)
     * @param fileOffset  偏移量(文件修改的起始位置)
     */
    public StorageModifyRequest(InputStream inputStream, long fileSize, String path, long fileOffset) {
        super();
        this.inputFile = inputStream;
        this.fileSize = fileSize;
        this.path = path;
        this.fileOffset = fileOffset;
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_MODIFY_FILE);

    }

    /**
     * 打包参数
     */
    @Override
    public byte[] encodeParam(Charset charset) {
        // 运行时参数在此计算值
        this.pathSize = path.getBytes(charset).length;
        return super.encodeParam(charset);
    }

    public long getPathSize() {
        return pathSize;
    }

    public long getFileOffset() {
        return fileOffset;
    }

    public String getPath() {
        return path;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

}
