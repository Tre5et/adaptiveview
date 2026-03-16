package net.treset.adaptiveview.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permissions;
import net.treset.adaptiveview.config.Config;
import net.treset.adaptiveview.config.Rule;
import net.treset.adaptiveview.config.RuleTarget;
import net.treset.adaptiveview.config.RuleType;
import net.treset.adaptiveview.tools.BroadcastLevel;
import net.treset.adaptiveview.tools.TextTools;

import java.io.IOException;
import java.util.function.BiConsumer;

public class ConfigCommandHandler {
    private final Config config;

    public ConfigCommandHandler(Config config) {
        this.config = config;
    }

    public int list(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Current Configuration:");
        TextTools.replyFormatted(ctx, "Update Rate: $b%d ticks", config.getUpdateRate());
        TextTools.replyFormatted(ctx, "View Distance Range: $b%d-%d chunks", config.getMinViewDistance(), config.getMaxViewDistance());
        TextTools.replyFormatted(ctx, "Simulation Distance Range: $b%d-%d chunks", config.getMinSimDistance(), config.getMaxSimDistance());
        TextTools.replyFormatted(ctx, "Rules: $b%s$b", config.getRules().size());
        return 1;
    }

    public int reload(CommandContext<CommandSourceStack> ctx) {
        Config config;
        try {
            config = Config.load();
        } catch (IOException e) {
            TextTools.replyError(ctx, "Failed to reload Config! Check for syntax errors.");
            return 0;
        }

        this.config.copy(config);
        TextTools.replyFormatted(ctx, "Reloaded Configuration!", false);
        return 1;
    }

    public int updateRate(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Update Rate: $b%s ticks", config.getUpdateRate());
        return 1;
    }

    public int setUpdateRate(CommandContext<CommandSourceStack> ctx) {
        Integer ticks = ctx.getArgument("ticks", Integer.class);
        config.setUpdateRate(ticks);
        config.save();
        TextTools.replyFormatted(ctx, "Set Update Rate to $b%s ticks", config.getUpdateRate());
        return 1;
    }

    public int maxView(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Max View Distance: $b%d chunks", config.getMaxViewDistance());
        return 1;
    }

    public int setMaxView(CommandContext<CommandSourceStack> ctx) {
        Integer chunks = ctx.getArgument("chunks", Integer.class);
        config.setMaxViewDistance(chunks);
        config.save();
        TextTools.replyFormatted(ctx, "Set Max View Distance to $b%d chunks", config.getMaxViewDistance());
        return 1;
    }

    public int minView(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Min View Distance: $b%s chunks", config.getMinViewDistance());
        return 1;
    }

    public int setMinView(CommandContext<CommandSourceStack> ctx) {
        Integer chunks = ctx.getArgument("chunks", Integer.class);
        config.setMinViewDistance(chunks);
        config.save();
        TextTools.replyFormatted(ctx, "Set Min View Distance to $b%s chunks", config.getMinViewDistance());
        return 1;
    }

    public int maxSim(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Max Simulation Distance: $b%s chunks", config.getMaxSimDistance());
        return 1;
    }

    public int setMaxSim(CommandContext<CommandSourceStack> ctx) {
        Integer chunks = ctx.getArgument("chunks", Integer.class);
        config.setMaxSimDistance(chunks);
        config.save();
        TextTools.replyFormatted(ctx, "Set Max Simulation Distance to $b%s chunks", config.getMaxSimDistance());
        return 1;
    }

    public int minSim(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Min Simulation Distance: $b%s chunks", config.getMinSimDistance());
        return 1;
    }

    public int setMinSim(CommandContext<CommandSourceStack> ctx) {
        Integer chunks = ctx.getArgument("chunks", Integer.class);
        config.setMinSimDistance(chunks);
        config.save();
        TextTools.replyFormatted(ctx, "Set Min Simulation Distance to $b%s chunks", config.getMinSimDistance());
        return 1;
    }

    public int broadcastChanges(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Broadcasting view distance changes to $b%s", switch(config.getBroadcastChangesDefault()) {
            case ALL -> "all players";
            case OPS -> "operators";
            case NONE -> "no one";
        });
        return 1;
    }

    public int broadcastChangesNone(CommandContext<CommandSourceStack> ctx) {
        config.setBroadcastChangesDefault(BroadcastLevel.NONE);
        config.save();
        TextTools.replyFormatted(ctx, "Set broadcast changes to $bno one");
        return 1;
    }

    public int broadcastChangesOps(CommandContext<CommandSourceStack> ctx) {
        config.setBroadcastChangesDefault(BroadcastLevel.OPS);
        config.save();
        TextTools.replyFormatted(ctx, "Set broadcast changes to $boperators");
        return 1;
    }

    public int broadcastChangesAll(CommandContext<CommandSourceStack> ctx) {
        config.setBroadcastChangesDefault(BroadcastLevel.ALL);
        config.save();
        TextTools.replyFormatted(ctx, "Set broadcast changes to $ball players");
        return 1;
    }

    public int broadcastLock(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Broadcasting view distance locking and unlocking to $b%s", switch(config.getBroadcastLockDefault()) {
            case ALL -> "all players";
            case OPS -> "operators";
            case NONE -> "no one";
        });
        return 1;
    }

    public int broadcastLockNone(CommandContext<CommandSourceStack> ctx) {
        config.setBroadcastLockDefault(BroadcastLevel.NONE);
        config.save();
        TextTools.replyFormatted(ctx, "Set broadcast lock to $bno one");
        return 1;
    }

    public int broadcastLockOps(CommandContext<CommandSourceStack> ctx) {
        config.setBroadcastLockDefault(BroadcastLevel.OPS);
        config.save();
        TextTools.replyFormatted(ctx, "Set broadcast lock to $boperators");
        return 1;
    }

    public int broadcastLockAll(CommandContext<CommandSourceStack> ctx) {
        config.setBroadcastLockDefault(BroadcastLevel.ALL);
        config.save();
        TextTools.replyFormatted(ctx, "Set broadcast lock to $ball players");
        return 1;
    }

    public int rules(CommandContext<CommandSourceStack> ctx) {
        TextTools.replyFormatted(ctx, "Current Rules:");
        for(int i = 0; i < config.getRules().size(); i++) {
            TextTools.replyFormatted(ctx, "%d. %s", i + 1, config.getRules().get(i));
        }
        return 1;
    }

    private int performRuleAction(CommandContext<CommandSourceStack> ctx, BiConsumer<Integer, Rule> action) {
        Integer index = ctx.getArgument("index", Integer.class);
        if(index == null || index <= 0 || index > config.getRules().size()) {
            TextTools.replyError(ctx, "Rule at index $b" + index + "$b doesn't exist. Needs to be at most " + (config.getRules().size() - 1) + ".");
            return 0;
        }
        action.accept(index, config.getRules().get(index - 1));
        return 1;
    }

    public int ruleIndex(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> TextTools.replyFormatted(ctx, "Rule $b%d$b: %s", i, r));
    }

    public int ruleRemove(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            config.getRules().remove(i - 1);
            config.save();
            TextTools.replyFormatted(ctx, "Removed rule $b%d$b.", i);
        });
    }

    public int ruleName(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Name of rule $b%d$b: $b%s$b", i, r.getName());
        });
    }

    public int ruleSetName(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            String name = ctx.getArgument("name", String.class);
            r.setName(name);
            config.save();
            TextTools.replyFormatted(ctx, "Set Name of rule $b%d$b to $b%s$b", i, r.getName());
        });
    }

    public int ruleClearName(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setName(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Name of rule $b%d$b", i);
        });
    }

    public int ruleCondition(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Condition of rule $b%d$b: %s", i, r.toConditionString());
        });
    }

    public int ruleType(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Condition Type of rule $b%d$b: $b%s$b", i, r.getType());
        });
    }

    private int setRuleType(CommandContext<CommandSourceStack> ctx, RuleType type) {
        return performRuleAction(ctx, (i, r) -> {
            r.setType(type);
            config.save();
            TextTools.replyFormatted(ctx, "Set Condition Type of rule $b%d$b to $b%s$b", i, r.getType());
        });
    }

    public int ruleTypeSetMspt(CommandContext<CommandSourceStack> ctx) {
        return setRuleType(ctx, RuleType.MSPT);
    }

    public int ruleTypeSetMemory(CommandContext<CommandSourceStack> ctx) {
        return setRuleType(ctx, RuleType.MEMORY);
    }

    public int ruleTypeSetPlayers(CommandContext<CommandSourceStack> ctx) {
        return setRuleType(ctx, RuleType.PLAYERS);
    }

    public int ruleValue(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
           TextTools.replyFormatted(ctx, "Value of rule $b%d$b: $b%s$b", i, r.getValue());
        });
    }

    public int ruleSetValue(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            String value = ctx.getArgument("value", String.class);
            r.setValue(value);
            config.save();
            TextTools.replyFormatted(ctx, "Set Value of rule $b%d$b to $b%s$b", i, r.getValue());
        });
    }

    public int ruleClearValue(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setValue(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Value of rule $b%d$b", i);
        });
    }

    public int ruleMin(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Min value of rule $b%d$b: $b%s$b", i, r.getMin());
        });
    }

    public int ruleSetMin(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer min = ctx.getArgument("min", Integer.class);
            r.setMin(min);
            config.save();
            TextTools.replyFormatted(ctx, "Set Min value of rule $b%d$b to $b%s$b", i, r.getMin());
        });
    }

    public int ruleClearMin(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setMin(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Min value of rule $b%d$b", i);
        });
    }

    public int ruleMax(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Max value of rule $b%d$b: $b%s$b", i, r.getMax());
        });
    }

    public int ruleSetMax(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer max = ctx.getArgument("max", Integer.class);
            r.setMax(max);
            config.save();
            TextTools.replyFormatted(ctx, "Set Max value of rule $b%d$b to $b%s$b", i, r.getMax());
        });
    }

    public int ruleClearMax(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setMax(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Max value of rule $b%d$b", i);
        });
    }

    public int ruleAction(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Action of rule $b%d$b: %s", i, r.toActionString());
        });
    }

    public int ruleTarget(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Action Target of rule $b%d$b: $b%s$b", i, r.getTarget().getName());
        });
    }

    private int ruleSetTarget(CommandContext<CommandSourceStack> ctx, RuleTarget target) {
        return performRuleAction(ctx, (i, r) -> {
            r.setTarget(target);
            config.save();
            TextTools.replyFormatted(ctx, "Set Action Target of rule $b%d$b to $b%s$b", i, r.getTarget().getName());
        });
    }

    public int ruleSetTargetView(CommandContext<CommandSourceStack> ctx) {
        return ruleSetTarget(ctx, RuleTarget.VIEW);
    }

    public int ruleSetTargetSim(CommandContext<CommandSourceStack> ctx) {
        return ruleSetTarget(ctx, RuleTarget.SIMULATION);
    }

    public int ruleUpdateRate(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Update Rate of rule $b%d$b: $b%s$b", i, r.getUpdateRate());
        });
    }

    public int ruleSetUpdateRate(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer updateRate = ctx.getArgument("ticks", Integer.class);
            r.setUpdateRate(updateRate);
            config.save();
            TextTools.replyFormatted(ctx, "Set Update Rate of rule $b%d$b to $b%s$b", i, r.getUpdateRate());
        });
    }

    public int ruleClearUpdateRate(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setUpdateRate(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Update Rate of rule $b%d$b", i);
        });
    }

    public int ruleStep(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Step of rule $b%d$b: $b%s$b", i, r.getStep());
        });
    }

    public int ruleSetStep(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer step = ctx.getArgument("step", Integer.class);
            r.setStep(step);
            config.save();
            TextTools.replyFormatted(ctx, "Set Step of rule $b%d$b to $b%s$b", i, r.getStep());
        });
    }

    public int ruleClearStep(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setStep(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Step of rule $b%d$b", i);
        });
    }

    public int ruleStepAfter(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Step After of rule $b%d$b: $b%s$b", i, r.getStepAfter());
        });
    }

    public int ruleSetStepAfter(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer step = ctx.getArgument("step-after", Integer.class);
            r.setStepAfter(step);
            config.save();
            TextTools.replyFormatted(ctx, "Set Step After of rule $b%d$b to $b%s$b", i, r.getStepAfter());
        });
    }

    public int ruleClearStepAfter(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setStepAfter(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Step After of rule $b%d$b", i);
        });
    }

    public int ruleMinDistance(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Min Distance of rule $b%d$b: $b%s$b", i, r.getMinDistance());
        });
    }

    public int ruleSetMinDistance(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer min = ctx.getArgument("chunks", Integer.class);
            r.setMinDistance(min);
            config.save();
            TextTools.replyFormatted(ctx, "Set Min Distance of rule $b%d$b to $b%s$b", i, r.getMinDistance());
        });
    }

    public int ruleClearMinDistance(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setMinDistance(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Min Distance of rule $b%d$b", i);
        });
    }

    public int ruleMaxDistance(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            TextTools.replyFormatted(ctx, "Max Distance of rule $b%d$b: $b%s$b", i, r.getMaxDistance());
        });
    }

    public int ruleSetMaxDistance(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            Integer max = ctx.getArgument("chunks", Integer.class);
            r.setMaxDistance(max);
            config.save();
            TextTools.replyFormatted(ctx, "Set Max Distance of rule $b%d$b to $b%s$b", i, r.getMaxDistance());
        });
    }

    public int ruleClearMaxDistance(CommandContext<CommandSourceStack> ctx) {
        return performRuleAction(ctx, (i, r) -> {
            r.setMaxDistance(null);
            config.save();
            TextTools.replyFormatted(ctx, "Cleared Max Distance of rule $b%d$b", i);
        });
    }

    private int addRule(CommandContext<CommandSourceStack> ctx, RuleType type, String value, Integer max, Integer min, RuleTarget target) {
        Rule r = new Rule(
                type,
                value,
                max,
                min,
                target,
                null,
                null,
                null,
                null,
                null,
                null
        );
        config.getRules().add(r);
        config.save();
        TextTools.replyFormatted(ctx, "Added new Rule at index $b%d$b. Modify the action to make it effective.", config.getRules().size());
        return 1;
    }

    public int addMsptMinView(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        return addRule(ctx, RuleType.MSPT, null, null, min, RuleTarget.VIEW);
    }

    public int addMsptMinSim(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        return addRule(ctx, RuleType.MSPT, null, null, min, RuleTarget.SIMULATION);
    }

    public int addMsptMaxView(CommandContext<CommandSourceStack> ctx) {
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MSPT, null, max, null, RuleTarget.VIEW);
    }

    public int addMsptMaxSim(CommandContext<CommandSourceStack> ctx) {
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MSPT, null, max, null, RuleTarget.SIMULATION);
    }

    public int addMsptRangeView(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MSPT, null, max, min, RuleTarget.VIEW);
    }

    public int addMsptRangeSim(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MSPT, null, max, min, RuleTarget.SIMULATION);
    }

    public int addMemoryMinView(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        return addRule(ctx, RuleType.MEMORY, null, null, min, RuleTarget.VIEW);
    }

    public int addMemoryMinSim(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        return addRule(ctx, RuleType.MEMORY, null, null, min, RuleTarget.SIMULATION);
    }

    public int addMemoryMaxView(CommandContext<CommandSourceStack> ctx) {
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MEMORY, null, max, null, RuleTarget.VIEW);
    }

    public int addMemoryMaxSim(CommandContext<CommandSourceStack> ctx) {
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MEMORY, null, max, null, RuleTarget.SIMULATION);
    }

    public int addMemoryRangeView(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MEMORY, null, max, min, RuleTarget.VIEW);
    }

    public int addMemoryRangeSim(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.MEMORY, null, max, min, RuleTarget.SIMULATION);
    }

    public int addPlayersMinView(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        return addRule(ctx, RuleType.PLAYERS, null, null, min, RuleTarget.VIEW);
    }

    public int addPlayersMinSim(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        return addRule(ctx, RuleType.PLAYERS, null, null, min, RuleTarget.SIMULATION);
    }

    public int addPlayersMaxView(CommandContext<CommandSourceStack> ctx) {
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.PLAYERS, null, max, null, RuleTarget.VIEW);
    }

    public int addPlayersMaxSim(CommandContext<CommandSourceStack> ctx) {
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.PLAYERS, null, max, null, RuleTarget.SIMULATION);
    }

    public int addPlayersRangeView(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.PLAYERS, null, max, min, RuleTarget.VIEW);
    }

    public int addPlayersRangeSim(CommandContext<CommandSourceStack> ctx) {
        Integer min = ctx.getArgument("min", Integer.class);
        Integer max = ctx.getArgument("max", Integer.class);
        return addRule(ctx, RuleType.PLAYERS, null, max, min, RuleTarget.SIMULATION);
    }

    public int addPlayersNameView(CommandContext<CommandSourceStack> ctx) {
        String name = ctx.getArgument("names", String.class);
        return addRule(ctx, RuleType.PLAYERS, name, null, null, RuleTarget.VIEW);
    }

    public int addPlayersNameSim(CommandContext<CommandSourceStack> ctx) {
        String name = ctx.getArgument("names", String.class);
        return addRule(ctx, RuleType.PLAYERS, name, null, null, RuleTarget.SIMULATION);
    }

    public void registerCommands(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("config")
                .requires(s -> s.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .executes(this::list)
                .then(Commands.literal("status")
                        .executes(this::list)
                )
                .then(Commands.literal("reload")
                        .executes(this::reload)
                )
                .then(Commands.literal("update-rate")
                        .executes(this::updateRate)
                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1, 72000))
                                .executes(this::setUpdateRate)
                        )
                )
                .then(Commands.literal("max-view-distance")
                        .executes(this::maxView)
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::setMaxView)
                        )
                )
                .then(Commands.literal("min-view-distance")
                        .executes(this::minView)
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::setMinView)
                        )
                )
                .then(Commands.literal("max-simulation-distance")
                        .executes(this::maxSim)
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::setMaxSim)
                        )
                )
                .then(Commands.literal("min-simulation-distance")
                        .executes(this::minSim)
                        .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                .executes(this::setMinSim)
                        )
                )
                .then(Commands.literal("broadcast-changes")
                        .executes(this::broadcastChanges)
                        .then(Commands.literal("none")
                                .executes(this::broadcastChangesNone)
                        )
                        .then(Commands.literal("ops")
                                .executes(this::broadcastChangesOps)
                        )
                        .then(Commands.literal("all")
                                .executes(this::broadcastChangesAll)
                        )
                )
                .then(Commands.literal("broadcast-lock")
                        .executes(this::broadcastLock)
                        .then(Commands.literal("none")
                                .executes(this::broadcastLockNone)
                        )
                        .then(Commands.literal("ops")
                                .executes(this::broadcastLockOps)
                        )
                        .then(Commands.literal("all")
                                .executes(this::broadcastLockAll)
                        )
                )
                .then(Commands.literal("rules")
                        .executes(this::rules)
                        .then(Commands.argument("index", IntegerArgumentType.integer(1, 100))
                                .executes(this::ruleIndex)
                                .then(Commands.literal("remove")
                                        .executes(this::ruleRemove)
                                )
                                .then(Commands.literal("name")
                                        .executes(this::ruleName)
                                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                                .executes(this::ruleSetName)
                                        )
                                        .then(Commands.literal("clear")
                                                .executes(this::ruleClearName)
                                        )
                                )
                                .then(Commands.literal("condition")
                                        .executes(this::ruleCondition)
                                        .then(Commands.literal("type")
                                                .executes(this::ruleType)
                                                .then(Commands.literal("mspt")
                                                        .executes(this::ruleTypeSetMspt)
                                                )
                                                .then(Commands.literal("memory")
                                                        .executes(this::ruleTypeSetMemory)
                                                )
                                                .then(Commands.literal("players")
                                                        .executes(this::ruleTypeSetPlayers)
                                                )
                                        )
                                        .then(Commands.literal("value")
                                                .executes(this::ruleValue)
                                                .then(Commands.argument("value", StringArgumentType.greedyString())
                                                        .executes(this::ruleSetValue)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearValue)
                                                )
                                        )
                                        .then(Commands.literal("min")
                                                .executes(this::ruleMin)
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0))
                                                        .executes(this::ruleSetMin)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearMin)
                                                )
                                        )
                                        .then(Commands.literal("max")
                                                .executes(this::ruleMax)
                                                .then(Commands.argument("max", IntegerArgumentType.integer(0))
                                                        .executes(this::ruleSetMax)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearMax)
                                                )
                                        )
                                )
                                .then(Commands.literal("action")
                                        .executes(this::ruleAction)
                                        .then(Commands.literal("target")
                                                .executes(this::ruleTarget)
                                                .then(Commands.literal("view")
                                                        .executes(this::ruleSetTargetView)
                                                )
                                                .then(Commands.literal("simulation")
                                                        .executes(this::ruleSetTargetSim)
                                                )
                                        )
                                        .then(Commands.literal("update-rate")
                                                .executes(this::ruleUpdateRate)
                                                .then(Commands.argument("ticks", IntegerArgumentType.integer(1, 72000))
                                                        .executes(this::ruleSetUpdateRate)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearUpdateRate)
                                                )
                                        )
                                        .then(Commands.literal("step")
                                                .executes(this::ruleStep)
                                                .then(Commands.argument("step", IntegerArgumentType.integer(-32, 32))
                                                        .executes(this::ruleSetStep)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearStep)
                                                )
                                        )
                                        .then(Commands.literal("step-after")
                                                .executes(this::ruleStepAfter)
                                                .then(Commands.argument("step-after", IntegerArgumentType.integer(1, 100))
                                                        .executes(this::ruleSetStepAfter)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearStepAfter)
                                                )
                                        )
                                        .then(Commands.literal("min-distance")
                                                .executes(this::ruleMinDistance)
                                                .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                                        .executes(this::ruleSetMinDistance)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearMinDistance)
                                                )
                                        )
                                        .then(Commands.literal("max-distance")
                                                .executes(this::ruleMaxDistance)
                                                .then(Commands.argument("chunks", IntegerArgumentType.integer(2, 32))
                                                        .executes(this::ruleSetMaxDistance)
                                                )
                                                .then(Commands.literal("clear")
                                                        .executes(this::ruleClearMaxDistance)
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("add")
                                .then(Commands.literal("mspt")
                                        .then(Commands.literal("min")
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0, 1000))
                                                        .executes(this::addMsptMinView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addMsptMinView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addMsptMinSim)
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("max")
                                                .then(Commands.argument("max", IntegerArgumentType.integer(0, 1000))
                                                        .executes(this::addMsptMaxView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addMsptMaxView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addMsptMaxSim)
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("range")
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0, 1000))
                                                        .then(Commands.argument("max", IntegerArgumentType.integer(0, 1000))
                                                                .executes(this::addMsptRangeView)
                                                                .then(Commands.literal("view")
                                                                        .executes(this::addMsptRangeView)
                                                                )
                                                                .then(Commands.literal("simulation")
                                                                        .executes(this::addMsptRangeSim)
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("memory")
                                        .then(Commands.literal("min")
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0, 100))
                                                        .executes(this::addMemoryMinView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addMemoryMinView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addMemoryMinSim)
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("max")
                                                .then(Commands.argument("max", IntegerArgumentType.integer(0, 100))
                                                        .executes(this::addMemoryMaxView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addMemoryMaxView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addMemoryMaxSim)
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("range")
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0, 100))
                                                        .then(Commands.argument("max", IntegerArgumentType.integer(0, 100))
                                                                .executes(this::addMemoryRangeView)
                                                                .then(Commands.literal("view")
                                                                        .executes(this::addMemoryRangeView)
                                                                )
                                                                .then(Commands.literal("simulation")
                                                                        .executes(this::addMemoryRangeSim)
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("players")
                                        .then(Commands.literal("min")
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0, 1000))
                                                        .executes(this::addPlayersMinView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addPlayersMinView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addPlayersMinSim)
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("max")
                                                .then(Commands.argument("max", IntegerArgumentType.integer(0, 1000))
                                                        .executes(this::addPlayersMaxView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addPlayersMaxView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addPlayersMaxSim)
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("range")
                                                .then(Commands.argument("min", IntegerArgumentType.integer(0, 1000))
                                                        .then(Commands.argument("max", IntegerArgumentType.integer(0, 1000))
                                                                .executes(this::addPlayersRangeView)
                                                                .then(Commands.literal("view")
                                                                        .executes(this::addPlayersRangeView)
                                                                )
                                                                .then(Commands.literal("simulation")
                                                                        .executes(this::addPlayersRangeSim)
                                                                )
                                                        )
                                                )
                                        )
                                        .then(Commands.literal("names")
                                                .then(Commands.argument("names", StringArgumentType.greedyString())
                                                        .executes(this::addPlayersNameView)
                                                        .then(Commands.literal("view")
                                                                .executes(this::addPlayersNameView)
                                                        )
                                                        .then(Commands.literal("simulation")
                                                                .executes(this::addPlayersNameSim)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
