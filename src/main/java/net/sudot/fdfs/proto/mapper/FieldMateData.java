package net.sudot.fdfs.proto.mapper;

import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.proto.OtherConstants;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.Set;

/**
 * 属性映射MateData定义
 * @author tobato
 */
class FieldMateData {

    /** 动态属性类型 */
    DynamicFieldType dynamicFieldType;
    /** 列 */
    private Field field;
    /** 列索引 */
    private int index;
    /** 单元最大长度 */
    private int max;
    /** 单元长度 */
    private int size;
    /** 列偏移量 */
    private int offsize;

    /**
     * 构造函数
     * @param mapedfield
     * @param offsize
     */
    public FieldMateData(Field mapedfield, int offsize) {
        FdfsColumn column = mapedfield.getAnnotation(FdfsColumn.class);
        this.field = mapedfield;
        this.index = column.index();
        this.max = column.max();
        this.size = getFieldSize(field);
        this.offsize = offsize;
        this.dynamicFieldType = column.dynamicField();
        // 如果强制设置了最大值，以最大值为准
        if (this.max > 0 && this.size > this.max) {
            this.size = this.max;
        }
    }

    /**
     * 获取Field大小
     * @param field
     * @return
     */
    private int getFieldSize(Field field) {//
        Class<?> fieldType = field.getType();
        if (String.class == fieldType) {
            return this.max;
        } else if (long.class == fieldType) {
            return OtherConstants.FDFS_PROTO_PKG_LEN_SIZE;
        } else if (int.class == fieldType) {
            return OtherConstants.FDFS_PROTO_PKG_LEN_SIZE;
        } else if (java.util.Date.class == fieldType) {
            return OtherConstants.FDFS_PROTO_PKG_LEN_SIZE;
        } else if (byte.class == fieldType) {
            return 1;
        } else if (boolean.class == fieldType) {
            return 1;
        } else if (Set.class == fieldType) {
            return 0;
        }
        throw new FdfsColumnMapException(field.getName() + "获取Field大小时未识别的FdfsColumn类型" + fieldType);
    }

    /**
     * 获取值
     * @param bs
     * @return
     */
    public Object getValue(byte[] bs, Charset charset) {
        if (String.class == field.getType()) {
            if (isDynamicField()) {
                return (new String(bs, offsize, bs.length - offsize, charset)).trim();
            }
            return (new String(bs, offsize, size, charset)).trim();
        } else if (long.class == field.getType()) {
            return BytesUtil.buff2long(bs, offsize);
        } else if (int.class == field.getType()) {
            return (int) BytesUtil.buff2long(bs, offsize);
        } else if (java.util.Date.class == field.getType()) {
            return new Date(BytesUtil.buff2long(bs, offsize) * 1000);
        } else if (byte.class == field.getType()) {
            return bs[offsize];
        } else if (boolean.class == field.getType()) {
            return bs[offsize] != 0;
        }
        throw new FdfsColumnMapException(field.getName() + "获取值时未识别的FdfsColumn类型" + field.getType());
    }

    public Field getField() {
        return field;
    }

    public int getIndex() {
        return index;
    }

    public int getMax() {
        return max;
    }

    public String getFieldName() {
        return field.getName();
    }

    public int getSize() {
        return size;
    }

    /**
     * 获取真实属性
     * @return
     */
    public int getRealeSize() {
        // 如果是动态属性
        if (isDynamicField()) {
            return 0;
        }
        return size;
    }

    public int getOffsize() {
        return offsize;
    }

    @Override
    public String toString() {
        return "FieldMateData [field=" + getFieldName() + ", index=" + index + ", max=" + max + ", size=" + size
                + ", offsize=" + offsize + "]";
    }

    /**
     * 将属性值转换为byte
     * @param charset
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public byte[] toByte(Object bean, Charset charset)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object value = this.getFieldValue(bean);
        if (isDynamicField()) {
            return getDynamicFieldByteValue(value, charset);
        } else if (String.class.equals(field.getType())) {
            // 如果是动态属性
            return BytesUtil.objString2Byte((String) value, max, charset);
        } else if (long.class == field.getType() || Long.class == field.getType()) {
            return BytesUtil.long2buff((Long) value);
        } else if (int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
            return BytesUtil.long2buff((Integer) value);
        } else if (Date.class.equals(field.getType())) {
            throw new FdfsColumnMapException("Date 还不支持");
        } else if (byte.class == field.getType() || Byte.class == field.getType()) {
            byte[] result = new byte[1];
            result[0] = (Byte) value;
            return result;
        } else if (boolean.class.equals(field.getType())) {
            throw new FdfsColumnMapException("boolean 还不支持");
        }
        throw new FdfsColumnMapException("将属性值转换为byte时未识别的FdfsColumn类型" + field.getName() + ":" + field.getType());
    }

    /**
     * 获取动态属性值
     * @param value
     * @param charset
     * @return
     */
    @SuppressWarnings("unchecked")
    private byte[] getDynamicFieldByteValue(Object value, Charset charset) {
        switch (dynamicFieldType) {
            // 如果是打包剩余的所有Byte
            case allRestByte:
                return BytesUtil.objString2Byte((String) value, charset);
            // 如果是文件matedata
            case matedata:
                return MetadataMapper.toByte((Set<MateData>) value, charset);
            default:
                return BytesUtil.objString2Byte((String) value, charset);
        }
    }

    /**
     * 获取单元对应值
     * @param bean
     * @return
     * @throws IllegalAccessException
     */
    private Object getFieldValue(Object bean)
            throws IllegalAccessException {
//        return PropertyUtils.getProperty(bean, field.getName());
        field.setAccessible(true);
        return field.get(bean);
    }

    /**
     * 获取动态属性长度
     * @param bean
     * @param charset
     * @return
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public int getDynamicFieldByteSize(Object bean, Charset charset) throws IllegalAccessException {
        Object value = getFieldValue(bean);
        if (null == value) { return 0; }
        switch (dynamicFieldType) {
            // 如果是打包剩余的所有Byte
            case allRestByte:
                return ((String) value).getBytes(charset).length;
            // 如果是文件matedata
            case matedata:
                return MetadataMapper.toByte((Set<MateData>) value, charset).length;
            default:
                return getFieldSize(field);
        }
    }

    /**
     * 是否动态属性
     * @return
     */
    public boolean isDynamicField() {
        return (!DynamicFieldType.NULL.equals(dynamicFieldType));
    }

}
