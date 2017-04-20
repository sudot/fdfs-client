package net.sudot.fdfs.domain;

import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

import java.text.SimpleDateFormat;

/**
 * 文件的基础信息
 * @author yuqih
 * @author sudot on 2017-04-20 0020.
 */
public class FileInfo {
    /** 长度 */
    @FdfsColumn(index = 0)
    private long fileSize;
    /** 创建时间 */
    @FdfsColumn(index = 1)
    private int createTime;
    /** 校验码 */
    @FdfsColumn(index = 2)
    private int crc32;
    /** ip地址 */
    @FdfsColumn(index = 3, max = OtherConstants.FDFS_IPADDR_SIZE)
    private String sourceIpAddress;

    /**
     *
     */
    public FileInfo() {
        super();
    }

    /**
     * @param sourceIpAddress
     * @param fileSize
     * @param createTime
     * @param crc32
     */
    public FileInfo(String sourceIpAddress, long fileSize, int createTime, int crc32) {
        super();
        this.sourceIpAddress = sourceIpAddress;
        this.fileSize = fileSize;
        this.createTime = createTime;
        this.crc32 = crc32;
    }

    /**
     * @return the sourceIpAddress
     */
    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    /**
     * @param sourceIpAddress the sourceIpAddress to set
     */
    public void setSourceIpAddress(String sourceIpAddress) {
        this.sourceIpAddress = sourceIpAddress;
    }

    /**
     * @return the size
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the size to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the createTime
     */
    public int getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the crc32
     */
    public int getCrc32() {
        return crc32;
    }

    /**
     * @param crc32 the crc32 to set
     */
    public void setCrc32(int crc32) {
        this.crc32 = crc32;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final StringBuilder sb = new StringBuilder("FileInfo{");
        sb.append("fileSize=").append(fileSize);
        sb.append(", createTime=").append(df.format(createTime));
        sb.append(", crc32=").append(crc32);
        sb.append(", sourceIpAddress='").append(sourceIpAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
