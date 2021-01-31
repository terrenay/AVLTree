package avl;

import java.util.LinkedList;
import java.util.Scanner;

public class AVLTree {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		AVLTree tree = new AVLTree();
		for (int i = 0; i < testCases; i++) {
			tree.insert(scanner.nextInt());
		}
		printBFS(tree);
	}

	private static void printBFS(AVLTree tree) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addLast(tree.root);
		while (queue.size() != 0) {
			Node cur = queue.poll();
			System.out.println("Value: " + cur.value + ", height: " + cur.height + ", balance: " + cur.balance
					+ ".   Children: " + (cur.left == null ? "-" : cur.left.value) + " and "
					+ (cur.right == null ? "-" : cur.right.value));
			if (cur.left != null)
				queue.addLast(cur.left);
			if (cur.right != null)
				queue.addLast(cur.right);
		}
	}

	Node root; // can change!

	public void insert(int newVal) {
		if (root == null) {
			root = new Node(newVal, null, null, null);
		} else {
			Node parent = null;
			Node cur = root;
			boolean right = true; // wenn cur das right child von parent ist
			while (cur != null) {
				parent = cur;
				if (newVal <= cur.value) {
					cur = cur.left;
					right = false;
				} else {
					cur = cur.right;
					right = true;
				}
			}
			Node newNode = new Node(newVal, parent, null, null);
			if (right) {
				parent.right = newNode;
			} else {
				parent.left = newNode;
			}
			rebalance(newNode);
		}
	}

	/**
	 * Pivot must have a right child (non-null)
	 * 
	 * @param pivot
	 */
	private void leftRotate(Node pivot) {
		Node sub = pivot.right;
		Node parent = pivot.parent;
		if (pivot == root)
			root = sub;
		sub.parent = parent;
		pivot.parent = sub;
		pivot.right = sub.left;
		sub.left = pivot;
		if (pivot.right != null)
			pivot.right.parent = pivot;
		if (parent != null) {
			if (pivot == parent.left)
				parent.left = sub;
			else
				parent.right = sub;
		}
		restoreHeight(pivot.left); //omg ist das hässlich
		restoreHeight(pivot.right);
		restoreHeight(pivot);
		restoreHeight(sub.right);
		restoreHeight(sub);
	}

	/**
	 * Pivot must have a left child (non-null)
	 * 
	 * @param pivot
	 */
	private void rightRotate(Node pivot) {
		Node sub = pivot.left;
		Node parent = pivot.parent;
		if (pivot == root)
			root = sub;
		sub.parent = parent;
		pivot.parent = sub;
		pivot.left = sub.right;
		sub.right = pivot;
		if (pivot.left != null)
			pivot.left.parent = pivot;
		if (parent != null) {
			if (pivot == parent.left)
				parent.left = sub;
			else
				parent.right = sub;
		}
		restoreHeight(pivot.left);
		restoreHeight(pivot.right); // zur Sicherheit lieber alle updaten xD wichtig: von unten nach oben
		restoreHeight(pivot);
		restoreHeight(sub.left);
		restoreHeight(sub);
	}

	private void restoreHeight(Node node) {
		if (node == null)
			return;
		if (node.left == null && node.right == null) {
			node.height = 0;
			return;
		}
		node.height = Math.max(node.left == null ? 0 : node.left.height, node.right == null ? 0 : node.right.height)
				+ 1;
	}

	/**
	 * Give the leaf as parameter after insertion
	 * 
	 * @param child Must have a parent
	 */
	private void rebalance(Node child) {
		Node pivot = child.parent;
		for (; pivot != null && child != null; child = pivot, pivot = pivot.parent) {
			int balance = 0;
			restoreHeight(pivot);
			if (pivot.right == null) {
				balance = -(pivot.left.height + 1);
			} else if (pivot.left == null) {
				balance = pivot.right.height + 1;
			} else {
				balance = pivot.right.height - pivot.left.height;
			}
			pivot.balance = balance;

			if (balance > 2 || balance < -2)
				throw new AssertionError();
			if (balance < 2 && balance > -2)
				continue;

			if (balance == -2 && child.balance == -1) { // Right Rotation
				rightRotate(pivot); // Achtung, pivot und child haben Platz vertauscht
				pivot.balance = 0;
				child.balance = 0;
				return;
			}
			if (balance == 2 && child.balance == 1) { // Left Rotation
				leftRotate(pivot);
				pivot.balance = 0;
				child.balance = 0;
				return;
			}
			if (balance == -2 && child.balance == 1) { // Left Right Rotation
				Node newChild = child.right;
				leftRotate(child);
				rightRotate(pivot);
				pivot.balance = 0;
				child.balance = 0;
				newChild.balance = 0;
				return;
			}
			if (balance == 2 && child.balance == -1) { // Right Left Rotation
				Node newChild = child.left;
				rightRotate(child);
				leftRotate(pivot);
				pivot.balance = 0;
				child.balance = 0;
				newChild.balance = 0;
				return;
			}
		}

	}
}
