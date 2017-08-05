package com.gynt.easysettings.example;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.gynt.easysettings.api.Config;
import com.gynt.easysettings.rendering.AlternativeRenderer;

public class ExampleFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExampleFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 582, 388);

		Config config = new ExampleConfig();
		AlternativeRenderer d = new AlternativeRenderer();
		JTabbedPane result = d.render(config);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(result, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new ExampleFrame();
				frame.setVisible(true);
			}
		});
	}

}
