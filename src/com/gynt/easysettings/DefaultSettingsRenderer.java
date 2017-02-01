package com.gynt.easysettings;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.gynt.easysettings.Settings.Dir;
import com.gynt.easysettings.Settings.Item;
import com.gynt.easysettings.Settings.Sub;

public class DefaultSettingsRenderer implements SettingsRenderer {
	


	@Override
	public void render(JComponent parent, Dir root) {

	}

	@Override
	public JComponent getDirComponent(JComponent parent, Dir dir) {
		Dir source = dir;
		JPanel panel = new JPanel();
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
		case FILE: {
			JLabel label = new JLabel(pi.description);
			JTextField jtf = new JTextField(pi.getValue().toString());
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
					int result = jfc.showOpenDialog(browse);
					if(result==JFileChooser.APPROVE_OPTION) {
						jtf.setText(jfc.getSelectedFile().toString());
						//pi.setValue(jfc.getSelectedFile().toString()); //Use listener?
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
			JPanel subsub=new JPanel();
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
			JSpinner js = new JSpinner(new SpinnerModel() {

				private ArrayList<ChangeListener> listeners = new ArrayList<>();

				@Override
				public void setValue(Object value) {
					pi.setValue(value);
					for(ChangeListener l : listeners) {
						l.stateChanged(new ChangeEvent(this));
					}
				}

				@Override
				public void removeChangeListener(ChangeListener l) {
					listeners.remove(l);
				}

				@Override
				public Object getValue() {
					return pi.getValue();
				}

				@Override
				public Object getPreviousValue() {
					return (Float)getValue()-0.1f;
				}

				@Override
				public Object getNextValue() {
					return (Float)getValue()+0.1f;
				}

				@Override
				public void addChangeListener(ChangeListener l) {
					listeners.add(l);
				}
			});
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}
		case INTEGER: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JSpinner js = new JSpinner(new SpinnerModel() {

				private ArrayList<ChangeListener> listeners = new ArrayList<>();

				@Override
				public void setValue(Object value) {
					pi.setValue(value);
					for(ChangeListener l : listeners) {
						l.stateChanged(new ChangeEvent(this));
					}
				}

				@Override
				public void removeChangeListener(ChangeListener l) {
					listeners.remove(l);
				}

				@Override
				public Object getValue() {
					return pi.getValue();
				}

				@Override
				public Object getPreviousValue() {
					return (Integer)getValue()-1;
				}

				@Override
				public Object getNextValue() {
					return (Integer)getValue()+1;
				}

				@Override
				public void addChangeListener(ChangeListener l) {
					listeners.add(l);
				}
			});
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}
		case DOUBLE: {
			JPanel subsub = new JPanel();
			JLabel label = new JLabel(pi.description);
			JTextField js = new JTextField();
			js.setText(pi.getValue().toString());
			js.setInputVerifier(new InputVerifier() {

				@Override
				public boolean verify(JComponent input) {
					String text = ((JTextField) input).getText();
				       try {
				            Double.parseDouble(text);
				            return true;
				        } catch (NumberFormatException e) {
				            return false;
				        }

				}
			});
			js.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					//pi.setValue(Double.parseDouble(js.getText()));
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					//pi.setValue(Double.parseDouble(js.getText()));
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					pi.setValue(Double.parseDouble(js.getText()));
				}
			});
			subsub.add(label);
			subsub.add(js);
			return subsub;
		}
		case LONG:
			break;
		case SHORT:
			break;
		default:
			break;
		}
		return null;
	}

}
