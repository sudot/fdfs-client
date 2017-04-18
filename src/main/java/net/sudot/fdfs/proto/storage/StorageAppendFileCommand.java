package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageAppendFileRequest;

import java.io.InputStream;

/**
 * 添加文件命令
 * @author tobato
 */
public class StorageAppendFileCommand extends AbstractFdfsCommand<Void> {

    /**
     * @param inputStream 添加的文件流
     * @param fileSize    添加的文件大小
     * @param path        文件添加的主文件路径(不含组名)
     */
    public StorageAppendFileCommand(InputStream inputStream, long fileSize, String path) {
        this.request = new StorageAppendFileRequest(inputStream, fileSize, path);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
