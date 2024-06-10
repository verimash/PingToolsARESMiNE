import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CheckUpdates {
    private static JSONObject getInfoServer() {
        try {
            URL url = new URL("https://pinghelper.aresstaff.xyz/actualVersion");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                return jsonResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void execute () {
        // Текущие настройки
        String version = "1.0";
        String dataVerst = "2024-06-08";

        // Полученные данные с сервера
        JSONObject DataSRV = getInfoServer();
        if (DataSRV == null) {

            // Обработка данных и сборка сообщения
            String message = " ====================" +
                    "\n = Разработано \"ARESMiNE #shorts\" (https://t.me/aresmineshorts)" +
                    "\n = Версия: " + version + " (выпущено " + dataVerst + ")" +
                    "\n = При попытке проверить обновление произошла ошибка!" +
                    "\n ====================";
            System.out.println(message);

        } else {
            String versionSRV = DataSRV.getString("version");
            String buildDate = DataSRV.getString("buildDate");
            String url = DataSRV.getString("url");
            String urlChanges = DataSRV.getString("urlChanges");

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
