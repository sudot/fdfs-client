package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

import java.io.InputStream;

/**
 * 从文件上传命令
 * @author tobato
 * Update by sudot on 2017-04-19 0019.
 */
public class StorageUploadSlaveFileRequest extends FdfsRequest {

    /** 主文件名长度 */
    @FdfsColumn(index = 0)
    private long masterFileNameSize;
    /** 发送文件长度 */
    @FdfsColumn(index = 1)
    private long fileSize;
    /** 名称前缀 */
    @FdfsColumn(index = 2, max = OtherConstants.FDFS_FILE_PREFIX_MAX_LEN)
    private final String prefixName;
    /** 文件扩展名 */
    @FdfsColumn(index = 3, max = OtherConstants.FDFS_FILE_EXT_NAME_MAX_LEN)
    private String fileExtName;
    /** 主文件名 */
    @FdfsColumn(index = 4, dynamicField = DynamicFieldType.allRestByte)
    private final String masterFilename;

    /**
     * 构造函数
     * @param inputStream    文件输入流
     * @param fileSize       文件大小
     * @param masterFilename 主文件路径(不包含组名)
     * @param prefixName     从文件存储前缀
     * @param fileExtName    从文件扩展名
     * @see net.sudot.fdfs.proto.storage.StorageUploadSlaveFileCommand
     */
    public StorageUploadSlaveFileRequest(InputStream inputStream, long fileSize, String masterFilename,
                                         String prefixName, String fileExtName) {
        super();
        this.inputFile = inputStream;
        this.fileSize = fileSize;
        this.masterFileNameSize = masterFilename.length();
        this.masterFilename = masterFilename;
        this.fileExtName = fileExtName;
        this.prefixName = prefixName;
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE);

    }

    public long getMasterFileNameSize() {
        return masterFileNameSize;
    }

    public StorageUploadSlaveFileRequest setMasterFileNameSize(long masterFileNameSize) {
        this.masterFileNameSize = masterFileNameSize;
        return this;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    public StorageUploadSlaveFileRequest setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getPrefixName() {
        return prefixName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public StorageUploadSlaveFileRequest setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
        return this;
    }

    public String getMasterFilename() {
        return masterFilename;
    }
}
