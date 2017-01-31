package vsoc.test;

import java.util.Enumeration;

import vsoc.nn.base.Layer;
import vsoc.nn.base.NeuronLayer;
import vsoc.nn.base.Synapse;
import vsoc.nn.base.TransferManager;
import vsoc.nn.base.Weighter;
import vsoc.nn.feedforward.AbstractFFNetConnector;
import vsoc.nn.feedforward.FFNet;
import vsoc.util.IntVector;

public class TestTransferManager extends AbstractTest {

    public TestTransferManager(String name) {
        super(name);
    }

    public void testTransferManager00() {
        FFNet n = createOneNeuronNet(4);
        assertInOut(n, 60, 60, 20, 20, 50);
    }

    public void testTransferManager01() {
        FFNet n = createOneNeuronNet(4);
        assertInOut(n, 99, 99, 0, 0, 50);
    }

    public void testTransferManager02() {
        FFNet n = createOneNeuronNet(5);
        assertInOut(n, 31, 64);
    }

    public void testTransferManager03() {
        FFNet n = createOneNeuronNet(5);
        assertInOut(n, 42, 68);
    }

    public void testTransferManager04() {
        FFNet n = createOneNeuronNet(5);
        assertInOut(n, 52, 73);
    }

    public void testTransferManager05() {
        FFNet n = createOneNeuronNet(5);
        assertInOut(n, 62, 76);
    }

    public void testTransferManager06() {
        FFNet n = createOneNeuronNet(5);
        assertInOut(n, 73, 80);
    }

    public void testTransferManagerWith700() {
        assertWith7(42, 66);
    }

    public void testTransferManagerWith701() {
        assertWith7(52, 70);
    }

    public void testTransferManagerWith702() {
        assertWith7(62, 73);
    }

    public void testTransferManagerWith703() {
        assertWith7(73, 76);
    }

    public void testTransferManagerWith1500() {
        assertWith15(42, 62);
    }

    public void testTransferManagerWith1501() {
        assertWith15(52, 65);
    }

    public void testTransferManagerWith1502() {
        assertWith15(62, 67);
    }

    public void testTransferManagerWith1503() {
        assertWith15(73, 70);
    }

    private void assertWith7(int in, int out) {
        FFNet n = createOneNeuronNet(7);
        n.setInputValue(0, (short) 10);
        n.setInputValue(1, (short) 10);
        n.setInputValue(2, (short) 12);
        n.setInputValue(3, (short) 12);
        n.setInputValue(4, (short) 50);
        n.setInputValue(5, (short) 50);
        n.setInputValue(6, (short) in);
        n.calculate();
        assertEquals(out, n.getOutputValue(0));
    }

    private void assertWith15(int in, int out) {
        FFNet n = createOneNeuronNet(15);
        n.setInputValue(0, (short) 10);
        n.setInputValue(1, (short) 10);
        n.setInputValue(2, (short) 12);
        n.setInputValue(3, (short) 12);
        n.setInputValue(4, (short) 50);
        n.setInputValue(5, (short) 50);
        n.setInputValue(6, (short) 50);
        n.setInputValue(7, (short) 50);
        n.setInputValue(8, (short) 50);
        n.setInputValue(9, (short) 50);
        n.setInputValue(10, (short) 50);
        n.setInputValue(11, (short) 50);
        n.setInputValue(12, (short) 90);
        n.setInputValue(13, (short) 90);
        n.setInputValue(14, (short) in);
        n.calculate();
        assertEquals(out, n.getOutputValue(0));
    }

    private void assertInOut(FFNet n, int in1, int in2, int in3, int in4,
            int out1) {
        n.setInputValue(0, (short) in1);
        n.setInputValue(1, (short) in2);
        n.setInputValue(2, (short) in3);
        n.setInputValue(3, (short) in4);
        n.calculate();
        assertEquals(out1, n.getOutputValue(0));
    }

    private void assertInOut(FFNet n, int in, int out) {
        this.assertInOut(n, 40, 40, 20, 20, in, out);
    }

    private void assertInOut(FFNet n, int in1, int in2, int in3, int in4,
            int in5, int out1) {
        n.setInputValue(0, (short) in1);
        n.setInputValue(1, (short) in2);
        n.setInputValue(2, (short) in3);
        n.setInputValue(3, (short) in4);
        n.setInputValue(4, (short) in5);
        n.calculate();
        assertEquals(out1, n.getOutputValue(0));
    }

    public void testCreateOneNeuronNetInputLayerCount00() {
        FFNet n = createOneNeuronNet(5);
        this.assertCreateOneNeuronNetInputLayerCount01(n, 5);
    }

    public void testCreateOneNeuronNetInputLayerCount05() {
        FFNet n = createOneNeuronNet(3);
        this.assertCreateOneNeuronNetInputLayerCount01(n, 3);
    }

    public void testCreateOneNeuronNetInputLayerCount01() {
        FFNet n = createOneNeuronNet(15);
        this.assertCreateOneNeuronNetInputLayerCount01(n, 15);
    }

    public void testCreateOneNeuronNetInputLayerCount02() {
        FFNet n = createOneNeuronNet(50);
        this.assertCreateOneNeuronNetInputLayerCount01(n, 50);
    }

    public void testCreateOneNeuronNetInputLayerCount03() {
        FFNet n = createOneNeuronNet(1);
        this.assertCreateOneNeuronNetInputLayerCount01(n, 1);
    }

    public void testCreateOneNeuronNetInputLayerCount04() {
        FFNet n = createOneNeuronNet(21);
        this.assertCreateOneNeuronNetInputLayerCount01(n, 21);
    }

    private void assertCreateOneNeuronNetInputLayerCount01(FFNet n, int count) {
        Layer in = n.getInputLayer();
        Enumeration enu = in.layerNodes();
        int i = 1;
        for (; i <= count; i++) {
            assertTrue("Input layer has " + i + " LayerNode(s)", enu
                    .hasMoreElements());
            Object o = enu.nextElement();
        }
        assertTrue("Input layer has more than " + i + " LayerNode(s)", !enu
                .hasMoreElements());
    }

    public void testCreateOneNeuronNetOutputLayerCount00() {
        FFNet n = createOneNeuronNet(3);
        assertCreateOneNeuronNetOutputLayerCount(n);
    }

    public void testCreateOneNeuronNetOutputLayerCount01() {
        FFNet n = createOneNeuronNet(1);
        assertCreateOneNeuronNetOutputLayerCount(n);
    }

    public void testCreateOneNeuronNetOutputLayerCount02() {
        FFNet n = createOneNeuronNet(15);
        assertCreateOneNeuronNetOutputLayerCount(n);
    }

    public void testCreateOneNeuronNetOutputLayerCount03() {
        FFNet n = createOneNeuronNet(60);
        assertCreateOneNeuronNetOutputLayerCount(n);
    }

    public void testCreateOneNeuronNetOutputLayerCount04() {
        FFNet n = createOneNeuronNet(200);
        assertCreateOneNeuronNetOutputLayerCount(n);
    }

    private void assertCreateOneNeuronNetOutputLayerCount(FFNet n) {
        Layer l = n.getOutputLayer();
        Enumeration enu = l.layerNodes();
        assertTrue("Input layer has one LayerNode", enu.hasMoreElements());
        Object o = enu.nextElement();
        assertTrue("Input layer has not two LayerNode", !enu.hasMoreElements());
    }

    private FFNet createOneNeuronNet(int synapseCount) {
        AbstractFFNetConnector c = oneNeuronConnector(synapseCount);
        FFNet net = new FFNet(c);
        NeuronLayer l = net.getOutputLayer();
        Enumeration enu = l.synapses();
        boolean isPos = true;
        while (enu.hasMoreElements()) {
            Synapse syn = (Synapse) enu.nextElement();
            if (isPos)
                syn.setWeight((short) 6);
            else
                syn.setWeight((short) -6);
            isPos = !isPos;
        }
        return net;
    }

    private AbstractFFNetConnector oneNeuronConnector(int synapseCount) {
        switch (synapseCount) {
        case 1:
            return new OneNeuronConnector1();
        case 4:
            return new OneNeuronConnector4();
        case 5:
            return new OneNeuronConnector5();
        case 7:
            return new OneNeuronConnector7();
        case 15:
            return new OneNeuronConnector15();
        case 3:
            return new OneNeuronConnector3();
        case 50:
            return new OneNeuronConnector50();
        case 21:
            return new OneNeuronConnector21();
        case 60:
            return new OneNeuronConnector60();
        case 200:
            return new OneNeuronConnector200();
        default:
            throw new Error("Invalid synapseCount: " + synapseCount);
        }
    }

    private abstract class OneNeuronConnector extends AbstractFFNetConnector {
        public int seed() {
            return 0;
        }

        public TransferManager transferManager() {
            return new TransferManager();
        }

        public Weighter weighter() {
            return new Weighter((float) 0.1, (float) 3.0);
        }

        public IntVector nodesPerLayer() {
            IntVector npl = new IntVector();
            npl.addElement(synapseCount());
            npl.addElement(1);
            return npl;
        }

        public IntVector connProbMatrix() {
            IntVector cpm = new IntVector();
            IntVector cpl = new IntVector();
            cpm.addElement(cpl);
            cpl = new IntVector();
            cpl.addElement(100);
            cpm.addElement(cpl);
            return cpm;
        }

        abstract int synapseCount();
    }

    private class OneNeuronConnector4 extends OneNeuronConnector {
        int synapseCount() {
            return 4;
        }
    }

    private class OneNeuronConnector5 extends OneNeuronConnector {
        int synapseCount() {
            return 5;
        }
    }

    private class OneNeuronConnector15 extends OneNeuronConnector {
        int synapseCount() {
            return 15;
        }
    }

    private class OneNeuronConnector1 extends OneNeuronConnector {
        int synapseCount() {
            return 1;
        }
    }

    private class OneNeuronConnector7 extends OneNeuronConnector {
        int synapseCount() {
            return 7;
        }
    }

    private class OneNeuronConnector3 extends OneNeuronConnector {
        int synapseCount() {
            return 3;
        }
    }

    private class OneNeuronConnector50 extends OneNeuronConnector {
        int synapseCount() {
            return 50;
        }
    }

    private class OneNeuronConnector21 extends OneNeuronConnector {
        int synapseCount() {
            return 21;
        }
    }

    private class OneNeuronConnector60 extends OneNeuronConnector {
        int synapseCount() {
            return 60;
        }
    }

    private class OneNeuronConnector200 extends OneNeuronConnector {
        int synapseCount() {
            return 200;
        }
    }

    public static void main(String[] args) {
        TestTransferManager t = new TestTransferManager("in main");
        FFNet n = t.createOneNeuronNet(4);
        n.setInputValue(0, (short) 10);
        n.setInputValue(1, (short) 20);
        n.setInputValue(2, (short) 30);
        n.setInputValue(3, (short) 40);
        System.out.print(n);
        n.calculate();
        System.out.print(n);
    }

}
