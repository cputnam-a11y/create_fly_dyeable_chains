package io.github.cputnama11y.createflydyeablechains;

import io.github.cputnama11y.createflydyeablechains.net.DyeChainC2SPayload;
import net.fabricmc.api.ModInitializer;

public class CreateFlyDyeableChains implements ModInitializer {
  public static final String MOD_ID = "create_fly_dyeable_chains";

  @Override
  public void onInitialize() {
    DyeChainC2SPayload.register();
  }
}
