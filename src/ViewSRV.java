import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.security.SecureRandom;
import java.math.BigInteger;

public class ViewSRV {
    private static SecureRandom random = new SecureRandom();

    public static String generateFileName() {
        return new BigInteger(130, random).toString(16).substring(0, 16);
    }

    public static void execute() {

        try {
            // Создание bat-файла
            String filename = generateFileName();
            File batFile = new File(filename + ".bat");

            // Запись содержимого в bat-файл
            FileWriter fileWriter = new FileWriter(batFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("@echo off\n" +
                    ":: Проверка прав Администратора\n" +
                    "NET FILE 1>NUL 2>NUL\n" +
                    "if '%errorlevel%' == '0' ( goto gotAdmin ) else ( goto UACPrompt )\n" +
                    "\n" +
                    ":UACPrompt\n" +
                    ":: Запуск скрипта с правами Администратора\n" +
                    "echo Set UAC = CreateObject^(\"Shell.Application\"^) > \"%temp%\\getadmin.vbs\"\n" +
                    "echo UAC.ShellExecute \"%~s0\", \"\", \"\", \"runas\", 1 >> \"%temp%\\getadmin.vbs\"\n" +
                    "\"%temp%\\getadmin.vbs\"\n" +
                    "del /f /q \"%temp%\\getadmin.vbs\"\n" +
                    "exit /B\n" +
                    "\n" +
                    ":gotAdmin\n" +
                    "@echo off\n" +
                    "\n" +
                    "set \"hostspath=%SystemRoot%\\System32\\drivers\\etc\\hosts\"\n" +
                    "\n" +
                    "findstr /C:\"mc.aresmine.ru\" %hostspath% > nul\n" +
                    "if %errorlevel% equ 0 (\n" +
                    "    echo String \"mc.aresmine.ru\" found!\n" +
                    ") else (\n" +
                    "    echo String \"mc.aresmine.ru\" not found!\n" +
                    ")\n" +
                    "\n" +
                    "findstr /C:\"mc.aresmine.me\" %hostspath% > nul\n" +
                    "if %errorlevel% equ 0 (\n" +
                    "    echo String \"mc.aresmine.me\" found!\n" +
                    ") else (\n" +
                    "    echo String \"mc.aresmine.me\" not found!\n" +
                    ")\n" +
                    "\n" +
                    "findstr /C:\"play.aresmine.me\" %hostspath% > nul\n" +
                    "if %errorlevel% equ 0 (\n" +
                    "    echo String \"play.aresmine.me\" found!\n" +
                    ") else (\n" +
                    "    echo String \"play.aresmine.me\" not found!\n" +
                    ")\n" +
                    "\n" +
                    "findstr /C:\"hot.aresmine.me\" %hostspath% > nul\n" +
                    "if %errorlevel% equ 0 (\n" +
                    "    echo String \"hot.aresmine.me\" found!\n" +
                    ") else (\n" +
                    "    echo String \"hot.aresmine.me\" not found!)\n"+
                    "pause");
            bufferedWriter.close();

            // Запуск bat-файла
            System.out.println("Проверяю hosts! Подтвердите права Администратора.");
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", filename + ".bat");
            processBuilder.directory(new File(System.getProperty("user.dir")));
            Process process = processBuilder.start();

            // Ждем завершения процесса
            int exitCode = process.waitFor();
        } catch (IOException e) {
            System.out.println("Произошла ошибка при создании или удаления временного файла: " + e.getMessage());
        } catch (InterruptedException e) {

        }
    }
}
