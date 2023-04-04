package com.project.travelbooking.helper;

import org.mindrot.jbcrypt.BCrypt;

public class HashHelper {
    public static String GeneratePasswordHash(String inputPassword) {
        String generatedSecuredPasswordHash = BCrypt.hashpw(inputPassword, BCrypt.gensalt(12));
        return generatedSecuredPasswordHash;
    }

    public  static  boolean CheckCorrectPassword(String hashedPassword, String inputPassword){
        boolean matched = BCrypt.checkpw(inputPassword, hashedPassword);
        return matched;
    }
}
