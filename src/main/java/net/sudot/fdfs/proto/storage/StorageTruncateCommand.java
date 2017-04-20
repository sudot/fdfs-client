package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageTruncateRequest;

/**
 * 文件删减(Truncate)命令
 * <pre>
 * 使用限制：
 * 1.创建文件时候需要采用<<源追加>>模式,之后才能Truncate
 * 2.truncatedFileSize是指截取后的文件大小
 * 3.truncatedFileSize不能大于文件总大小
 * 4.文件的截取是从文件起始部分保留truncatedFileSize,后面的部分全部清除
 * </pre>
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageTruncateCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件删减(Truncate)命令(从文件起始部分删减)
     * @param path              文件路径
     * @param truncatedFileSize 删减后的文件大小(需要保留的文件大小)
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
