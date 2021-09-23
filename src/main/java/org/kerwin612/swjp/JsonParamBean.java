package org.kerwin612.swjp;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import net.minidev.json.JSONUtil;

/**
 * Author: kerwin612@qq.com
 */
public class JsonParamBean {

    private String name;

    private boolean required;

    private String defaultValue;

    private Class clazz;

    public JsonParamBean() {
    }

    public JsonParamBean(String name, boolean required, String defaultValue, Class clazz) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getValue(String json) {
        try {
            Object value = null;
            try {
                value = JsonPath.parse(json).read(getName(), getClazz());
            } catch (PathNotFoundException e) {
            }
            if (value != null) return value;
            if (isRequired()) throw new RuntimeException(String.format("param {%s} is required.", getName()));
            String defaultValue = getDefaultValue();
            if (defaultValue == null || "".equals(defaultValue.trim())) return null;
            if (Boolean.class == clazz) return Boolean.parseBoolean(defaultValue);
            if (Byte.class == clazz) return Byte.parseByte(defaultValue);
            if (Short.class == clazz) return Short.parseShort(defaultValue);
            if (Integer.class == clazz) return Integer.parseInt(defaultValue);
            if (Long.class == clazz) return Long.parseLong(defaultValue);
            if (Float.class == clazz) return Float.parseFloat(defaultValue);
            if (Double.class == clazz) return Double.parseDouble(defaultValue);
            return JSONUtil.convertToX(defaultValue, getClazz());
        } catch (Exception e) {
            throw new RuntimeException(String.format("param {%s} format error: %s", getName(), e.getMessage()), e);
        }
    }

}
