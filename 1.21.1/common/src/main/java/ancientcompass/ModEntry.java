package ancientcompass;

import ancientcompass.component.ModComponents;
import ancientcompass.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModEntry {
    public static final String MOD_ID = "ancientcompass";
    public static final Logger _LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // Write common init code here.
        ModComponents.init();
        ModItems.init();
    }

    public static void preInitialize() {

    }
}
