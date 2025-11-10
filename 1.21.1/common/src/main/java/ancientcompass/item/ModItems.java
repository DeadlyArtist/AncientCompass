package ancientcompass.item;

import ancientcompass.ModEntry;
import ancientcompass.utils.Lazy;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModItems {

    public static Map<Identifier, Supplier<Item>> data = new HashMap<>();
    public static Map<Identifier, List<RegistryKey<ItemGroup>>> groups = new HashMap<>();

    public static final Lazy<Item> ANCIENT_COMPASS = register("ANCIENT_COMPASS", () -> new AncientCompassItem(new Item.Settings()), List.of(ItemGroups.TOOLS));


    public static Lazy<Item> register(String id, Supplier<Item> item, List<RegistryKey<ItemGroup>> groups) {
        var fullId = Identifier.of(ModEntry.MOD_ID, id.toLowerCase());
        var lazy = new Lazy<Item>(item);
        data.put(fullId, lazy);
        ModItems.groups.put(fullId, groups);
        return lazy;
    }

    public static void init() {

    }
}
