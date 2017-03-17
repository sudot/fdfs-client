package net.sudot.fdfs.domain;

/**
 * 缩略图生成配置支持
 * @author tobato
 * Update by sudot on 2017-03-17 0017.
 */
public interface ThumbImageConfig {

    /**
     * 获得缩略图宽
     * @return
     */
    int getWidth();

    /**
     * 获得缩略图高
     * @return
     */
    int getHeight();

    /**
     * 获得缩略图后缀
     * @return
     */
    String getSuffixName();

    /**
     * 获得缩略图路径
     * @param masterFilePath
     * @return
     */
    String getThumbImagePath(String masterFilePath);

}
