package dev.ftb.mods.ftbmaterials.dev.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a simple way to spawn all items and blocks from this mods resources for debugging
 * textures, functionality, etc.
 */
public class ConstructAllResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructAllResources.class);

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("construct-all-resources")
                .requires(e -> e.hasPermission(Commands.LEVEL_OWNERS))
                .executes(ConstructAllResources::construct);
    }

    private static int construct(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerLevel level = context.getSource().getLevel();

        BlockPos pos = player.blockPosition();

        int xOffset = 0;
        int yOffset = 0;

        for (Resource resource : Resource.values()) {
            var resourceHolder = ResourceRegistry.RESOURCE_REGISTRY_HOLDERS.stream()
                    .filter(e -> e.getType().equals(resource))
                    .findFirst();

            if (resourceHolder.isEmpty()) {
                LOGGER.warn("Unable to find {} in registry holders", resource);
                continue;
            }

            ResourceRegistryHolder resourceRegistryHolder = resourceHolder.get();
            for (RegistrySupplier<Block> block : resourceRegistryHolder.getBlocks()) {
                Block actualBlock = block.get();
                level.setBlock(pos.relative(Direction.UP, yOffset).relative(Direction.NORTH, xOffset), actualBlock.defaultBlockState(), Block.UPDATE_ALL);
                yOffset ++;
            }

            for (RegistrySupplier<Item> item : resourceRegistryHolder.getItems()) {
                BlockPos relativeLocation = pos.relative(Direction.UP, yOffset).relative(Direction.NORTH, xOffset);
                level.setBlock(relativeLocation, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);

                ItemFrame itemFrame = new ItemFrame(level, relativeLocation.relative(Direction.EAST), Direction.EAST);
                ItemStack itemStack = new ItemStack(item.get());
                itemStack.setHoverName(item.get().getName(itemStack));
                itemFrame.setItem(itemStack);
                itemFrame.setCustomNameVisible(true);

                level.addFreshEntity(itemFrame);

                yOffset ++;
            }

            xOffset ++;
            yOffset = 0;
        }

        return 0;
    }
}
