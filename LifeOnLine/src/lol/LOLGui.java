package lol;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LOLGui {
	final Integer i = new Integer(0);
	int numTabPressed = 0;

	public LOLGui(){
		//line 10 to 46 create LOL's GUI
		JFrame frame = new JFrame("LOL - Life On Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel displayPanel = new JPanel(); 
		displayPanel.setLayout(new BorderLayout());
		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setLayout(new FlowLayout());
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		//a panel for mainDisplayTP and mainDisplayTP2
		JPanel mainDisplayPanel = new JPanel();
		mainDisplayPanel.setLayout(new GridLayout(1, 2));

		JPanel feedbackAndInputPanel = new JPanel();
		feedbackAndInputPanel.setLayout(new GridLayout(2,1));

		final JTextPane mainDisplayTP1 = new JTextPane();
		mainDisplayTP1.setEditable(false);
		mainDisplayTP1.setText("**Upcoming Tasks List**");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTP1);
		mainDisplayPanel.add(scrollPane);

		final JTextPane mainDisplayTP2 = new JTextPane();
		mainDisplayTP2.setEditable(false);
		mainDisplayTP2.setText("**To-Do Anytime List**");
		JScrollPane scrollPane2 = new JScrollPane(mainDisplayTP2);
		mainDisplayPanel.add(scrollPane2);

		displayPanel.add(mainDisplayPanel, BorderLayout.CENTER);

		feedbackPanel.add(new JLabel("Feedback: "));

		final JTextArea feedbackDisplayTA = new JTextArea(2, 28);
		feedbackDisplayTA.setEditable(false);
		feedbackDisplayTA.setText("Feedback will be displayed here.");
		feedbackDisplayTA.setLineWrap(true);
		feedbackDisplayTA.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		feedbackDisplayTA.setFocusable(false);
		feedbackPanel.add(feedbackDisplayTA);


		final JTextField inputTF = new JTextField(35);
		inputPanel.add("South", inputTF);

		feedbackAndInputPanel.add(feedbackPanel);
		feedbackAndInputPanel.add(inputPanel);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(displayPanel, BorderLayout.CENTER);
		contentPane.add(feedbackAndInputPanel, BorderLayout.SOUTH);

		frame.setSize(new Dimension(500, 500));
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		inputTF.requestFocus();

		//frame.addWindowListener( new WindowAdapter() {
			//public void windowOpened( WindowEvent e ){
			//	inputTF.requestFocus();
			//}
		//});
		
		inputTF.addFocusListener(new FocusAdapter(){
			Border original = inputTF.getBorder();
			@Override
			public void focusGained(FocusEvent e){
				inputTF.setBorder(BorderFactory.createLoweredBevelBorder()); 
			}
			public void focusLost(FocusEvent e){
				inputTF.setBorder(original);
			}
		});
		
		mainDisplayTP1.addFocusListener(new FocusAdapter(){
			Border original = mainDisplayTP1.getBorder();
			@Override
			public void focusGained(FocusEvent e){
				mainDisplayTP1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createLineBorder(Color.BLACK))); 
			}
			public void focusLost(FocusEvent e){
				mainDisplayTP1.setBorder(original);
			}
		});
		
		mainDisplayTP2.addFocusListener(new FocusAdapter(){
			Border original = mainDisplayTP2.getBorder();
			@Override
			public void focusGained(FocusEvent e){
				mainDisplayTP2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createLineBorder(Color.BLACK)));
			}
			public void focusLost(FocusEvent e){
				mainDisplayTP2.setBorder(original);
			}
		});
		

		inputTF.addActionListener(new InputTextFieldListener(mainDisplayTP1,mainDisplayTP2, feedbackDisplayTA, inputTF, i));	

	}
}