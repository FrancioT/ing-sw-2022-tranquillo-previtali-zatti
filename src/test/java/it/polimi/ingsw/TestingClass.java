package it.polimi.ingsw;

import junit.framework.TestCase;
import org.junit.Test;

public class TestingClass extends TestCase
{
    @Test
    public void test() throws Exception
    {
        TestDashboard testDashboard=new TestDashboard();
        testDashboard.testEntrance();
        testDashboard.testClassrooms();
        IslandTest islandTest=new IslandTest();
        islandTest.isTest();
        // modifico il costruttore di towers per costruire il numero giusto di torri
        // in base alla modalità
    }
}
