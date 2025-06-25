package tool;

import AddAndPing.Ping;
import AddAndPing.PingAndAddMirror;
import tool.utils.Utils;

import java.util.Scanner;

public final class PingTool {

    private final Scanner scanner = new Scanner(System.in);

    public void update() {
        System.out.println(" ====================");
        System.out.println(" 1 - Указать самый лучший сервер (изменение файла hosts, указание самого ближайшего Proxy-сервера)");
        System.out.println(" 2 - Удалить самый лучший сервер (изменение файла hosts, удаление указанного самого ближайшего Proxy-сервера)");
        System.out.println(" 3 - Посмотреть статус самого лучшего сервера (просмотр файла hosts)");
        System.out.println(" 4 - Тестовая проверка серверов (+ оригинальный сервер)");
        System.out.println(" ====================");
        System.out.print(" Введите цифру действия: ");

        if (scanner.hasNextInt()) {
            final int choice = scanner.nextInt();
            scanner.nextLine(); // Чтобы очистить буфер после nextInt()

            switch (choice) {
                case 1:
                    System.out.println("OK, начинаю.");
                    PingAndAddMirror.execute();
                    break;
                case 2:
                    System.out.println("OK, начинаю.");
                    removeSRV();
                    break;
                case 3:
                    System.out.println("OK, начинаю.");
                    viewSRV();
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

    private void removeSRV() {
        Utils.execute(
                """
                        @echo off
                        :: Проверка прав администратора
                        NET FILE 1>NUL 2>NUL
                        if '%errorlevel%' == '0' ( goto gotAdmin ) else ( goto UACPrompt )

                        :UACPrompt
                        :: Запуск скрипта с правами администратора
                        echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\\getadmin.vbs"
                        echo UAC.ShellExecute "%~s0", "", "", "runas", 1 >> "%temp%\\getadmin.vbs"
                        "%temp%\\getadmin.vbs"
                        del /f /q "%temp%\\getadmin.vbs"
                        exit /B

                        :gotAdmin
                        :: Замена IP-адресов зеркал на обычный IP-адрес
                        findstr /V "mc.aresmine.ru mc.aresmine.me play.aresmine.me hot.aresmine.me" "C:\\Windows\\System32\\drivers\\etc\\hosts" > "C:\\Windows\\System32\\drivers\\etc\\hosts.tmp"
                        move /y "C:\\Windows\\System32\\drivers\\etc\\hosts.tmp" "C:\\Windows\\System32\\drivers\\etc\\hosts"
                        """
        );
    }

    public void viewSRV() {
        Utils.execute(
                """
                        @echo off
                        :: Проверка прав Администратора
                        NET FILE 1>NUL 2>NUL
                        if '%errorlevel%' == '0' ( goto gotAdmin ) else ( goto UACPrompt )
                                        
                        :UACPrompt
                        :: Запуск скрипта с правами Администратора
                        echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\\getadmin.vbs"
                        echo UAC.ShellExecute "%~s0", "", "", "runas", 1 >> "%temp%\\getadmin.vbs"
                        "%temp%\\getadmin.vbs"
                        del /f /q "%temp%\\getadmin.vbs"
                        exit /B
                                        
                        :gotAdmin
                        @echo off
                                        
                        set "hostspath=%SystemRoot%\\System32\\drivers\\etc\\hosts"
                                        
                        findstr /C:"mc.aresmine.ru" %hostspath% > nul
                        if %errorlevel% equ 0 (
                            echo String "mc.aresmine.ru" found!
                        ) else (
                            echo String "mc.aresmine.ru" not found!
                        )
                                        
                        findstr /C:"mc.aresmine.me" %hostspath% > nul
                        if %errorlevel% equ 0 (
                            echo String "mc.aresmine.me" found!
                        ) else (
                            echo String "mc.aresmine.me" not found!
                        )
                                        
                        findstr /C:"play.aresmine.me" %hostspath% > nul
                        if %errorlevel% equ 0 (
                            echo String "play.aresmine.me" found!
                        ) else (
                            echo String "play.aresmine.me" not found!
                        )
                                        
                        findstr /C:"hot.aresmine.me" %hostspath% > nul
                        if %errorlevel% equ 0 (
                            echo String "hot.aresmine.me" found!
                        ) else (
                            echo String "hot.aresmine.me" not found!)
                        pause"""
        );
    }
}
