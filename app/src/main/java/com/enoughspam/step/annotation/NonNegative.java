package com.enoughspam.step.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 23:56
 *
 * Denotes that a parameter, field or method return value can't be a negative number.
 */

@Target({METHOD, PARAMETER, FIELD})
public @interface NonNegative {
}
