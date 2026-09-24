package com.example.hotel;

import java.math.BigDecimal;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PaymentService {

    public String encryptCard(String cardNumber) {
        try {
            SecretKeySpec key = new SecretKeySpec("hotel1234567890a".getBytes(), "AES");
            byte[] iv = "1234567890abcdef".getBytes();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(cardNumber.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal serviceCharge(double amount) {
        BigDecimal rate = new BigDecimal(0.1);
        return rate.multiply(BigDecimal.valueOf(amount));
    }

    public BigDecimal tax(double amount) {
        BigDecimal rate = new BigDecimal(0.18);
        return rate.multiply(BigDecimal.valueOf(amount));
    }

    public double total(double amount, String method) {
        double total = amount + serviceCharge(amount).doubleValue() + tax(amount).doubleValue();
        switch (method) {
            case "CARD":
                total = total * 1.02;
            case "CASH":
                total = Math.round(total);
                break;
            case "BANK":
                total = total + 100;
                break;
        }
        return total;
    }
}
