package net.treset.adaptiveview;

import net.fabricmc.api.ClientModInitializer;

public class AdaptiveViewClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AdaptiveViewMod.setClient(true);
        if(!AdaptiveViewMod.getConfig().isOverrideClient()) {
            AdaptiveViewMod.LOGGER.warn("AdaptiveView is a server only mod and will thus not be loaded in a client environment. To override this behaviour, set overrideClient to true in the config.");
        }
    }
}
