package net.devstudy.resume.annotation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Size(min = 8)
@NotNull
@MinDigitCount
@MinLowerCharCount
@MinUpperCharCount
@MinSpecCharCount
@EnglishLanguage(withPunctuations = false)
@ReportAsSingleViolation
public @interface PasswordStrength {
	String message() default "PasswordStrength";

	Class<? extends Payload>[] payload() default {};

	Class<?>[] groups() default {};
}
