package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageDeleteFileRequest;

/**
 * 文件删除命令
 * @author tobato
 */
public class StorageDeleteFileCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件删除命令
     * @param groupName 文件所在的组名
     * @param path      删除的文件路径
     */
    public StorageDeleteFileCommand(String groupName, String path) {
        super();
        this.request = new StorageDeleteFileRequest(groupName, path);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
