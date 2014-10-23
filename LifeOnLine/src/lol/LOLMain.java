package lol;

import javax.swing.SwingUtilities;

import logic.LOLControl;

public class LOLMain {
	public static void main(String[] args) {
		LOLControl.loadTaskList();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LOLGui();
			}
		});
	}
}