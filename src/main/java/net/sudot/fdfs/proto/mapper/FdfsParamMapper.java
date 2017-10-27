package net.sudot.fdfs.proto.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * param对象与byte映射器
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class FdfsParamMapper {

    /** 对象映射缓存 */
    private static Map<String, ObjectMetaData> mapCache = new HashMap<String, ObjectMetaData>();
    /** 类属性映射缓存 */
    private static Map<String, Map<String, Field>> fieldCache = new HashMap<String, Map<String, Field>>();

    private FdfsParamMapper() {
        // hide for utils
    }

    /**
     * 将byte解码为对象
     * @param content
     * @param genericType
     * @param charset
     * @param <T>
     * @return
     */
    public static <T> T map(byte[] content, Class<T> genericType, Charset charset) {
        // 获取映射对象
        ObjectMetaData objectMap = getObjectMap(genericType);
        try {
            return mapByIndex(content, genericType, objectMap, charset);
        } catch (InstantiationException e) {
            throw new FdfsColumnMapException(e);
        } catch (IllegalAccessException e) {
            throw new FdfsColumnMapException(e);
        } catch (NoSuchFieldException e) {
            throw new FdfsColumnMapException(e);
        }
    }

    /**
     * 获取对象映射定义
     * @param genericType
     * @return
     */
    public static <T> ObjectMetaData getObjectMap(Class<T> genericType) {
        ObjectMetaData objectMetaData = mapCache.get(genericType.getName());
        if (null == objectMetaData) {
            // 还未缓存过
            objectMetaData = new ObjectMetaData(genericType);
            mapCache.put(genericType.getName(), objectMetaData);
        }
        return objectMetaData;
    }

    /**
     * 获取类属性定义
     * @param genericType 需要获取的属性类
     * @param <T>         类泛型
     * @return 返回该类的属性定义集合
     */
    private static <T> Map<String, Field> getFieldMap(Class<T> genericType) {
        String genericTypeName = genericType.getName();
        Map<String, Field> stringFieldMap = fieldCache.get(genericTypeName);
        if (stringFieldMap == null) {
            Field[] declaredFields = genericType.getDeclaredFields();
            stringFieldMap = new HashMap<>(declaredFields.length);
            for (Field field : declaredFields) {
                field.setAccessible(true);
                stringFieldMap.put(field.getName(), field);
            }
            fieldCache.put(genericTypeName, stringFieldMap);
        }
        return stringFieldMap;
    }

    /**
     * 按列顺序映射
     * @param content
     * @param genericType
     * @param objectMap
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private static <T> T mapByIndex(byte[] content, Class<T> genericType, ObjectMetaData objectMap, Charset charset)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {

        if (genericType == Void.class) { throw new IllegalAccessException("genericType不能为Void"); }
        List<FieldMetaData> mappingFields = objectMap.getFieldList();
        Map<String, Field> fieldMap = getFieldMap(genericType);
        T obj = genericType.newInstance();
        for (FieldMetaData field : mappingFields) {
            Object value = field.getValue(content, charset);
            Field declaredField = fieldMap.get(field.getFieldName());
            declaredField.set(obj, value);
        }
        return obj;
    }

    /**
     * 序列化为Byte
     * @param object
     * @param charset
     * @return
     */
    public static byte[] toByte(Object object, Charset charset) {
        ObjectMetaData objectMap = getObjectMap(object.getClass());
        try {
            return convertFieldToByte(objectMap, object, charset);
        } catch (NoSuchMethodException e) {
            throw new FdfsColumnMapException("Cannot invoke get method", e);
        } catch (IllegalAccessException e) {
            throw new FdfsColumnMapException("Illegal access", e);
        } catch (InvocationTargetException e) {
            throw new FdfsColumnMapException("Cannot invoke method", e);
        }
    }

    /**
     * 将属性转换为byte
     * @param objectMap
     * @param object
     * @param charset
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private static byte[] convertFieldToByte(ObjectMetaData objectMap, Object object, Charset charset)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<FieldMetaData> mappingFields = objectMap.getFieldList();
        // 获取报文长度 (固定长度+动态长度)
        int size = objectMap.getFieldsSendTotalByteSize(object, charset);
        byte[] result = new byte[size];
        int offset = 0;
        for (FieldMetaData metaData : mappingFields) {
            byte[] fieldByte = metaData.toByte(object, charset);
            if (null != fieldByte) {
                System.arraycopy(fieldByte, 0, result, offset, fieldByte.length);
                offset += fieldByte.length;
            }
        }
        return result;
    }

}