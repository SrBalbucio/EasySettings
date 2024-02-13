package com.gynt.easysettings.api.rendering;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.gynt.easysettings.api.Config;
import com.gynt.easysettings.api.Directory;
import com.gynt.easysettings.api.Section;
import com.gynt.easysettings.api.Setting;

public interface Renderer {

	public JTabbedPane render(Config config);

	public JPanel renderDirectory(Directory dir);

	public JPanel renderSection(Section section);

	public JComponent[] renderSettingComponents(Setting setting, ButtonGroup bg);

}
