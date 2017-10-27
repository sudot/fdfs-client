package net.sudot.fdfs.proto.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 映射对象元数据
 *
 * <pre>
 * 映射对象元数据必须由{@code @FdfsColumn}注解
 * </pre>
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class ObjectMetaData {
    /** 映射对象类名 */
    private String className;

    /** 映射列(全部) */
    private List<FieldMetaData> fieldList = new ArrayList<FieldMetaData>();

    /** 动态计算列(部分)fieldList包含dynamicFieldList */
    private List<FieldMetaData> dynamicFieldList = new ArrayList<FieldMetaData>();

    /** FieldsTotalSize */
    private int fieldsTotalSize = 0;

    /**
     * 映射对象元数据构造函数
     * @param genericType
     */
    public <T> ObjectMetaData(Class<T> genericType) {
        // 获得对象类名
        this.className = genericType.getName();
        this.fieldList = praseFieldList(genericType);
        // 校验映射定义
        validatFieldListDefine();
    }

    public String getClassName() {
        return className;
    }

    public List<FieldMetaData> getFieldList() {
        return Collections.unmodifiableList(fieldList);
    }

    /**
     * 解析映射对象数据映射情况
     * <pre>
     * // 原代码(包含隐藏BUG,实体中定义的列顺序和FdfsColumn.index标记顺序不一致,会导致参数错误)
     * Field[] fields = genericType.getDeclaredFields();
     * List<FieldMetaData> mapedFieldList = new ArrayList<FieldMetaData>();
     * for (int i = 0; i < fields.length; i++) {
     *     if (fields[i].isAnnotationPresent(FdfsColumn.class)) {
     *         FieldMetaData fieldMetaData = new FieldMetaData(fields[i], fieldsTotalSize);
     *         mapedFieldList.add(fieldMetaData);
     *         // 计算偏移量
     *         fieldsTotalSize += fieldMetaData.getRealSize();
     *         // 如果是动态计算列
     *         if (fieldMateData.isDynamicField()) {
     *             dynamicFieldList.add(fieldMetaData);
     *         }
     *     }
     * }
     * </pre>
     *
     * @return
     */
    private <T> List<FieldMetaData> praseFieldList(Class<T> genericType) {
        Field[] fields = genericType.getDeclaredFields();
        FieldMetaData[] mateFieldArrays = new FieldMetaData[fields.length];
        for (Field field : fields) {
            FdfsColumn annotation = field.getAnnotation(FdfsColumn.class);
            if (annotation == null) { continue; }
            FieldMetaData fieldMetaData = new FieldMetaData(field, fieldsTotalSize);
            mateFieldArrays[annotation.index()] = fieldMetaData;
            // 计算偏移量
            fieldsTotalSize += fieldMetaData.getRealSize();
        }
        List<FieldMetaData> mateFieldList = new ArrayList<FieldMetaData>();
        for (FieldMetaData fieldMetaData : mateFieldArrays) {
            if (fieldMetaData == null) { continue; }
            mateFieldList.add(fieldMetaData);
            // 如果是动态计算列
            if (fieldMetaData.isDynamicField()) {
                dynamicFieldList.add(fieldMetaData);
            }
        }
        return mateFieldList;
    }

    /**
     * 检查数据列定义
     *
     * <pre>
     * 为了减少编码的错误，检查数据列定义是否存在列名相同或者索引定义相同(多个大于0相同的)的
     * </pre>
     */
    private void validatFieldListDefine() {
        for (FieldMetaData field : fieldList) {
            validateFieldItemDefineByIndex(field);
        }
    }

    /**
     * 检查按索引映射
     * @param field
     */
    private void validateFieldItemDefineByIndex(FieldMetaData field) {
        for (FieldMetaData otherField : fieldList) {
            if (!field.equals(otherField) && (field.getIndex() == otherField.getIndex())) {
                StringBuilder builder = new StringBuilder();
                builder.append("在类[").append(className).append("]映射定义中.[");
                builder.append(field.getFieldName()).append("]与[").append(otherField.getFieldName());
                builder.append("]索引定义相同为[").append(field.getIndex()).append("]");
                throw new FdfsColumnMapException(builder.toString());
            }
        }
    }

    /**
     * 是否有动态数据列
     * @return
     */
    private boolean hasDynamicField() {
        for (FieldMetaData field : fieldList) {
            if (field.isDynamicField()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取动态数据列长度
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private int getDynamicFieldSize(Object obj, Charset charset)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int size = 0;
        for (FieldMetaData field : dynamicFieldList) {
            size = size + field.getDynamicFieldByteSize(obj, charset);
        }
        return size;
    }

    /**
     * 获取固定参数对象总长度
     * @return
     */
    public int getFieldsFixTotalSize() {
        if (hasDynamicField()) {
            throw new FdfsColumnMapException(
                    className + " class hasDynamicField, unsupported operator getFieldsTotalSize");
        }
        return fieldsTotalSize;
    }

    /**
     * 获取需要发送的报文长度
     * @param bean
     * @param charset
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public int getFieldsSendTotalByteSize(Object bean, Charset charset) {
        if (!hasDynamicField()) {
            return fieldsTotalSize;
        } else {
            return getDynamicTotalFieldSize(bean, charset);
        }
    }

    /**
     * 获取动态属性长度
     * @param bean
     * @param charset
     * @return
     */
    private int getDynamicTotalFieldSize(Object bean, Charset charset) {
        try {
            int dynamicFieldSize = getDynamicFieldSize(bean, charset);
            return fieldsTotalSize + dynamicFieldSize;
        } catch (NoSuchMethodException e) {
            throw new FdfsColumnMapException("Cannot invoke get method", e);
        } catch (IllegalAccessException e) {
            throw new FdfsColumnMapException("Illegal access", e);
        } catch (InvocationTargetException e) {
            throw new FdfsColumnMapException("Cannot invoke method", e);
        }
    }

}
