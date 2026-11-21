package org.vitalii.fedyk.minio.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation to specify a required HTTP header for a method. */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredHeader {
  /** The name of the required HTTP header. */
  String value();
}
