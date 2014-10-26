import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * The test class emptyConstructorTest.
 *
 * @author (Matthew McGuire)
 * @version (0.1 16 September 2013)
 */
public class TreeTest {
    private Tree<Integer> tree1;
    private Tree<Integer> tree2;
    private Tree<Integer> tree3;
    private Tree<Integer> tree4;
    private Tree<Integer> tree5;
    private Tree<Integer> tree6;
    private Tree<Integer> tree7;
    private Tree<Integer> tree8;
    private Tree<Integer> tree9;

    private List<Tree<Integer>> list1;
    private List<Tree<Integer>> list2;
    private List<Tree<Integer>> list3;
    private List<Tree<Integer>> list4;
    private List<Tree<Integer>> list5;
    private List<Tree<Integer>> listA;
    private List<Tree<Integer>> listB;

    private Tree<Integer> tree11;
    private Tree<Integer> tree12;
    private Tree<Integer> tree13;
    private Tree<Integer> tree14;
    private Tree<Integer> tree15;
    private Tree<Integer> tree16;
    private Tree<Integer> tree17;
    private Tree<Integer> tree18;
    private Tree<Integer> tree19;

    private List<Tree<Integer>> list11;
    private List<Tree<Integer>> list12;
    private List<Tree<Integer>> list13;
    private List<Tree<Integer>> list14;
    private List<Tree<Integer>> list15;
    private List<Tree<Integer>> list1A;
    private List<Tree<Integer>> list1B;

    /**
     * Sets up the test fixture.
     * <p/>
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        tree1 = new Tree<Integer>(1);
        tree2 = new Tree<Integer>(2);
        tree3 = new Tree<Integer>(3);
        tree4 = new Tree<Integer>(4);
        tree5 = new Tree<Integer>(5);
        tree6 = new Tree<Integer>(6);
        tree7 = new Tree<Integer>(7);
        tree8 = new Tree<Integer>(8);
        tree9 = new Tree<Integer>(9);

        list1 = new ArrayList<Tree<Integer>>();
        list1.add(tree2);
        list1.add(tree3);
        tree1.setRootChildren(list1);

        list2 = new ArrayList<Tree<Integer>>();
        list2.add(tree4);
        tree2.setRootChildren(list2);

        list3 = new ArrayList<Tree<Integer>>();
        list3.add(tree5);
        list3.add(tree6);
        list3.add(tree5);
        list3.add(tree9);
        tree3.setRootChildren(list3);

        list4 = new ArrayList<Tree<Integer>>();
        list4.add(tree6);
        tree4.setRootChildren(list4);

        list5 = new ArrayList<Tree<Integer>>();
        list5.add(tree7);
        tree5.setRootChildren(list5);

        listA = new ArrayList<Tree<Integer>>();
        listA.add(tree8);
        tree7.setRootChildren(listA);

        listB = new ArrayList<Tree<Integer>>();
        listB.add(tree9);
        tree8.setRootChildren(listB);
    }

    /**
     * Test to ensure that empty constructor creates empty tree.
     */
    @Test(expected = NullPointerException.class)
    public void testEmptyConstructor() {
        Tree<Integer> treeInt = new Tree<Integer>();
        assertEquals(null, treeInt.getRootValue());
    }

    /**
     * Testing Constructor that passes in parameter for rootValue.
     */
    @Test
    public void testConstructor() {
        Tree<String> treeString = new Tree<String>("ROOT VALUE");
        assertEquals("ROOT VALUE", treeString.getRootValue());
    }

    /**
     * Test setRootChildren method on non empty tree.
     */
    @Test
    public void testSetRootChildren() {
        Tree<String> tree = new Tree<String>("First");
        Tree<String> tree2 = new Tree<String>("Second");
        Tree<String> tree3 = new Tree<String>("Third");

        List<Tree<String>> list = new ArrayList<Tree<String>>();
        list.add(tree2);
        tree.setRootChildren(list);

        List<Tree<String>> list2 = new ArrayList<Tree<String>>();
        list2.add(tree3);
        assertEquals(list, tree.setRootChildren(list2));
    }

    /**
     * Test setRootChildren method when called on an empty tree.
     */
    @Test(expected = NullPointerException.class)
    public void testEmptySetRootChildren() {
        Tree<Integer> empty = new Tree<Integer>();
        empty.setRootChildren(list1);
    }

    /**
     * Test setRootValue on non empty tree.
     */
    @Test
    public void testSetRootValue() {
        Tree<String> stringRootVal = new Tree<String>("Root Value");
        assertEquals("Root Value", stringRootVal.setRootValue("New Root Value"));
        assertEquals("New Root Value",
                stringRootVal.setRootValue("Another Value"));
    }

    /**
     * Test setRootValue when called on an empty tree.
     */
    @Test(expected = NullPointerException.class)
    public void testEmptySetRootValue() {
        Tree<Integer> empty = new Tree<Integer>();
        empty.setRootValue(1);
    }

    /**
     * Test getRootValue method.
     */
    @Test
    public void testGetRootValue() {
        Tree<String> sTree = new Tree<String>("root v");
        assertEquals("root v", sTree.getRootValue());
    }

    /**
     * Test getRootValue on empty tree.
     */
    @Test(expected = NullPointerException.class)
    public void testEmptyGetRootValue() {
        Tree<Integer> empty = new Tree<Integer>();
        empty.getRootValue();
    }

    /**
     * Test numberOfNodes method.
     */
    @Test
    public void testNumberOfNodes() {
        assertEquals((int) 15, (int) tree1.numberOfNodes(), 0);
    }

    /**
     * Test number of trees with no children.
     */
    @Test
    public void testNumberOfLeafNodes() {
        assertEquals((int) 5, (int) tree1.numberOfLeafNodes(), 0);
    }

    /**
     * Test number of parent nodes in tree.
     */
    @Test
    public void testNumberOfInternalNodes() {
        assertEquals((int) 10, (int) tree1.numberOfInternalNodes(), 0);
    }

    /**
     * Test get root children for empty list.
     * (Modified from original)
     */
    @Test
    public void testEmptyGetRootChildren() {
        Tree<String> emp = new Tree<String>();
        assertEquals(new ArrayList<Tree<Integer>>(), emp.getRootChildren());
    }

    /**
     * Test getRootChildren method.
     */
    @Test
    public void testGetRootChildren() {
        Tree<String> tree = new Tree<String>("One");
        Tree<String> tree2 = new Tree<String>("2");
        Tree<String> tree3 = new Tree<String>("3");
        Tree<String> tree4 = new Tree<String>("4");
        List<Tree<String>> list = new ArrayList<Tree<String>>();
        list.add(tree2);
        list.add(tree3);
        list.add(tree4);
        tree.setRootChildren(list);

        List<Tree<String>> expected = new ArrayList<Tree<String>>();
        expected.add(tree2);
        expected.add(tree3);
        expected.add(tree4);

        boolean equalLists = expected.equals(tree.getRootChildren());

        assertEquals(true, equalLists);
    }

    /**
     * Test depthFirstSubtrees.
     */
    @Test
    public void testDepthFirstSubtrees() {
        List<Tree<Integer>> expected = new ArrayList<Tree<Integer>>();
        expected.add(tree1);
        expected.add(tree2);
        expected.add(tree4);
        expected.add(tree6);
        expected.add(tree3);
        expected.add(tree5);
        expected.add(tree7);
        expected.add(tree8);
        expected.add(tree9);
        expected.add(tree6);
        expected.add(tree5);
        expected.add(tree7);
        expected.add(tree8);
        expected.add(tree9);
        expected.add(tree9);

        assertEquals(expected, tree1.depthFirstSubtrees());
    }

    /**
     * Test method to recieve list of values in preorder traversal.
     */
    @Test
    public void testDepthFirstValues() {
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(2);
        expected.add(4);
        expected.add(6);
        expected.add(3);
        expected.add(5);
        expected.add(7);
        expected.add(8);
        expected.add(9);
        expected.add(6);
        expected.add(5);
        expected.add(7);
        expected.add(8);
        expected.add(9);
        expected.add(9);

        assertEquals(expected, tree1.depthFirstValues());
    }

    /**
     * Test breadth first subtrees.
     */
    @Test
    public void testBreadthFirstSubtrees() {
        List<Tree<Integer>> l = new ArrayList<Tree<Integer>>();
        l.add(tree1);
        l.add(tree2);
        l.add(tree3);
        l.add(tree4);
        l.add(tree5);
        l.add(tree6);
        l.add(tree5);
        l.add(tree9);
        l.add(tree6);
        l.add(tree7);
        l.add(tree7);
        l.add(tree8);
        l.add(tree8);
        l.add(tree9);
        l.add(tree9);

        assertEquals(l, tree1.breadthFirstSubtrees());
    }

    /**
     * Test breadth first root values.
     */
    @Test
    public void testBreadthFirstValues() {
        List<Integer> l = new ArrayList<Integer>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);
        l.add(6);
        l.add(5);
        l.add(9);
        l.add(6);
        l.add(7);
        l.add(7);
        l.add(8);
        l.add(8);
        l.add(9);
        l.add(9);

        assertEquals(l, tree1.breadthFirstValues());
    }

    /**
     * Test contains method.
     */
    @Test
    public void testContainsFalse() {
        assertEquals(false, tree1.contains(1000));
    }

    /**
     * Test contains method.
     */
    @Test
    public void testContainsTrue() {
        assertEquals(true, tree1.contains((Integer) 6));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        tree11 = new Tree<Integer>(1);
        tree12 = new Tree<Integer>(2);
        tree13 = new Tree<Integer>(3);
        tree14 = new Tree<Integer>(4);
        tree15 = new Tree<Integer>(5);
        tree16 = new Tree<Integer>(6);
        tree17 = new Tree<Integer>(7);
        tree18 = new Tree<Integer>(8);
        tree19 = new Tree<Integer>(9);

        list11 = new ArrayList<Tree<Integer>>();
        list11.add(tree12);
        list11.add(tree13);
        tree11.setRootChildren(list11);

        list12 = new ArrayList<Tree<Integer>>();
        list12.add(tree14);
        tree12.setRootChildren(list12);

        list13 = new ArrayList<Tree<Integer>>();
        list13.add(tree15);
        list13.add(tree16);
        list13.add(tree15);
        list13.add(tree19);
        tree13.setRootChildren(list13);

        list14 = new ArrayList<Tree<Integer>>();
        list14.add(tree16);
        tree14.setRootChildren(list14);

        list15 = new ArrayList<Tree<Integer>>();
        list15.add(tree17);
        tree15.setRootChildren(list15);

        list1A = new ArrayList<Tree<Integer>>();
        list1A.add(tree18);
        tree17.setRootChildren(list1A);

        list1B = new ArrayList<Tree<Integer>>();
        list1B.add(tree19);
        tree18.setRootChildren(list1B);

        Tree<String> treeFALSE = new Tree<String>("FALSE");
        Tree<Integer> treeNO = new Tree<Integer>(100);

        assertEquals(true, tree1.equals(tree11));
        assertEquals(false, tree1.equals(treeFALSE));
        assertEquals(false, tree1.equals(treeNO));
        assertEquals(false, treeNO.equals(treeFALSE));
    }

    /**
     * test for expected false on equals.
     */
    @Test
    public void testFalseEquals() {
        assertEquals(false, tree1.equals(tree2));
    }

    /**
     * Test the iterator for Tree.
     */
    @Test
    public void testIterator() {
        java.util.Iterator treeIt = tree1.iterator();
        assertEquals(tree1, tree1.iterator().next());
    }

    /**
     * Test save method.
     */
    @Test
    public void testSave() {
        try {
            assertEquals(true, tree1.save("tree1.ser"));
        } catch (Exception e) {
            System.out.println("Error saving file " + e);
        }
    }

    /**
     * Test restore method.
     */
    @Test
    public void testRestore() {
        try {
            assertEquals(true, tree1.restore("tree1.ser"));
        } catch (Exception e) {
            System.err.println("Error restoring: " + e);
        }
    }

    /**
     * Test to determine the height of the tree.
     */
    @Test
    public void testHeight() {
        assertEquals((int) 6, (int) tree1.height(), 0);
    }

    /**
     * Test to determine max amout of children in tree.
     */
    @Test
    public void testMaxDegree() {
        assertEquals((int) 4, (int) tree1.maxDegree(), 0);
    }

    /**
     * Test for getting a list of subtrees.
     */
    @Test
    public void testListSubtrees() {
        List<Tree<Integer>> expected = new ArrayList<Tree<Integer>>();
        expected.add(tree1);
        expected.add(tree2);
        expected.add(tree4);
        expected.add(tree6);
        expected.add(tree3);
        expected.add(tree5);
        expected.add(tree7);
        expected.add(tree8);
        expected.add(tree9);
        expected.add(tree6);
        expected.add(tree5);
        expected.add(tree7);
        expected.add(tree8);
        expected.add(tree9);
        expected.add(tree9);

        assertEquals(expected, tree1.subtrees());
    }

    /**
     * Test for getting list of values.
     */
    @Test
    public void testListValues() {
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(2);
        expected.add(4);
        expected.add(6);
        expected.add(3);
        expected.add(5);
        expected.add(7);
        expected.add(8);
        expected.add(9);
        expected.add(6);
        expected.add(5);
        expected.add(7);
        expected.add(8);
        expected.add(9);
        expected.add(9);

        assertEquals(expected, tree1.values());
    }

    @Test
    public void testToString() {
        System.out.println(tree1.toString());
    }

    /**
     * Test creation of hashcode for each tree.
     */
    @Test
    public void testHashCode() {
        assertEquals(0, tree1.hashCode());
    }

    /**
     * Tears down the test fixture.
     * <p/>
     * Called after every test case method.
     */
    @After
    public void tearDown() {
    }
}


