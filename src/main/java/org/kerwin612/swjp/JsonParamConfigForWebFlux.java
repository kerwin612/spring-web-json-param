package org.kerwin612.swjp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.ui.Model;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.AbstractMessageReaderArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: kerwin612@qq.com
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class JsonParamConfigForWebFlux implements WebFluxConfigurer {

    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new HandlerMethodArgumentResolverForFlux());
    }

    class HandlerMethodArgumentResolverForFlux extends AbstractMessageReaderArgumentResolver {

        private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

        public HandlerMethodArgumentResolverForFlux() {
            super(ServerCodecConfigurer.create().getReaders(), ReactiveAdapterRegistry.getSharedInstance());
        }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return JsonParamResolver.supportsParameter(parameter);
        }

        public Mono<Object> resolveArgument(MethodParameter methodParameter, BindingContext bindingContext, ServerWebExchange serverWebExchange) {
            JsonParamBean jsonParamBean = JsonParamResolver.resolveArgument(methodParameter);
            Model model = bindingContext.getModel();
            return Mono.defer(() -> {
                if (model.asMap().containsKey(JSONBODYATTRIBUTE)) {
                    return Mono.create(
                            (callback -> ((Map<JsonParamBean, MonoSink<Object>>) model.asMap().get(JSONBODYATTRIBUTE)).put(jsonParamBean, callback))
                    );
                }
                model.addAttribute(JSONBODYATTRIBUTE, new HashMap<JsonParamBean, MonoSink<Object>>());
                return readBody(methodParameter, false, bindingContext, serverWebExchange).map(json -> {
                    ((Map<JsonParamBean, MonoSink<Object>>) model.asMap().get(JSONBODYATTRIBUTE)).forEach(
                            (JsonParamBean bean, MonoSink<Object> callback) -> callback.success(bean.getValue((String) json))
                    );
                    model.asMap().remove(JSONBODYATTRIBUTE);
                    return jsonParamBean.getValue((String) json);
                });
            });
        }

    }


}
