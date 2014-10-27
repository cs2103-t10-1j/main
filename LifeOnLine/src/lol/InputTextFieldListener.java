package lol;

import java.awt.Color;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import logic.LOLControl;

public class InputTextFieldListener implements ActionListener, KeyListener {
	final String[] commands = {"", "add ", "delete ", "edit ", "done ", "undo", "redo"};
	JTextField inputTF;
	JTextPane mainDisplayTP1;
	static StyledDocument doc1 = new DefaultStyledDocument();
	JTextPane mainDisplayTP2;
	static StyledDocument doc2 = new DefaultStyledDocument();
	JTextPane mainDisplayTP3;
	static StyledDocument doc3 = new DefaultStyledDocument();
	JLabel label;
	Integer i;

	final static boolean isHeader = true;
	
	// custom colors
	final static Color DARK_ORANGE = new Color(253, 101, 0);
	final static Color PURPLE = new Color(204, 0, 204);
	final static Color BG = new Color(0, 129, 72);

	public InputTextFieldListener(JTextPane mainDisplayTP,JTextPane mainDisplayTP2, JTextPane mainDisplayTP3,JLabel label, JTextField inputTF, Integer i){
		this.inputTF = inputTF;

		this.mainDisplayTP1 = mainDisplayTP;
		this.mainDisplayTP1.setDocument(doc1);
		addStyleToDoc(doc1);
		this.mainDisplayTP2 = mainDisplayTP2;
		this.mainDisplayTP2.setDocument(doc2);
		addStyleToDoc(doc2);
		this.mainDisplayTP3 = mainDisplayTP3;
		this.mainDisplayTP3.setDocument(doc3);
		addStyleToDoc(doc3);

		this.label = label;
		this.i = i;
		inputTF.addKeyListener(this);
	}

	//this method add different styles to document which are needed to display task of
	//different type and to display time, location and description of task in different 
	//font type
	public static void addStyleToDoc(StyledDocument doc){
		Style style = doc.addStyle(Constants.FORMAT_HEADER_FLOATING, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.GRAY);
		StyleConstants.setUnderline(style, true);


		style = doc.addStyle(Constants.FORMAT_HEADER_NORMAL, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);
		StyleConstants.setUnderline(style, true);

		style = doc.addStyle(Constants.FORMAT_HEADER_UPCOMING, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);
		StyleConstants.setUnderline(style, true);
		
		style = doc.addStyle(Constants.FORMAT_HEADER_DATE, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, true);
		StyleConstants.setForeground(style, Color.BLACK);
		StyleConstants.setUnderline(style, true);
		
		style = doc.addStyle(Constants.FORMAT_NUMBER, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, true);
		StyleConstants.setForeground(style, Color.BLACK);
		
		
		style = doc.addStyle(Constants.FORMAT_TICK, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, BG);

		style = doc.addStyle(Constants.FORMAT_DESCRIPTION, null);

		style = doc.addStyle(Constants.FORMAT_TIME, null);
		StyleConstants.setForeground(style, DARK_ORANGE);

		style = doc.addStyle(Constants.FORMAT_LOCATION, null);
		StyleConstants.setForeground(style, PURPLE);

		style = doc.addStyle(Constants.FORMAT_HEADER_OVERDUE, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.RED);
		StyleConstants.setUnderline(style, true);

		
		style = doc.addStyle(Constants.FORMAT_TIME_STRIKE, null);
		StyleConstants.setForeground(style, DARK_ORANGE);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(Constants.FORMAT_DESCRIPTION_STRIKE, null);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(Constants.FORMAT_LOCATION_STRIKE, null);
		StyleConstants.setForeground(style, PURPLE);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(Constants.FORMAT_OVERDUE_STRIKE, null);
		StyleConstants.setForeground(style, Color.RED);
		StyleConstants.setStrikeThrough(style, true);
	}

	@Override
	public void actionPerformed(ActionEvent event){ 
		String inputStr = inputTF.getText();
		try {
			refreshFeedbackDisplay(inputStr);
		} catch (Exception e) {
			// do nothing
		}

		TaskList<Task> taskList = LOLControl.getTaskList();
		refreshMainDisplay(taskList);

		clear(inputTF);
		i = new Integer(0);
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

		showInMainDisplayTP(taskList);
	}

	public static void showInMainDisplayTP(TaskList<Task> taskList){
		Date previousDueDate = new Date(-1, -1, -9999, null); //set as impossible date

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
				
				taskToFormat.format(isHeader, task, i, Constants.DISPLAY_IN_TP3);
				toBeDisplayedIn = Constants.DISPLAY_IN_TP3;
			}
			else if(currentDueDate != null && !currentDueDate.equals(previousDueDate)){
				previousDueDate = currentDueDate;

				taskToFormat.format(isHeader, task, i, Constants.DISPLAY_IN_TP1);
				toBeDisplayedIn = Constants.DISPLAY_IN_TP1;
			}
			else if(currentDueDate == null){
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
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			i = new Integer((i.intValue() + 1)%commands.length);
			inputTF.setText(commands[i.intValue()]);
			inputTF.grabFocus();
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if((i.intValue()-1)<0)
				i = new Integer((i.intValue() - 1)+commands.length);
			else
				i = new Integer((i.intValue() - 1));
			inputTF.setText(commands[i.intValue()]);
			inputTF.grabFocus();
		}
		if (e.getKeyCode() == KeyEvent.VK_HOME)
			try{ refreshFeedbackDisplay("home");
				TaskList<Task> taskList = LOLControl.getTaskList();
			refreshMainDisplay(taskList);
			}catch (Exception e1){
				e1.printStackTrace();
			}
	}
}
