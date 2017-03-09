package net.sudot.fdfs.proto.storage;

import java.util.Set;

import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.proto.storage.internal.StorageGetMetadataResponse;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.storage.internal.StorageGetMetadataRequest;

/**
 * 设置文件标签
 * 
 * @author tobato
 *
 */
public class StorageGetMetadataCommand extends AbstractFdfsCommand<Set<MateData>> {

    /**
     * 设置文件标签(元数据)
     * 
     * @param groupName
     * @param path
     * @param metaDataSet
     * @param type
     */
    public StorageGetMetadataCommand(String groupName, String path) {
        this.request = new StorageGetMetadataRequest(groupName, path);
        // 输出响应
        this.response = new StorageGetMetadataResponse();
    }

}
