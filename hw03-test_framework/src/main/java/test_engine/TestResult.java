package test_engine;

import java.lang.reflect.Method;

public class TestResult {
    private final Method testMethod;
    private Throwable exception;

    TestResult(Method testMethod, Throwable exception) {
        this.testMethod = testMethod;
        this.exception = exception;
    }

    Method getTestMethod() {
        return testMethod;
    }

    Throwable getException() {
        return exception;
    }

    void setException(Throwable exception) {
        this.exception = exception;
    }
}
