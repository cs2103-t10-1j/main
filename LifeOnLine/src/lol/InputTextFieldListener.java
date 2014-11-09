/**
 * This class is the listener to the input text field in LOL's GUI
 * This class is responsible to listen to the changes in the input text field and refresh the GUI after every user input.
 */

package lol;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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

/**
 * 
 * @author Sevin, Aviral
 *
 */
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
	JLabel alertLabel;

	private static ArrayList<String> commands = new ArrayList<String>();
	private static int indexOfCurrentShowingTask;

	final static boolean IS_HEADER = true;
	final Timer timer;
	final JProgressBar progressBar;

	public InputTextFieldListener(JTextPane mainDisplayTP,
			JTextPane mainDisplayTP2, JTextPane mainDisplayTP3, JLabel label,
			JTextField inputTF, Timer timer, JLabel progressLabel,
			JProgressBar progressBar, JLabel alertLabel) {
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

		this.progressBar = progressBar;
		this.timer = timer;
		this.label = label;
		this.progressLabel = progressLabel;
		this.alertLabel = alertLabel;
	}

	/**
	 * Add the required Styles that are used in the GUI to doc so that it can be
	 * used in GUI
	 * 
	 * @param doc
	 */
	public static void addStyleToDoc(StyledDocument doc) {
		Style style = doc.addStyle(Constants.FORMAT_HEADER_FLOATING, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Constants.DARK_BLUE);

		style = doc.addStyle(Constants.FORMAT_HEADER_NORMAL, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);

		style = doc.addStyle(Constants.FORMAT_HEADER_UPCOMING, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Constants.DARK_BLUE);

		style = doc.addStyle(Constants.FORMAT_HEADER_DATE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Constants.MEDIUM_BLUE);

		style = doc.addStyle(Constants.FORMAT_NUMBER, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, true);
		StyleConstants.setForeground(style, Color.BLACK);

		style = doc.addStyle(Constants.FORMAT_TICK, null);
		StyleConstants.setForeground(style, Constants.BG);

		style = doc.addStyle(Constants.FORMAT_DESCRIPTION, null);
		StyleConstants.setFontSize(style, 16);

		style = doc.addStyle(Constants.FORMAT_TIME, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Constants.DARK_ORANGE);

		style = doc.addStyle(Constants.FORMAT_LOCATION, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Constants.PURPLE);

		style = doc.addStyle(Constants.FORMAT_HEADER_OVERDUE, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.RED);

		style = doc.addStyle(Constants.FORMAT_DONE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Color.GRAY);

		style = doc.addStyle(Constants.FORMAT_IS_JUST_ADDED, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setBackground(style, Color.YELLOW);
	}

	/**
	 * This function will be called whenever user key in some text into the
	 * input text field and press enter.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		String inputStr = inputTF.getText();
		if(inputStr.equalsIgnoreCase("alert")){
			LOLControl.isAlertMode = !LOLControl.isAlertMode;
			alertLabel.setText(LOLControl.isAlertMode ? ": On" : ": Off");
			label.setText("Alert is now " + (LOLControl.isAlertMode ? "On!" : "Off!"));
		} else {
		addUserInputToCommands(inputStr);
		refreshFeedbackDisplay(inputStr);

		TaskList<Task> taskList = LOLControl.getTaskList();
		refreshMainDisplay(taskList);

		//GUI already refreshes after each user action therefore we want to restart the refresh timer
		timer.setInitialDelay(60000);
		timer.restart();
		}

		clear(inputTF);
	}

	/**
	 * add userInput String to an array list call commands and automatically add
	 * an empty String at the end of the array list
	 * 
	 * @param userInput
	 */
	private void addUserInputToCommands(String userInput) {
		if (commands.isEmpty()) {
			commands.add(userInput);
			commands.add(Constants.EMPTY_STRING);
		} else {
			commands.set(commands.size() - 1, userInput);
			commands.add(Constants.EMPTY_STRING);
		}

		indexOfCurrentShowingTask = Constants.IMPOSSIBLE_ARRAYLIST_INDEX;
	}

	/**
	 * Refresh the feedback display panel in GUI
	 * 
	 * @param inputStr
	 */
	public void refreshFeedbackDisplay(String inputStr) {
		String feedback = LOLControl.executeUserInput(inputStr);
		label.setText(feedback);
	}

	/**
	 * refresh all the main displaying panels displaying panels include the
	 * three main display text panes for tasks, progress bar, alerting display
	 * (on desktop and email)
	 * 
	 * @param taskList
	 */
	public void refreshMainDisplay(TaskList<Task> taskList) {
		refreshDisplayTextPanes(taskList);
		refreshProgressBar();
		refreshAlert();
	}

	/**
	 * refresh the three main display panels for tasks with taskList
	 * 
	 * @param taskList
	 */
	private void refreshDisplayTextPanes(TaskList<Task> taskList) {
		resetScrollPanePosition();
		clear(mainDisplayTP1);
		clear(mainDisplayTP2);
		clear(mainDisplayTP3);

		showInMainDisplayTP(taskList);
	}

	/**
	 * refresh the progress bar which show today's progress
	 */
	private void refreshProgressBar() {
		LOLControl.refreshProgress();
		if (LOLControl.progressMaximum > 0) {
			progressLabel.setText("Today's report: " + LOLControl.progress
					+ "/" + LOLControl.progressMaximum);
			progressBar.setMaximum(LOLControl.progressMaximum);
			progressBar.setValue(LOLControl.progressMaximum);
			progressBar.setValue(LOLControl.progress);
		} else {
			progressBar.setMaximum(1);
			progressBar.setValue(1);
			progressLabel.setText("No deadlines today");
		}
	}

	/**
	 * refresh the alert time so that tasks that are about to be overdue will
	 * cause the GUI to pop up an alert window
	 */
	private void refreshAlert() {

		// checks if alert mode is on
		if (LOLControl.isAlertMode) {
			Task alertTask = LOLControl.refreshAlert();

			// does not alert if there is no task to be alerted
			if (alertTask != null) {
				JOptionPane.showMessageDialog(null, alertMessage(alertTask),
						"LOL Alert", JOptionPane.WARNING_MESSAGE);

				//
				if (LOLControl.userEmail != null
						&& !LOLControl.userEmail.equals("example@example.com")) {
					LOLEmail.send(LOLControl.userEmail, alertMessage(alertTask));
				}
			}
		}
	}

	/**
	 * Generate an alert message
	 * 
	 * @param alertTask
	 * @return an alert message String associated with the alertTask
	 */
	private String alertMessage(Task alertTask) {
		String message = "YOU HAVE AN UPCOMING TASK";

		message += "\n" + alertTask.getTaskDescription();
		message += "\n Time: " + alertTask.getStartTime();

		if (alertTask.getEndTime() != null) {
			message += "-" + alertTask.getEndTime();
		}
		if (alertTask.getTaskLocation() != null) {
			message += "\n Location: " + alertTask.getTaskLocation();
		}

		return message;
	}

	/**
	 * determine what will be shown in the display panels
	 * 
	 * @param taskList
	 */
	public void showInMainDisplayTP(TaskList<Task> taskList) {
		FormatToString formatToString = new FormatToString();
		formatToString.format(taskList);

		addToDisplay(formatToString, doc1, doc2, doc3);
	}

	/**
	 * add task which is formatted to String in formatToString class to the
	 * corresponding doc which belongs to its corresponding task display
	 * JTextPane
	 * 
	 * doc1 will be holding display details for upcoming task display panel doc2
	 * will be holding display details for floating task display panel doc3 will
	 * be holding display details for overdue task display panel
	 * 
	 * @param formatToString
	 * @param doc1
	 * @param doc2
	 * @param doc3
	 */
	public void addToDisplay(FormatToString formatToString,
			StyledDocument doc1, StyledDocument doc2, StyledDocument doc3) {
		try {
			for (int j = 1; j <= formatToString.getLinkedListNum(); j++) {
				LinkedList<StringWithFormat> strToShow = formatToString
						.getLinkedList(j);

				for (int i = 0; i < strToShow.size(); i++) {
					if (j == 1) {
						// if the task is just added, GUI will auto scroll to
						// the newly added task
						if (strToShow.get(i).getIsJustAdded()) {
							mainDisplayTP1.setCaretPosition(doc1.getLength());
						}
						doc1.insertString(doc1.getLength(), strToShow.get(i)
								.getString(), doc1.getStyle(strToShow.get(i)
								.getFormat()));
					} else if (j == 2) {
						if (strToShow.get(i).getIsJustAdded()) {
							mainDisplayTP2.setCaretPosition(doc2.getLength());
						}
						doc2.insertString(doc2.getLength(), strToShow.get(i)
								.getString(), doc2.getStyle(strToShow.get(i)
								.getFormat()));
					} else if (j == 3) {
						if (strToShow.get(i).getIsJustAdded()) {
							mainDisplayTP3.setCaretPosition(doc3.getLength());
						}
						doc3.insertString(doc3.getLength(), strToShow.get(i)
								.getString(), doc3.getStyle(strToShow.get(i)
								.getFormat()));
					}
				}
			}
		} catch (BadLocationException badLocationException) {
			System.err.println("Bad Location Exception in reading doc");
		}
	}

	/**
	 * will clear all the words in the text field
	 * 
	 * @param TF
	 */
	private void clear(JTextField TF) {
		TF.setText(Constants.EMPTY_STRING);
	}

	/**
	 * will clear all the words in the text pane
	 * 
	 * @param TP
	 */
	private void clear(JTextPane TP) {
		TP.setText(Constants.EMPTY_STRING);
	}

	/**
	 * reset the position of all the scroll pane currently present in GUI to
	 * their topmost position
	 */
	private void resetScrollPanePosition() {
		mainDisplayTP1.setCaretPosition(0);
		mainDisplayTP2.setCaretPosition(0);
		mainDisplayTP3.setCaretPosition(0);
	}

	/**
	 * Handle the key-released event from the text field. Will display
	 * previously entered user input when press up arrow key
	 * 
	 * And can navigate through different previously entered user input using up
	 * and down arrow keys.
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (!commands.isEmpty()) {
				if (indexOfCurrentShowingTask == Constants.IMPOSSIBLE_ARRAYLIST_INDEX
						&& commands.size() - 2 >= 0) {
					indexOfCurrentShowingTask = commands.size() - 1;
				}

				if (indexOfCurrentShowingTask - 1 >= 0) {
					inputTF.setText(commands.get(--indexOfCurrentShowingTask));
					inputTF.grabFocus();
				}
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (!commands.isEmpty()
					&& indexOfCurrentShowingTask != Constants.IMPOSSIBLE_ARRAYLIST_INDEX) {
				if (indexOfCurrentShowingTask + 1 < commands.size()) {
					inputTF.setText(commands.get(++indexOfCurrentShowingTask));
					inputTF.grabFocus();
				}
			}
		}
	}

	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) {
		// none
	}

	/** Handle the key-pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		// none
	}
}
