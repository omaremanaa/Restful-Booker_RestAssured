package resources;

import io.qameta.allure.Allure;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class AllureSoftAssert extends SoftAssert {

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String message = getAssertionMessage(assertCommand);
        LogUtils.info("✔️ SoftAssert PASSED -> " + message);
        Allure.step("✔️ SoftAssert PASSED: " + message);
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String message = getAssertionMessage(assertCommand);
        LogUtils.error("❌ SoftAssert FAILED -> " + message + " | Error: " + ex.getMessage());
        Allure.step("❌ SoftAssert FAILED: " + message + " | Error: " + ex.getMessage());
    }

    private String getAssertionMessage(IAssert<?> assertCommand) {
        String customMessage = assertCommand.getMessage();
        if (customMessage != null && !customMessage.isEmpty()) {
            return customMessage;
        }
        return assertCommand.toString();
    }

}