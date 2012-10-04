package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Andrew Post
 */
public class SuperUserDefaultPasswordGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(StringUtil.md5("defaultpassword"));
    }
}
