package com.test.may;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String home = System.getProperty("user.home");
        if (home == null) {
            home = System.getenv("HOME");
        }
        String transfer = home + "\\transfer";
        new File(transfer).mkdir();
        System.out.println(home);
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

        System.out.println("Ready to send");
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String text = scanner.nextLine();
                if (text.equals("\n")) continue;
                String text2 = text;
                String access = "";
                String username = "";
                String newPassword = "";
                String password = "";
                String refresh = "";
                String rec = "";
                String sender = "";
                String limit = "";
                String textForServer = "";
                System.out.println(text);
                System.out.println("got access");
                String uriScanner = "";
                if (text.startsWith("get")) {
                    text = text.replace("get ", "");
                    String[] list = text.split(" ");
                    username = list[0];
                    password = list[1];
                } else if (text.startsWith("insert ")) { // TODO сделать проверку существующих пользователей
                    text = text.replace("insert ", "");
                    String[] list = text.split(" ");
                    username = list[0];
                    password = list[1];
                } else if (text.startsWith("access ")) {
                    text = text.replace("access ", "");
                    refresh = new String(text);
                } else if (text.startsWith("refresh ")) {
                    text = text.replace("refresh ", "");
                    username = new String(text).split(" ")[0];
                    password = new String(text).split(" ")[1];
                } else if (text.startsWith("send ")) {
                    String nextLine = scanner.nextLine();
                    text = text.replace("send ", "");
                    textForServer = Base64.getEncoder().encodeToString(nextLine.getBytes(StandardCharsets.UTF_8));
                    rec = new String(text).split(" ")[0];
                    access = new String(text).split(" ")[1];
                } else if (text.startsWith("new ")) {
                    text = text.replace("new ", "");
                    access = new String(text);
                } else if (text.startsWith("old ")) {
                    text = text.replace("old ", "");
                    access = text.split(" ")[0];
                    sender = text.split(" ")[1];
                    limit = text.split(" ")[2];
                } else if (text.startsWith("changePassword ")) {
                    text = text.replace("changePassword ", "");
                    username = text.split(" ")[0];
                    password = text.split(" ")[1];
                    newPassword = text.split(" ")[2];
                } else if (text.startsWith("createContact ")) {
                    text = text.replace("createContact ", "");
                    rec = text.split(" ")[0];
                    sender = text.split(" ")[1];
                } else if (text.startsWith("deleteContact ")) {
                    text = text.replace("deleteContact ", "");
                    rec = text.split(" ")[0];
                    sender = text.split(" ")[1];
                } else if (text.startsWith("gtContacts ")) {
                    text = text.replace("gtContacts ", "");
                    sender = new String(text);
                    System.out.println(sender);
                }
                uriScanner = "command=" + text2.split(" ")[0];
                URI uri = URI.create("http://217.18.61.102:80/?" + uriScanner);
                HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).headers("access", access, "refresh", refresh, "username", username, "password", password, "rec", rec, "text", textForServer, "limit", limit, "sender", sender, "newPassword", newPassword).build();
                System.out.println("okay1");
                HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
                int status = response.statusCode();
                System.out.println(status);
                Scanner scanner2 = new Scanner(response.body());
                StringBuilder result = new StringBuilder();
                while (scanner2.hasNextLine()) {
                    result.append(scanner2.nextLine()).append(System.lineSeparator());
                }
                System.out.println(result);
                String dataFromServer = result.toString();
                if (text2.startsWith("new") || text2.startsWith("old")) {
                    dataFromServer = dataFromServer.replace("], [", " & ");
                    dataFromServer = dataFromServer.replace("[[", "");
                    dataFromServer = dataFromServer.replace("]]", "");
                    System.out.println(dataFromServer);
                    String [ ] resultAll = dataFromServer.split(" & ");
                    String [ ] result1 = resultAll[0].split(", ");
                    String [ ] result2 = resultAll[1].split(", ");
                    String [ ] result3 = resultAll[2].split(", ");
                    ArrayList<String> realResult1 = new ArrayList<>();
                    ArrayList<String> realResult2 = new ArrayList<>();
                    ArrayList<String> realResult3 = new ArrayList<>();
                    for (String iResult1 : result1) {
                        realResult1.add(new String(Base64.getDecoder().decode(iResult1)));
                    } for (String iResult2 : result2) {
                        realResult2.add(new String(Base64.getDecoder().decode(iResult2)));
                    }
                    result3[result3.length - 1] = result3[result3.length - 1].replace("\n", "");
                    Collections.addAll(realResult3, result3);

                    System.out.println(realResult1);
                    System.out.println(realResult2);
                    System.out.println(realResult3);
                }
                if (text2.equals("quit")) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
