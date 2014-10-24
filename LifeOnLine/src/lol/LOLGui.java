package lol;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ui.TrayClass;
import logic.LOLControl;

@SuppressWarnings("serial")
public class LOLGui extends JFrame {

	private boolean isNewRun = true;

	TrayClass displayTrayIcon = new TrayClass();

	final Integer i = new Integer(0);
	int numTabPressed = 0;

	final static Color BG = new Color(255, 255, 255);
	final static Color BG2 = new Color(217, 232, 245);
	final static Color BG3 = new Color(145, 190, 212);
	final static Color BG4 = new Color(48, 66, 105);
	Border compound = BorderFactory.createCompoundBorder(
			BorderFactory.createRaisedBevelBorder(),
			BorderFactory.createLoweredBevelBorder());

	public LOLGui() {

		// line 10 to 46 create LOL's GUI
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

		// a panel for mainDisplayTP and mainDisplayTP2
		JPanel mainDisplayPanel = new JPanel();
		mainDisplayPanel.setLayout(new GridLayout(1, 2));

		JPanel feedbackAndInputPanel = new JPanel();

		feedbackAndInputPanel.setLayout(new GridLayout(2, 1));

		final JTextPane mainDisplayTP1 = new JTextPane();
		mainDisplayTP1.setBackground(BG);
		mainDisplayTP1.setEditable(false);
		mainDisplayTP1.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		mainDisplayTP1.setText("**Upcoming Tasks List**");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTP1);
		mainDisplayPanel.add(scrollPane);

		final JTextPane mainDisplayTP2 = new JTextPane();
		mainDisplayTP2.setEditable(false);
		mainDisplayTP2.setText("**To-Do Anytime List**");
		mainDisplayTP2.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollPane2 = new JScrollPane(mainDisplayTP2);
		mainDisplayPanel.add(scrollPane2);

		displayPanel.add(mainDisplayPanel, BorderLayout.CENTER);

		JLabel label = new JLabel("Welcome to Life on Line");
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

		final JTextField inputTF = new JTextField(45);
		inputPanel.add("South", inputTF);

		feedbackAndInputPanel.add(feedbackPanel);
		feedbackAndInputPanel.add(inputPanel);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(displayPanel, BorderLayout.CENTER);
		contentPane.add(feedbackAndInputPanel, BorderLayout.SOUTH);

		frame.setSize(new Dimension(550, 550));
		// frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		inputTF.requestFocus();

		// show Tasks stored in storage on initial run
		try {
			LOLControl.executeUserInput("home");
			new InputTextFieldListener(mainDisplayTP1, mainDisplayTP2, label,
					inputTF, i);
		} catch (Exception e) {
			// do nothing
		}
		TaskList<Task> taskList = LOLControl.getTaskList();
		InputTextFieldListener.showInMainDisplayTP(taskList);

		// frame.addWindowListener( new WindowAdapter() {
		// public void windowOpened( WindowEvent e ){
		// inputTF.requestFocus();
		// }
		// });

		TrayClass.trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					frame.setExtendedState(getExtendedState());
					frame.setVisible(true);
				}
			}
		});

		MenuItem restoreItem = TrayClass.trayIcon.getPopupMenu().getItem(i);

		restoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setExtendedState(getExtendedState());
				frame.setVisible(true);
			}
		});

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (isNewRun) {
					TrayClass.trayIcon.displayMessage("Hey!",
							"LoL is still running in the background!",
							TrayIcon.MessageType.INFO);
					isNewRun = false;
				}
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

		inputTF.addActionListener(new InputTextFieldListener(mainDisplayTP1,
				mainDisplayTP2, label, inputTF, i));

	}
}