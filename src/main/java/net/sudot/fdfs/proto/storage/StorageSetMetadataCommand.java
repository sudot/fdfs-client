package net.sudot.fdfs.proto.storage;

import java.util.Set;

import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.enums.StorageMetdataSetType;
import net.sudot.fdfs.proto.storage.internal.StorageSetMetadataRequest;

/**
 * 设置文件标签
 * 
 * @author tobato
 *
 */
public class StorageSetMetadataCommand extends AbstractFdfsCommand<Void> {

    /**
     * 设置文件标签(元数据)
     * 
     * @param groupName
     * @param path
     * @param metaDataSet
     * @param type
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
