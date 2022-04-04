package it.polimi.ingsw;

import junit.framework.TestCase;
import org.junit.Test;

public class TestingClass extends TestCase
{
    @Test
    public void test() throws Exception
    {
        TestDashboard t=new TestDashboard();
        t.testEntrance();
        t.testClassrooms();
    }
}
