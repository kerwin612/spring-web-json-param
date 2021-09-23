package org.kerwin612.swjp;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: kerwin612@qq.com
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({JsonParamConfigForWebMvc.class, JsonParamConfigForWebFlux.class})
public @interface EnableJsonParam {
}
