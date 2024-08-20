import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public final class CheckUpdates {

    private static JSONObject getInfoServer() {
        try {
            final URL url = URI.create("https://pinghelper.aresstaff.xyz/actualVersion").toURL();
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                final var response = new StringBuilder();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void execute() {
        // Текущие настройки
        final String version = "1.0";
        final String dataVerst = "2024-06-08";

        // Полученные данные с сервера
        final JSONObject DataSRV = getInfoServer();
        if (DataSRV == null) {

            // Обработка данных и сборка сообщения
            final String message = " ====================" +
                    "\n = Разработано \"ARESMiNE #shorts\" (https://t.me/aresmineshorts)" +
                    "\n = Версия: " + version + " (выпущено " + dataVerst + ")" +
                    "\n = При попытке проверить обновление произошла ошибка!" +
                    "\n ====================";
            System.out.println(message);

        } else {
            final String versionSRV = DataSRV.getString("version");
            final String buildDate = DataSRV.getString("buildDate");
            final String url = DataSRV.getString("url");
            final String urlChanges = DataSRV.getString("urlChanges");

            // Обработка данных и сборка сообщения
            String message = " ====================" +
                    "\n = Разработано \"ARESMiNE #shorts\" (https://t.me/aresmineshorts)" +
                    "\n = Версия: " + version + " (выпущено " + dataVerst + ")";

            if (version.equals(versionSRV)) {
                message = message.concat("\n = Обновления не найдены");
            } else {
                message = message.concat("\n = Найдено новое обновление! (версия: " + versionSRV + "; дата сборки: " + buildDate);
                message = message.concat("\n = Просмотр изменений доступен по ссылке: " + urlChanges);
                message = message.concat("\n = Ссылка на скачивание: " + url);
            }

            message = message.concat("\n ====================");

            System.out.println(message);
        }
    }
}
