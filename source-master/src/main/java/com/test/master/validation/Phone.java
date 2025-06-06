package com.test.master.validation;

import com.test.master.constant.WinWinConstant;
import com.test.master.validation.impl.PhoneValidation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidation.class)
@Documented
public @interface Phone {
  boolean allowNull() default false;

  String pattern() default WinWinConstant.PHONE_PATTERN;

  String message() default "The phone number should begin with 0 and have exactly 10 digits";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
