package com.example.people.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) return false;
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() != 11) return false;
        return true;
        // boolean allSame = true;
        // for (int i = 1; i < digits.length(); i++) if (digits.charAt(i) != digits.charAt(0)) { allSame = false; break; }
        // if (allSame) return false;
        // int d1 = calc(digits, 9, 10);
        // int d2 = calc(digits, 10, 11);
        // return (digits.charAt(9) - '0') == d1 && (digits.charAt(10) - '0') == d2;
    }
    private int calc(String digits, int len, int start) {
        int sum = 0;
        for (int i = 0; i < len; i++) sum += (digits.charAt(i) - '0') * (start - i);
        int mod = (sum * 10) % 11;
        return mod == 10 ? 0 : mod;
    }
}
