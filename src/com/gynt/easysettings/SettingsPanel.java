package com.gynt.easysettings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
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
import javax.swing.SpinnerModel;
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

public class SettingsPanel extends JPanel {

	public static void expandAllNodes(JTree tree) {
	    int j = tree.getRowCount();
	    int i = 0;
	    while(i < j) {
	        tree.expandRow(i);
	        i += 1;
	        j = tree.getRowCount();
	    }
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4104785855475407998L;
	private JPanel prefPanel;
	private JTree prefTree;
	private SettingsRenderer renderer;
	private Dir dirroot;

	/**
	 * Create the panel.
	 */
	private SettingsPanel() {
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.33);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		prefTree = new JTree();
		//prefTree.setRootVisible(false);
		prefTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				JPanel d = (JPanel) ((DefaultMutableTreeNode) arg0.getNewLeadSelectionPath()
						.getLastPathComponent()).getUserObject();

				prefPanel.removeAll();
				prefPanel.add(d, BorderLayout.CENTER);

				d.revalidate();
				prefPanel.repaint();
			}
		});
		scrollPane.setViewportView(prefTree);

		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		prefPanel = new JPanel(new BorderLayout(0, 0));
		panel_1.add(prefPanel);

		renderer = new DefaultSettingsRenderer();
	}
	
	public SettingsPanel(Dir root) {
		this();
		dirroot = root;
	}
	
	public void setRenderer(SettingsRenderer sr) {
		renderer=sr;
	}

	public void render() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(renderer.getDirComponent(null, dirroot));
		prefTree.setModel(new DefaultTreeModel(root));
		dirbuild(root, dirroot);
		expandAllNodes(prefTree);
	}

	public void dirbuild(DefaultMutableTreeNode currentnode, Dir current) {
		for (Dir dir : current.dirs) {
			DefaultMutableTreeNode sub = new DefaultMutableTreeNode(renderer.getDirComponent(null, dir));
			currentnode.add(sub);
			dirbuild(sub, dir);
		}
	}
}
