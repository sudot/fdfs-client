package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageTruncateRequest;

/**
 * 文件Truncate命令
 * @author tobato
 */
public class StorageTruncateCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件Truncate命令
     * @param path     文件路径
     * @param fileSize 文件大小
     */
    public StorageTruncateCommand(String path, long fileSize) {
        super();
        this.request = new StorageTruncateRequest(path, fileSize);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
