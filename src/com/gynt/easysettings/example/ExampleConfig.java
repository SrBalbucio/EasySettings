package com.gynt.easysettings.example;

import com.gynt.easysettings.api.Directory;
import com.gynt.easysettings.api.Section;
import com.gynt.easysettings.api.Setting;
import com.gynt.easysettings.implementation.FileType;
import com.gynt.easysettings.implementation.SimpleConfig;
import com.gynt.easysettings.implementation.SimpleDirectory;
import com.gynt.easysettings.implementation.SimpleSection;
import com.gynt.easysettings.implementation.SimpleSetting;
import com.gynt.easysettings.implementation.Types;

public class ExampleConfig extends SimpleConfig {

	{
		Types t = new Types();
		Directory general = new SimpleDirectory("general", "General", "General configuration");
		this.add(general);
		Section page = new SimpleSection("page", "Page", "Page configuration");
		general.add(page);
		Setting width = new SimpleSetting("width", "Width (mm)", "Width of the page", t.forType("INTEGER"));
		width.setValue(210);
		page.add(width);
		Setting height = new SimpleSetting("height", "Height (mm)", "Height of the page", t.forType("INTEGER"));
		height.setValue(297);
		page.add(height);
		Section pdf = new SimpleSection("pdf", "PDF", "PDF configuration");
		Setting source = new SimpleSetting("src", "Source PDF", "Select the source PDF",
				new FileType("FILE", String.class, "pdf"));
		pdf.add(source);
		Setting test = new SimpleSetting("test", "Test", "", t.forType("STRING"));
		pdf.add(test);
		general.add(pdf);
	}

}
