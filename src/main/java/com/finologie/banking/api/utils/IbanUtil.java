package com.finologie.banking.api.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class IbanUtil {


    private static final SecureRandom random = new SecureRandom();
    public static final String BANK_CODE = "456";

    public static String generateLuxembourgIban() {
        String countryCode = "LU";
        String checkDigits = "00"; // Temporary check digits to calculate the final ones
        String bankCode = BANK_CODE;
        String accountNumber = RandomStringUtils.randomNumeric(13);

        String bban = bankCode + accountNumber;
        String ibanWithoutCheckDigits = countryCode + checkDigits + bban;

        checkDigits = calculateCheckDigits(ibanWithoutCheckDigits);

        return countryCode + checkDigits + bban;
    }


    private static String calculateCheckDigits(String iban) {
        String rearrangedIban = iban.substring(4) + iban.substring(0, 4);
        String numericIban = convertToNumeric(rearrangedIban);
        BigInteger ibanNumber = new BigInteger(numericIban);
        BigInteger mod97 = ibanNumber.mod(BigInteger.valueOf(97));
        int checkDigitsValue = 98 - mod97.intValue();
        return String.format("%02d", checkDigitsValue);
    }

    private static String convertToNumeric(String iban) {
        StringBuilder numericIban = new StringBuilder();
        for (char ch : iban.toCharArray()) {
            int numericValue;
            if (Character.isDigit(ch)) {
                numericValue = Character.getNumericValue(ch);
            } else {
                numericValue = 10 + (ch - 'A');
            }
            numericIban.append(numericValue);
        }
        return numericIban.toString();
    }
}
