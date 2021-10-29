package de.paetz.feuerwehr.informationdashboard.security.password;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    // SecureRandom() constructs a secure random number generator (RNG) implementing the default random number algorithm.
    private final SecureRandom crunchifyRandomNo = new SecureRandom();

    private final List<Character> crunchifyValueObj;

    // Just initialize ArrayList crunchifyValueObj and add ASCII Decimal Values
    public PasswordGenerator() {

        crunchifyValueObj = new ArrayList<>();

        // Adding ASCII Decimal value between 33 and 53
        for (int i = 33; i < 53; i++) {
            crunchifyValueObj.add((char) i);
        }

        // Adding ASCII Decimal value between 54 and 85
        for (int i = 54; i < 85; i++) {
            crunchifyValueObj.add((char) i);
        }

        // Adding ASCII Decimal value between 86 and 128
        for (int i = 86; i < 127; i++) {
            crunchifyValueObj.add((char) i);
        }

        // crunchifyValueObj.add((char) 64);

        // rotate() rotates the elements in the specified list by the specified distance. This will create strong password
        // Totally optional
        Collections.rotate(crunchifyValueObj, 5);
    }
    // Get Char value from above added Decimal values
    // Enable Logging below if you want to debug
    public char crunchifyGetRandom() {

        char crunchifyChar = this.crunchifyValueObj.get(crunchifyRandomNo.nextInt(this.crunchifyValueObj.size()));

        // log(String.valueOf(crunchifyChar));
        return crunchifyChar;
    }

    public String getPassword(int pLength) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        for (int length = 0; length < pLength; length++) {
            crunchifyBuilder.append(this.crunchifyGetRandom());
        }
        return crunchifyBuilder.toString();
    }
}
