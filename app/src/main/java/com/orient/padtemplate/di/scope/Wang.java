package com.orient.padtemplate.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;
import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Author WangJie
 * Created on 2018/9/3.
 */
@Qualifier
@Retention(RUNTIME)
public @interface Wang {}
