package net.sudot.fdfs.service;

import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorageNodeInfo;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.StorageAppendFileCommand;
import net.sudot.fdfs.proto.storage.StorageModifyCommand;
import net.sudot.fdfs.proto.storage.StorageTruncateCommand;
import net.sudot.fdfs.proto.storage.StorageUploadFileCommand;

import java.io.InputStream;

/**
 * 存储服务客户端接口实现
 * @author tobato
 * Update by sudot on 2017-03-19 0019.
 */
public class DefaultAppendFileStorageClient extends DefaultGenerateStorageClient implements AppendFileStorageClient {

    @Override
    public StorePath uploadAppenderFile(InputStream inputStream, long fileSize, String fileExtName) {
        StorageNode client = trackerClient.getStoreStorage();
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
                fileExtName, fileSize, true);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        StorageNode client = trackerClient.getStoreStorage(groupName);
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
                fileExtName, fileSize, true);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void appendFile(String groupName, String path, InputStream inputStream, long fileSize) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageAppendFileCommand command = new StorageAppendFileCommand(inputStream, fileSize, path);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void appendFile(String fullPath, InputStream inputStream, long fileSize) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageAppendFileCommand command = new StorageAppendFileCommand(inputStream, fileSize, storePath.getPath());
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void appendFile(StorePath storePath, InputStream inputStream, long fileSize) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageAppendFileCommand command = new StorageAppendFileCommand(inputStream, fileSize, storePath.getPath());
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void modifyFile(String groupName, String path, InputStream inputStream, long fileSize, long fileOffset) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageModifyCommand command = new StorageModifyCommand(path, inputStream, fileSize, fileOffset);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void modifyFile(String fullPath, InputStream inputStream, long fileSize, long fileOffset) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageModifyCommand command = new StorageModifyCommand(storePath.getPath(), inputStream, fileSize, fileOffset);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void modifyFile(StorePath storePath, InputStream inputStream, long fileSize, long fileOffset) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageModifyCommand command = new StorageModifyCommand(storePath.getPath(), inputStream, fileSize, fileOffset);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void truncateFile(String groupName, String path, long truncatedFileSize) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageTruncateCommand command = new StorageTruncateCommand(path, truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void truncateFile(String fullPath, long truncatedFileSize) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageTruncateCommand command = new StorageTruncateCommand(storePath.getPath(), truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void truncateFile(StorePath storePath, long truncatedFileSize) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageTruncateCommand command = new StorageTruncateCommand(storePath.getPath(), truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void truncateFile(String groupName, String path) {
        long truncatedFileSize = 0;
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageTruncateCommand command = new StorageTruncateCommand(path, truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void truncateFile(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        long truncatedFileSize = 0;
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageTruncateCommand command = new StorageTruncateCommand(storePath.getPath(), truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void truncateFile(StorePath storePath) {
        long truncatedFileSize = 0;
        StorageNodeInfo client = trackerClient.getUpdateStorage(storePath.getGroup(), storePath.getPath());
        StorageTruncateCommand command = new StorageTruncateCommand(storePath.getPath(), truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

}
