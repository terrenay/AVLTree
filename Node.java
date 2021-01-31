package avl;

public class Node {
	int value;
	int balance;
	int height;
	Node parent, left, right;

	public Node(int value, Node par, Node left, Node right) {
		this.value = value;
		this.parent = par;
		this.left = left;
		this.right = right;
	}
}
