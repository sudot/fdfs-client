package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.storage.internal.StorageGetMetaDataRequest;
import net.sudot.fdfs.proto.storage.internal.StorageGetMetaDataResponse;

import java.util.Set;

/**
 * 设置文件标签
 * @author tobato
 */
public class StorageGetMetaDataCommand extends AbstractFdfsCommand<Set<MetaData>> {

    /**
     * 设置文件标签(元数据)
     * @param groupName 文件所在的组名
     * @param path      文件路径
     */
    public StorageGetMetaDataCommand(String groupName, String path) {
        this.request = new StorageGetMetaDataRequest(groupName, path);
        // 输出响应
        this.response = new StorageGetMetaDataResponse();
    }

}
