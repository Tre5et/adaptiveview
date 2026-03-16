package net.treset.adaptiveview.tools;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.treset.adaptiveview.AdaptiveViewMod;

import java.util.List;
import java.util.function.Function;

public class TextTools {
    public static MutableComponent formatText(String text) {
        boolean italic = false;
        boolean bold = false;
        boolean underline = false;
        TextColor color = TextColor.fromLegacyFormat(ChatFormatting.WHITE);

        int lastSplitIndex = 0;

        MutableComponent out = Component.literal("");

        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) != '$') continue;

            if(text.charAt(i + 1) == 'i') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                italic = !italic;
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'b') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                bold = !bold;
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'u') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                underline = !underline;
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'R') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.RED);
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'N') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.GRAY);
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'g') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN);
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'P') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE);
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'G') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.GOLD);
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'A') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.DARK_AQUA);
                lastSplitIndex = i + 2;
            }
            else if(text.charAt(i + 1) == 'W') {
                out.append(applyFormatting(text.substring(lastSplitIndex, i), italic, bold, underline, color));
                color = TextColor.fromLegacyFormat(ChatFormatting.WHITE);
                lastSplitIndex = i + 2;
            }
        }

        out.append(applyFormatting(text.substring(lastSplitIndex), italic, bold, underline, color));

        return out;
    }

    private static MutableComponent applyFormatting(String text, boolean italic, boolean bold, boolean underline, TextColor color) {
        MutableComponent add = Component.literal(text);
        if(italic) add.withStyle(ChatFormatting.ITALIC);
        if(bold) add.withStyle(ChatFormatting.BOLD);
        if(underline) add.withStyle(ChatFormatting.UNDERLINE);
        if(color != TextColor.fromLegacyFormat(ChatFormatting.WHITE)) add.withColor(color.getValue());
        return add;
    }

    public static void replyFormatted(CommandContext<CommandSourceStack> ctx, boolean broadcastToOps, String text, Object... args) {
        ctx.getSource().sendSuccess(() -> formatText(String.format(text, args)), broadcastToOps);
    }

    public static void replyFormatted(CommandContext<CommandSourceStack> ctx, String text, Object... args) {
        replyFormatted(ctx, false, text, args);
    }

    public static void replyError(CommandContext<CommandSourceStack> ctx, String text) {
        ctx.getSource().sendFailure(Component.literal(text));
    }

    public static void broadcastIf(Function<ServerPlayer, Boolean> condition, String message, Object... args) {
        message = "$N$i[AdaptiveView] " + message;
        Component formated = formatText(String.format(message, args));

        List<ServerPlayer> players = AdaptiveViewMod.getServer().getPlayerList().getPlayers();
        for(ServerPlayer player : players) {
            if(condition == null || condition.apply(player)) {
                player.sendSystemMessage(formated);
            }
        }
    }

    public static void replyAndBroadcastIf(Function<ServerPlayer, Boolean> condition, CommandContext<CommandSourceStack> ctx, String message, Object... args) {
        replyFormatted(ctx, message, args);
        broadcastIf((player) -> player != ctx.getSource().getPlayer() && (condition == null || condition.apply(player)), message, args);
    }

    public static boolean containsIgnoreCase(List<String> list, String str) {
        return list.stream().map(String::toLowerCase).toList().contains(str.toLowerCase());
    }
}
