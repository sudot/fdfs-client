package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.ConnectionManager;
import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorageNodeInfo;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadCallback;
import net.sudot.fdfs.proto.storage.StorageAppendFileCommand;
import net.sudot.fdfs.proto.storage.StorageDeleteFileCommand;
import net.sudot.fdfs.proto.storage.StorageDownloadCommand;
import net.sudot.fdfs.proto.storage.StorageGetMetadataCommand;
import net.sudot.fdfs.proto.storage.StorageModifyCommand;
import net.sudot.fdfs.proto.storage.StorageQueryFileInfoCommand;
import net.sudot.fdfs.proto.storage.StorageSetMetadataCommand;
import net.sudot.fdfs.proto.storage.StorageTruncateCommand;
import net.sudot.fdfs.proto.storage.StorageUploadFileCommand;
import net.sudot.fdfs.proto.storage.StorageUploadSlaveFileCommand;
import net.sudot.fdfs.proto.storage.enums.StorageMetdataSetType;
import net.sudot.fdfs.util.Validate;

import java.io.InputStream;
import java.util.Set;

/**
 * 基本存储客户端操作实现
 * @author tobato
 * @author sudot on 2017-03-17 0017.
 */
public class DefaultStorageClient implements StorageClient {

    /** trackerClient */
    protected TrackerClient trackerClient;
    /** connectManager */
    protected ConnectionManager connectionManager;

    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName) {
        String groupName = null;
        return uploadFile(groupName, inputStream, fileSize, fileExtName);
    }

    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        StorageNode client = trackerClient.getStoreStorage(groupName);
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
                fileExtName, fileSize, false);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
        String groupName = null;
        return uploadFile(groupName, inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        StorageNode client = trackerClient.getStoreStorage(groupName);
        return uploadFileAndMateData(client, inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadSlaveFile(String groupName, String masterFilename, InputStream inputStream, long fileSize,
                                     String suffixName, String fileExtName) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, masterFilename);
        StorageUploadSlaveFileCommand command = new StorageUploadSlaveFileCommand(inputStream, fileSize, masterFilename,
                suffixName, fileExtName);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public StorePath uploadSlaveFile(String masterFullPath, InputStream inputStream, long fileSize,
                                     String suffixName, String fileExtName) {
        StorePath storePath = StorePath.parseFullPath(masterFullPath);
        return uploadSlaveFile(storePath.getGroup(), storePath.getPath(), inputStream, fileSize, suffixName, fileExtName);
    }

    @Override
    public StorePath uploadSlaveFile(StorePath masterStorePath, InputStream inputStream, long fileSize,
                                     String suffixName, String fileExtName) {
        return uploadSlaveFile(masterStorePath.getGroup(), masterStorePath.getPath(), inputStream, fileSize, suffixName, fileExtName);
    }

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

    @Override
    public FileInfo queryFileInfo(String groupName, String path) {
        StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
        StorageQueryFileInfoCommand command = new StorageQueryFileInfoCommand(groupName, path);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public FileInfo queryFileInfo(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return queryFileInfo(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public FileInfo queryFileInfo(StorePath storePath) {
        return queryFileInfo(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
        long fileOffset = 0;
        long fileSize = 0;
        return downloadFile(groupName, path, fileOffset, fileSize, callback);
    }

    @Override
    public <T> T downloadFile(String fullPath, DownloadCallback<T> callback) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return downloadFile(storePath.getGroup(), storePath.getPath(), callback);
    }

    @Override
    public <T> T downloadFile(StorePath storePath, DownloadCallback<T> callback) {
        return downloadFile(storePath.getGroup(), storePath.getPath(), callback);
    }

    @Override
    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize,
                              DownloadCallback<T> callback) {
        StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
        StorageDownloadCommand<T> command = new StorageDownloadCommand<T>(groupName, path, fileOffset, fileSize, callback);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public <T> T downloadFile(String fullPath, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return downloadFile(storePath.getGroup(), storePath.getPath(), fileOffset, fileSize, callback);
    }

    @Override
    public <T> T downloadFile(StorePath storePath, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        return downloadFile(storePath.getGroup(), storePath.getPath(), fileOffset, fileSize, callback);
    }

    @Override
    public void deleteFile(String groupName, String path) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageDeleteFileCommand command = new StorageDeleteFileCommand(groupName, path);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void deleteFile(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        deleteFile(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public void deleteFile(StorePath storePath) {
        deleteFile(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public void mergeMetadata(String groupName, String path, Set<MateData> metaDataSet) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageSetMetadataCommand command = new StorageSetMetadataCommand(groupName, path, metaDataSet,
                StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void mergeMetadata(String fullPath, Set<MateData> metaDataSet) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        mergeMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void mergeMetadata(StorePath storePath, Set<MateData> metaDataSet) {
        mergeMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void overwriteMetadata(String groupName, String path, Set<MateData> metaDataSet) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageSetMetadataCommand command = new StorageSetMetadataCommand(groupName, path, metaDataSet,
                StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void overwriteMetadata(String fullPath, Set<MateData> metaDataSet) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        overwriteMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void overwriteMetadata(StorePath storePath, Set<MateData> metaDataSet) {
        overwriteMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public Set<MateData> getMetadata(String groupName, String path) {
        StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
        StorageGetMetadataCommand command = new StorageGetMetadataCommand(groupName, path);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public Set<MateData> getMetadata(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return getMetadata(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public Set<MateData> getMetadata(StorePath storePath) {
        return getMetadata(storePath.getGroup(), storePath.getPath());
    }

    /**
     * 上传文件和元数据
     * @param client
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @param metaDataSet
     * @return
     */
    private StorePath uploadFileAndMateData(StorageNode client, InputStream inputStream, long fileSize,
                                            String fileExtName, Set<MateData> metaDataSet) {
        // 上传文件
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
                fileExtName, fileSize, false);
        StorePath path = connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
        // 上传matadata
        if (null != metaDataSet && !metaDataSet.isEmpty()) {
            StorageSetMetadataCommand setMDCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                    metaDataSet, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            connectionManager.executeFdfsCmd(client.getInetSocketAddress(), setMDCommand);
        }
        return path;
    }

    public TrackerClient getTrackerClient() {
        return trackerClient;
    }

    public DefaultStorageClient setTrackerClient(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
        return this;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public DefaultStorageClient setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        return this;
    }

}
