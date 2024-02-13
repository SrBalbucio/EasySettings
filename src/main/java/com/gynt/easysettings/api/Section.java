package com.gynt.easysettings.api;

import com.gynt.easysettings.api.abstraction.Collectable;
import com.gynt.easysettings.api.abstraction.Keyable;
import com.gynt.easysettings.api.abstraction.Settingable;

public interface Section extends Collectable<Setting>, Keyable<Setting>, Settingable {

}
