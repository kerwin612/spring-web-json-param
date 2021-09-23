package org.kerwin612.swjp;

import com.google.common.io.ByteStreams;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Author: kerwin612@qq.com
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JsonParamConfigForWebMvc extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new HandlerMethodArgumentResolverForMvc());
    }

    class HandlerMethodArgumentResolverForMvc implements HandlerMethodArgumentResolver {

        private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

        @Override
        public boolean supportsParameter(MethodParameter methodParameter) {
            return JsonParamResolver.supportsParameter(methodParameter);
        }

        public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
            return JsonParamResolver.resolveArgument(methodParameter).getValue(getRequestBody(nativeWebRequest));
        }

        private String getRequestBody(NativeWebRequest webRequest) {
            HttpServletRequest servletRequest = webRequest
                    .getNativeRequest(HttpServletRequest.class);
            String jsonBody = (String) servletRequest
                    .getAttribute(JSONBODYATTRIBUTE);
            if (jsonBody == null) {
                try {
                    String body = new String(ByteStreams
                            .toByteArray(servletRequest.getInputStream()));
                    servletRequest.setAttribute(JSONBODYATTRIBUTE, body);
                    return body;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return jsonBody;
        }

    }

}
