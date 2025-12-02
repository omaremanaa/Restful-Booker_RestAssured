package CustomListeners;

import org.testng.*;

public class TestNGListeners implements IInvokedMethodListener, ITestListener , IExecutionListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            System.out.println(method.getTestMethod().getMethodName()+" "+ " About to execute " );
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            //take screenshot after each test method
            System.out.println(method.getTestMethod().getMethodName()+" "+ " Finished " );
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() +" "+ "passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() +" "+ "Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() +" "+ "skipped");
    }

    @Override
    public void onExecutionStart() {
        System.out.println("Execution started");


    }
    @Override
    public void onExecutionFinish() {
        System.out.println("Execution Finished");
    }

}