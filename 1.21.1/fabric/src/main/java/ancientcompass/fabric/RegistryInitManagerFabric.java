package ancientcompass.fabric;

import ancientcompass.component.ModComponents;
import ancientcompass.item.AncientCompassItem;
import ancientcompass.item.ModItems;
import ancientcompass.utils.Loader;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistryInitManagerFabric {
    public static void registerComponents() {
        ModComponents.data.forEach((id, obj) -> {
            Registry.register(Registries.DATA_COMPONENT_TYPE, id, obj);
        });
    }

    public static void registerItems() {
        ModItems.data.forEach((id, obj) -> {
            Registry.register(Registries.ITEM, id, obj.get());
            for (var group : ModItems.groups.get(id)) {
                ItemGroupEvents.modifyEntriesEvent(group).register((itemGroup) -> itemGroup.add(obj.get()));
            }
        });

        if (Loader.isClient() && ModItems.ANCIENT_COMPASS.get() instanceof AncientCompassItem compassItem) {
            compassItem.registerModel();
        }
    }

    public static void init() {
        registerComponents();
        registerItems();
    }
}
