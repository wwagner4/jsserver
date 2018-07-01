package vsoc.util;

/**
 * Testcases for Persistence
 */

public class TestPersistor extends AbstractTest {

	private Serializer ser = Serializer.current();

    public TestPersistor(String name) {
        super(name);
    }
    public void testPersB00() throws Exception {
        assertPersB("hallo", 10.0, 234);
    }
    public void testPersB01() throws Exception {
        assertPersB("", 10.0, 234);
    }
    public void testPersB02() throws Exception {
        assertPersB("       ", 10293847.0, -0);
    }
    public void testPersB03() throws Exception {
        assertPersB("h a l l o", 10.00000000123123, 0);
    }
    public void testPersB04() throws Exception {
        assertPersB("/\n\t", 0.000213, 223434);
    }
    public void testPersA00() throws Exception {
        assertPersB("hallo", 10.0, 234);
    }
    public void testPersA01() throws Exception {
        assertPersB("", 10.0, 234);
    }
    public void testPersA02() throws Exception {
        assertPersB("       ", 10293847.0, -0);
    }
    public void testPersA03() throws Exception {
        assertPersB("h a l l o", 10.00000000123123, 0);
    }
    public void testPersA04() throws Exception {
        assertPersB("/\n\t", 0.000213, 223434);
    }
    private void assertPersB(String str, double doubleVal, int intVal) throws Exception {
        PersB b = new PersB();
        b.stringValue = str;
        b.doubleValue = doubleVal;
        b.intValue = intVal;
        this.ser.serialize(b, TestUtil.tmp("test.object"));
        b = (PersB) this.ser.deserialize(TestUtil.tmp("test.object"));
        assertEquals(str, b.stringValue);
        assertEquals(doubleVal, b.doubleValue, 0.00001);
        assertEquals(intVal, b.intValue);
    }
    public void testPersANull() throws Exception {
        PersA a = new PersA();
        a.persB = null;
        this.ser.serialize(a, TestUtil.tmp("test.object"));
        a = (PersA) this.ser.deserialize(TestUtil.tmp("test.object"));
        assertNull("b is null", a.persB);
    }
}
