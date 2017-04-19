package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageTruncateRequest;

/**
 * 文件删减(Truncate)命令
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageTruncateCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件删减(Truncate)命令
     * @param path              文件路径
     * @param truncatedFileSize 文件大小
     */
    public StorageTruncateCommand(String path, long truncatedFileSize) {
        super();
        this.request = new StorageTruncateRequest(path, truncatedFileSize);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
