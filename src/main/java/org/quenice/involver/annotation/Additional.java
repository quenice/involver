package org.quenice.involver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 附加的数据。
 * 作用于使用者自行定义的业务接口的方法参数，用于传输个性化数据。
 * 使用场景：
 * 当需要根据不同数据采用不同的加密密钥时，可以在调用业务接口方法时，把密钥作为参数传递，
 * 在{@link org.quenice.involver.handler.CodecHandler} 中通过{@link org.quenice.involver.entity.ExposedConfig} 拿到密钥进行操作
 *
 * @author damon.qiu 12/24/20 2:26 PM
 * @since 1.0.0
 * @see org.quenice.involver.handler.CodecHandler
 * @see org.quenice.involver.entity.ExposedConfig
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Additional {
}
