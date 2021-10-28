package test_engine;

import java.util.ArrayList;
import java.util.List;

public class TestResultsStore {
    private final List<TestResult> testResults;

    TestResultsStore() {
        this.testResults = new ArrayList<>();
    }
    void addTestResult(TestResult testResult) {
        testResults.add(testResult);
    }
    void showTestResults() {
        final var testPassed = testResults.stream().filter(testResult -> testResult.getException() == null).count();
        final var testFailed = this.testResults.stream().filter(testResult -> testResult.getException() != null).toList();
        final var testExecuted = testResults.size();
        System.out.printf("%s test methods executed, %s passed,\u001B[31m %s failed%n\u001B[0m", testExecuted, testPassed, testFailed.size());
        testFailed.forEach(testResult -> {
            final var testMethod = testResult.getTestMethod();
            final var testException = testResult.getException();
            final var cause = testException.toString();

            System.out.println(testMethod.getDeclaringClass());
            System.out.println(testMethod.getName() + " : " + "\u001B[31m" + cause + "\u001B[0m");
            System.err.println(testMethod.getName() + " " + cause);
            testException.printStackTrace();
        });
    }
}
