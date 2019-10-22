
public class PlayerLinkedList{
	//makes LinkedList that contains username and level

	PlayerNode currentNode;
	PlayerNode head;
	public PlayerNode next;


	//An empty linked list
	public PlayerLinkedList(){
		head = null;
		currentNode = null;
		next = null;
	}

	public PlayerLinkedList(String username, double level){

		head = new PlayerNode(username, level);
		currentNode = head;
		next = currentNode.getNext();

		//Player node holds: 
		head.username = username;
		head.level = level;
		head.next = null;
	}

	//takes name and returns level the person is on
	public double findLevel(String username){
		currentNode = head;

		if(currentNode == null){
			return 0;
		}

		while(currentNode != null){
			if(username.equals(currentNode.getName())){
				return currentNode.getLevel();
			}
			else{
				currentNode = currentNode.getNext();
			}
		}

		return 0;
	}

	//adds new node itself
	public void add(PlayerNode node){
		currentNode = head;

		if(currentNode == null){
			head = node;
		}

		//goes to the end of the list to add the node
		else{
			while(currentNode.getNext() != null){
				currentNode = currentNode.getNext();
			}
			currentNode.setNext(node);
		}
	}

	//adds info to new node
	public void addInfo(String username, double level){
		PlayerNode node = new PlayerNode(username, level);

		if(head == null){
			head = node;
		}
		
		else{
			currentNode = head;

			while(currentNode.getNext() != null){
				currentNode = currentNode.getNext();
			}
			
			currentNode.setNext(node);
		}
	}
	
	/*displays the content of the playerLinkedList*/
	public void display(){
		currentNode = head;
		
		while(currentNode != null){
			System.out.println(currentNode.getName() + currentNode.getLevel());
			System.out.println("the next node is " + currentNode.getNext());
			currentNode = currentNode.getNext();
		}
		
	}
}
