package dev.ftb.mods.ftbmaterials.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Provides a simple way to spawn all items and blocks from this mods resources for debugging
 * textures, functionality, etc.
 */
public class ConstructAllResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructAllResources.class);

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("construct-all-resources")
                .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                .executes(ConstructAllResources::construct);
    }

    private static int construct(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerLevel level = context.getSource().getLevel();

        Vec3 horizView = player.getViewVector(1f);
        horizView.subtract(0, horizView.y, 0);
        BlockPos pos = player.blockPosition().relative(Direction.getNearest(new Vec3i((int) horizView.x, (int) horizView.y, (int) horizView.z), Direction.NORTH), 2);

        int xOffset = 0;
        int yOffset = 0;

        List<Resource> resources = Arrays.stream(Resource.values())
                .sorted(Comparator.comparingInt(r -> r.getResourceTypes().size()))
                .toList();

        for (Resource resource : resources) {
            var registryHolder = ResourceRegistries.get(resource);

            for (var block : registryHolder.getBlocks()) {
                level.setBlock(pos.relative(Direction.UP, yOffset).relative(Direction.NORTH, xOffset), block.get().defaultBlockState(), Block.UPDATE_ALL);
                yOffset++;
            }

            for (var item : registryHolder.getItems()) {
                BlockPos relativeLocation = pos.relative(Direction.UP, yOffset).relative(Direction.NORTH, xOffset);
                level.setBlock(relativeLocation, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);

                ItemFrame itemFrame = new ItemFrame(level, relativeLocation.relative(Direction.EAST), Direction.EAST);
                ItemStack itemStack = new ItemStack(item.get());
                itemStack.set(DataComponents.CUSTOM_NAME, item.get().getName(itemStack));
                itemFrame.setItem(itemStack);
                itemFrame.setCustomNameVisible(true);

                level.addFreshEntity(itemFrame);

                yOffset++;
            }

            xOffset++;
            yOffset = 0;
        }

        return Command.SINGLE_SUCCESS;
    }
}
