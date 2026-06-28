package io.github.cputnama11y.createflydyeablechains.mixin;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.zurrtum.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import io.github.cputnama11y.createflydyeablechains.duck.ChainConveyorDuck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ChainConveyorBlockEntity.class)
public class ChainConveyorBlockEntityMixin implements ChainConveyorDuck {
    @Shadow
    public Set<BlockPos> connections;
    @Unique
    private final Map<BlockPos, DyeColor> create_fly_dyeable_chains$connectionColors = new HashMap<>();

    @Override
    public Map<BlockPos, DyeColor> create_fly_dyeable_chains$getConnectionColorMap() {
        return create_fly_dyeable_chains$connectionColors;
    }


    @Inject(method = "read", at = @At("TAIL"))
    private void read(ValueInput view, boolean clientPacket, CallbackInfo ci) {
        create_fly_dyeable_chains$connectionColors.clear();
        Iterable<Pair<BlockPos, DyeColor>> connectionColors = view.list("ConnectionColors", Codec.pair(BlockPos.CODEC.fieldOf("Connection").codec(), DyeColor.CODEC.fieldOf("Color").codec())).map(l -> (Iterable<Pair<BlockPos, DyeColor>>) l).orElse(Collections.emptyList());
        connectionColors.forEach(colorPair -> {
            create_fly_dyeable_chains$connectionColors.put(colorPair.getFirst(), colorPair.getSecond());
        });
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void write(ValueOutput view, boolean clientPacket, CallbackInfo ci) {
        ValueOutput.TypedOutputList<Pair<BlockPos, DyeColor>> list = view.list("ConnectionColors", Codec.pair(BlockPos.CODEC.fieldOf("Connection").codec(), DyeColor.CODEC.fieldOf("Color").codec()));
        create_fly_dyeable_chains$connectionColors.forEach((blockPos, dyeColor) -> list.add(Pair.of(blockPos, dyeColor)));
    }

    @Inject(method = "notifyUpdate", at = @At("TAIL"))
    private void notifyUpdate(CallbackInfo ci) {
        Set<BlockPos> posToBeRemoved = new HashSet<>();
        create_fly_dyeable_chains$connectionColors.forEach((blockPos, _) -> {
            if (!connections.contains(blockPos)) {
                posToBeRemoved.add(blockPos);
            }
        });
        posToBeRemoved.forEach(create_fly_dyeable_chains$connectionColors::remove);
    }
}
