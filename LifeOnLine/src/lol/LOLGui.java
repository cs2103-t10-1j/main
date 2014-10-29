package lol;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import ui.TrayClass;
import logic.LOLControl;

@SuppressWarnings("serial")
public class LOLGui extends JFrame implements HotkeyListener {
	
	public static ArrayList<String> commands = new ArrayList<String>();

	private boolean isNewRun = true;
	private boolean isNewMini = true;
	private boolean isFocus;

	TrayClass displayTrayIcon = new TrayClass();

	int i =0;
	int numTabPressed = 0;

	final static Color BG = new Color(255, 255, 255);
	final static Color BG2 = new Color(217, 232, 245);
	final static Color BG3 = new Color(145, 190, 212);
	final static Color BG4 = new Color(48, 66, 105);
	final static Color DARK_BLUE = new Color(3, 97, 148);
	
	Border compound = BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createLoweredBevelBorder());

	public LOLGui() {

		if (!JIntellitype.isJIntellitypeSupported()) {
			JOptionPane.showMessageDialog(null, "Error occured");
			System.exit(1);
		}
		// If instance already running, exit new instance
		if (JIntellitype.checkInstanceAlreadyRunning("LOL - Life On Line")) {
			TrayClass.trayIcon.displayMessage("LOL is Already Running!",
					"CTRL + L to Restore", TrayIcon.MessageType.INFO);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			JIntellitype.getInstance().cleanUp();
			System.exit(1);
		}

		// ** SET-UP GUI ** //

		final JFrame frame = new JFrame("LOL - Life On Line");
		frame.getRootPane().setBorder(
				BorderFactory.createMatteBorder(4, 4, 4, 4, BG4));
		frame.setVisible(true);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.ICONIFIED);

		JPanel displayPanel = new JPanel();
		displayPanel.setBackground(BG4);
		displayPanel.setLayout(new BorderLayout());
		
		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setLayout(new FlowLayout());
		feedbackPanel.setBackground(BG2);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(BG3);
		inputPanel.setLayout(new FlowLayout());
		
		JPanel commandsPanel1 = new JPanel();
		commandsPanel1.setLayout(new FlowLayout());
		commandsPanel1.setBackground(DARK_BLUE);
		
		JPanel commandsPanel2 = new JPanel();
		commandsPanel2.setLayout(new FlowLayout());
		commandsPanel2.setBackground(DARK_BLUE);

		// a panel for mainDisplayTP and mainDisplayTP2
		JPanel mainDisplayPanel = new JPanel();
		mainDisplayPanel.setLayout(new GridLayout(1, 2));

		JPanel feedbackInputAndCommandsPanel = new JPanel();

		feedbackInputAndCommandsPanel.setLayout(new GridLayout(4, 2));

		final JTextPane mainDisplayTP1 = new JTextPane();
		mainDisplayTP1.setBackground(BG);
		mainDisplayTP1.setEditable(false);
		mainDisplayTP1.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		mainDisplayTP1.setText("**Upcoming Tasks List**");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTP1);
		DefaultCaret caret1 = (DefaultCaret) mainDisplayTP1.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		JPanel mainDisplayPanelLeft = new JPanel();
		mainDisplayPanelLeft.setLayout(new GridLayout(2, 1));

		final JTextPane mainDisplayTP2 = new JTextPane();
		mainDisplayTP2.setEditable(false);
		mainDisplayTP2.setText("**To-Do Anytime List**");
		mainDisplayTP2.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollPane2 = new JScrollPane(mainDisplayTP2);
		DefaultCaret caret2 = (DefaultCaret) mainDisplayTP2.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		final JTextPane mainDisplayTP3 = new JTextPane();
		mainDisplayTP3.setEditable(false);
		mainDisplayTP3.setText("**Overdue**");
		mainDisplayTP3.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollPane3 = new JScrollPane(mainDisplayTP3);
		DefaultCaret caret3 = (DefaultCaret) mainDisplayTP3.getCaret();
		caret3.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		mainDisplayPanelLeft.add(scrollPane3);
		mainDisplayPanelLeft.add(scrollPane2);

		mainDisplayPanel.add(scrollPane);
		mainDisplayPanel.add(mainDisplayPanelLeft);

		displayPanel.add(mainDisplayPanel, BorderLayout.CENTER);

		final JLabel label = new JLabel("Welcome to Life on Line");
		feedbackPanel.add(label);

		/*
		 * final JTextArea feedbackDisplayTA = new JTextArea(2, 40);
		 * feedbackDisplayTA.setEditable(false);
		 * feedbackDisplayTA.setText("Feedback will be displayed here.");
		 * feedbackDisplayTA.setLineWrap(true);
		 * feedbackDisplayTA.setBorder(BorderFactory
		 * .createEtchedBorder(EtchedBorder.RAISED));
		 * feedbackDisplayTA.setFocusable(false);
		 * feedbackPanel.add(feedbackDisplayTA);
		 */

		final JTextField inputTF = new JTextField(39);
		inputPanel.add("South", inputTF);
		
		final JLabel commandsLabel1 = new JLabel("add      delete      edit      show      search");
		commandsPanel1.add(commandsLabel1);
		Border paddingBorder = BorderFactory.createEmptyBorder(5, 0, 0, 0);
		Border border = BorderFactory.createLineBorder(DARK_BLUE);
		commandsPanel1.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
		
		final JLabel commandsLabel2 = new JLabel("undo      redo      done      undone      exit      help");
		commandsPanel2.add(commandsLabel2);
		
		feedbackInputAndCommandsPanel.add(feedbackPanel);
		feedbackInputAndCommandsPanel.add(inputPanel);
		feedbackInputAndCommandsPanel.add(commandsPanel1);
		feedbackInputAndCommandsPanel.add(commandsPanel2);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(displayPanel, BorderLayout.CENTER);
		contentPane.add(feedbackInputAndCommandsPanel, BorderLayout.SOUTH);

		frame.setSize(new Dimension(600, 600));
		// frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		inputTF.requestFocus();

		Timer timer = new Timer(Constants.REFRESH_TIME, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					LOLControl.executeUserInput("home");
				} catch (Exception e) {
					// do nothing
				}
				TaskList<Task> taskList = LOLControl.getTaskList();
				InputTextFieldListener textfield = new InputTextFieldListener(
						mainDisplayTP1, mainDisplayTP2, mainDisplayTP3, label, inputTF, commandsLabel1, commandsLabel2, commands.size());
				textfield.refreshMainDisplay(taskList);
				System.out.println("refreshed");
			}
		});
		timer.setInitialDelay(0); // to start first refresh after 0s when
									// program opens
		timer.start();

		// **HOTKEY-INTERFACE** //

		// Assigning global HotKeys to CTRL+L and CTRL+M
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL,
				(int) 'L');
		JIntellitype.getInstance().registerHotKey(2, 0, KeyEvent.VK_ESCAPE);

		// Assign this class to be a HotKeyListener
		JIntellitype.getInstance().addHotKeyListener(this);

		// Listen for HotKey
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			@Override
			public void onHotKey(int aIdentifier) {

				// Restore GUI
				if (aIdentifier == 1) {
					frame.setVisible(true);
					frame.setExtendedState(getExtendedState());
				}
				// Minimize GUI
				if (aIdentifier == 2) {
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

		MenuItem restoreItem = TrayClass.trayIcon.getPopupMenu().getItem(i);

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

		inputTF.addFocusListener(new FocusAdapter() {
			Border original = inputTF.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				inputTF.setBorder(BorderFactory.createLoweredBevelBorder());
			}

			public void focusLost(FocusEvent e) {
				inputTF.setBorder(original);
			}
		});

		mainDisplayTP1.addFocusListener(new FocusAdapter() {
			Border original = mainDisplayTP1.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				mainDisplayTP1.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLoweredBevelBorder(),
						BorderFactory.createLineBorder(Color.BLACK)));
			}

			public void focusLost(FocusEvent e) {
				mainDisplayTP1.setBorder(original);
			}
		});

		mainDisplayTP2.addFocusListener(new FocusAdapter() {
			Border original = mainDisplayTP2.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				mainDisplayTP2.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLoweredBevelBorder(),
						BorderFactory.createLineBorder(Color.BLACK)));
			}

			public void focusLost(FocusEvent e) {
				mainDisplayTP2.setBorder(original);
			}
		});

		mainDisplayTP3.addFocusListener(new FocusAdapter() {
			Border original = mainDisplayTP3.getBorder();

			@Override
			public void focusGained(FocusEvent e) {
				mainDisplayTP3.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLoweredBevelBorder(),
						BorderFactory.createLineBorder(Color.BLACK)));
			}

			public void focusLost(FocusEvent e) {
				mainDisplayTP3.setBorder(original);
			}
		});

		JOptionPane.showMessageDialog(null, Constants.WELCOME_MESSAGE,
				"Welcome to LOL", JOptionPane.INFORMATION_MESSAGE);

		inputTF.addActionListener(new InputTextFieldListener(mainDisplayTP1,
				mainDisplayTP2, mainDisplayTP3, label, inputTF, commandsLabel1, commandsLabel2, commands.size()));

	}

	@Override
	public void onHotKey(int arg0) {
		// TODO Auto-generated method stub

	}
}