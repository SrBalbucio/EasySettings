package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gynt.easysettings.Settings;
import com.gynt.easysettings.SettingsPanel;

public class SimpleTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507073283162687096L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleTest frame = new SimpleTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimpleTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Settings s = new Settings(null);
		s.getRoot().registerSub("simplicity", "Simplicity Level").registerItem("too", "Too simple?", Settings.Type.BOOLEAN, Boolean.FALSE);
		
		SettingsPanel sp = new SettingsPanel(s.getRoot());
		sp.render();
		contentPane.add(sp, BorderLayout.CENTER);
		
		
	}

}
