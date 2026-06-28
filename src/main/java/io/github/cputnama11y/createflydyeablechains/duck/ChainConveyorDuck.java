package io.github.cputnama11y.createflydyeablechains.duck;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

public interface ChainConveyorDuck {
  Map<BlockPos, DyeColor> create_fly_dyeable_chains$getConnectionColorMap();
}
