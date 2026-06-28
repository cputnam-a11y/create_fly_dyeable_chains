package io.github.cputnama11y.createflydyeablechains.mixin;

import com.zurrtum.create.client.content.kinetics.chainConveyor.ChainConveyorRenderer;
import io.github.cputnama11y.createflydyeablechains.duck.ChainConveyorChainRenderStateDuck;
import net.minecraft.world.item.DyeColor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChainConveyorRenderer.ChainRenderState.class)
public class ChainRenderStateMixin implements ChainConveyorChainRenderStateDuck {
    @Unique
    @Nullable
    private DyeColor chainColor;


    @Override
    public DyeColor create_fly_dyeable_chains$getConnectionColor() {
        return chainColor;
    }

    @Override
    public void create_fly_dyeable_chains$setConnectionColor(DyeColor color) {
        this.chainColor = color;
    }


}
