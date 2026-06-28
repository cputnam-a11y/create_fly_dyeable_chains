package io.github.cputnama11y.createflydyeablechains.client.util;

import com.zurrtum.create.content.fluids.transfer.GenericItemEmptying;
import io.github.cputnama11y.createflydyeablechains.CreateFlyDyeableChains;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public class ClientChainDyeing {
    public static Identifier getDyedChainTexture(DyeColor dyeColor) {
        return Identifier.fromNamespaceAndPath(
                CreateFlyDyeableChains.MOD_ID,
                "textures/block/" + dyeColor.getName() + "_chain.png"
        );
    }

    public static boolean isHoldingDye() {
        Minecraft client = Minecraft.getInstance();
        ItemStack handItem = Optionull.map(client.player, Player::getMainHandItem);
        if (handItem == null) return false;
        boolean itemHasWater = Optionull.mapOrDefault(client.level, level -> GenericItemEmptying.emptyItem(level, handItem, true)
                .getFirst()
                .getFluid()
                .isSame(Fluids.WATER), false);
        return itemHasWater || handItem.is(ConventionalItemTags.DYES);
    }
}
