package constants;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    public static String randomEmail = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    public static String randomPassword = RandomStringUtils.randomNumeric(5);
    public static String randomName = RandomStringUtils.randomAlphabetic(10);
}
