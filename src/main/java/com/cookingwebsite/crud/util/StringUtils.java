package com.cookingwebsite.crud.util;

import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils {
	
	/**
	 * @param length
	 * @return String
	 */
	public static String randomString(int length) {
		// Character bank
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-|@#&$*_";
		// String in which we will add a random character
		String characterString = "";
		for (int x = 0; x < length; x++) {
			int indexCharacter = randomNumberInRange(0, characters.length() - 1);
			char randomCharacter = characters.charAt(indexCharacter);
			characterString += randomCharacter;
		}
		return characterString;
	}
	
	/**
	 * @param minimum
	 * @param maximum
	 * @return int
	 */
	public static int randomNumberInRange(int minimum, int maximum) {
		// nextInt returns in range but with an exclusive upper limit, so we add
		// 1
		return ThreadLocalRandom.current().nextInt(minimum, maximum + 1);
	}
}
