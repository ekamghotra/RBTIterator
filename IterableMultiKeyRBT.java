import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.Assert.*;
import java.util.NoSuchElementException;
import java.util.Stack;

public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>> implements IterableMultiKeySortedCollectionInterface<T>{
public Comparable<T> start;
public int numberOfKeys;

/**
* Inserts value into tree that can store multiple objects per key by keeping
* lists of objects in each node
* @param key object to insert
* @return true if new node inserted, false if key added into existing node
*/

public boolean insertSingleKey(T key) {
	KeyList<T> newKeyList = new KeyList<>(key);
	Node<KeyListInterface<T>> currentNode = findNode(newKeyList);
	numberOfKeys++;
	if (currentNode == null) { // nonexistent key
		insert(newKeyList);
		return true;
	} else { // key exists
		KeyListInterface<T> keyList = currentNode.data;
		keyList.addKey(key);
		return false;
	}
	
}

/**
* @return number of values in the tree
*/
public int numKeys() {
	return numberOfKeys;
}

/**
* Returns iterator that does an in-order iteration over the tree
*/
public Iterator<T> iterator() {
	return new Iterator<>() {
		private Stack<Node<KeyListInterface<T>>> stack = getStartStack();
		private Iterator<T> iterator = null;

		@Override
		public boolean hasNext() {
			return !stack.isEmpty() || (iterator != null && iterator.hasNext());
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			while (iterator == null || !iterator.hasNext()) {
				if (!stack.isEmpty()) {
					Node<KeyListInterface<T>> node = stack.pop();
					iterator = node.data.iterator();
					if (node.down[1] != null) {
						stack.push(node.down[1]);
					}
				}
			}
			return iterator.next();
		}
	};
}

/**
* Sets starting point for iterations. Iterations will start at
* starting point or key closest to it. This setting is remembered
* until a reset. Passing in null disables starting point.
* @param start the start point for iterations
*/
public void setIterationStartPoint(Comparable<T> start) {
	this.start = start;
}

/**
* Calls old clear mehod of BinarySearchTree and
* sets field that counds number of keys to 0
*/
public void clear() {
	super.clear();
	numberOfKeys = 0;
}

/**
* helper method
*/
protected Stack<Node<KeyListInterface<T>>> getStartStack() {
	Stack<Node<KeyListInterface<T>>> stack = new Stack();
	Node<KeyListInterface<T>> node = root;
	if (start == null) {
		while (node != null) {
			stack.push(node);
			node = node.down[0];
		}
	} else {
		while (node != null) {
			int comparison = start.compareTo(((KeyListInterface<T>) node.data).iterator().next());
			if (comparison <= 0) {
				stack.push(node);
				node = node.down[0]; 
			}
			if (comparison > 0) {
				node = node.down[1];
			} else {
				node = node.down[1];
			}
		}
	}
	return stack;
}

/**
* Test ensures number of keys is 0, then adds a key, then checks number of keys again
*/
@Test
public void JunitTest1() {
	IterableMultiKeyRBT test1 = new IterableMultiKeyRBT();

	if (test1.numKeys() != 0) {
		 Assertions.fail("Wrong number of keys");
	}

	test1.insertSingleKey(4);

	if (test1.numKeys() != 1) {
		Assertions.fail("Wrong number of keys");
	}
}


/**
* Test adds two keys and ensures number of keys equals two
*/
@Test
public void JunitTest2() {
	IterableMultiKeyRBT test2 = new IterableMultiKeyRBT();
	test2.insertSingleKey(5);
	test2.insertSingleKey(3);

	if (test2.numKeys() != 2) {
		Assertions.fail("Number of keys isn't correct");
	}
}

/**
* Test ensures insertSingleKey() is false when a conmflicting type passed in for key
*/
@Test
public void JunitTest3() {
	IterableMultiKeyRBT test3 = new IterableMultiKeyRBT();
	test3.insertSingleKey(3);
	if (test3.insertSingleKey("e") != false){
		Assertions.fail("Key somehow added");
	}
}
}
