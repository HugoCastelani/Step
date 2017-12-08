package com.hugocastelani.blockbook.annotation;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 23:56
 *
 * Denotes that a parameter, field or method return value can't be a negative number.
 */

@Target({METHOD, PARAMETER, FIELD})
@IntRange(from = 0, to = Integer.MAX_VALUE)
@FloatRange(from = 0, to = Float.MAX_VALUE)
public @interface NonNegative {
}
