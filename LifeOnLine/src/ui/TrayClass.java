package ui;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class TrayClass {

	public static TrayIcon trayIcon;

	public TrayClass() {
		show();
	}

	public void show() {
		if (!SystemTray.isSupported()) {
			System.exit(0);
		}

		trayIcon = new TrayIcon(createIcon("/resources/icon.png", "Icon"));
		trayIcon.setToolTip("LifeOnLine");
		final SystemTray tray = SystemTray.getSystemTray();

		final PopupMenu menu = new PopupMenu();

		MenuItem restore = new MenuItem("Restore");
		MenuItem about = new MenuItem("About");
		MenuItem exit = new MenuItem("Exit");

		menu.add(restore);
		menu.add(about);
		menu.addSeparator();
		menu.add(exit);

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"LifeonLine\nVersion: 0.5");
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});

		trayIcon.setPopupMenu(menu);
		try {
			tray.add(trayIcon);
		} catch (Exception e) {
		}
	}

	protected static Image createIcon(String path, String desc) {
		URL imageURL = TrayClass.class.getResource(path);
		return (new ImageIcon(imageURL, desc)).getImage();
	}
}