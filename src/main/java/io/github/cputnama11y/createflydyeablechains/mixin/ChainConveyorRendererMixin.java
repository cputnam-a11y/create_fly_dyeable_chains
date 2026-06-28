package io.github.cputnama11y.createflydyeablechains.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.content.kinetics.chainConveyor.ChainConveyorRenderer;
import com.zurrtum.create.client.foundation.render.CreateRenderTypes;
import com.zurrtum.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import io.github.cputnama11y.createflydyeablechains.client.util.ClientChainDyeing;
import io.github.cputnama11y.createflydyeablechains.duck.ChainConveyorDuck;
import io.github.cputnama11y.createflydyeablechains.duck.ChainConveyorChainRenderStateDuck;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(ChainConveyorRenderer.class)
public class ChainConveyorRendererMixin {
    @Inject(
            method = "getChainsRenderState",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    private void addColorToRenderState(
            ChainConveyorBlockEntity be, Level level,
            BlockPos tilePos, Vec3 cameraPos,
            CallbackInfoReturnable<List<ChainConveyorRenderer.ChainRenderState>> cir,
            @Local(name = "blockPos") BlockPos blockPos, @Local(name = "state") ChainConveyorRenderer.ChainRenderState state) {
        ((ChainConveyorChainRenderStateDuck) state).create_fly_dyeable_chains$setConnectionColor(((ChainConveyorDuck) be).create_fly_dyeable_chains$getConnectionColorMap().get(blockPos));
    }
    @WrapOperation(
            method = "submit(Lcom/zurrtum/create/client/content/kinetics/chainConveyor/ChainConveyorRenderer$ChainConveyorRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lcom/zurrtum/create/client/content/kinetics/chainConveyor/ChainConveyorRenderer$ChainRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V")
    )
    private void renderChain(ChainConveyorRenderer.ChainRenderState instance, PoseStack matrices, RenderType layer, SubmitNodeCollector queue, Operation<Void> original) {
        if (((ChainConveyorChainRenderStateDuck)instance).create_fly_dyeable_chains$getConnectionColor() != null)
            layer = CreateRenderTypes.chain(ClientChainDyeing.getDyedChainTexture(((ChainConveyorChainRenderStateDuck)instance).create_fly_dyeable_chains$getConnectionColor()));
        original.call(instance, matrices, layer, queue);
    }
}
