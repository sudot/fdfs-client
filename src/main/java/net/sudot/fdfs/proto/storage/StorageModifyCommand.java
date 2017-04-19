package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.internal.StorageModifyRequest;

import java.io.InputStream;

/**
 * 文件修改命令
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageModifyCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件修改命令
     * <pre>
     *     1.【替换内容的输入流】大小不得超过【替换内容的大小】,否则会引起IO异常:socket io exception occured while sending cmd
     *     2.【替换内容的大小】必须为正整数(大于零的整数)
     *     3.【文件修改的起始位置】必须为自然数(大于等于零的整数)
     * </pre>
     * @param path         需要修改的文件路径(不含组名)
     * @param inputStream  替换内容的输入流
     * @param modifySize   替换内容的大小(其大小不可大于输入流实际大小)
     * @param modifyOffset 文件修改的起始位置(原文件内容索引值,从0开始计数)
     */
    public StorageModifyCommand(String path, InputStream inputStream, long modifySize, long modifyOffset) {
        super();
        if (modifySize <= 0L) { throw new IllegalArgumentException("[替换内容的大小]必须为正整数.当前值:" + modifySize); }
        if (modifyOffset < 0L) { throw new IllegalArgumentException("[文件修改的起始位置]必须为自然数.当前值:" + modifyOffset); }
        this.request = new StorageModifyRequest(inputStream, modifySize, path, modifyOffset);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
