import AddAndPing.Ping;
import AddAndPing.PingAndAddMirror;

import java.util.Scanner;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        File folder = new File(".");
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".bat") && !file.getName().equals("start.bat")) {
                    if (file.delete()) {

                    } else {

                    }
                }
            }
        }

        CheckUpdates.execute();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(" ====================");
            System.out.println(" 1 - Указать самый лучший сервер (изменение файла hosts, указание самого ближайшего Proxy-сервера)");
            System.out.println(" 2 - Удалить самый лучший сервер (изменение файла hosts, удаление указанного самого ближайшего Proxy-сервера)");
            System.out.println(" 3 - Посмотреть статус самого лучшего сервера (просмотр файла hosts)");
            System.out.println(" 4 - Тестовая проверка серверов (+ оригинальный сервер)");
            System.out.println(" ====================");
            System.out.print(" Введите цифру действия: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Чтобы очистить буфер после nextInt()

                switch (choice) {
                    case 1:
                        System.out.println("OK, начинаю.");
                        PingAndAddMirror.execute();
                        break;
                    case 2:
                        System.out.println("OK, начинаю.");
                        RemoveSRV.execute();
                        break;
                    case 3:
                        System.out.println("OK, начинаю.");
                        ViewSRV.execute();
                        break;
                    case 4:
                        System.out.println("OK, начинаю.");
                        Ping.execute();
                        break;
                    default:
                        System.out.println(" Ошибка: неверный ввод");
                }
            } else {
                System.out.println(" Ошибка: неверный ввод");
                scanner.nextLine(); // Очистка буфера после неверного ввода
            }
        }
    }
}