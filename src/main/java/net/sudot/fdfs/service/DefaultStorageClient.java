package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.ConnectionManager;
import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorageNodeInfo;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadCallback;
import net.sudot.fdfs.proto.storage.StorageAppendFileCommand;
import net.sudot.fdfs.proto.storage.StorageDeleteFileCommand;
import net.sudot.fdfs.proto.storage.StorageDownloadCommand;
import net.sudot.fdfs.proto.storage.StorageGetMetaDataCommand;
import net.sudot.fdfs.proto.storage.StorageModifyCommand;
import net.sudot.fdfs.proto.storage.StorageQueryFileInfoCommand;
import net.sudot.fdfs.proto.storage.StorageSetMetaDataCommand;
import net.sudot.fdfs.proto.storage.StorageTruncateCommand;
import net.sudot.fdfs.proto.storage.StorageUploadFileCommand;
import net.sudot.fdfs.proto.storage.StorageUploadSlaveFileCommand;
import net.sudot.fdfs.proto.storage.enums.StorageMetaDataSetType;

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
        StorageNode client = trackerClient.getStoreStorage(groupName);
        // 上传文件
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream, fileExtName, fileSize, false);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        String groupName = null;
        return uploadFile(groupName, inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        // 上传文件
        StorePath storePath = uploadFile(groupName, inputStream, fileSize, fileExtName);
        // 上传Metadata
        if (null != metaDataSet && !metaDataSet.isEmpty()) {
            overwriteMetaData(storePath, metaDataSet);
        }
        return storePath;
    }

    @Override
    public StorePath uploadAppenderFile(InputStream inputStream, long fileSize, String fileExtName) {
        String groupName = null;
        return uploadAppenderFile(groupName, inputStream, fileSize, fileExtName);
    }

    @Override
    public StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        StorageNode client = trackerClient.getStoreStorage(groupName);
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
                fileExtName, fileSize, true);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public StorePath uploadAppenderFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        String groupName = null;
        return uploadAppenderFile(groupName, inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        StorePath storePath = uploadAppenderFile(groupName, inputStream, fileSize, fileExtName);
        // 上传Metadata
        if (null != metaDataSet && !metaDataSet.isEmpty()) {
            overwriteMetaData(storePath, metaDataSet);
        }
        return storePath;
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
        appendFile(storePath.getGroup(), storePath.getPath(), inputStream, fileSize);
    }

    @Override
    public void appendFile(StorePath storePath, InputStream inputStream, long fileSize) {
        appendFile(storePath.getGroup(), storePath.getPath(), inputStream, fileSize);
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
    public void modifyFile(String groupName, String path, InputStream inputStream, long modifySize, long modifyOffset) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageModifyCommand command = new StorageModifyCommand(path, inputStream, modifySize, modifyOffset);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void modifyFile(String fullPath, InputStream inputStream, long modifySize, long modifyOffset) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        modifyFile(storePath.getGroup(), storePath.getPath(), inputStream, modifySize, modifyOffset);
    }

    @Override
    public void modifyFile(StorePath storePath, InputStream inputStream, long modifySize, long modifyOffset) {
        modifyFile(storePath.getGroup(), storePath.getPath(), inputStream, modifySize, modifyOffset);
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
        truncateFile(storePath.getGroup(), storePath.getPath(), truncatedFileSize);
    }

    @Override
    public void truncateFile(StorePath storePath, long truncatedFileSize) {
        truncateFile(storePath.getGroup(), storePath.getPath(), truncatedFileSize);
    }

    @Override
    public void truncateFile(String groupName, String path) {
        long truncatedFileSize = 0;
        truncateFile(groupName, path, truncatedFileSize);
    }

    @Override
    public void truncateFile(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        long truncatedFileSize = 0;
        truncateFile(storePath.getGroup(), storePath.getPath(), truncatedFileSize);
    }

    @Override
    public void truncateFile(StorePath storePath) {
        long truncatedFileSize = 0;
        truncateFile(storePath.getGroup(), storePath.getPath(), truncatedFileSize);
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
    public <T> T downloadFile(String groupName, String path, long downloadOffset, long downloadSize,
                              DownloadCallback<T> callback) {
        StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
        StorageDownloadCommand<T> command = new StorageDownloadCommand<T>(groupName, path, downloadOffset, downloadSize, callback);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public <T> T downloadFile(String fullPath, long downloadOffset, long downloadSize, DownloadCallback<T> callback) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return downloadFile(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, callback);
    }

    @Override
    public <T> T downloadFile(StorePath storePath, long downloadOffset, long downloadSize, DownloadCallback<T> callback) {
        return downloadFile(storePath.getGroup(), storePath.getPath(), downloadOffset, downloadSize, callback);
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
    public void mergeMetaData(String groupName, String path, Set<MetaData> metaDataSet) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageSetMetaDataCommand command = new StorageSetMetaDataCommand(groupName, path, metaDataSet,
                StorageMetaDataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void mergeMetaData(String fullPath, Set<MetaData> metaDataSet) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        mergeMetaData(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void mergeMetaData(StorePath storePath, Set<MetaData> metaDataSet) {
        mergeMetaData(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void overwriteMetaData(String groupName, String path, Set<MetaData> metaDataSet) {
        StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
        StorageSetMetaDataCommand command = new StorageSetMetaDataCommand(groupName, path, metaDataSet,
                StorageMetaDataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public void overwriteMetaData(String fullPath, Set<MetaData> metaDataSet) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        overwriteMetaData(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void overwriteMetaData(StorePath storePath, Set<MetaData> metaDataSet) {
        overwriteMetaData(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public Set<MetaData> getMetaData(String groupName, String path) {
        StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
        StorageGetMetaDataCommand command = new StorageGetMetaDataCommand(groupName, path);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    @Override
    public Set<MetaData> getMetaData(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return getMetaData(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public Set<MetaData> getMetaData(StorePath storePath) {
        return getMetaData(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public TrackerClient getTrackerClient() {
        return trackerClient;
    }

    public DefaultStorageClient setTrackerClient(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
        return this;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public DefaultStorageClient setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        return this;
    }

}
