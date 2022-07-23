package net.treset.dynview.distance;

import net.minecraft.server.MinecraftServer;
import net.treset.dynview.DynViewMod;
import net.treset.dynview.config.Config;
import net.treset.dynview.tools.MathTools;

import java.util.*;

public class ServerTickHandler {

    private static int tickCounter = 0;
    private static final List<Long> tickLengths = new ArrayList<>();

    public static void onTick(MinecraftServer server) {

        tickCounter++;


        if(tickCounter % 100 == 0 || tickCounter % Config.getUpdateInterval() == 0) {
            int endValue = server.getTicks() % 100 + 1;
            int startValue = endValue - 1 - (tickCounter - 1) % 100;
            int carry = Math.max(0, -startValue) - 1;
            startValue = Math.max(0, startValue);
            long[] arr = Arrays.copyOfRange(server.lastTickLengths, startValue, endValue);
            for(long e : arr) {
                tickLengths.add(e);
            }
            if(carry != -1) {
                for(long e : Arrays.copyOfRange(server.lastTickLengths, 99 - carry, 100)) {
                    tickLengths.add(e);
                }
            }

            if(tickCounter == Config.getUpdateInterval()) {
                tickCounter = 0;
                DynViewMod.LOGGER.info(String.valueOf(MathTools.longArrayAverage(tickLengths.toArray(new Long[0]))));
                tickLengths.clear();
            }
        }
    }
}
