package AddAndPing;

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

import java.security.SecureRandom;
import java.math.BigInteger;

public class Ping {
    public static void execute() {
        List<String> ipLists = new ArrayList<>();
        try {
            String[] urls = {"https://tcpshield.com/v4/", "https://tcpshield.com/v4-cf/"};
            for (String url : urls) {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        // Находим IP-адреса в содержимом страницы и обрабатываем их
                        if (inputLine.matches(".*\\b(?:\\d{1,3}\\.){3}\\d{1,3}/\\d{1,2}\\b.*")) {
                            ipLists.add(inputLine);
                        }
                    }
                    in.close();
                } else {
                    System.out.println("GET request failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Получил список Proxy-серверов защиты без AnyCast: " + ipLists);

        String bestIp = null;
        float bestTime = 250; // Максимальное время задержки

        boolean shouldBreak = false; // Флаг для определения, нужно ли выйти из внешнего цикла
        outerloop: // метка для внешнего цикла
        for (String ipInput : ipLists) {
            List<String> masksIP = extractIP(ipInput);
            for (String ip : masksIP) {
                try {
                    String address = ip.split("/")[0];
                    float time = ping(address);
                    if (time != -1) {
                        if (time >= 250) {
                            System.out.println("IP: " + address + " с высокой задержкой! Задержка: " + time + "мс (более 250мс)");
                            break;
                        } else {
                            System.out.println("IP: " + address + ", Задержка: " + time + " мс");
                            if (time < bestTime) {
                                bestTime = time;
                                bestIp = address;
                            }
                        }
                    } else {
                        System.out.println("IP: " + address + " не смог ответить на запрос! Удаляем из списка.");
                        break;
                    }

                } catch (IOException e) {
                    System.out.println("Внутренняя ошибка при пинге IP-адреса " + ip + "\n" + e);
                    break; // Выходим из внутреннего цикла
                }
            }
        }


        if (bestIp != null) {
            System.out.println("\nСамый лучший Proxy-сервер: " + bestIp + ", с задержкой " + bestTime + " мс");
        } else {
            System.out.println("К сожалению, не смогли найти лучший Proxy-сервер защиты! Попробуйте отключить VPN, перезагрузить роутер/модем.");
        }
    }

    private static List<String> extractIP(String InputMask) {
        List<String> availableIPs = new ArrayList<>();
        try {
            String[] parts = InputMask.split("/");
            String ipAddress = parts[0];
            int mask = Integer.parseInt(parts[1]);

            byte[] addr = InetAddress.getByName(ipAddress).getAddress();
            int ipInt = ByteBuffer.wrap(addr).getInt();

            int maskInt = 0xFFFFFFFF << (32 - mask);
            int netAddr = ipInt & maskInt;

            for (int i = 0; i < (1 << (32 - mask)); i++) {
                int currentIP = netAddr + i;
                byte[] bytes = new byte[]{
                        (byte) ((currentIP & 0xFF000000) >> 24),
                        (byte) ((currentIP & 0x00FF0000) >> 16),
                        (byte) ((currentIP & 0x0000FF00) >> 8),
                        (byte) (currentIP & 0x000000FF)
                };
                InetAddress inetAddress = Inet4Address.getByAddress(bytes);
                availableIPs.add(inetAddress.getHostAddress());
            }

            // System.out.println("Available IP addresses:");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return availableIPs;
    }

    private static List<String> extractIpRanges(String url) {
        List<String> ipRanges = new ArrayList<>();
        try {
            URL urlObject = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlObject.openStream()));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            Pattern pattern = Pattern.compile("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}/[0-9]{1,2}\\b");
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                ipRanges.add(matcher.group());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipRanges;
    }

    private static float ping(String ipAddress) throws IOException {
        Process process = Runtime.getRuntime().exec("ping -n 1 -w 250 " + ipAddress); // Пингуем каждый IP один раз с таймаутом 1 секунда
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String decodedString = new String(line.getBytes("cp866"), "UTF-8");
            if (decodedString.contains("TTL=")) {
                Pattern pattern = Pattern.compile("([^\\s]+)\\s+TTL"); // Используем регулярное выражение для поиска значения перед "TTL"
                Matcher matcher = pattern.matcher(decodedString);
                if (matcher.find()) {
                    String valueBeforeTTL = matcher.group(1); // Получаем значение перед "TTL"
                    String Ping_ = valueBeforeTTL.split("=")[1].split("\\?")[0]; // Выводим результат
                    return Float.parseFloat(Ping_);
                }
            }
        }
        return -1; // Если не удалось получить время, возвращаем -1
    }

    private static SecureRandom random = new SecureRandom();

    public static String generateFileName() {
        return new BigInteger(130, random).toString(16).substring(0, 16);
    }
}
