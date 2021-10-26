package app;

import test_engine.annotations.After;
import test_engine.exceptions.AssertException;
import test_engine.annotations.Before;
import test_engine.annotations.Test;

public class CalcTest {
    Calc calc;

    @Before
    public void setUp() {
        calc = new Calc();
        System.out.print("@Before method. Instance class ");
        System.out.println(Integer.toHexString(hashCode()));
    }

    @Test
    public void addTest() {
        System.out.print("@Test method. Class instance ");
        System.out.println(Integer.toHexString(hashCode()));
        int result = calc.add(1,3);
        int expected = 4;
        if (result != expected) {
            throw new AssertException(String.format("expected %s, but was %s", expected, result));
        }
    }
    @Test
    public void subTest() {
        System.out.print("@Test method. Class instance ");
        System.out.println(Integer.toHexString(hashCode()));
        int result = calc.sub(10, 5);
        int expected = 5;
        if (result != expected) {
            throw new AssertException(String.format("expected %s, but was %s", expected, result));
        }
    }
    @Test
    public void multiplyTest() {
        System.out.print("@Test method. Class instance ");
        System.out.println(Integer.toHexString(hashCode()));
        long result = calc.multiply(10L, 5L);
        long expected = 50;
        if (result != expected) {
            throw new AssertException(String.format("expected %s, but was %s", expected, result));
        }
    }
    @Test
    public void divideTest() {
        System.out.print("@Test method. Class instance ");
        System.out.println(Integer.toHexString(hashCode()));
        long result = calc.divide(10L, 5L);
        long expected = 2;
        if (result != expected) {
            throw new AssertException(String.format("expected %s, but was %s", expected, result));
        }
    }
    @After
    public void tearDown() {
        System.out.print("@After method. Class instance ");
        System.out.println(Integer.toHexString(hashCode()));
        System.out.println("***************************************");
    }
}
