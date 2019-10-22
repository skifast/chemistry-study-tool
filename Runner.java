import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/*refer to readme
 *
 * */


public class Runner extends Applet implements Runnable, ActionListener{
	//height and width of the applet
	final int WIDTH = 1200, HEIGHT = 600;
	Thread thread;

	Graphics gfx;
	Image img;

	/*These are all of the files used in this program. The fileNames are packaged into a fileNames array for easy parameters*/
	String userNameFile = "userNameFile.txt";
	String fileName1 = "PolyatomicIons.txt";
	String fileName2 = "TernaryCompounds.txt";
	String fileName3 = "chemType3.txt";
	String fileName4 = "chemType4.txt";
	String fileName5 =  "/Users/" + System.getProperty("user.name") + "/Documents/playerInfoFile.txt";
	String fileName6 =  "/Users/" + System.getProperty("user.name") + "/Documents/errors.txt";

	//images used in the garden
	Image garden;
	Image tomato;
	Image cabbage;
	Image pumpkin;
	Image carrot;
	Image corn;

	/*an array of all of the names of the files
	 * the four types can be accessed with just their name
	 * so this array is implemented so all filenames can be 
	 * passed as a single parameter*/
	String[] fileNames;
	
	String username;
	double level;
	
	//the playerLinkedList holds the usernames and scores of all previous players
	PlayerLinkedList playerLinkedList;
	//the masterarray holds the name, compound, and personal difficulty of 
	//the questions being asked
	MasterArray masterArray;

	/*These are arrays of QuesitonNodes that are used to determine which questions
	 * are difficu;t for a user for a single turn. In the future I want to 
	 * make the system "learn" over multiple turns.*/
	QuestionNode[] hard;
	QuestionNode[] harder;
	QuestionNode[] hardest;
	QuestionNode[] unanswered;
	QuestionNode[] easy;
	//like the fileName array, this  exists for parameter reasons
	QuestionNode[][] levels;

	/*newNode is the question being asked currently*/
	QuestionNode newNode = null;

	//deals with the files
	IOFile fileReaderWriter;
	//deals with "learning" difficulties
	Lottery lottery;

	Button screen1b1;
	Button screen2b1;
	Button screen2b2;
	Button screen2b3;
	Button screen2b4;
	Button screen2b5;
	Button screen3b1;
	Button screen3b2;
	Button screen3b3;
	Button screen3b4;
	Button screen3b5;
	Button screen4b1;
	
	Label screen3l1;
	Label screen3l3;
	Label winningLabel;
	String screen3LabelText;

	//difficulty of the question
	int currentDifficulty = 0; 
	//graphics dots that allow the user to see their progress
	int progressDot = 0; 

	/*these booleans exist so the user can select multiple types.
	 *they determine whether or not to add compounds from a certain
	 *type and also to highlight or not highlight the button using
	 *graphics*/
	Boolean s2b1bool = false;
	Boolean s2b2bool = false;
	Boolean s2b3bool = false;
	Boolean s2b4bool = false;
	Boolean s3b4bool = true;
	Boolean s3b5bool = false;
	Boolean gardenbool = false;
	Boolean hintbool = false;
	Boolean gameOver = false;
	Boolean labelMade = false;

	Boolean goBool = false;

	TextField screen1tf1;
	TextField screen3tf1;
	
	RedirectErrors errorCatch;

	public void init(){
		
		this.resize(WIDTH, HEIGHT);

		/*gfx is an off screen canvas that things are added to.
		 * This off screen canvas prevents flickering graphics*/
		img = createImage(WIDTH, HEIGHT);
		gfx = img.getGraphics();

		garden = getImage(getDocumentBase(),"garden.png");  
		garden = garden.getScaledInstance(650, garden.getHeight(this), HEIGHT);
		tomato = getImage(getDocumentBase(), "Tomato.png");
		tomato = tomato.getScaledInstance(70, 70, 70);
		cabbage = getImage(getDocumentBase(), "cabbage.png");
		cabbage = cabbage.getScaledInstance(70, 70, 70);
		pumpkin = getImage(getDocumentBase(), "pumpkin.png");
		pumpkin = pumpkin.getScaledInstance(70, 70, 70);
		carrot = getImage(getDocumentBase(), "carrot.png");
		carrot = carrot.getScaledInstance(70, 70, 70);
		corn = getImage(getDocumentBase(), "corn.png");
		corn = corn.getScaledInstance(70, 70, 70);
		
		/*Level starts out as zero. Will be changed if the user has 
		 * played before.*/
		level = 0; 
		
		setBackground(Color.CYAN);

		/*Initializes all the things*/
		fileNames = new String[4];
		fileReaderWriter = new IOFile();
		masterArray = new MasterArray();
		playerLinkedList = new PlayerLinkedList();
		lottery = new Lottery();
		
		thread = new Thread(this);
		thread.start();	
	}

	public void run() {
		fileNames[0] = fileName1;
		fileNames[1] = fileName2;
		fileNames[2] = fileName3;
		fileNames[3] = fileName4;
		

		/*Reads the previous usernames and adds the player nodes to a linked list*/
		try {
			playerLinkedList = fileReaderWriter.fileReadingLinkedList(fileName5, playerLinkedList);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			errorCatch = new RedirectErrors();
		}
		
		/*reads the selected type files and adds them to their perspective arrays*/
		try {
			masterArray = fileReaderWriter.fileReadingArray(fileNames, masterArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorCatch = new RedirectErrors();
		}
		
		drawSignIn();
	}
	
	public  void drawSignIn(){
		/*graphics with buttons/input etc*/
		screen1tf1 = new TextField(25);
		screen1tf1.setBounds(425, 250, 170, 60);

		setLayout(new BorderLayout());

		/*When this button is pressed the user is
		 * taken to the select games screen*/
		screen1b1 = new Button("Go!");
		screen1b1.setBounds(625, 250, 60, 60);
		screen1b1.addActionListener(this);

		/*key listeners allow the user to press
		 * "go" or simply type enter*/
		KeyListener m = new KeyListener(){
			public void keyPressed(KeyEvent k){
				int keyCode = k.getKeyCode();
				if(keyCode == KeyEvent.VK_ENTER){
					//username is assigned to the user input into the username box
					username = screen1tf1.getText();
					
					/*returns level of the player if they are in the system. Otherwise
					 * returns 0 so that level remains at 0 in that case*/
					level = playerLinkedList.findLevel(username);
					dualScreens();
					//they can select their games now
					drawSelectGames();
					/*gardenbool is true so that the user can see their garden.
					 * If they are in the system, their progress gets saved and
					 * they have the same number of plants as they did on their last
					 * turn. otherwise there are no plants*/
					gardenbool = true;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};

		screen1tf1.addKeyListener(m);

		Label screen1label1 = new Label("Enter Username");
		screen1label1.setBounds(425, 150, 250, 60);
		setLayout(new BorderLayout());

		Font font = new Font("Times New Roman", Font.PLAIN, 22);
		screen1label1.setFont(font);
		setLayout(new BorderLayout());

		add(screen1tf1); add(screen1b1); add(screen1label1);
		setLayout(new BorderLayout());
	}

	public void drawSelectGames(){
		//different buttons for the available types
		screen2b1 = new Button("Polyatomic Ions");
		screen2b2 = new Button("Ternary Compounds");
		screen2b5 = new Button("Next");
		
		/*
		//These types have not been added in yet
		screen2b3 = new Button("Type 3");
		screen2b4 = new Button("Type 4");
		
		*/

		screen2b1.setBounds(700, 150, 400, 170);
		screen2b2.setBounds(700, 350, 400, 170);
		screen2b5.setBounds(1050, 540, 105, 60);
		/*
		screen2b3.setBounds(700, 300, 400, 75);
		screen2b4.setBounds(700, 400, 400, 75);
		*/
	

		screen2b1.addActionListener(this);
		screen2b2.addActionListener(this);
		screen2b5.addActionListener(this);
		/*
		screen2b3.addActionListener(this);
		screen2b4.addActionListener(this);
		*/
		

		
		screen2b1.setFocusable(false);
		screen2b2.setFocusable(false);
		screen2b5.setFocusable(false);
		/*
		screen2b3.setFocusable(false);
		screen2b4.setFocusable(false);
		*/

		Label screen2l1 = new Label("Select Type:");
		Font font = new Font("Times New Roman", Font.PLAIN, 26);
		screen2l1.setFont(font);
		screen2l1.setBounds(830, 100, 400, 50);


		//add(screen2b3); add(screen2b4); 
		add(screen2b1); add(screen2b2); add(screen2b5);
		add(screen2l1); 
	}

	public void dualScreens(){
		removeAll();
		setBackground(Color.WHITE);
	}

	/*called when certain buttons are pressed -- see action Listeners*/
	public  void runGame(){
		//pool of 10 question nodes based on difficulty
		QuestionNode[] randomLotto = lottery.Lottery(hard, harder, hardest, unanswered, easy);
		//gets a random node from those 10 nodes
		newNode = lottery.getRandomNode(randomLotto);
	}

	public void actionPerformed(ActionEvent e){
		//if next is pressed and the text box is not empty
		//will not run if the text box is empty
		if(e.getSource().equals(screen1b1) && !screen1tf1.getText().equals("")){
			//username is assigned to the user input into the username box
			username = screen1tf1.getText();
			
			/*returns level of the player if they are in the system. Otherwise
			 * returns 0 so that level remains at 0 in that case*/
			level = playerLinkedList.findLevel(username);
			dualScreens();
			//they can select their games now
			drawSelectGames();
			/*gardenbool is true so that the user can see their garden.
			 * If they are in the system, their progress gets saved and
			 * they have the same number of plants as they did on their last
			 * turn. otherwise there are no plants*/
			gardenbool = true;
		}
		
		//if the first type is pressed
		if(e.getSource().equals(screen2b1)){
			/*set the highlight/outline to green if the user
			 * has selected that and grey if they haven't selected
			 * it or choose to unselect it.*/
			if(!s2b1bool){
				selectedButton(e.getSource(), screen2b1);
				s2b1bool = true;
			}
			else if(s2b1bool){
				unselectedButton(e.getSource(), screen2b1);
				s2b1bool = false;
			}
		}
		//if the second type is pressed
		if(e.getSource().equals(screen2b2)){
			if(!s2b2bool){
				selectedButton(e.getSource(), screen2b2);
				s2b2bool = true;
			}
			else if(s2b2bool){
				unselectedButton(e.getSource(), screen2b2);
				s2b2bool = false;
			}
		}
		//highlights the name button
		if(e.getSource().equals(screen3b4)){
			s3b4bool = true;
			s3b5bool = false;
			questionScreen();
			
			/*
			if(!s3b4bool ){
				
				s3b4bool = true;
				s3b5bool = false;
				questionScreen();
			}
			*/
		}
		
		//highlights the formula button
		if(e.getSource().equals(screen3b5)){
			s3b4bool = false;
			s3b5bool = true;
			questionScreen();
			/*
			if(!s3b5bool){
				
				s3b5bool = true;
				s3b4bool = false;
				questionScreen();
			}
			*/
		}
		
		/*
		if(e.getSource().equals(screen2b3)){
			if(!s2b3bool){
				selectedButton(e.getSource(), screen2b3);
				s2b3bool = true;
			}
			else if(s2b3bool){
				unselectedButton(e.getSource(), screen2b3);
				s2b3bool = false;
			}
		}
		if(e.getSource().equals(screen2b4)){
			if(!s2b4bool){
				selectedButton(e.getSource(), screen2b4);
				s2b4bool = true;
			}
			else if(s2b4bool){
				unselectedButton(e.getSource(), screen2b4);
				s2b4bool = false;
			}
		}
		*/
		
		//screen2b5 is next button
		if(e.getSource().equals(screen2b5)){
			/*Prevents there from being a million zeros after the decimal point in the level*/
			DecimalFormat numberFormat = new DecimalFormat("#.0");
			String levelString = numberFormat.format(level);
			
			screen3LabelText = "Welcome " + username + "! Your current level is " + level;
			//at least one of the four types must be selected and their level
			//cannot exceed 5.3
			if((s2b1bool || s2b2bool || s2b3bool || s2b4bool) && level < 5.4){
				/*Since we only have two types at this point, the maximum length
				 * for any one of these arrays would be the total number of those 
				 * two types of compounds combined. These are only initialized at 
				 * the beginning so they are assuming the largest possible number
				 * of nodes in each of them, even though they cannot all have 132
				 * nodes at the same time*/
				if(s2b1bool && s2b2bool){
					levels = new QuestionNode[5][132];
					hard = new QuestionNode[132];
					harder = new QuestionNode[132];
					hardest = new QuestionNode[132];
					easy = new QuestionNode[132];
					unanswered = new QuestionNode[132];
					unanswered = masterArray.addToUnanswered(unanswered, "both");
					
				}
				//if the user wants to practice polyatomic ions
				else if(s2b1bool){
					levels = new QuestionNode[5][31];
					hard = new QuestionNode[31];
					harder = new QuestionNode[31];
					hardest = new QuestionNode[31];
					easy = new QuestionNode[31];
					unanswered = new QuestionNode[31];
					unanswered = masterArray.addToUnanswered(unanswered, "polyatomicIons");
				}
				//if the user wants to practice ternary compounds
				else if (s2b2bool){
					levels = new QuestionNode[5][101];
					hard = new QuestionNode[101];
					harder = new QuestionNode[101];
					hardest = new QuestionNode[101];
					easy = new QuestionNode[101];
					unanswered = new QuestionNode[101];
					unanswered = masterArray.addToUnanswered(unanswered, "ternaryCompounds");
				}
				levels[0] = easy;
				levels[1] = unanswered;
				levels[2] = hard;
				levels[3] = harder;
				levels[4] = hardest;
				goBool = true;
				removeAll();
				
				//gets another controlled random question
				runGame();
				
				//updates the garden and labels	
				repaint();
				questionScreen();
				
			}
		}
		/*screen 3b1 is the enter button*/
		if(e.getSource().equals(screen3b1)){
			/*if the name is being shown, the entered information must
			 * agree with the compound*/
			if(s3b4bool){
				//String rawAnswer = screen3tf1.getText();
				//filterString(rawAnswer);
				if(screen3tf1.getText().toLowerCase().equals(newNode.getName().toLowerCase()) && hintbool == false){
					//1 is the easiest category
					if(newNode.getDifficulty() != -1){
						lottery.shiftDifficulty(levels[newNode.getDifficulty() + 1], levels[newNode.getDifficulty()], newNode);
					}
					currentDifficulty = newNode.getDifficulty() + 1;
					//(lottery.shiftDifficulty(findDifficulty(newNode)));
					newNode.setDifficulty(currentDifficulty);
					if(newNode.getDifficulty() < -1){
						newNode.setDifficulty(-1);
					}
					screen3LabelText = "That's correct. Good Job!";
					progressDot += 1;
					if(progressDot == 5){
						/*Algorithm for determining levels*/
						double holding = level;
						level = level - Math.round(level) + 0.1;
						level = Math.round(holding) + level;
						progressDot = 0;
					}
					if(level - Math.round(level) > 0.3){
						level = Math.round(level) + 0.1 + 1;
					}
					if(Math.round(level) > 5){
						level = 5.3;
					}
				
					removeAll();
					//will not run past the maximum levels
					if(level < 5.4){
						runGame();
						repaint();
						questionScreen();
					}
					else{
						winnerWinner();
					}

				}
				else if(screen3tf1.getText().toLowerCase().equals(newNode.getName().toLowerCase()) && hintbool == true){
					if(newNode.getDifficulty() != 3){
						currentDifficulty = newNode.getDifficulty() + 1;
						newNode.setDifficulty(currentDifficulty);
						lottery.shiftDifficulty(levels[newNode.getDifficulty()], levels[newNode.getDifficulty() + 1], newNode);
					}
					else{
						currentDifficulty = 3;
						newNode.setDifficulty(currentDifficulty);
					}
					//(lottery.shiftDifficulty(findDifficulty(newNode)));
					newNode.setDifficulty(currentDifficulty);
					
					screen3LabelText = "Correct..but you used a hint! Difficulty is " + currentDifficulty + ".";
					

					removeAll();
					if(level < 5.3){
						hintbool = false;
						runGame();
						repaint();
						questionScreen();
					}
					else{
						winnerWinner();
					}
				}
				else{

					if(newNode.getDifficulty() != 3){
						currentDifficulty = newNode.getDifficulty() + 1;
						newNode.setDifficulty(currentDifficulty);
						lottery.shiftDifficulty(levels[newNode.getDifficulty()], levels[newNode.getDifficulty() + 1], newNode);
					}
					else{
						currentDifficulty = 3;
						newNode.setDifficulty(currentDifficulty);
					}
					//(lottery.shiftDifficulty(findDifficulty(newNode)));
					newNode.setDifficulty(currentDifficulty);
					
					screen3LabelText = "Inncorrect! Difficulty is " + currentDifficulty + ". Try another one!";
					

					removeAll();
					if(level < 5.3){
						hintbool = false;
						runGame();
						repaint();
						questionScreen();
					}
					else{
						winnerWinner();
					}
					
				}
			}
			/*If the shown label is the compound, the user must enter the correct
			 * name to recieve credit*/
			else{
				if(screen3tf1.getText().toLowerCase().equals(newNode.getCompound().toLowerCase()) && hintbool == false){
					//1 is the easiest category
					if(newNode.getDifficulty() != -1){
						lottery.shiftDifficulty(levels[newNode.getDifficulty() + 1], levels[newNode.getDifficulty()], newNode);
					}
					currentDifficulty = newNode.getDifficulty() + 1;
					//(lottery.shiftDifficulty(findDifficulty(newNode)));
					newNode.setDifficulty(currentDifficulty);
					if(newNode.getDifficulty() < -1){
						newNode.setDifficulty(-1);
					}
					screen3LabelText = "That's correct. Good Job!";
					progressDot += 1;
					if(progressDot == 5){
						/*Algorithm for determining levels*/
						double holding = level;
						level = level - Math.round(level) + 0.1;
						level = Math.round(holding) + level;
						progressDot = 0;
					}
					if(level - Math.round(level) > 0.3){
						level = Math.round(level) + 0.1 + 1;
					}
					if(Math.round(level) > 5){
						level = 5.3;
					}
				
					removeAll();
					//will not run past the maximum levels
					if(level < 5.4){
						runGame();
						repaint();
						questionScreen();
					}
					else{
						winnerWinner();
					}

				}
				else if(screen3tf1.getText().toLowerCase().equals(newNode.getName().toLowerCase()) && hintbool == true){
					if(newNode.getDifficulty() != 3){
						currentDifficulty = newNode.getDifficulty() + 1;
						newNode.setDifficulty(currentDifficulty);
						lottery.shiftDifficulty(levels[newNode.getDifficulty()], levels[newNode.getDifficulty() + 1], newNode);
					}
					else{
						currentDifficulty = 3;
						newNode.setDifficulty(currentDifficulty);
					}
					//(lottery.shiftDifficulty(findDifficulty(newNode)));
					newNode.setDifficulty(currentDifficulty);
					
					screen3LabelText = "Correct..but you used a hint! Difficulty is " + currentDifficulty + ".";
					

					removeAll();
					if(level < 5.3){
						hintbool = false;
						runGame();
						repaint();
						questionScreen();
					}
					else{
						winnerWinner();
					}
				}
				else{

					if(newNode.getDifficulty() != 3){
						currentDifficulty = newNode.getDifficulty() + 1;
						newNode.setDifficulty(currentDifficulty);
						lottery.shiftDifficulty(levels[newNode.getDifficulty()], levels[newNode.getDifficulty() + 1], newNode);
					}
					else{
						currentDifficulty = 3;
						newNode.setDifficulty(currentDifficulty);
					}
					//(lottery.shiftDifficulty(findDifficulty(newNode)));
					newNode.setDifficulty(currentDifficulty);
					
					screen3LabelText = "Inncorrect! Difficulty is " + currentDifficulty + ". Try another one!";
					

					removeAll();
					if(level < 5.3){
						hintbool = false;
						runGame();
						repaint();
						questionScreen();
					}
					else{
						winnerWinner();
					}
					
				}
			}
			
		}
		/*screen3b2 is the exit button*/
		if(e.getSource().equals(screen3b2)){
			removeAll();
			repaint();
			Label screen4label1 = new Label("Thanks for practicing " + username);
			screen4label1.setBounds((550/2 + WIDTH/2) - 40, 170, 170, 50);
			add(screen4label1);
			
			playerLinkedList.addInfo(username, level);
			/*Adds the updated playerlinkedlist to the playerINformation document*/
			try {
				IOFile.fileWriting(playerLinkedList, fileName5);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(1);
		}
		
		/*screen3b3 is the hints button*/
		if(e.getSource().equals(screen3b3)){
			screen3LabelText = getHint();
			hintbool = true;
			questionScreen();
		}
		
	
	}

	//lets the user know they have selected a certain button
	public void selectedButton(Object source, Button checkForThis){
		if(checkForThis == null){
			((Component) source).setBackground(Color.green);
		}
		else if(source.equals(checkForThis)){
			checkForThis.setBackground(Color.green);
		}
		
	}

	//lets the user know they have unselected a certain button
	public void unselectedButton(Object source, Button checkForThis){
		if(checkForThis == null){
			((Component) source).setBackground(Color.white);
		}
		else if(source.equals(checkForThis)){
			checkForThis.setBackground(Color.white);
		}
	}


	public void questionScreen(){

		removeAll();
		paint(gfx);
		
		Font font2 = new Font("Times New Roman", Font.PLAIN, 20);

		screen3tf1 = new TextField();
		screen3tf1.setFont(font2);
		screen3tf1.setBounds(700, 200, 400, 100);
		screen3tf1.setEditable(true);
		
		//will check if the user has the right answer
		screen3b1 = new Button("Enter");
		screen3b1.setBounds(700, 320, 195, 90);
		screen3b1.addActionListener(this);
		screen3b1.setFocusable(false);

		//saves and exits
		screen3b2 = new Button("Save & Exit");
		screen3b2.setBounds(905, 320, 195, 90);
		screen3b2.addActionListener(this);
		screen3b2.setFocusable(false);
		
		//provides a hint
		screen3b3 = new Button("Hint");
		screen3b3.setBounds(700, 420, 400, 50);
		screen3b3.addActionListener(this);
		screen3b3.setFocusable(false);
		
		//changes shown property to the formula
		screen3b4 = new Button("Name");
		screen3b4.setBounds(700, 480, 200, 40);
		screen3b4.addActionListener(this);
		screen3b4.setFocusable(false);
		labelMade = true;
		
		//changes shown property to the name
		screen3b5 = new Button("Formula");
		screen3b5.setBounds(900, 480, 200, 40);
		screen3b5.addActionListener(this);
		screen3b5.setFocusable(false);
		selectedButton(screen3b4, null);
		labelMade = true;
		
		//this label changes often to communicate a message to the user
				screen3l1 = new Label(screen3LabelText);
				Font font1 = new Font("Times New Roman", Font.PLAIN, 20);
				screen3l1.setFont(font1);
				screen3l1.setBounds(700, 50, 440, 50);
				
				
				if(s3b4bool){
					
					Label screen3l3 = new Label(newNode.getCompound());
					//screen3l3.setText(newNode.getCompound());
					System.out.println(newNode.getName());

					if(labelMade){
						screen3b4.setBackground(Color.green);
						screen3b5.setBackground(Color.white);
					}
					screen3l3.setFont(font2);
					screen3l3.setBounds((550/2 + WIDTH/2) - 40, 100, 170, 50);
					add(screen3l3);

				}
				else if(s3b5bool){
					Label screen3l3 = new Label(newNode.getName());
					//screen3l3.setText(newNode.getName());
					System.out.println(newNode.getCompound());
					
					if(labelMade){
						screen3b4.setBackground(Color.white);
						screen3b5.setBackground(Color.green);
					}
					
					//the compound the user has to identify
					Font font4 = new Font("Times New Roman", Font.PLAIN, 14);

					screen3l3.setFont(font4);
					screen3l3.setBounds((550/2 + WIDTH/2) - 40, 100, 170, 50);
					add(screen3l3);
				}
				else{
					Label screen3l3 = new Label("ugggh");
					screen3l3.setFont(font2);
					screen3l3.setBounds((550/2 + WIDTH/2) - 40, 100, 170, 50);
					add(screen3l3);
				}
				

		add(screen3l1); add(screen3b1); add(screen3tf1); add(screen3b2); add(screen3b3); add(screen3b4); add(screen3b5);
		//add(screen3l2);
	}


	//graphics
	public void paint(Graphics g){
		if(gardenbool){
			gfx.setColor(Color.green);
			gfx.drawRect(0, 0, 650, 600);
			gfx.fillRect(0, 0, 650, 600);


			gfx.drawImage(garden, 0, HEIGHT/2 - garden.getHeight(this)/2, this);			
			
			drawGarden();
			

			for(int i = 0; i< 5; i++){
				gfx.setColor(Color.black);
				gfx.drawOval(100 * (i+1), 540, 20, 20);
			}
			for(int i = 0; i< progressDot; i++){
				gfx.setColor(Color.black);
				gfx.fillOval(100 * (i+1), 540, 20, 20);
			}

			gfx.setColor(Color.white);
			gfx.drawRect(650, 0, 540, 600);
			gfx.fillRect(650, 0, 540, 600);

			g.drawImage(img, 0, 0, this);
		}
	}
	
	
	//draws garden at different stages based on user's score
	public void drawGarden(){

		if(level >= 0.1){
			gfx.drawImage(tomato, 0, 390, this);
		}

		if(level >= 0.2){
			gfx.drawImage(tomato, 20, 340, this);
		}

		if(level >= 0.3){
			gfx.drawImage(tomato, 45, 290, this);
		}

		if(level >= 1.1){
			gfx.drawImage(cabbage, 145, 290, this);
		}

		if(level >= 1.2){
			gfx.drawImage(cabbage, 125, 340, this);
		}

		if(level >= 1.3){
			gfx.drawImage(cabbage, 95, 390, this);
		}
		if(level >= 3.1){
			gfx.drawImage(pumpkin, 240, 270, this);
		}
		if(level >= 3.2){
			gfx.drawImage(pumpkin, 240, 330, this);
		}
		if(level >= 3.3){
			gfx.drawImage(pumpkin, 240, 390, this);
		}
		if(level >= 4.1){
			gfx.drawImage(carrot, 350, 270, this);
			
		}
		if(level >= 4.2){
			gfx.drawImage(carrot, 350, 330, this);
		}
		if(level >= 4.3){
			gfx.drawImage(carrot, 350, 390, this);
		}
		if(level >= 5.1){
			gfx.drawImage(corn, 445, 270, this);
		}
		if(level >= 5.2){
			gfx.drawImage(corn, 455, 330, this);
		}
		if(level >= 5.3){
			gfx.drawImage(corn, 460, 390, this);
		}
	}

	//if the user wins, this will be the only thing left on the screen 
	//they cannot do anything after this
	public void winnerWinner(){
		removeAll();
		winningLabel = new Label("That's correct. Good job!");
		winningLabel.setBounds(700, 140, 440, 50);
		add(winningLabel);
	}
	
	
	public String getHint(){
		if(s3b4bool){
			//if returning the name, return the first word of the name
			String returnThis = newNode.getName();
			String[] returnThisArray = returnThis.split(" ");
			returnThis = returnThisArray[0];
			return "The first word is " + returnThis;
		}
		else{
			String returnThis = newNode.getCompound();
			//if there is a lower case letter after the first letter
			if(Character.isLowerCase(returnThis.charAt(1))){
				return  "The first element is " + returnThis.substring(0, 2);
			}
			else{
				//if there is not 
				return "The first element is " + returnThis.charAt(0);
			}
			
		}
	}
}