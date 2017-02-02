package com.gynt.easysettings;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import com.gynt.easysettings.Settings.Dir;
import com.gynt.easysettings.Settings.Item;
import com.gynt.easysettings.Settings.Sub;

public interface SettingsRenderer {

	void render(JComponent parent, Dir root);

	JComponent getSettingsComponent(JComponent parent, Settings settings);

	JComponent getDirComponent(JComponent parent, Dir dir);

	JComponent getSubComponent(JComponent parent, Sub sub);

	JComponent getItemComponent(JComponent parent, Item item, ButtonGroup bg);

}
