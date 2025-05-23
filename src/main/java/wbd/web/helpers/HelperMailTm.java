package wbd.web.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wbd.web.data.UserData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperMailTm {
    private static final String API_URL = "https://api.mail.tm";
    private static final Logger logger = LoggerFactory.getLogger(HelperMailTm.class);

    //======================= EmailHelper ==========================

    // создаем временный e-mail перед регистрацией, используя сервис "Guerrilla Mail",
    // который дает временные e-mail через API
    // генерируем временный email через mail.tm
    public static String generateEmail() throws IOException {
        String domain = getDomain(); // получаем временный домен для почты
        if (domain == null || domain.isEmpty()) {
            throw new IOException("No valid domain found!");
        }
        String username = "test" + System.currentTimeMillis();// генерируем уник. имя юзера
        String email = username + "@" + domain; // собираем имейл
        registerEmail(email, UserData.VALID_PASSWORD); // регистрируем email через API

        logger.info("Generated email: " + email);

        return email;
    }

    // получаем ссылку подтверждения из письма
    public static String getConfirmationLink(String email) throws IOException, InterruptedException {
        String token = authenticate(email, UserData.VALID_PASSWORD);// токен для авторизации
        logger.info("Generated token: " + token);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(5000);

            JSONArray messages = getMessages(token);// получаем все сообщения
            if (!messages.isEmpty()) {                    // берем ID первого сообщения
                String messageId = messages.getJSONObject(0).getString("id");  // Берем ID как строку
                return extractLinkFromEmail(token, messageId); // берем ссылку из письма

            }
        }
        return null;
    }

    // вспомогательные методы
    private static String getDomain() throws IOException {
        String response = getResponse(API_URL + "/domains");// получ.список доменов
        logger.info("Response from API (getDomain): " + response);

        try {         // преобразуем строку ответа в JSON-объект
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("hydra:member")) {// см.существует ли ключ "hydra:member" в ответе
                JSONArray domains = jsonResponse.getJSONArray("hydra:member");//массив значений, связанных с этим ключом
                if (!domains.isEmpty()) {// если массив не пуст, берем 1-е значение и возвращаем значение по ключу "domain"
                    return domains.getJSONObject(0).getString("domain");
                }
            }
            logger.error("No valid domain found in API response.");
            return null;
        } catch (JSONException e) {
            logger.error("Error parsing JSON response: ", e);
            return null;
        }
    }

    private static void registerEmail(String email, String password) throws IOException {
        JSONObject request = new JSONObject();
        request.put("address", email);
        request.put("password", password);

        logger.info("Registering email: " + request.toString());

        postRequest(API_URL + "/accounts", request.toString());
    }

    private static String authenticate(String email, String password)
            throws IOException {
        JSONObject request = new JSONObject();
        request.put("address", email);
        request.put("password", password);

        JSONObject response = new JSONObject(postRequest(API_URL + "/token", request.toString()));
        return response.getString("token");
    }

    private static JSONArray getMessages(String token) throws IOException {
        String response = getAuthResponse(API_URL + "/messages", token);
        JSONObject jsonResponse = new JSONObject(response);

        if (jsonResponse.has("hydra:member")) {
            return jsonResponse.getJSONArray("hydra:member");
        }

        logger.error("No messages found in API response.");
        return new JSONArray();  //  пустой массив, если писем нет
    }

    private static String extractLinkFromEmail(String token, String messageId) throws IOException {
        JSONObject message = new JSONObject(getAuthResponse(API_URL + "/messages/" + messageId, token));

        // получаем тело письма как текст
        String body = message.optString("text");  // Используем текстовое содержание письма
        logger.info("Email body: " + body);

        // ищем ссылку в тексте с помощью регулярного выражения
        String regex = "(http[^\s]+)";  // регекс для нахождения URL
        //ищем все символы, которые не являются пробелами и их >= 1
        Pattern pattern = Pattern.compile(regex); // компилирует строку regex в объект patern, для работы с регулярными выражениями
        Matcher matcher = pattern.matcher(body);// объект Matcher, который используется для выполнения поиска по тексту с помощью ранее созданного шаблона (регулярного выражения)

        if (matcher.find()) {
            return matcher.group(1);  // возвращаем первую найденную ссылку
        } else {
            throw new IOException("Confirmation link not found in email body!");
        }

    }

    // вспомогательные методы для http-запросов
    private static String getResponse(String apiUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        return readResponse(conn);
    }

    private static String getAuthResponse(String apiUrl, String token) throws IOException { //выполняет HTTP GET запрос с использованием токена авторизации
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection(); //созд. HTTP соединение для заданного URL (apiUrl), openConnection - открывает соединение с сервером по указанному URL.
        conn.setRequestMethod("GET");// устанавливает тип HTTP-запроса — у нас GET
        conn.setRequestProperty("Authorization", "Bearer " + token);// set заголовок Authorization, который используется для передачи токена авторизации. Токен передается в виде "Bearer {token}".
        return readResponse(conn); // метод возвращает ответ от сервера в виде строки
    }

    private static String postRequest(String apiUrl, String payload) throws IOException {   // устанавливает соединение с сервером по переданному URL (apiUrl).
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);//в теле запроса будут отправляться данные (больше для Post)

        conn.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        // данные, переданные в параметре payload, преобразуются в байты с использованием кодировки UTF-8 и отправляются в теле POST-запроса через поток OutputStream, что позволяет отправить любые данные (например, JSON-строку) в теле запроса.

        return readResponse(conn); // метод считывает и возвращает ответ от сервера.
    }
    private static String readResponse(HttpURLConnection conn) throws IOException {  // получения получение ответа от сервера
        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();// собирает строки в StringBuilder и озвращает итоговый результат как строку.
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        return response.toString();
    }
}
