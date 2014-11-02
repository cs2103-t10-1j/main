package lol;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import logic.LOLControl;


public class InputTextFieldListener implements ActionListener, KeyListener {
	JTextField inputTF;
	JTextPane mainDisplayTP1;
	static StyledDocument doc1 = new DefaultStyledDocument();
	JTextPane mainDisplayTP2;
	static StyledDocument doc2 = new DefaultStyledDocument();
	JTextPane mainDisplayTP3;
	static StyledDocument doc3 = new DefaultStyledDocument();
	JLabel label;
	JLabel progressLabel;
	int size;

	final static boolean isHeader = true;
	final Timer timer;
	final JProgressBar progressBar;
	
	// custom colors
	final static Color DARK_ORANGE = new Color(253, 101, 0);
	final static Color PURPLE = new Color(204, 0, 204);
	final static Color BG = new Color(0, 129, 72);
	final static Color DARK_BLUE = new Color(3, 97, 148);
	final static Color MEDIUM_BLUE = new Color(82, 161, 204);
	
	// fonts
	final static Font TREBUCHET_14 = new Font("Trebuchet MS", Font.PLAIN, 14);
	final static Font TREBUCHET_BOLD_14 = new Font("Trebuchet MS", Font.BOLD, 14);
	final static Font TREBUCHET_BOLD_16 = new Font("Trebuchet MS", Font.BOLD, 16);
	final static Font TREBUCHET_16 = new Font("Trebuchet MS", Font.PLAIN, 16);

	public InputTextFieldListener(JTextPane mainDisplayTP,JTextPane mainDisplayTP2, JTextPane mainDisplayTP3,JLabel label, JTextField inputTF, int size, Timer timer, JLabel progressLabel, JProgressBar progressBar){
		this.inputTF = inputTF;

		// Welcome to LifeOnLine
		label.setFont(TREBUCHET_BOLD_16);
		
		// Tasks with no date
		mainDisplayTP2.setFont(TREBUCHET_16);
		
		// Upcoming tasks
		mainDisplayTP.setFont(TREBUCHET_16);
		
		// Overdue tasks
		mainDisplayTP3.setFont(TREBUCHET_16);
		
		inputTF.setFont(TREBUCHET_BOLD_16);
		

		
		this.mainDisplayTP1 = mainDisplayTP;
		this.mainDisplayTP1.setDocument(doc1);
		addStyleToDoc(doc1);
		this.mainDisplayTP2 = mainDisplayTP2;
		this.mainDisplayTP2.setDocument(doc2);
		addStyleToDoc(doc2);
		this.mainDisplayTP3 = mainDisplayTP3;
		this.mainDisplayTP3.setDocument(doc3);
		addStyleToDoc(doc3);
		
		this.progressBar = progressBar;
		this.timer = timer;
		this.label = label;
		this.progressLabel = progressLabel;
		this.size = size;
		inputTF.addKeyListener(this);
		
		
	}

	//this method add different styles to document which are needed to display task of
	//different type and to display time, location and description of task in different 
	//font type
	public static void addStyleToDoc(StyledDocument doc){
		// header for floating tasks
		Style style = doc.addStyle(Constants.FORMAT_HEADER_FLOATING, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, DARK_BLUE);
		//StyleConstants.setUnderline(style, true);


		style = doc.addStyle(Constants.FORMAT_HEADER_NORMAL, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);
		//StyleConstants.setUnderline(style, true);

		// header for upcoming tasks
		style = doc.addStyle(Constants.FORMAT_HEADER_UPCOMING, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, DARK_BLUE);
		//StyleConstants.setUnderline(style, true);
		
		style = doc.addStyle(Constants.FORMAT_HEADER_DATE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setBold(style, true);
		//StyleConstants.setItalic(style, true);
		StyleConstants.setForeground(style, MEDIUM_BLUE);
		//StyleConstants.setUnderline(style, true);
		
		style = doc.addStyle(Constants.FORMAT_NUMBER, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, true);
		StyleConstants.setForeground(style, Color.BLACK);
		
		
		style = doc.addStyle(Constants.FORMAT_TICK, null);
		//StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, BG);

		style = doc.addStyle(Constants.FORMAT_DESCRIPTION, null);
		StyleConstants.setFontSize(style, 16);

		style = doc.addStyle(Constants.FORMAT_TIME, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, DARK_ORANGE);

		style = doc.addStyle(Constants.FORMAT_LOCATION, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, PURPLE);

		// header for overdue tasks
		style = doc.addStyle(Constants.FORMAT_HEADER_OVERDUE, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.RED);
		//StyleConstants.setUnderline(style, true);

		
		style = doc.addStyle(Constants.FORMAT_TIME_STRIKE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, DARK_ORANGE);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(Constants.FORMAT_DESCRIPTION_STRIKE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(Constants.FORMAT_LOCATION_STRIKE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, PURPLE);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(Constants.FORMAT_OVERDUE_STRIKE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Color.RED);
		StyleConstants.setStrikeThrough(style, true);
	}

	@Override
	public void actionPerformed(ActionEvent event){ 
		String inputStr = inputTF.getText();
		LOLGui.commands.add(inputStr);
		try {
			refreshFeedbackDisplay(inputStr);
		} catch (Exception e) {
			// do nothing
		}

		TaskList<Task> taskList = LOLControl.getTaskList();
		refreshMainDisplay(taskList);
		
		timer.setInitialDelay(60000);
		timer.restart();

		
		
		clear(inputTF);
		size = LOLGui.commands.size();
	}

	public void refreshFeedbackDisplay(String inputStr) throws Exception{
		String feedback = passStringToControlAndGetFeedback(inputStr);
		label.setText(feedback);
	}

	public String passStringToControlAndGetFeedback(String inputStr) throws Exception{
		return LOLControl.executeUserInput(inputStr);
	}

	public void refreshMainDisplay(TaskList<Task> taskList){
		clear(mainDisplayTP1);
		clear(mainDisplayTP2);
		clear(mainDisplayTP3);

		LOLControl.refreshProgress();
		if(LOLControl.progressMaximum>0){
			progressLabel.setText("Today's report: "+LOLControl.progress+"/"+LOLControl.progressMaximum);
			progressBar.setMaximum(LOLControl.progressMaximum);
			progressBar.setValue(LOLControl.progressMaximum);
			progressBar.setValue(LOLControl.progress);
		}
		else{
			progressBar.setMaximum(1);
			progressBar.setValue(1);
			progressLabel.setText("No deadlines today");
		}
		
		showInMainDisplayTP(taskList);
		
		if(LOLControl.isAlertMode){
			Task alertTask = LOLControl.refreshAlert();
			if(alertTask!=null)
				JOptionPane.showMessageDialog(null, alertMessage(alertTask),
						"LOL Alert", JOptionPane.WARNING_MESSAGE);
			if(LOLControl.userEmail!=null && LOLControl.userEmail.length()>=11)
			LOLEmail.send(LOLControl.userEmail, alertMessage(alertTask));
			}
	}

	private String alertMessage(Task alertTask) {
		
		String message="YOU HAVE AN UPCOMING TASK";
		message+="\n"+alertTask.getTaskDescription();
		message+="\n Time: "+alertTask.getStartTime();
		if(alertTask.getEndTime()!=null)
			message+="-"+alertTask.getEndTime();
		if(alertTask.getTaskLocation()!=null)
			message+="\n Location: "+ alertTask.getTaskLocation();
		return message;
	}

	public static void showInMainDisplayTP(TaskList<Task> taskList){
		Date previousDueDate = new Date(-1, -1, -9999, null); //set as impossible date
		Task previousTask = new Task("ŒŒŒŒŒŒŒŒ","",new Date(), new Time(),new Time()); //impossible task

		FormatToString taskToFormat = new FormatToString();
		//below should be removed and added to another class
		FormatToString.clearAllLinkedList();
		FormatToString.hasOverdueHeader = false;
		FormatToString.hasFloatingHeader = false;
		FormatToString.hasUpcomingHeader = false;
		FormatToString.isFirst = true;

		for(int i = 0; i < taskList.size(); i++){
			Task task = taskList.get(i);

			Date currentDueDate = task.getTaskDueDate();

			assert !(currentDueDate.getDay() == -1 && currentDueDate.getMonth() == -1 
					&& currentDueDate.getYear4Digit() == -9999) : "impossible date entered";

			int toBeDisplayedIn = 0;
			//add header
			if(task.getIsOverdue() && !currentDueDate.equals(previousDueDate)){
				previousDueDate = currentDueDate;
				previousTask = task;
				
				taskToFormat.format(isHeader, task, i, Constants.DISPLAY_IN_TP3);
				toBeDisplayedIn = Constants.DISPLAY_IN_TP3;
			}
			else if(previousTask.getIsOverdue() && !task.getIsOverdue() && currentDueDate != null && currentDueDate.equals(previousDueDate)){
				previousDueDate = currentDueDate;
				previousTask=task;

				taskToFormat.format(isHeader, task, i, Constants.DISPLAY_IN_TP1);
				toBeDisplayedIn = Constants.DISPLAY_IN_TP1;
			}
			else if(currentDueDate != null && !currentDueDate.equals(previousDueDate)){
				previousDueDate = currentDueDate;
				previousTask=task;

				taskToFormat.format(isHeader, task, i, Constants.DISPLAY_IN_TP1);
				toBeDisplayedIn = Constants.DISPLAY_IN_TP1;
			}
			
			else if(currentDueDate == null){
				previousTask=task;
				taskToFormat.format(isHeader, task, i, Constants.DISPLAY_IN_TP2);
				toBeDisplayedIn = Constants.DISPLAY_IN_TP2;
			}

			assert !(toBeDisplayedIn == 0) : "toBeDisplayed in is 0 which is invalid";

			//add task below header
			taskToFormat.format(!isHeader, task, i, toBeDisplayedIn);
			
			//add a line between overdue task and upcoming task
			/*if(i+1 < taskList.size() && task.getIsOverdue() && !taskList.get(i+1).getIsOverdue() && taskList.get(i+1).getTaskDueDate() != null){
				FormatToString.getLinkedList(1).add(new StringWithFormat("=================================" + "\n", Constants.FORMAT_HEADER_OVERDUE));
			}*/
		}

		addToDisplay(doc1, doc2, doc3);
	}

	public static void addToDisplay(StyledDocument doc, StyledDocument doc2, StyledDocument doc3){
		try {
			for(int j = 1; j <= FormatToString.getLinkedListNum(); j++){
				LinkedList<StringWithFormat> strToShow = FormatToString.getLinkedList(j);

				for(int i = 0; i < strToShow.size(); i++){
					if(j==1){
						doc.insertString(doc.getLength(), strToShow.get(i).getString(), doc.getStyle(strToShow.get(i).getFormat()));
					}
					else if(j==2){
						doc2.insertString(doc2.getLength(), strToShow.get(i).getString(), doc2.getStyle(strToShow.get(i).getFormat()));
					}
					else if(j==3){
						doc3.insertString(doc3.getLength(), strToShow.get(i).getString(), doc3.getStyle(strToShow.get(i).getFormat()));
					}
				}
			}
		} 
		catch (BadLocationException badLocationException) {
			System.err.println("Oops");
		}
	}

	private void clear(JTextField TF){
		TF.setText("");
	}

	private void clear(JTextPane TP){
		TP.setText("");
	}

	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) {
		;
	}

	/** Handle the key-pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		;
	}

	/** Handle the key-released event from the text field. */
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if(!LOLGui.commands.isEmpty()){
			int index = size-1;
			if(index>=0){
			inputTF.setText(LOLGui.commands.get(index));
			inputTF.grabFocus();
			size--;
			}
		}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(!LOLGui.commands.isEmpty()){
			int index =size+1;
			if(index>=0&&index<LOLGui.commands.size()){
			inputTF.setText(LOLGui.commands.get(index));
			inputTF.grabFocus();
			size++;
			}
		}
		}
		
	}
}
