package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.mapper.MetaDataMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * 列出分组信息执行结果
 * @author tobato
 */
public class StorageGetMetaDataResponse extends FdfsResponse<Set<MetaData>> {

    /**
     * 解析反馈内容
     */
    @Override
    public Set<MetaData> decodeContent(InputStream in, Charset charset) throws IOException {
        // 解析报文内容
        byte[] bytes = new byte[(int) getContentLength()];
        int contentSize = in.read(bytes);
        if (contentSize != getContentLength()) {
            throw new IOException("读取到的数据长度与协议长度不符");
        }
        return MetaDataMapper.fromByte(bytes, charset);

    }

}
