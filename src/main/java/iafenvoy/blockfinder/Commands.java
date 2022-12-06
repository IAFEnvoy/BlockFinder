package iafenvoy.blockfinder;

import com.ibm.icu.impl.Pair;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import iafenvoy.blockfinder.finder.BlocksFinder;
import iafenvoy.blockfinder.render.Color;
import iafenvoy.blockfinder.util.RegistryUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class Commands {
    public static void register() {
        ClientCommandManager.DISPATCHER.register(literal(BlockFinder.MOD_ID).
                then(literal("add")
                        .then(argument("block", StringArgumentType.word())
                                .then(argument("red", IntegerArgumentType.integer())
                                        .then(argument("green", IntegerArgumentType.integer())
                                                .then(argument("blue", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            Block block = RegistryUtils.getBlockByName(StringArgumentType.getString(ctx, "block"));
                                                            if (block == Blocks.AIR) {
                                                                ctx.getSource().sendError(Text.of("Block is not valid!"));
                                                                return 1;
                                                            }
                                                            if (BlocksFinder.hadBlock(block)) {
                                                                ctx.getSource().sendError(Text.of("This block already has a finder!"));
                                                                return 1;
                                                            }
                                                            int r = IntegerArgumentType.getInteger(ctx, "red");
                                                            int g = IntegerArgumentType.getInteger(ctx, "green");
                                                            int b = IntegerArgumentType.getInteger(ctx, "blue");
                                                            BlocksFinder.addBlock(block, r, g, b);
                                                            return 0;
                                                        }))))))
                .then(literal("remove")
                        .then(argument("block", StringArgumentType.word())
                                .executes(ctx -> {
                                    Block block = RegistryUtils.getBlockByName(StringArgumentType.getString(ctx, "block"));
                                    if (block == Blocks.AIR) {
                                        ctx.getSource().sendError(Text.of("Block is not valid!"));
                                        return 1;
                                    }
                                    BlocksFinder.removeBlock(block);
                                    return 0;
                                })))
                .then(literal("list")
                        .executes(ctx -> {
                            List<Pair<Block, Color>> blocks = BlocksFinder.getBlockFinders();
                            for (Pair<Block, Color> pair : blocks)
                                ctx.getSource().sendFeedback(Text.of(String.format("%s %s", pair.first.getName().getString(), pair.second.toString())));
                            return 0;
                        }))
                .then(literal("mode")
                        .then(literal("all")
                                .executes(ctx -> {
                                    BlocksFinder.parseOnData = true;
                                    return 0;
                                }))
                        .then(literal("append")
                                .executes(ctx -> {
                                    BlocksFinder.parseOnData = false;
                                    return 0;
                                })))
                .then(literal("range")
                        .then(argument("range", IntegerArgumentType.integer(0, 36))
                                .executes(ctx -> {
                                    BlocksFinder.setRange(IntegerArgumentType.getInteger(ctx, "range"));
                                    return 0;
                                })))
                .then(literal("clear")
                        .executes(ctx -> {
                            BlocksFinder.clear();
                            return 0;
                        })));
    }
}
