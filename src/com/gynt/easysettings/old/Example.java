package com.gynt.easysettings.old;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gynt.easysettings.old.Settings.ChangeListener;
import com.gynt.easysettings.old.Settings.Item;

public class Example extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507073283162687096L;
	private JPanel contentPane;

	public boolean simple = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Example frame = new Example();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setupFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	public Example() {
		setupFrame();

		Settings s = new Settings(null);
		s.getRoot().registerSub("simplicity", "Simplicity Level").registerItem("too", "Too simple?",
				Settings.Type.BOOLEAN, Boolean.FALSE);
		s.getRoot().registerSub("choice", "Current simplicity level").registerItem("simple", "Simple",
				Settings.Type.RADIO, Boolean.FALSE, new ChangeListener() {
					@Override
					public void onChange(Item source, Object oldValue, Object newValue) {
						simple = (boolean) newValue;
					}
				});
		s.getRoot().registerSub("choice", "Current simplicity level").registerItem("hard", "Hard", Settings.Type.RADIO,
				Boolean.TRUE);

		s.getRoot().registerDir("Dir").registerSub("Sub", "Sub!").registerItem("item", "Item description",
				Settings.Type.FOLDER, new File(""));
		s.getRoot().registerDir("Dir").registerSub("Sub", "Sub!").registerItem("number", "Count", Settings.Type.INTEGER,
				5, new ChangeListener() {

					@Override
					public void onChange(Item source, Object oldValue, Object newValue) {
						System.out.println(String.format("old value: %s, new value: %s", oldValue.toString(),
								newValue.toString()));
					}
				});

		SettingsPanel sp = new SettingsPanel(s);
		contentPane.add(sp, BorderLayout.CENTER);
	}

}
