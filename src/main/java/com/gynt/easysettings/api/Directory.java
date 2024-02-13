package com.gynt.easysettings.api;

import com.gynt.easysettings.api.abstraction.Collectable;
import com.gynt.easysettings.api.abstraction.Keyable;
import com.gynt.easysettings.api.abstraction.Settingable;

public interface Directory extends Collectable<Section>, Keyable<Section>, Settingable {

}
