package lol;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.Timer;

import parser.DateParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import ui.TrayClass;
import logic.LOLControl;

@SuppressWarnings("serial")
/**
 * 
 * @author Sevin, Aviral
 *
 */
public class LOLGui extends JFrame implements HotkeyListener {
	private boolean isNewRun = true;
	private boolean isNewMini = true;
	private boolean isFocus;

	TrayClass displayTrayIcon = new TrayClass();
	
	public LOLGui() {

		if (!JIntellitype.isJIntellitypeSupported()) {
			JOptionPane.showMessageDialog(null, Constants.MSG_ERROR);
			System.exit(1);
		}
		// If instance already running, exit new instance
		// Prevent multiple LOL to run at the same time
		if (JIntellitype.checkInstanceAlreadyRunning(Constants.LOL_NAME)) {
			TrayClass.trayIcon.displayMessage(Constants.MSG_LOL_IS_RUNNING,
					Constants.MSG_RESTORE, TrayIcon.MessageType.INFO);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			JIntellitype.getInstance().cleanUp();
			System.exit(1);
		}

		//*************Setting up the GUI****************//

		final JFrame frame = new JFrame("LOL - LifeOnLine");
		frame.setBackground(new Color(3, 97, 148));
		frame.getContentPane().setForeground(new Color(47, 79, 79));
		frame.getContentPane().setBackground(new Color(217, 232, 245));
		frame.setBounds(100, 100, 752, 517);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.ICONIFIED);
		frame.setResizable(false);
		frame.getContentPane().setPreferredSize(new Dimension(736, 478));
		frame.pack();

		/*JPanel backgroundLabel = new JPanel();
		backgroundLabel.setBackground(new Color(217, 232, 245));
		final Image backgroundImage;
		try {
			ClassLoader cldr = this.getClass().getClassLoader();
			java.net.URL imageURL   = cldr.getResource("resources/background2.jpg");
			backgroundImage = javax.imageio.ImageIO.read(imageURL);
			backgroundLabel = new JPanel(new BorderLayout()) {
				@Override public void paintComponent(Graphics g) {
					g.drawImage(backgroundImage, 0, 0, null);
				}
			};
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		JLabel backgroundLabel = new JLabel();
		backgroundLabel.setBackground(new Color(217, 232, 245));
		BufferedImage img;
		try {
			URL url = this.getClass().getResource("/resources/background2.jpg");
			img = ImageIO.read(url);
			backgroundLabel= new JLabel(new ImageIcon(img));
			} catch (Exception e){
				e.printStackTrace();
			}
		

		JPanel panel = new JPanel();
		panel.setBackground(new Color(3, 97, 148));
		panel.setBounds(0, 391, 736, 87);

		frame.getContentPane().add(panel);
		panel.setLayout(null);

		final JTextField inputTF = new JTextField();
		inputTF.setBounds(20, 46, 706, 30);
		inputTF.setFont(Constants.TREBUCHET_BOLD_16);
		panel.add(inputTF);
		inputTF.setColumns(10);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(50, 205, 50));
		panel_10.setBounds(10, 46, 17, 30);
		panel.add(panel_10);

		final JLabel feedbackLabel = new JLabel();
		feedbackLabel.setText(Constants.MSG_WELCOME);
		feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		feedbackLabel.setBackground(new Color(3, 97, 148));
		feedbackLabel.setForeground(new Color(255,255,255));
		feedbackLabel.setBounds(20, 11, 706, 30);
		feedbackLabel.setFont(Constants.TREBUCHET_BOLD_16);
		panel.add(feedbackLabel);

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setBounds(250, 302, 1, 1);
		frame.getContentPane().add(horizontalGlue);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(248, 248, 255));
		panel_1.setBounds(0, 0, 168, 46);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		final JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 11, 146, 14);
		progressBar.setForeground(new Color(34, 139, 34));
		progressBar.setStringPainted(true);
		progressBar.setMinimum(0);
		progressBar.setMaximum(0);
		panel_1.add(progressBar);

		final JLabel progressLabel = new JLabel("Progress Bar");
		progressLabel.setBounds(10, 21, 146, 25);
		panel_1.add(progressLabel);

		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(3, 97, 148));
		panel_6.setBounds(0, 46, 168, 10);
		frame.getContentPane().add(panel_6);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(new Color(3, 97, 148));
		panel_7.setBounds(0, 46, 10, 345);
		frame.getContentPane().add(panel_7);

		//upcoming panel
		final JTextPane mainDisplayTP1 = new JTextPane();
		mainDisplayTP1.setFont(Constants.TAHOMA_14);
		mainDisplayTP1.setBounds(250, 35, 243, 345);
		mainDisplayTP1.setEditable(false);
		mainDisplayTP1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		mainDisplayTP1.setFont(Constants.CALIBRI_16);
		DefaultCaret caret1 = (DefaultCaret) mainDisplayTP1.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		frame.getContentPane().add(mainDisplayTP1);

		//tasks with no date panel
		final JTextPane mainDisplayTP2 = new JTextPane();
		mainDisplayTP2.setFont(Constants.TAHOMA_14);
		mainDisplayTP2.setEditable(false);
		mainDisplayTP2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		mainDisplayTP2.setBounds(513, 94, 203, 231);
		mainDisplayTP2.setFont(Constants.CALIBRI_16);
		DefaultCaret caret2 = (DefaultCaret) mainDisplayTP2.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		frame.getContentPane().add(mainDisplayTP2);

		//overdue tasks panel
		final JTextPane mainDisplayTP3 = new JTextPane();
		mainDisplayTP3.setFont(Constants.TAHOMA_14);
		mainDisplayTP3.setBounds(20, 94, 209, 231);
		mainDisplayTP3.setEditable(false);
		mainDisplayTP3.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		mainDisplayTP3.setFont(Constants.CALIBRI_16);
		DefaultCaret caret3 = (DefaultCaret) mainDisplayTP3.getCaret();
		caret3.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		frame.getContentPane().add(mainDisplayTP3);

		JScrollPane scrollPane = new JScrollPane(mainDisplayTP3);
		scrollPane.setBounds(20, 94, 209, 231);
		frame.getContentPane().add(scrollPane);

		JScrollPane scrollPane_1 = new JScrollPane(mainDisplayTP1);
		scrollPane_1.setBounds(250, 35, 243, 345);
		frame.getContentPane().add(scrollPane_1);

		JScrollPane scrollPane_2 = new JScrollPane(mainDisplayTP2);
		scrollPane_2.setBounds(513, 94, 203, 231);
		frame.getContentPane().add(scrollPane_2);

		DigitalClock digitalClock = new DigitalClock();
		digitalClock.setFont(Constants.TAHOMA_14);
		digitalClock.setHorizontalAlignment(SwingConstants.RIGHT);
		digitalClock.setText("3.18 PM");
		digitalClock.setBounds(409, 10, 84, 23);
		frame.getContentPane().add(digitalClock);
		digitalClock.setForeground(new Color(220, 20, 60));

		final JLabel lblToday = new JLabel("Today");
		lblToday.setFont(Constants.TAHOMA_14);
		lblToday.setBounds(250, 10, 104, 23);
		frame.getContentPane().add(lblToday);
		lblToday.setForeground(new Color(220, 20, 60));

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(248, 248, 255));
		panel_2.setBounds(568, 0, 168, 46);
		frame.getContentPane().add(panel_2);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(3, 97, 148));
		panel_3.setBounds(558, 46, 168, 10);
		frame.getContentPane().add(panel_3);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(3, 97, 148));
		panel_4.setBounds(726, 46, 10, 345);
		frame.getContentPane().add(panel_4);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(3, 97, 148));
		panel_8.setBounds(167, 0, 391, 10);
		frame.getContentPane().add(panel_8);

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(new Color(3, 97, 148));
		panel_9.setBounds(167, 0, 10, 56);
		frame.getContentPane().add(panel_9);

		JPanel panel_14 = new JPanel();
		panel_14.setBackground(new Color(3, 97, 148));
		panel_14.setBounds(558, 0, 10, 56);
		frame.getContentPane().add(panel_14);

		/*final JLabel blockLabel = new JLabel(LOLControl.isBlockMode?": On":": Off");
		blockLabel.setBounds(129, 15, 46, 14);
		panel_2.add(blockLabel);

		JButton blockButton = new JButton("Block Slots");
		blockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LOLControl.isBlockMode = !LOLControl.isBlockMode;
				if(LOLControl.isBlockMode){
					blockLabel.setText(": On");
				}
				else
					blockLabel.setText(": Off");
			}
		});
		blockButton.setBounds(10, 11, 109, 23);
		panel_2.add(blockButton);*/

		final JLabel labelAlert = new JLabel(LOLControl.isAlertMode?": On":": Off");
		labelAlert.setBounds(109, 12, 49, 20);
		panel_2.add(labelAlert);

		JButton alertButton = new JButton("Alert");
		alertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOLControl.isAlertMode = !LOLControl.isAlertMode;
				if(LOLControl.isAlertMode){
					labelAlert.setText(": On");
				}
				else
					labelAlert.setText(": Off");
			}
		});
		alertButton.setBounds(10, 11, 89, 23);
		panel_2.add(alertButton);

		backgroundLabel.setBounds(0, 0, 736, 391);
		frame.getContentPane().add(backgroundLabel);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		inputTF.requestFocus();

		//*********End of setting up the GUI***********//

		final Timer timer = new Timer(Constants.REFRESH_TIME,
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				LOLControl.executeUserInput("home");

				TaskList<Task> taskList = LOLControl.getTaskList();
				InputTextFieldListener textfield = new InputTextFieldListener(
						mainDisplayTP1, mainDisplayTP2, mainDisplayTP3,
						feedbackLabel, inputTF, null,
						progressLabel, progressBar, labelAlert);
				textfield.refreshMainDisplay(taskList);

				DateParser dp = new DateParser();
				Date currentDate = dp.getTodaysDate();
				lblToday.setText(currentDate.toString());

				System.out.println("refreshed");
			}
		});

		timer.setInitialDelay(0); // to start first refresh after 0s when program opens
		timer.start();

		final InputTextFieldListener listener = new InputTextFieldListener(
				mainDisplayTP1, mainDisplayTP2, mainDisplayTP3,
				feedbackLabel, inputTF, timer,
				progressLabel, progressBar, labelAlert);

		// **HOTKEY-INTERFACE** //

		// Assigning global HotKeys GUI
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL,     //ctrl + L to maximise
				(int) 'L');
		JIntellitype.getInstance().registerHotKey(2, 0, KeyEvent.VK_ESCAPE); //escape to minimise
		JIntellitype.getInstance().registerHotKey(3, 0, KeyEvent.VK_HOME);//home to display main screen
		JIntellitype.getInstance().registerHotKey(4, 0, 46); //delete to execute delete
		JIntellitype.getInstance().registerHotKey(5, JIntellitype.MOD_CONTROL, (int) 'Z'); //ctrl+z to undo
		JIntellitype.getInstance().registerHotKey(6, JIntellitype.MOD_CONTROL, (int) 'Y'); //ctrl+y to redo
		JIntellitype.getInstance().registerHotKey(7, JIntellitype.MOD_CONTROL, (int) 'F'); //ctrl+f to search
		JIntellitype.getInstance().registerHotKey(8, JIntellitype.MOD_CONTROL, (int) 'D'); //ctrl+d to mark as done
		JIntellitype.getInstance().registerHotKey(9, JIntellitype.MOD_CONTROL, (int) 'U'); //ctrl+u to mark as undone
		JIntellitype.getInstance().registerHotKey(10, 0, 112);//F1 to get help

		//JIntellitype.getInstance().addHotKeyListener(this);
		//do not need this line?

		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			@Override
			public void onHotKey(int aIdentifier) {
				// Restore GUI
				if (aIdentifier == 1) {
					frame.setVisible(true);
					frame.setExtendedState(getExtendedState());
				}
				// Minimize GUI
				else if (aIdentifier == 2) {
					if ((isFocus == true)) {
						frame.setState(ICONIFIED);
						frame.setVisible(false);
						if (isNewMini) {
							TrayClass.trayIcon.displayMessage("Minimized!",
									"CTRL + L to Restore",
									TrayIcon.MessageType.INFO);
							isNewMini = false;
						}
					}
				}
				else if (aIdentifier == 3){
					System.out.println("home");
					try{ listener.refreshFeedbackDisplay("home");
					TaskList<Task> taskList = LOLControl.getTaskList();
					listener.refreshMainDisplay(taskList);
					}catch (Exception e1){
						e1.printStackTrace();
					}
				}
				else if (aIdentifier == 4){
					String inputStr = inputTF.getText();
					inputStr.trim();
					inputStr = Constants.COMMAND_DELETE + " "+ inputStr;
					refreshGUI(listener, timer, inputStr);
				}
				else if (aIdentifier == 5){
					String inputStr = Constants.COMMAND_UNDO;
					refreshGUI(listener, timer, inputStr);
				}
				else if (aIdentifier == 6){
					String inputStr = Constants.COMMAND_REDO;
					refreshGUI(listener, timer, inputStr);
				}
				else if (aIdentifier == 7){
					String inputStr = inputTF.getText();
					inputStr.trim();
					inputStr = Constants.COMMAND_SEARCH + " "+ inputStr;
					refreshGUI(listener, timer, inputStr);
				}
				else if (aIdentifier == 8){
					String inputStr = inputTF.getText();
					inputStr.trim();
					inputStr = Constants.COMMAND_DONE + " "+ inputStr;
					refreshGUI(listener, timer, inputStr);
				}
				else if (aIdentifier == 9){
					String inputStr = inputTF.getText();
					inputStr.trim();
					inputStr = Constants.COMMAND_NOT_DONE + " "+ inputStr;
					refreshGUI(listener, timer, inputStr);
				}
				else if(aIdentifier == 10){
					showHelpWindow(); 
				}
			}

			private void refreshGUI(InputTextFieldListener listener, Timer timer, String inputStr) {
				listener.refreshFeedbackDisplay(inputStr);
				TaskList<Task> taskList = LOLControl.getTaskList();
				listener.refreshMainDisplay(taskList);
				timer.setInitialDelay(60000);
				timer.restart();
				inputTF.setText("");
			}
		});


		TrayClass.trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					frame.setVisible(true);
					frame.setExtendedState(getExtendedState());
				}
			}
		});

		MenuItem restoreItem = TrayClass.trayIcon.getPopupMenu().getItem(0);

		restoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				frame.setExtendedState(getExtendedState());
			}
		});

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (isNewRun) {
					TrayClass.trayIcon.displayMessage("Hey!",
							"LOL is still running in the background!",
							TrayIcon.MessageType.INFO);
					isNewRun = false;
				}
			}
		});

		frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowGainedFocus(java.awt.event.WindowEvent windowEvent) {
				isFocus = true;
			}

			@Override
			public void windowLostFocus(java.awt.event.WindowEvent windowEvent) {
				isFocus = false;
			}
		});

		popUpAnInputDialogForEmailFunctionality();
		addFocusListenerToAllPanel(inputTF, mainDisplayTP1, mainDisplayTP2, mainDisplayTP3, alertButton);

		inputTF.addActionListener(listener);
		inputTF.addKeyListener(listener);
	}
	
	public static void showHelpWindow(){
		JOptionPane.showMessageDialog(null, 
				 Constants.MSG_HELP_INFO, 
				 Constants.MSG_WELCOME_HELP, 
				 JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * this pop up dialog window will ask the user for the user's email and will send an
	 * email to the user when his task is near deadline if the user enables it
	 */
	private void popUpAnInputDialogForEmailFunctionality(){
		String s = (String)JOptionPane.showInputDialog(
				null,
				Constants.MSG_PLEASE_ENTER_EMAIL,
				Constants.MSG_WELCOME,
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				Constants.MSG_EMAIL_EXAMPLE);

		//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			LOLControl.userEmail = s.trim();
		}
	}

	@Override
	public void onHotKey(int arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * enable the user to choose the below panels (params) by pressing tab and showing
	 * them in bolder border
	 * 
	 * @param inputTF
	 * @param mainDisplayTP1
	 * @param mainDisplayTP2
	 * @param mainDisplayTP3
	 */
	private void addFocusListenerToAllPanel(final JTextField inputTF, final JTextPane mainDisplayTP1, final JTextPane mainDisplayTP2, final JTextPane mainDisplayTP3, final JButton alertButton){
		inputTF.addFocusListener(new FocusAdapter() {
			Border original = inputTF.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				inputTF.setBorder(Constants.INPUT_TF_FOCUS_BORDER);
			}
			@Override
			public void focusLost(FocusEvent e) {
				inputTF.setBorder(original);
			}
		});

		mainDisplayTP1.addFocusListener(new FocusAdapter() {
			Border original = mainDisplayTP1.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				mainDisplayTP1.setBorder(Constants.DISPLAY_PANEL_FOCUS_BORDER);
			}

			@Override
			public void focusLost(FocusEvent e) {
				mainDisplayTP1.setBorder(original);
			}
		});

		mainDisplayTP2.addFocusListener(new FocusAdapter() {
			Border original = mainDisplayTP2.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				mainDisplayTP2.setBorder(Constants.DISPLAY_PANEL_FOCUS_BORDER);
			}

			@Override
			public void focusLost(FocusEvent e) {
				mainDisplayTP2.setBorder(original);
			}
		});

		mainDisplayTP3.addFocusListener(new FocusAdapter() {
			Border original = mainDisplayTP3.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				mainDisplayTP3.setBorder(Constants.DISPLAY_PANEL_FOCUS_BORDER);
			}

			@Override
			public void focusLost(FocusEvent e) {
				mainDisplayTP3.setBorder(original);
			}
		});

		alertButton.addFocusListener(new FocusAdapter() {
			Border original = alertButton.getBorder();

			@Override
			public void focusGained(FocusEvent e){
				alertButton.setBorder(Constants.ALERT_BUTTON_FOCUS_BORDER);
			}

			@Override
			public void focusLost(FocusEvent e){
				alertButton.setBorder(original);
			}
		});
	}
}
