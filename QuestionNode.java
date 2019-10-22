
public class QuestionNode {
	public String name;
	public String compound;
	public boolean used;
	public int difficulty;
	public QuestionNode next;
	public QuestionNode node;

	public QuestionNode(){
		this.name = null;
		this.compound = null;
		this.difficulty = 0;
		next = null;
	}

	/*Question node contains all of the chemistry information for a single line of a type file
	 * regardless of the type*/
	public QuestionNode(String compound, String name, int difficulty){
		//makes current node word and count, sets next node w/o any contents
		this.name = name;
		this.compound = compound;
		this.difficulty = difficulty;
		next = null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCompound(String compound) {
		this.compound = compound;
	}

	public String getCompound() {
		return compound;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean getUsed() {
		return used;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setNext(QuestionNode next) {
		this.next = next;
	}

	public QuestionNode getNext() {
		return next;
	}


}


