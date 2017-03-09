package net.sudot.fdfs.proto.storage;

import java.io.InputStream;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageAppendFileRequest;

/**
 * 添加文件命令
 * 
 * @author tobato
 *
 */
public class StorageAppendFileCommand extends AbstractFdfsCommand<Void> {

    public StorageAppendFileCommand(InputStream inputStream, long fileSize, String path) {
        this.request = new StorageAppendFileRequest(inputStream, fileSize, path);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
