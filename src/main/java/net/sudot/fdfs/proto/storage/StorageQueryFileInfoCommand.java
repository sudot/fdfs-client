package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageQueryFileInfoRequest;

/**
 * 文件查询命令
 * @author tobato
 */
public class StorageQueryFileInfoCommand extends AbstractFdfsCommand<FileInfo> {

    /**
     * 文件查询命令
     * @param groupName 文件所在的组名
     * @param path      文件路径
     */
    public StorageQueryFileInfoCommand(String groupName, String path) {
        super();
        this.request = new StorageQueryFileInfoRequest(groupName, path);
        // 输出响应
        this.response = new FdfsResponse<FileInfo>() {
            // default response
        };
    }

}
