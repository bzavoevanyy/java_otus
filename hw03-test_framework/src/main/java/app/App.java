package app;

import static test_engine.TestsRunner.runTests;
public class App {
    public static void main(String[] args) throws Exception {
        boolean testsPassed = runTests(CalcTest.class);
        if (!testsPassed) {
            return;
        }
        Calc calc = new Calc();
        System.out.println("Addition 1 + 3 = " + calc.add(1,3));
        System.out.println("Subtraction 10 - 5 = "+ calc.sub(10,5));
        System.out.println("Multiplication 10 * 5 = " + calc.multiply(10,5));
        System.out.println("Division 10 / 5 = " + calc.divide(10,5));
    }
}
