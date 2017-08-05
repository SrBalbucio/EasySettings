package com.gynt.easysettings.rendering;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import com.gynt.easysettings.api.Config;
import com.gynt.easysettings.api.Directory;
import com.gynt.easysettings.api.Section;
import com.gynt.easysettings.api.Setting;
import com.gynt.easysettings.api.rendering.Renderer;
import com.gynt.easysettings.implementation.FileType;

public class DefaultRenderer implements Renderer {

	@Override
	public JTabbedPane render(Config config) {
		JTabbedPane jtp = new JTabbedPane();

		List<Directory> dirs = config.get();
		List<JPanel> panels = new ArrayList<>();
		for (Directory d : dirs) {
			panels.add(renderDirectory(d));
		}

		for (int i = 0; i < dirs.size(); i++) {
			Directory d = dirs.get(i);
			jtp.addTab(d.getDisplayText(), null, panels.get(i), d.getTooltipText());
		}

		return jtp;
	}

	@Override
	public JPanel renderDirectory(Directory dir) {
		List<JPanel> jsections = new ArrayList<>();
		List<Section> sections = dir.get();
		for (Section s : sections) {
			jsections.add(renderSection(s));
		}

		JPanel panel = new JPanel();
		GroupLayout g = new GroupLayout(panel);
		panel.setLayout(g);

		SequentialGroup h = g.createSequentialGroup();
		SequentialGroup v = g.createSequentialGroup();

		ParallelGroup c1 = g.createParallelGroup(GroupLayout.Alignment.LEADING);
		for (JPanel j : jsections) {
			c1 = c1.addComponent(j);
		}
		h = h.addGroup(c1);
		g.setHorizontalGroup(h);

		for (JPanel j : jsections) {
			ParallelGroup row = g.createParallelGroup(GroupLayout.Alignment.BASELINE);
			row = row.addComponent(j);
			v = v.addGroup(row);
		}

		g.setVerticalGroup(v);

		return panel;
	}

	@Override
	public JPanel renderSection(Section section) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 1, true), section.getDisplayText()));
		panel.setToolTipText(section.getTooltipText());

		ButtonGroup bg = new ButtonGroup();

		List<Setting> settings = section.get();
		List<JComponent> components = new ArrayList<>();
		for (Setting s : settings) {
			components.add(renderSettingComponent(s, bg));
		}
		List<JLabel> labels = new ArrayList<>();
		for (Setting s : settings) {
			JLabel j = new JLabel(s.getDisplayText());
			j.setToolTipText(s.getTooltipText());
			labels.add(j);
		}

		GroupLayout g = new GroupLayout(panel);
		panel.setLayout(g);
		g.setAutoCreateContainerGaps(true);
		g.setAutoCreateGaps(true);

		ParallelGroup column1 = g.createParallelGroup(GroupLayout.Alignment.LEADING);

		ParallelGroup column2 = g.createParallelGroup(GroupLayout.Alignment.TRAILING);

		for (int i = 0; i < settings.size(); i++) {
			column1 = column1.addComponent(labels.get(i));
		}

		for (int i = 0; i < settings.size(); i++) {
			column2 = column2.addComponent(components.get(i));
		}

		SequentialGroup horizontal = g.createSequentialGroup();
		horizontal = horizontal.addGroup(column1).addGroup(column2);

		g.setHorizontalGroup(horizontal);

		SequentialGroup vertical = g.createSequentialGroup();

		for (int i = 0; i < settings.size(); i++) {
			ParallelGroup row = g.createParallelGroup(GroupLayout.Alignment.BASELINE);
			row = row.addComponent(labels.get(i));
			row = row.addComponent(components.get(i));
			vertical = vertical.addGroup(row);
		}

		g.setVerticalGroup(vertical);

		return panel;
	}

	@Override
	public JComponent renderSettingComponent(Setting setting, ButtonGroup bg) {
		switch (setting.getType().getType().toUpperCase()) {
		case "STRING": {
			JTextField jtf = new JTextField((String) setting.getValue());
			jtf.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					setting.setValue(jtf.getText());
					jtf.getParent().revalidate();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					setting.setValue(jtf.getText());
					jtf.getParent().revalidate();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					setting.setValue(jtf.getText());
					jtf.getParent().revalidate();
				}
			});
			return jtf;
		}
		case "INTEGER": {
			JSpinner js = new JSpinner(
					new SpinnerNumberModel(new Integer((int) setting.getValue()), null, null, new Integer(1)));
			js.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					setting.setValue(js.getValue());
				}

			});
			return js;
		}
		case "DOUBLE": {
			JSpinner js = new JSpinner(
					new SpinnerNumberModel(new Double((double) setting.getValue()), null, null, new Double(1)));
			js.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					setting.setValue(js.getValue());
				}

			});
			return js;
		}
		case "FOLDER": {
			JTextField jtf = new JTextField((String) setting.getValue());
			jtf.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					setting.setValue(jtf.getText());
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					setting.setValue(jtf.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					setting.setValue(jtf.getText());
				}
			});
			JButton browse = new JButton("Browse");
			browse.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser((String) setting.getValue());
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = jfc.showOpenDialog(browse);
					if (result == JFileChooser.APPROVE_OPTION) {
						jtf.setText(jfc.getSelectedFile().toString());
					}
				}
			});
			JPanel subsub = new JPanel();
			GroupLayout g = new GroupLayout(subsub);
			subsub.setLayout(g);
			g.setAutoCreateContainerGaps(true);
			g.setAutoCreateGaps(true);
			g.setHorizontalGroup(g.createSequentialGroup()
					.addGroup(g.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jtf))
					.addGroup(g.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(browse)));
			g.setVerticalGroup(
					g.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jtf).addComponent(browse));

			return subsub;
		}
		case "FILE": {
			JTextField jtf = new JTextField((String) setting.getValue());
			jtf.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					jtf.getParent().revalidate();
					setting.setValue(jtf.getText());
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					jtf.getParent().revalidate();
					setting.setValue(jtf.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					jtf.getParent().revalidate();
					setting.setValue(jtf.getText());
				}
			});

			JButton browse = new JButton("Browse");
			browse.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser((String) setting.getValue());
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

					if (setting.getType() instanceof FileType) {
						jfc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return ((FileType) setting.getType()).getExtension();
							}

							@Override
							public boolean accept(File f) {
								if (f.isDirectory())
									return true;
								return f.getPath().contains(".") ? f.getPath().split("\\.")[1]
										.startsWith(((FileType) setting.getType()).getExtension()) : false;
							}
						});
					}

					int result = jfc.showOpenDialog(browse);
					if (result == JFileChooser.APPROVE_OPTION) {
						jtf.setText(jfc.getSelectedFile().toString());
					}
				}
			});
			JPanel subsub = new JPanel();
			GroupLayout g = new GroupLayout(subsub);
			subsub.setLayout(g);
			g.setAutoCreateContainerGaps(false);
			g.setAutoCreateGaps(false);
			g.setHorizontalGroup(g.createSequentialGroup()
					.addGroup(g.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jtf))
					.addGroup(g.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(browse)));
			g.setVerticalGroup(g.createSequentialGroup().addGroup(
					g.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jtf).addComponent(browse)));

			return subsub;
		}
		case "BOOLEAN": {
			JCheckBox jcb = new JCheckBox("");
			jcb.setSelected((boolean) setting.getValue());
			jcb.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					setting.setValue(jcb.isSelected());
				}
			});
			return jcb;
		}
		case "RADIO":
			JRadioButton jrb = new JRadioButton("");
			if (bg != null)
				bg.add(jrb);
			jrb.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					setting.setValue(jrb.isSelected());
				}
			});
			return jrb;
		}
		;
		return null;
	}

}
