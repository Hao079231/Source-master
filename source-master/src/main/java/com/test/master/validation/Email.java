package com.test.master.validation;

import com.test.master.constant.WinWinConstant;
import com.test.master.validation.impl.EmailValidation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidation.class)
@Documented
public @interface Email {
  boolean allowNull() default false;

  String pattern() default WinWinConstant.EMAIL_PATTERN;

  String message() default "Please enter a valid email address in the correct format, such as test@example.com";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
