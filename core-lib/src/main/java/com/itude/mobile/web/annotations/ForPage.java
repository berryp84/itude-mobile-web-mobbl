package com.itude.mobile.web.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Target(value = {METHOD, TYPE, FIELD, PARAMETER})
@Qualifier
@Retention(RUNTIME)
public @interface ForPage {
  /** The name of the page. */
  String value() default "";

}
