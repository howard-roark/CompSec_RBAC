/*
*Sources Used:
*   -Core Java: Volume I -- Fundamentals 9th Edition Chapter 6
*       Interfaces, Chapter 13 Collections
*   -Java API: Interface List<E>
*   -The Java Tutorials The List Interface
*   -The Java Tutorials The Set Interface
*   -A lot of guessing from Console error reports
*/

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.io.Serializable;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;


/**
 * A general purpose tree with arbitrary degree. The empty tree contains no nodes.
 * A non empty tree has exactly one node designated as its root and zero or more
 * child subtrees each of whose are connected with the root by a directed edge.
 *
 * @author McGuire, Matthew (mmcgui36@msudenver.edu)
 * @version initial(0.2) [10 September 2013]
 */

public class Tree<V> implements Iterable<Tree<V>>, Serializable {

    /**
     * Serialization version indicator used to determine
     * if a file is compatible with the class
     */
    private static final long serialVersionUID = 20130910L;
    /**
     * Children of the root of this tree
     */
    private List<Tree<V>> rootChildren;
    /**
     * Root Value of the Tree
     */
    private V rootValue;
    /**
     * Default save/restore file name
     */
    private static final String SERIAL_FILENAME = "tree.ser";
    /**
     * Empty tree indicator
     *
     * @serial
     */
    private Boolean isEmpty;

    /**
     * Tree Constructor Method
     */
    public Tree() {
        this.rootValue = null;
        this.rootChildren = null;
        this.isEmpty = true;
    }

    /**
     * Tree Constructor passing in value of V as root value
     *
     * @param rootValue will be the root value of the tree
     */
    public Tree(final V rootValue) {
        this.rootValue = rootValue;
        this.rootChildren = new ArrayList<Tree<V>>();
        this.isEmpty = false;
    }

    /**
     * Returns a collection of subtrees in breadth first order
     *
     * @return List of subtrees
     */
    public List<Tree<V>> breadthFirstSubtrees() {
        List<Tree<V>> subTrees = new ArrayList<Tree<V>>();
        if (this.rootChildren != null) {
            Queue<Tree<V>> que = new LinkedList<Tree<V>>();
            que.add(this);
            while (que.size() > 0) {
                Tree<V> tree = que.remove();
                subTrees.add(tree);
                if (tree.rootChildren != null) {
                    for (Tree<V> element : tree.getRootChildren()) {
                        que.add(element);
                    }
                }
            }
        }
        return subTrees;
    }

    /**
     * Returns a collection of values in breadth first order
     *
     * @return list of values
     */
    public List<V> breadthFirstValues() {
        List<V> values = new ArrayList<V>();
        if (this.rootChildren != null) {
            Queue<Tree<V>> que = new LinkedList<Tree<V>>();
            que.add(this);
            while (que.size() > 0) {
                Tree<V> tree = que.remove();
                values.add(tree.getRootValue());
                if (tree.rootChildren != null) {
                    for (Tree<V> element : tree.getRootChildren()) {
                        que.add(element);
                    }
                }
            }
        }
        return values;
    }

    /**
     * Search for specific value in tree
     *
     * @param value The value to be searched in the Tree
     * @return boolean value for found / not found value
     */
    public boolean contains(V value) {
        boolean val = false;
        if (this.rootValue.equals(value)) {
            val = true;
        } else if (this.rootChildren != null) {
            for (Tree<V> element : this.rootChildren) {
                val = val || element.contains(value);
            }
        }
        return val;
    }

    /**
     * Column order of nodes in tree
     *
     * @return list of subtrees
     */
    public List<Tree<V>> depthFirstSubtrees() {
        List<Tree<V>> returnedSubtrees = new ArrayList<Tree<V>>();
        returnedSubtrees.add(this);
        if (this.rootChildren != null) {
            for (Tree<V> element : this.rootChildren) {
                returnedSubtrees.addAll(element.depthFirstSubtrees());
            }
        }
        return returnedSubtrees;
    }

    /**
     * Returns a collection of values in the subtree nodes in
     * column first order
     *
     * @return list of values in tree
     */
    public List<V> depthFirstValues() {
        List<V> returnedValues = new ArrayList<V>();
        returnedValues.add(this.rootValue);
        if (this.getRootChildren() != null) {
            for (Tree<V> element : this.getRootChildren()) {
                if (element != null) {
                    returnedValues.addAll(element.depthFirstValues());
                }
            }
        }
        return returnedValues;
    }

    /**
     * Return a list of all children of particular root, empty list if no
     * children already exist.
     *
     * @return List of children in this tree
     */
    public final List<Tree<V>> getRootChildren() {
        List<Tree<V>> children;
        if (this.isEmpty()) {
            children = new ArrayList<Tree<V>>();
        } else {
            children = (this.rootChildren);
        }
        return children;
    }

    /**
     * Returns a value for the root node of this tree
     *
     * @return Value at Root
     */
    public final V getRootValue() {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
        return this.rootValue;
    }

    /**
     * Return the height of the Tree
     *
     * @return Height of Tree
     */
    public final int height() {
        if (this.isEmpty()) {
            return 0;
        }
        if (this.getRootChildren().size() < 1) {
            return 1;
        }
        int height = 0;
        for (Tree<V> child : this.getRootChildren()) {
            height = Math.max(height, child.height());
        }
        return 1 + height;
    }

    /**
     * Returns an iterator over the subtrees of the Tree
     *
     * @return returns an iterator to move through treeList
     */
    @Override
    public Iterator<Tree<V>> iterator() {
        return new TreeIterator<Tree<V>>(this);
    }

    /**
     * Iterator for the Tree class
     */
    private class TreeIterator<T> implements Iterator<Tree<V>> {
        Iterator<Tree<V>> internalIterator;

        /**
         * Construct iterator
         *
         * @param root of the tree to be iterated
         */
        public TreeIterator(Tree<V> root) {
            List<Tree<V>> subs = root.subtrees();
            internalIterator = subs.iterator();
        }

        /**
         * Overriding java hasNext for this hasNext
         *
         * @return boolean true for has a following element otherwise false
         */
        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        /**
         * Overriding java next()
         *
         * @return Tree<V> for the next element in tree
         */
        @Override
        public Tree<V> next() {
            return internalIterator.next();
        }

        /**
         * Remove not supported
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() method not supported");
        }
    }

    /**
     * Return the maximum amount of children for Tree
     *
     * @return Max amount of children for tree
     */
    public int maxDegree() {
        int mostChildren = 0;
        for (Tree<V> element : this.rootChildren) {
            mostChildren = Math.max(mostChildren, element.getRootChildren().size());
        }
        return mostChildren;
    }

    /**
     * Return the number of parents in the Tree
     *
     * @return amount of parent nodes
     */
    public final int numberOfInternalNodes() {
        if (this.isEmpty) {
            return 0;
        }
        if (this.getRootChildren().size() < 1) {
            return 0;
        }
        int numIntNodes = 1;
        for (Tree<V> element : this.getRootChildren()) {
            numIntNodes += element.numberOfInternalNodes();
        }
        return numIntNodes;
    }

    /**
     * Return the number of all nodes other than parent nodes
     *
     * @return number of all non-parent nodes in tree
     */
    public final int numberOfLeafNodes() {
        if (this.isEmpty()) {
            return 0;
        }
        if (this.getRootChildren().size() < 1) {
            return 1;
        }
        int numLeafNodes = 0;
        for (Tree<V> child : this.getRootChildren()) {
            numLeafNodes += child.numberOfLeafNodes();
        }
        return numLeafNodes;
    }

    /**
     * Return total number of nodes in tree
     *
     * @return Total number of nodes in tree
     */
    public int numberOfNodes() {
        if (this.isEmpty) {
            return 0;
        }
        int numNodes = 1;
        for (Tree<V> element : this.getRootChildren()) {
            numNodes += (element != null) ? element.numberOfNodes() : 0;
        }
        return numNodes;
    }

    /**
     * Restore a tree from a file
     * <br /><em>Postconditions:</em>
     * <blockquote>If successful, previous contents of this tree have
     * been replaced by the contents of the file.
     * If unsuccessful, content of the tree is unchanged.</blockquote>
     *
     * @param filename filename to restore tree from
     * @return <code>true</code> if successful restore;
     * <code>false</code> otherwise
     * @throws java.io.IOException if unexpected IO error
     */
    public final boolean restore(final String filename)
            throws java.io.IOException {
        boolean success = false;
        String treeFileName = filename;
        if (treeFileName == null) {
            treeFileName = Tree.SERIAL_FILENAME;
        }
        Tree<V> restored = null;
        try {
            InputStream file = new FileInputStream(treeFileName);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                @SuppressWarnings("unchecked") // For type erasure
                        Tree<V> retrieved = (Tree<V>) input.readObject();
                restored = retrieved;
            } finally {
                input.close();
                success = true;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Unsuccessful deserialization: Class not found. "
                    + e);
            success = false;
        } catch (IOException e) {
            System.err.println("Unsuccessful deserialization: " + e);
            success = false;
        } catch (Exception e) {
            System.err.println("Unsuccessful deserialization: " + e);
            success = false;
        }
        if (restored == null) {
            System.err.println("Unsuccessful deserialization: restored == null");
            success = false;
        } else {
            this.rootValue = restored.rootValue;
            this.rootChildren = restored.rootChildren;
            this.isEmpty = restored.isEmpty;
        }
        return success;
    }

    /**
     * Save this tree to a file
     *
     * @param filename filename to store this tree to
     * @return <code>true</code> successfully saved
     * <code>false</code> otherwise
     * @throws java.io.IOException if unexpected IO error
     */
    public final boolean save(final String filename)
            throws java.io.IOException {
        boolean success = true;
        String treeFileName = filename;
        if (treeFileName == null) {
            treeFileName = Tree.SERIAL_FILENAME;
        }
        //Serialize the tree
        try {
            OutputStream file = new FileOutputStream(treeFileName);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(this);
            } finally {
                output.close();
            }
        } catch (IOException e) {
            System.err.println("Unsuccessful Save: " + e);
            throw e;
        } catch (Exception e) {
            System.err.println("Unsuccessful Save:" + e);
            throw new IOException();
        }
        //Attempt to deserialize the graph as verification
        try {
            InputStream file = new FileInputStream(treeFileName);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                @SuppressWarnings("unchecked") //For Type Erasure
                        Tree<V> restored = (Tree<V>) input.readObject();
                //Make sure deserialized data matches original
                if (!this.equals(restored)) {
                    System.err.println
                            ("[1] State restore did not match save!");
                    success = false;
                }
                if (!this.equals(restored)) {
                    System.err.println
                            ("[2] State restore did not match save!");
                    success = false;
                }
            } finally {
                input.close();
            }
        } catch (ClassNotFoundException e) {
            System.err.println
                    ("Unsuccessful deserialization: Class not found. " + e);
            success = false;
        } catch (IOException e) {
            System.err.println("Unsuccessful deserialization: " + e);
            success = false;
        } catch (Exception e) {
            System.out.println("Unsuccessful deserialization: " + e);
            success = false;
        }
        return success;
    }

    /**
     * Pass in new children nodes to tree
     *
     * @param children new list of children
     * @return return the value of the old children
     * that were replaced
     */
    public final List<Tree<V>> setRootChildren(final List<Tree<V>> children) {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
        List<Tree<V>> prevRootChildren = this.getRootChildren();
        this.rootChildren = children;
        return prevRootChildren;
    }

    /**
     * Set value for root
     *
     * @param value new value of root
     * @return previous value of root
     */
    public final V setRootValue(final V value) {
        if (this.isEmpty()) {
            throw new NullPointerException();
        }
        V prevRootVal = this.rootValue;
        this.rootValue = value;
        return prevRootVal;
    }

    /**
     * Returns the subtrees for this tree
     *
     * @return the subtrees
     */
    public List<Tree<V>> subtrees() {
        List<Tree<V>> sTrees =
                new ArrayList<Tree<V>>(this.depthFirstSubtrees());
        return sTrees;
    }

    /**
     * Returns the values of this tree
     *
     * @return the values of this tree
     */
    public List<V> values() {
        List<V> vals = new ArrayList<V>(this.depthFirstValues());
        return vals;
    }

    /**
     * Returns an integer value for the hashCode of this tree
     *
     * @return the value of the hash code
     */
    public final int hashCode() {
        int nodes = this.numberOfNodes();
        int leafs = this.numberOfLeafNodes();
        int code = (5 * nodes + 3 * leafs) % 15;
        return code;
    }

    /**
     * Deep test of equality between 2 trees
     *
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public final boolean equals(Object obj) {
        Queue<Tree<V>> org = new LinkedList<Tree<V>>();
        Queue<Tree<V>> pass = new LinkedList<Tree<V>>();
        org.addAll(this.depthFirstSubtrees());
        pass.addAll(((Tree<V>) obj).depthFirstSubtrees());
        boolean result = false;
        int comp = 0;
        if (obj == this) {
            result = true;
        } else if (!obj.getClass().equals(this.getClass()) ||
                org.size() != pass.size() ||
                pass.hashCode() != org.hashCode()) {
            result = false;
        } else {
            while (org.size() > 0) {
                comp += (org.remove().compareTo(pass.remove()));
            }
            if (comp == 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * helper method for equals()
     *
     * @return 0 if equal else 1
     */
    private final int compareTo(Tree<V> v) {
        int comp = 1;
        if (this.getRootValue().equals(v.getRootValue())) {
            comp = 0;
        } else {
            comp = 1;
        }
        return comp;
    }

    /**
     * Determine if this is an empty tree
     *
     * @return <code>true</code> is this is an empty tree,
     * <code>false</code> otherwise
     */
    private boolean isEmpty() {
        return this.isEmpty;
    }

    /**
     * Return string value for the rootValue of a Tree
     *
     * @return String
     */
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "-";
        }
        String rendering = "<";
        rendering += this.getRootValue();
        for (Tree<V> child : this.getRootChildren()) {
            rendering += ", " + child.toString();
        }
        rendering += ">";
        return rendering;
    }
}

