package net.sudot.fdfs.service;

import net.coobird.thumbnailator.Thumbnails;
import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.domain.ThumbImageConfig;
import net.sudot.fdfs.exception.FdfsUnsupportImageTypeException;
import net.sudot.fdfs.exception.FdfsUploadImageException;
import net.sudot.fdfs.proto.storage.DownloadCallback;
import net.sudot.fdfs.proto.storage.StorageSetMetadataCommand;
import net.sudot.fdfs.proto.storage.StorageUploadFileCommand;
import net.sudot.fdfs.proto.storage.StorageUploadSlaveFileCommand;
import net.sudot.fdfs.proto.storage.enums.StorageMetdataSetType;
import net.sudot.fdfs.util.Validate;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 面向应用的接口实现
 * @author tobato
 * Update by sudot on 2017-03-17 0017.
 */
@Component
public class DefaultFastFileStorageClient extends DefaultGenerateStorageClient implements FastFileStorageClient {

    /** 支持的图片类型 */
    private static final String[] SUPPORT_IMAGE_TYPE = {"JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP"};
    private static final List<String> SUPPORT_IMAGE_LIST = Arrays.asList(SUPPORT_IMAGE_TYPE);
    /** 缩略图生成配置 */
    @Resource
    private ThumbImageConfig thumbImageConfig;

    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        StorageNode client = trackerClient.getStoreStorage();
        if (LOGGER.isDebugEnabled()) { LOGGER.debug("获取到storageNode:{}", client); }
        return uploadFileAndMateData(client, inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        StorageNode client = trackerClient.getStoreStorage(groupName);
        if (LOGGER.isDebugEnabled()) { LOGGER.debug("获取到storageNode:{}", client); }
        return uploadFileAndMateData(client, inputStream, fileSize, fileExtName, metaDataSet);
    }

    @Override
    public StorePath uploadSlaveFile(String masterFullPath, InputStream inputStream, long fileSize, String suffixName, String fileExtName) {
        StorePath storePath = StorePath.parseFullPath(masterFullPath);
        return uploadSlaveFile(storePath.getGroup(), storePath.getPath(), inputStream, fileSize, suffixName, fileExtName);
    }

    @Override
    public StorePath uploadSlaveFile(StorePath masterStorePath, InputStream inputStream, long fileSize, String suffixName, String fileExtName) {
        return uploadSlaveFile(masterStorePath.getGroup(), masterStorePath.getPath(), inputStream, fileSize, suffixName, fileExtName);
    }

    @Override
    public StorePath uploadImageAndCrtThumbImage(InputStream inputStream, long fileSize, String fileExtName,
                                                 Set<MateData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        // 检查是否能处理此类图片
        if (!isSupportImage(fileExtName)) {
            throw new FdfsUnsupportImageTypeException("不支持的图片格式" + fileExtName);
        }
        StorageNode client = trackerClient.getStoreStorage();
        byte[] bytes = inputStreamToByte(inputStream);

        // 上传文件和mateData
        StorePath storePath = uploadFileAndMateData(client, new ByteArrayInputStream(bytes), fileSize, fileExtName,
                metaDataSet);
        // 上传缩略图
        uploadThumbImage(client, new ByteArrayInputStream(bytes), storePath.getPath(), fileExtName);
        bytes = null;
        return storePath;
    }

    @Override
    public Set<MateData> getMetadata(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return super.getMetadata(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public Set<MateData> getMetadata(StorePath storePath) {
        return super.getMetadata(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public void overwriteMetadata(String fullPath, Set<MateData> metaDataSet) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        super.overwriteMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void overwriteMetadata(StorePath storePath, Set<MateData> metaDataSet) {
        super.overwriteMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void mergeMetadata(String fullPath, Set<MateData> metaDataSet) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        super.mergeMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public void mergeMetadata(StorePath storePath, Set<MateData> metaDataSet) {
        super.mergeMetadata(storePath.getGroup(), storePath.getPath(), metaDataSet);
    }

    @Override
    public FileInfo queryFileInfo(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return super.queryFileInfo(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public FileInfo queryFileInfo(StorePath storePath) {
        return super.queryFileInfo(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public void deleteFile(String fullPath) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        super.deleteFile(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public void deleteFile(StorePath storePath) {
        super.deleteFile(storePath.getGroup(), storePath.getPath());
    }

    @Override
    public <T> T downloadFile(String fullPath, DownloadCallback<T> callback) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return super.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
    }

    @Override
    public <T> T downloadFile(StorePath storePath, DownloadCallback<T> callback) {
        return super.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
    }

    @Override
    public <T> T downloadFile(String fullPath, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        StorePath storePath = StorePath.parseFullPath(fullPath);
        return super.downloadFile(storePath.getGroup(), storePath.getPath(),fileOffset, fileSize, callback);
    }

    @Override
    public <T> T downloadFile(StorePath storePath, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        return super.downloadFile(storePath.getGroup(), storePath.getPath(),fileOffset, fileSize, callback);
    }


    /**
     * 获取byte流
     * @param inputStream
     * @return
     */
    private byte[] inputStreamToByte(InputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            LOGGER.error("image inputStream to byte error", e);
            throw new FdfsUploadImageException("upload ThumbImage error", e.getCause());
        }
    }

    /**
     * 检查是否有MateData
     * @param metaDataSet
     * @return
     */
    private boolean hasMateData(Set<MateData> metaDataSet) {
        return null != metaDataSet && !metaDataSet.isEmpty();
    }

    /**
     * 是否是支持的图片文件
     * @param fileExtName
     * @return
     */
    private boolean isSupportImage(String fileExtName) {
        return SUPPORT_IMAGE_LIST.contains(fileExtName.toUpperCase());
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
        if (hasMateData(metaDataSet)) {
            StorageSetMetadataCommand setMDCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                    metaDataSet, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            connectionManager.executeFdfsCmd(client.getInetSocketAddress(), setMDCommand);
        }
        return path;
    }

    /**
     * 上传缩略图
     * @param client
     * @param inputStream
     * @param masterFilePath
     * @param fileExtName
     */
    private void uploadThumbImage(StorageNode client, InputStream inputStream, String masterFilePath,
                                  String fileExtName) {
        ByteArrayInputStream thumbImageStream = null;
        try {
            thumbImageStream = getThumbImageStream(inputStream);// getFileInputStream
            // 获取文件大小
            long fileSize = thumbImageStream.available();
            // 获取缩略图前缀
            String prefixName = thumbImageConfig.getSuffixName();
            StorageUploadSlaveFileCommand command = new StorageUploadSlaveFileCommand(thumbImageStream, fileSize,
                    masterFilePath, prefixName, fileExtName);
            connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);

        } catch (IOException e) {
            LOGGER.error("upload ThumbImage error", e);
            throw new FdfsUploadImageException("upload ThumbImage error", e.getCause());
        } finally {
            IOUtils.closeQuietly(thumbImageStream);
        }
    }

    /**
     * 获取缩略图
     * @param inputStream 源图信息
     * @return
     * @throws IOException
     */
    private ByteArrayInputStream getThumbImageStream(InputStream inputStream) throws IOException {
        // 在内存当中生成缩略图
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //@formatter:off
        Thumbnails
                .of(inputStream)
                .size(thumbImageConfig.getWidth(), thumbImageConfig.getHeight())
                .toOutputStream(out);
        //@formatter:on
        return new ByteArrayInputStream(out.toByteArray());
    }

}
