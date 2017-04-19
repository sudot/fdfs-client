package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageUploadFileRequest;

import java.io.InputStream;

/**
 * 文件上传命令
 * @author tobato
 * @author sudot on 2017-03-17 0017.
 */
public class StorageUploadFileCommand extends AbstractFdfsCommand<StorePath> {

    /**
     * 文件上传命令
     * @param storeIndex     存储节点
     * @param inputStream    输入流
     * @param fileExtName    文件扩展名
     * @param fileSize       文件大小
     * @param isAppenderFile 是否支持断点续传
     */
    public StorageUploadFileCommand(byte storeIndex, InputStream inputStream, String fileExtName, long fileSize,
                                    boolean isAppenderFile) {
        super();
        this.request = new StorageUploadFileRequest(storeIndex, inputStream, fileExtName, fileSize, isAppenderFile);
        // 输出响应
        this.response = new FdfsResponse<StorePath>() {
            // default response
        };
    }

}
