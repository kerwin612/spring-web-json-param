package org.kerwin612.swjp;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * Author: kerwin612@qq.com
 */
public final class JsonParamResolver {

    public static boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    public static JsonParamBean resolveArgument(MethodParameter parameter) {
        JsonParam parameterAnnotation = parameter.getParameterAnnotation(JsonParam.class);
        if (parameterAnnotation == null) {
            Annotation[] parameterAnnotations = parameter.getParameterAnnotations();
            for (Annotation annotation : parameterAnnotations) {
                if (JsonParam.class.equals(annotation.annotationType())) {
                    parameterAnnotation = (JsonParam) annotation;
                    break;
                }
            }
        }
        if (parameterAnnotation == null) return null;
        String value = parameterAnnotation.value();
        if (StringUtils.isEmpty(value)) {
            value = parameter.getParameterName();
        }
        return new JsonParamBean(value, parameterAnnotation.required(), parameterAnnotation.defaultValue(), parameter.getParameterType());
    }

}
