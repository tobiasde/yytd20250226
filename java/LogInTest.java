import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;


public class LogInTest {
    public static String globalinfoValue = "这是一个全局字符串";
    @Test
    public static void logIn() {
        String[][] credentials = {{"1907289890", "qazwsx12","500"},//正确的账号+密码
                {"1907289890", "ewqewr@#$","500"},//正确的账号+错误密码
                {"1907289890", "","500"},//正确的账号+空置密码
                {"1907289890", "3234wqewq23","500"},//错误的账号+密码
                {"1907289890", "3234wqewq23","500"},//不存在的账号+密码
                {"", "3234wqewq23","500"},//空置账号+密码
                {"1907289890", "","500"},//空置账号+空置密码
        };

        for (String[] pair : credentials) {
            try {
                String username = pair[0];
                String password = pair[1];
                String code = pair[2];
                // 定义请求的 URL
                URL url = new URL("http://subtest.kongtrolink.com:84/api/user/checkUser");
                // 打开连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法为 POST
                connection.setRequestMethod("POST");
                // 允许输出流，用于发送请求体
                connection.setDoOutput(true);

                // 设置请求头
                connection.setRequestProperty("Content-Type", "application/json");

                // 定义请求体
                String jsonInputString = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

                // 获取输出流并写入请求体
                try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                    dos.write(input, 0, input.length);
                }

                // 获取响应码
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                // 读取响应内容
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                    String regex1 = "\"code\":(\\d+)";
                    Pattern pattern1 = Pattern.compile(regex1);
                    Matcher matcher1 = pattern1.matcher(response.toString());
                    if (matcher1.find()) {
                        String codeValue = matcher1.group(1);
                        System.out.println("提取到的code值: " + codeValue);
                        assertEquals(codeValue, code, "响应码与预期码不相等");
                        if(Objects.equals(codeValue, code)){
                            String regex2 = "\"info\":\"([^\"]+)\".*?";
                            Pattern pattern2 = Pattern.compile(regex2);
                            Matcher matcher2 = pattern2.matcher(response.toString());
                            if (matcher2.find()) {
                                String infoValue = matcher2.group(1);
                                System.out.println("提取到的infoValue值: " + infoValue);
                                globalinfoValue = infoValue;
                                System.out.println("设置的全局变量是:" + globalinfoValue);
                                String filePath = "test.txt";
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                                    writer.write(infoValue);
                                    System.out.println("token已成功写入文件");
                                } catch (IOException e) {
                                    System.out.println("token写入文件时发生错误: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                }
            } catch (UnknownHostException e) {
                // 捕获到 UnknownHostException 时断言失败
                Assert.fail("请求过程中发生 UnknownHostException: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}