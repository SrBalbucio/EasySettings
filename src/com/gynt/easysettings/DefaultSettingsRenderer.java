package com.gynt.easysettings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.gynt.easysettings.Settings.Dir;
import com.gynt.easysettings.Settings.Item;
import com.gynt.easysettings.Settings.Sub;

public class DefaultSettingsRenderer implements SettingsRenderer {

	private JTree prefTree;
	private JPanel prefPanel;

	public void dirbuild(DefaultMutableTreeNode currentnode, Dir current) {
		for (Dir dir : current.dirs) {
			DefaultMutableTreeNode sub = new DefaultMutableTreeNode(getDirComponent(null, dir));
			currentnode.add(sub);
			dirbuild(sub, dir);
		}
	}

	public static void expandAllNodes(JTree tree) {
		int j = tree.getRowCount();
		int i = 0;
		while (i < j) {
			tree.expandRow(i);
			i += 1;
			j = tree.getRowCount();
		}
	}

	@Override
	public void render(JComponent parent, Dir root) {
		DefaultMutableTreeNode r = new DefaultMutableTreeNode(getDirComponent(null, root));
		prefTree.setModel(new DefaultTreeModel(r));
		dirbuild(r, root);
		expandAllNodes(prefTree);
	}

	@Override
	public JComponent getSettingsComponent(JComponent parent, Settings settings) {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		p.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.33);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		prefPanel = new JPanel(new BorderLayout(0, 0));
		panel_1.add(prefPanel);

		prefTree = new JTree();
		prefTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				JPanel d = (JPanel) ((DefaultMutableTreeNode) arg0.getNewLeadSelectionPath().getLastPathComponent())
						.getUserObject();

				prefPanel.removeAll();
				prefPanel.add(d, BorderLayout.CENTER);

				d.revalidate();
				prefPanel.repaint();
			}
		});
		scrollPane.setViewportView(prefTree);
		render(p, settings.getRoot());
		return p;
	}

	@Override
	public JComponent getDirComponent(JComponent parent, Dir dir) {
		Dir source = dir;
		JPanel panel = new JPanel() {
			/**
			 *
			 */
			private static final long serialVersionUID = -1417088770258417598L;

			@Override
			public String toString() {
				return source.name;
			}
		};
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));

		for (Sub sub : source.subs) {
			JComponent subpanel = getSubComponent(panel, sub);
			panel.add(subpanel);
		}

		return panel;
	}

	@Override
	public JComponent getSubComponent(JComponent panel, Sub sub) {
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(0, 1));
		subpanel.setBorder(BorderFactory.createTitledBorder(sub.description));

		ButtonGroup bg = new ButtonGroup();
		for (int i = 0; i < sub.items.size(); i++) {
			Item pi = sub.items.get(i);
			JComponent ip = getItemComponent(subpanel, pi, bg);
			subpanel.add(ip);
		}
		subpanel.revalidate();
		return subpanel;
	}

	@Override
	public JComponent getItemComponent(JComponent subpanel, Item pi, ButtonGroup bg) {
		switch (pi.type) {
		case FOLDER: {
			JLabel label = new JLabel(pi.description);
			JTextField jtf = new JTextField(pi.getValue().toString()) {
				/**
				 *
				 */
				private static final long serialVersionUID = -683998504709538631L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					if(getText()==null || getText().length()==0) {
						d.width=50;
					}
					return d;
				}
			};
			jtf.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					subpanel.revalidate();
					pi.setValue(jtf.getText());
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					subpanel.revalidate();
					pi.setValue(jtf.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					subpanel.revalidate();
					pi.setValue(jtf.getText());
				}
			});
			JButton browse = new JButton("Browse");
			browse.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser(pi.getValue().toString());
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = jfc.showOpenDialog(browse);
					if (result == JFileChooser.APPROVE_OPTION) {
						jtf.setText(jfc.getSelectedFile().toString());
					}
				}
			});
			JPanel subsub = new JPanel(new BorderLayout());
			subsub.add(label, BorderLayout.WEST);
			subsub.add(jtf, BorderLayout.CENTER);
			subsub.add(browse, BorderLayout.EAST);
			return subsub;
		}
		case FILE: {
			JLabel label = new JLabel(pi.description);
			JTextField jtf = new JTextField(pi.getValue().toString()) {
				/**
				 *
				 */
				private static final long serialVersionUID = 326700658857108258L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					if(getText()==null || getText().length()==0) {
						d.width=50;
					}
					return d;
				}
			};
			jtf.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					subpanel.revalidate();
					pi.setValue(jtf.getText());
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					subpanel.revalidate();
					pi.setValue(jtf.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					subpanel.revalidate();
					pi.setValue(jtf.getText());
				}
			});
			JButton browse = new JButton("Browse");
			browse.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser(pi.getValue().toString());
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int result = jfc.showOpenDialog(browse);
					if (result == JFileChooser.APPROVE_OPTION) {
						jtf.setText(jfc.getSelectedFile().toString());
						// pi.setValue(jfc.getSelectedFile().toString()); //Use
						// listener?
					}
				}
			});
			JPanel subsub = new JPanel(new BorderLayout());
			subsub.add(label, BorderLayout.WEST);
			subsub.add(jtf, BorderLayout.CENTER);
			subsub.add(browse, BorderLayout.EAST);
			return subsub;
		}
		case RADIO: {
			JRadioButton jrb = new JRadioButton(pi.description);
			jrb.setSelected((Boolean) pi.getValue());
			jrb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					pi.setValue(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
			bg.add(jrb);
			return jrb;
		}
		case BOOLEAN: {

			JCheckBox jcb = new JCheckBox(pi.description);
			jcb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					pi.setValue(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
			jcb.setSelected((Boolean) pi.getValue());
			return jcb;
		}
		case STRING: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JTextField jtf = new JTextField((String) pi.getValue());
			jtf.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					pi.setValue(jtf.getText());
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					pi.setValue(jtf.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					pi.setValue(jtf.getText());
				}
			});
			subsub.add(label);
			subsub.add(jtf);
			return subsub;
		}
		case FLOAT: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JSpinner js = new JSpinner(new SpinnerNumberModel(new Float((float) pi.getValue()), null, null, new Float(0.1F))) {

				/**
				 *
				 */
				private static final long serialVersionUID = 8728683428180496505L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width+=getValue().toString().length();
					return d;
				}

			};
			//js.setValue(pi.getValue());
			js.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					pi.setValue(js.getValue());
					js.revalidate();
				}
			});
			//((JSpinner.DefaultEditor) js.getEditor()).getTextField().setColumns(4);
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}
		case INTEGER: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JSpinner js = new JSpinner(new SpinnerNumberModel(new Integer((int) pi.getValue()), null, null, new Integer(1))) {

				/**
				 *
				 */
				private static final long serialVersionUID = 8728683428180496505L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width+=getValue().toString().length();
					return d;
				}

			};
			//js.setValue(pi.getValue());
			js.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					pi.setValue(js.getValue());
					js.revalidate();
				}
			});
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}
		case DOUBLE: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JSpinner js = new JSpinner(new SpinnerNumberModel(new Double((double) pi.getValue()), null, null, 0.1D)) {

				/**
				 *
				 */
				private static final long serialVersionUID = 8728683428180496505L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width+=getValue().toString().length();
					return d;
				}

			};
			//js.setValue(pi.getValue());
			js.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					pi.setValue(js.getValue());
					js.revalidate();
				}
			});
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}
		case LONG: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JSpinner js = new JSpinner(new SpinnerNumberModel(new Long((long) pi.getValue()), null, null, new Long(1))) {

				/**
				 *
				 */
				private static final long serialVersionUID = 8728683428180496505L;

				@Override
				public Dimension getPreferredSize() {
					Dimension d = super.getPreferredSize();
					d.width+=getValue().toString().length();
					return d;
				}

			};
			//js.setValue(pi.getValue());
			js.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					pi.setValue(js.getValue());
					js.revalidate();
				}
			});
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}

		case SHORT:
		 {
				JPanel subsub = new JPanel();
				JLabel label = new JLabel(pi.description);
				JSpinner js = new JSpinner(new SpinnerNumberModel(new Short((short) pi.getValue()), null, null, new Short((short) 1))) {

					/**
					 *
					 */
					private static final long serialVersionUID = 8728683428180496505L;

					@Override
					public Dimension getPreferredSize() {
						Dimension d = super.getPreferredSize();
						d.width+=getValue().toString().length();
						return d;
					}

				};
				//js.setValue(pi.getValue());
				js.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						pi.setValue(js.getValue());
						js.revalidate();
					}
				});
				subsub.add(label);
				subsub.add(js);
				return subsub;
			}
		default:
			throw new RuntimeException();
		}
	}

}
