package net.sudot.fdfs.domain;

import net.sudot.fdfs.util.Validate;


/**
 * 缩略图配置参数
 * @author tobato
 * Update by sudot on 2017-03-17 0017.
 */
public class DefaultThumbImageConfig implements ThumbImageConfig {

    private static String cachedSuffixName;
    private int width;
    private int height;

    /**
     * 生成后缀如:_150x150
     */
    @Override
    public String getSuffixName() {
        if (cachedSuffixName == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("_").append(width).append("x").append(height);
            cachedSuffixName = new String(builder);
        }
        return cachedSuffixName;
    }

    /**
     * 根据文件名获取缩略图路径
     */
    @Override
    public String getThumbImagePath(String masterFilePath) {
        Validate.notBlank(masterFilePath, "主文件不能为空");
        StringBuilder buff = new StringBuilder(masterFilePath);
        int index = buff.lastIndexOf(".");
        buff.insert(index, getSuffixName());
        return buff.toString();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
