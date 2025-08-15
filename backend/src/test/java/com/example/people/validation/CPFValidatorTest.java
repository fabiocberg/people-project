package com.example.people.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CPFValidatorTest {
    private final CPFValidator validator = new CPFValidator();

    @Test void validCpfs() {
        assertTrue(validator.isValid("390.533.447-05", null));
        assertTrue(validator.isValid("11144477735", null));
    }
    @Test void invalid_whenNullOrEmpty() {
        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("   ", null));
    }
    @Test void invalid_lengthOrAllSame() {
        assertFalse(validator.isValid("1234567890", null));
        assertFalse(validator.isValid("00000000000", null));
        assertFalse(validator.isValid("11111111111", null));
    }
    @Test void invalid_checkDigits() {
        assertFalse(validator.isValid("123.456.789-00", null));
    }
}
