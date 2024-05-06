package org.okten.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {

    @Value("${reference-data.categories:}#{T(java.util.Collections).emptyList()}")
    private List<String> categories;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return categories.contains(value);
    }
}
