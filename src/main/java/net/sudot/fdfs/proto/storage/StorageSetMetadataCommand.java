package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.enums.StorageMetdataSetType;
import net.sudot.fdfs.proto.storage.internal.StorageSetMetadataRequest;

import java.util.Set;

/**
 * 设置文件标签
 * @author tobato
 */
public class StorageSetMetadataCommand extends AbstractFdfsCommand<Void> {

    /**
     * 设置文件标签(元数据)
     * @param groupName   组名
     * @param path        文件路径
     * @param metaDataSet 元数据信息
     * @param type        元数据操作方式
     */
    public StorageSetMetadataCommand(String groupName, String path, Set<MateData> metaDataSet,
                                     StorageMetdataSetType type) {
        this.request = new StorageSetMetadataRequest(groupName, path, metaDataSet, type);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
