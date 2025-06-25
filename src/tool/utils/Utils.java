package tool.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;

public final class Utils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateFileName() {
        return new BigInteger(130, SECURE_RANDOM).toString(16).substring(0, 16);
    }

    public static void execute(String cmd) {
        try {
            // Создание bat-файла
            final String filename = Utils.generateFileName();
            final var batFile = new File(filename + ".bat");

            // Запись содержимого в bat-файл
            final var fileWriter = new FileWriter(batFile);
            final var bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(cmd);
            bufferedWriter.close();

            // Запуск bat-файла
            System.out.println("Очищаю hosts! Подтвердите права Администратора.");

            final var processBuilder = new ProcessBuilder("cmd.exe", "/c", filename + ".bat");
            processBuilder.directory(new File(System.getProperty("user.dir")));

            // Ждем завершения процесса
            int exitCode = processBuilder.start().waitFor();
        } catch (Throwable e) {
            System.out.println("Произошла ошибка при создании или удаления временного файла: " + e.getMessage());
        }
    }
}
