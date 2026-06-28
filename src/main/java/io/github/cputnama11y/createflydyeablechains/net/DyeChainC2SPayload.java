package io.github.cputnama11y.createflydyeablechains.net;

import com.zurrtum.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.zurrtum.create.foundation.blockEntity.SyncedBlockEntity;
import io.github.cputnama11y.createflydyeablechains.CreateFlyDyeableChains;
import io.github.cputnama11y.createflydyeablechains.duck.ChainConveyorDuck;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;

public class DyeChainC2SPayload implements CustomPacketPayload {
  public static final Type<DyeChainC2SPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(CreateFlyDyeableChains.MOD_ID, "chain_dye_interact"));
  public static final StreamCodec<FriendlyByteBuf, DyeChainC2SPayload> STREAM_CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC, packet -> packet.pos,
      BlockPos.STREAM_CODEC, packet -> packet.selectedConnection,
      ByteBufCodecs.optional(DyeColor.STREAM_CODEC), packet -> packet.dyeColor,
      DyeChainC2SPayload::new
  );
  private final BlockPos pos;
  private final BlockPos selectedConnection;
  private final Optional<DyeColor> dyeColor;

  public DyeChainC2SPayload(BlockPos pos, BlockPos selectedConnection, Optional<DyeColor> dyeColor) {
    this.pos = pos;
    this.selectedConnection = selectedConnection;
    this.dyeColor = dyeColor;
  }

  @Override
  public @NonNull Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void register() {
    PayloadTypeRegistry.serverboundPlay().register(TYPE, STREAM_CODEC);
    ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
      var player = context.player();
      if (player.isSpectator() || !player.mayBuild() && !player.isSpectator()) //:thinking:
        return;
      Level world = player.level();
      if (!world.isLoaded(payload.pos))
        return;
      if (!player.isWithinBlockInteractionRange(payload.pos, 20))
        return;
      BlockEntity blockEntity = world.getBlockEntity(payload.pos);
      if (blockEntity instanceof ChainConveyorBlockEntity chainConveyor) {
        if (payload.selectedConnection == null) return;
        BlockPos localConnection = payload.selectedConnection.subtract(chainConveyor.getBlockPos());
        if (!chainConveyor.connections.contains(localConnection)) return;

        ChainConveyorDuck mixedChainConveyor = (ChainConveyorDuck) chainConveyor;
        Map<BlockPos, DyeColor> connectionColors = mixedChainConveyor.create_fly_dyeable_chains$getConnectionColorMap();

        if (payload.dyeColor.isPresent()) {
          connectionColors.put(localConnection, payload.dyeColor.orElse(null));
        }
        else {
          connectionColors.remove(localConnection);
        }
        ((SyncedBlockEntity) blockEntity).sendData();
        blockEntity.setChanged();
      }
    });
  }
}
