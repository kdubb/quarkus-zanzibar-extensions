package io.quarkiverse.zanzibar.jaxrs.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface ObjectQuery {

    enum Source {
        PATH,
        QUERY,
        HEADER,
        REQUEST
    }

    Source source() default Source.PATH;

    String sourceProperty();

    String type();

}
