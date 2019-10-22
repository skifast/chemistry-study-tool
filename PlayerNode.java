
public class PlayerNode{

	String username;
	double level;
	PlayerNode next;
	PlayerNode head;

	/*holds username, level, and points to the next node in the linked list*/
	public PlayerNode(String username, double l){
		this.username = username;
		this.level = l;
		this.next = null;
	}

	public PlayerNode getNext() {
		return next;
	}
	public void setNext(PlayerNode next) {
		this.next = next;
	}
	public String getName() {
		return username;
	}
	public void setName(String name) {
		this.username = name;
	}
	public double getLevel() {
		return level;
	}
	public void setLevel(double l) {
		this.level = l;
	}

	public void addInfo(String name, double l){
		head.setName(name);
		head.setLevel(l);
	}
}