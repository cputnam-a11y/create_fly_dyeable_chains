package io.github.cputnama11y.createflydyeablechains.mixin;

import com.zurrtum.create.client.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import io.github.cputnama11y.createflydyeablechains.net.DyeChainC2SPayload;
import io.github.cputnama11y.createflydyeablechains.client.util.ClientChainDyeing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ChainConveyorInteractionHandler.class)
public class ChainConveyorInteractionHandlerMixin {
  @Shadow
  public static BlockPos selectedLift;

  @Shadow
  public static BlockPos selectedConnection;

  @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
  private static void isActive(CallbackInfoReturnable<Boolean> cir) {
    if (ClientChainDyeing.isHoldingDye()) {
      cir.setReturnValue(true);
    }
  }

  @ModifyVariable(method = "clientTick", name = "isWrench", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEyePosition()Lnet/minecraft/world/phys/Vec3;",
      shift = At.Shift.AFTER))
  private static boolean isWrench(boolean isWrench) {
    return ClientChainDyeing.isHoldingDye() || isWrench;
  }

  @Inject(method = "onUse", at = @At(value = "TAIL"), cancellable = true)
  private static void onUse(CallbackInfoReturnable<Boolean> cir) {
    if (ClientChainDyeing.isHoldingDye()) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      ItemStack dyeItem = player.getMainHandItem();
      ClientPlayNetworking.send(new DyeChainC2SPayload(selectedLift, selectedLift.offset(selectedConnection), Optional.ofNullable(dyeItem.get(DataComponents.DYE))));

      cir.setReturnValue(true);
    }
  }
}
