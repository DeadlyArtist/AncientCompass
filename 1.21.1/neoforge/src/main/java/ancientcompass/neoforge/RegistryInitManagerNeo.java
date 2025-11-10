package ancientcompass.neoforge;

import ancientcompass.ModEntry;
import ancientcompass.component.ModComponents;
import ancientcompass.item.AncientCompassItem;
import ancientcompass.item.ModItems;
import ancientcompass.utils.Loader;
import ancientcompass.utils.qwe;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryInitManagerNeo {
    public static final DeferredRegister<ComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents((RegistryKey<Registry<ComponentType<?>>>) Registries.DATA_COMPONENT_TYPE.getKey(), ModEntry.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModEntry.MOD_ID);
    public static final Map<RegistryKey<ItemGroup>, List<Supplier<Item>>> GROUPS = new HashMap<>();


    public static void registerComponents() {
        ModComponents.data.forEach((id, obj) -> {
            Supplier<ComponentType<?>> sup = () -> obj;
            DATA_COMPONENT_TYPES.register(id.getPath(), sup);
        });
    }

    public static void registerItems() {
        ModItems.data.forEach((id, obj) -> {
            ITEMS.register(id.getPath(), obj);
            for (var group : ModItems.groups.get(id)) {
                if (!GROUPS.containsKey(group)) GROUPS.put(group, new ArrayList<>());
                GROUPS.get(group).add(obj);
            }
        });
    }

    public static void init() {
        registerComponents();
        registerItems();
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
        ITEMS.register(eventBus);
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        var key = event.getTabKey();
        if (GROUPS.containsKey(key)) {
            for (var item : GROUPS.get(key)) {
                event.add(item.get());
            }
        }
    }
}
