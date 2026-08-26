package utilities;

import net.datafaker.Faker;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String firstName() {
        return faker.name().firstName();
    }

    public static String lastName() {
        return faker.name().lastName();
    }

    public static String address() {
        return faker.address().streetAddress();
    }

    public static String city() {
        return faker.address().city();
    }

    public static String phoneNumber() {
        return faker.phoneNumber().subscriberNumber(10);
    }
}
