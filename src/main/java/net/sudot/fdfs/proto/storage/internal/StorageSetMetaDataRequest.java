package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;
import net.sudot.fdfs.proto.mapper.MetaDataMapper;
import net.sudot.fdfs.proto.storage.enums.StorageMetaDataSetType;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * 设置文件标签
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class StorageSetMetaDataRequest extends FdfsRequest {

    /** 文件名byte长度 */
    @FdfsColumn(index = 0)
    private int fileNameByteLength;
    /** 元数据byte长度 */
    @FdfsColumn(index = 1)
    private int metaDataByteLength;
    /** 操作标记（重写/覆盖） */
    @FdfsColumn(index = 2)
    private byte opFlag;
    /** 组名 */
    @FdfsColumn(index = 3, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 文件路径 */
    @FdfsColumn(index = 4, dynamicField = DynamicFieldType.ALL_REST_BYTE)
    private String path;
    /** 元数据 */
    @FdfsColumn(index = 5, dynamicField = DynamicFieldType.META_DATA)
    private Set<MetaData> metaDataSet;

    /**
     * 设置文件元数据
     * @param groupName   文件存储组
     * @param path        文件路径
     * @param metaDataSet 元数据集合
     * @param type        操作类型
     */
    public StorageSetMetaDataRequest(String groupName, String path, Set<MetaData> metaDataSet,
                                     StorageMetaDataSetType type) {
        super();
        this.groupName = groupName;
        this.path = path;
        this.metaDataSet = metaDataSet;
        this.opFlag = type.getType();
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_SET_METADATA);
    }

    /**
     * 打包参数
     */
    @Override
    public byte[] encodeParam(Charset charset) {
        // 运行时参数在此计算值
        this.fileNameByteLength = path.getBytes(charset).length;
        this.metaDataByteLength = getMetaDataSetByteSize(charset);
        return super.encodeParam(charset);
    }

    /**
     * 获取metaDataSet长度
     * @param charset
     * @return
     */
    private int getMetaDataSetByteSize(Charset charset) {
        return MetaDataMapper.toByte(metaDataSet, charset).length;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<MetaData> getMetaDataSet() {
        return metaDataSet;
    }

    public byte getOpFlag() {
        return opFlag;
    }

    public String getPath() {
        return path;
    }

    public int getFileNameByteLength() {
        return fileNameByteLength;
    }

    public int getMetaDataByteLength() {
        return metaDataByteLength;
    }

}
