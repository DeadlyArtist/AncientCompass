package ancientcompass.component;

import ancientcompass.ModEntry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ModComponents {

    public static Map<Identifier, ComponentType<?>> data = new HashMap<>();

    public static final ComponentType<GlobalPos> ANCIENT_COMPASS_DIMENSION = register("ANCIENT_COMPASS_DIMENSION", builder -> builder.codec(GlobalPos.CODEC).packetCodec(GlobalPos.PACKET_CODEC));


    public static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        var result = ((ComponentType.Builder) builderOperator.apply(ComponentType.builder())).build();
        data.put(Identifier.of(ModEntry.MOD_ID, id.toLowerCase()), result);
        return result;
    }

    public static void init() {

    }
}
