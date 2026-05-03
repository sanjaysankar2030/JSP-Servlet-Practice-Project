package com.myapp.util;

import java.util.UUID;

/**
 * TokenGenerator.java
 * --------------------
 * Generates a unique, random token for email verification.
 *
 * HOW IT WORKS:
 *   UUID (Universally Unique Identifier) generates a random string like:
 *   "550e8400-e29b-41d4-a716-446655440000"
 *   This is very hard to guess, making it safe for email verification.
 */
public class TokenGenerator {

    /**
     * Returns a random UUID string to be used as a verification token.
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
