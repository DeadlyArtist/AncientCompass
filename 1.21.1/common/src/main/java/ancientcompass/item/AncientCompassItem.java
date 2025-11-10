package ancientcompass.item;

import ancientcompass.component.ModComponents;
import ancientcompass.utils.Loader;
import ancientcompass.utils.StructureUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.gen.structure.Structures;

public class AncientCompassItem extends Item {
    public AncientCompassItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void registerModel() {
        ModelPredicateProviderRegistry.register(this, Identifier.of("angle"), new CompassAnglePredicateProvider(AncientCompassItem::getTargetPos));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient) return;

        var nearestCity = StructureUtils.locate((ServerWorld) world, StructureKeys.ANCIENT_CITY, entity.getBlockPos(), 100, false);
        if (nearestCity == null) {
            stack.remove(ModComponents.ANCIENT_COMPASS_DIMENSION);
        } else {
            var globalPos = GlobalPos.create(world.getRegistryKey(), nearestCity.getFirst());
            stack.set(ModComponents.ANCIENT_COMPASS_DIMENSION, globalPos);
        }
    }

    public static GlobalPos getTargetPos(ClientWorld world, ItemStack stack, Entity entity) {
        return stack.get(ModComponents.ANCIENT_COMPASS_DIMENSION);
    }
}
