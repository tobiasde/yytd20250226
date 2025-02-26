import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ViewGoodsTestTest {
    private static final String FILE_READ_ERROR_MSG = "文件读取内容为空";

    @Test
    public void FileRead() {
        System.out.println("引用Login中的全局变量为:" + LogInTest.globalinfoValue);
        String filePath = "test.txt";
        String token = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                token += line + "\n";
            }
        } catch (IOException e) {
            Assert.fail("读取文件时发生错误: " + e.getMessage());
        }
        Assert.assertTrue(!token.isEmpty(), FILE_READ_ERROR_MSG);
        System.out.println(token);
    }
}