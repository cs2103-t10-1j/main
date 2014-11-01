package lol;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
//import javax.swing.plaf.LayerUI;
//import javax.swing.plaf.LayerUI;
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
import java.io.File;
import java.io.IOException;
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

	int i = 0;
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
		final JFrame frame = new JFrame();
		frame.setBackground(new Color(3, 97, 148));
		frame.getContentPane().setForeground(new Color(47, 79, 79));
		frame.getContentPane().setBackground(new Color(217, 232, 245));
		frame.setBounds(100, 100, 752, 517);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.ICONIFIED);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(217, 232, 245));
		final Image backgroundImage;
		try {
			 ClassLoader cldr = this.getClass().getClassLoader();
			 java.net.URL imageURL   = cldr.getResource("resources/background2.jpg");
			backgroundImage = javax.imageio.ImageIO.read(imageURL);
			panel_5 = new JPanel(new BorderLayout()) {
		        @Override public void paintComponent(Graphics g) {
		            g.drawImage(backgroundImage, 0, 0, null);
		        }
		    };
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	


		JPanel panel = new JPanel();
		panel.setBackground(new Color(3, 97, 148));
		panel.setBounds(0, 391, 736, 87);

		frame.getContentPane().add(panel);
		panel.setLayout(null);

		final JTextField inputTF = new JTextField();
		inputTF.setBounds(20, 46, 706, 30);
		panel.add(inputTF);
		inputTF.setColumns(10);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(50, 205, 50));
		panel_10.setBounds(10, 46, 17, 30);
		panel.add(panel_10);

		final JLabel label = new JLabel("Welcome to LOL");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(new Color(255,255,255));
		label.setBounds(20, 11, 706, 30);
		panel.add(label);

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

		final JTextPane mainDisplayTP1 = new JTextPane();
		mainDisplayTP1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mainDisplayTP1.setBounds(250, 35, 243, 345);
		mainDisplayTP1.setEditable(false);
		mainDisplayTP1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		DefaultCaret caret1 = (DefaultCaret) mainDisplayTP1.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		frame.getContentPane().add(mainDisplayTP1);
		
		
		final JTextPane mainDisplayTP2 = new JTextPane();
		mainDisplayTP2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mainDisplayTP2.setEditable(false);
		mainDisplayTP2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		mainDisplayTP2.setBounds(513, 94, 203, 231);
		DefaultCaret caret2 = (DefaultCaret) mainDisplayTP2.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		frame.getContentPane().add(mainDisplayTP2);

		final JTextPane mainDisplayTP3 = new JTextPane();
		mainDisplayTP3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mainDisplayTP3.setBounds(20, 94, 209, 231);
		mainDisplayTP3.setEditable(false);
		mainDisplayTP3.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
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
		digitalClock.setFont(new Font("Tahoma", Font.PLAIN, 14));
		digitalClock.setHorizontalAlignment(SwingConstants.RIGHT);
		digitalClock.setText("3.18 PM");
		digitalClock.setBounds(409, 10, 84, 23);
		frame.getContentPane().add(digitalClock);
		digitalClock.setForeground(new Color(220, 20, 60));
			
		final JLabel lblToday = new JLabel("Today");
		lblToday.setFont(new Font("Tahoma", Font.PLAIN, 14));
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

		

		
		panel_5.setBounds(0, 0, 736, 391);
		frame.getContentPane().add(panel_5);
		

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		inputTF.requestFocus();

		final Timer timer = new Timer(Constants.REFRESH_TIME,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						try {
							LOLControl.executeUserInput("home");
						} catch (Exception e) {
							// do nothing
						}
						TaskList<Task> taskList = LOLControl.getTaskList();
						InputTextFieldListener textfield = new InputTextFieldListener(
								mainDisplayTP1, mainDisplayTP2, mainDisplayTP3,
								label, inputTF, commands.size(), null,
								progressLabel, progressBar);
						textfield.refreshMainDisplay(taskList);
						
						DateParser dp = new DateParser();
						Date currentDate = dp.getTodaysDate();
						lblToday.setText(currentDate.toString());
					
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
				mainDisplayTP2, mainDisplayTP3, label, inputTF,
				commands.size(), timer, progressLabel, progressBar));

	}

	@Override
	public void onHotKey(int arg0) {
		// TODO Auto-generated method stub

	}
}
