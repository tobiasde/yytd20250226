import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.ITestResult;
import java.util.ArrayList;
import java.util.List;

public class TestNGProgrammaticRunner {
    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("testng.xml");
        testNG.setTestSuites(suites);

        // 使用匿名内部类创建监听器
        TestListenerAdapter listener = new TestListenerAdapter() {
            @Override
            public void onTestStart(ITestResult result) {
                System.out.println("测试用例开始执行: " + result.getName());
            }

            @Override
            public void onTestSuccess(ITestResult result) {
                System.out.println("测试用例执行成功: " + result.getName());
            }

            @Override
            public void onTestFailure(ITestResult result) {
                System.out.println("测试用例执行失败: " + result.getName());
                System.out.println("失败原因: " + result.getThrowable().getMessage());
                result.getThrowable().printStackTrace();
            }

            @Override
            public void onTestSkipped(ITestResult result) {
                System.out.println("测试用例被跳过: " + result.getName());
            }
        };

        testNG.addListener(listener);
        testNG.run();

        int failedCount = listener.getFailedTests().size();
        if (failedCount != 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
}