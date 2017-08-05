package com.gynt.easysettings.old;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;

public class SettingsPanel extends JPanel {

	public static void expandAllNodes(JTree tree) {
		int j = tree.getRowCount();
		int i = 0;
		while (i < j) {
			tree.expandRow(i);
			i += 1;
			j = tree.getRowCount();
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4104785855475407998L;
	private SettingsRenderer renderer;
	private Settings settings;

	private SettingsPanel() {
		renderer = new DefaultSettingsRenderer();
		setLayout(new BorderLayout(0, 0));
	}

	public SettingsPanel(Settings s) {
		this();
		settings = s;
		add(renderer.getSettingsComponent(this, settings));
	}

	public Settings getSettings() {
		return settings;
	}

	public void setRenderer(SettingsRenderer sr) {
		renderer = sr;
		removeAll();
		add(renderer.getSettingsComponent(this, settings));
	}

}
