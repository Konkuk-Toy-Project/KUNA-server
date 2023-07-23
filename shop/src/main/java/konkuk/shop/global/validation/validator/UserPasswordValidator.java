package konkuk.shop.global.validation.validator;

import konkuk.shop.global.validation.annotation.UserPasswordValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class UserPasswordValidator implements ConstraintValidator<UserPasswordValid, String> {

    private String pattern;

    @Override
    public void initialize(UserPasswordValid constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.matches(pattern);
    }
}