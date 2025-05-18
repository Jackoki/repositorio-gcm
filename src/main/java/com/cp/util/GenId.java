package com.cp.util;

import java.util.Locale;
import java.util.Random;
import net.datafaker.Faker;

public class GenId {

    private static final Random RANDOM = new Random();
    private static final Faker FAKER = new Faker(new Locale("pt_BR"));

    public int getIdPrimo() {
        int id;
        do {
            id = RANDOM.nextInt(Integer.MAX_VALUE);
        } 
        
        while (!isPrime(id));

        AppLog.getInstance().info("Número primo gerado com sucesso!");
        return id;
    }

    public String getNome() {
        return FAKER.name().fullName();
    }

    private static boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;

        int limit = (int) Math.sqrt(number);
        for (int i = 5; i <= limit; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }
        return true;
    }
}
