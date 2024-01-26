import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

	protected static class RBTNode<T> extends Node<T> {
   		public int blackHeight = 0;
    		public RBTNode(T data) { super(data); }
    		public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
   		public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
    		public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
	}

	protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> node) {
		if (node.getUp() == null) node.blackHeight = 1;

		if (node.blackHeight == 0 && node.getUp().blackHeight == 0) {
			if (node.isRightChild() == node.getUp().isRightChild()) {
				RBTNode<T> nodeB = node.getUp();
				RBTNode<T> nodeC = node.getUp().getUp();
				RBTNode<T> nodeD;

				if (node.isRightChild()) {
					nodeD = node.getUp().getUp().getDownLeft();
				} else {
					nodeD = node.getUp().getUp().getDownRight();
				}

				if (nodeD == null || nodeD.blackHeight == 1) {
					rotate(nodeB, nodeC);
					nodeB.blackHeight = 1;
					nodeC.blackHeight = 0;
				} else {
					nodeC.blackHeight = 0;
					nodeB.blackHeight = 1;
					nodeD.blackHeight = 1;
					enforceRBTreePropertiesAfterInsert(nodeC);
				}

			} else {
				RBTNode<T> nodeA = node.getUp();
				RBTNode<T> nodeC = node.getUp().getUp();
				RBTNode<T> nodeD;
				
				if (node.isRightChild()) {
                                        nodeD = node.getUp().getUp().getDownLeft();
                                } else {
                                        nodeD = node.getUp().getUp().getDownRight();
                                }

				if (nodeD == null || nodeD.blackHeight == 1) {
					rotate(node, nodeA);
					rotate(node, nodeC);
					node.blackHeight = 1;
					nodeC.blackHeight = 0;
				} else {
					nodeC.blackHeight = 0;
					nodeA.blackHeight = 1;
					nodeD.blackHeight = 1;
					enforceRBTreePropertiesAfterInsert(nodeC);
				}
			}
		}
	}

	@Override
    	public boolean insert(T data) throws NullPointerException {

		if (data == null) {
			throw new NullPointerException();
		}

		// Create a new RBTNode with the data
		RBTNode<T> newNode = new RBTNode<>(data);
		boolean insert = super.insertHelper(newNode);

		if (insert) {
			enforceRBTreePropertiesAfterInsert(newNode);
			if (root instanceof RBTNode) {
				((RBTNode<T>) root).blackHeight = 1;
			}
		}
		return insert;
    	}

    	@Test
    	public void testInsertCase1() {
        	// Test inserting into empty tree
        	RedBlackTree<Integer> tree = new RedBlackTree<>();
        	tree.insert(10);
        
        	// Assert that root is black
        	assertTrue(((RBTNode<Integer>) tree.root).blackHeight == 1);
    	}

    	@Test
    	public void testInsertCase2() {
        	// Test inserting red node with black parent
        	RedBlackTree<Integer> tree = new RedBlackTree<>();
        	tree.insert(10);
        	tree.insert(5); // Insert red node with black parent

        	// Assert that tree is still valid
        	assertTrue(((RBTNode<Integer>) tree.root).blackHeight == 1);
    	}

    	@Test
    	public void testInsertCase3() {
        	// Test inserting another red node
        	RedBlackTree<Integer> tree = new RedBlackTree<>();
        	tree.insert(10);
        	tree.insert(5);
        	tree.insert(15); // Insert red node with black parent

        	// Assert that tree is valid 
        	assertTrue(((RBTNode<Integer>) tree.root).blackHeight == 1);
    	}
}
