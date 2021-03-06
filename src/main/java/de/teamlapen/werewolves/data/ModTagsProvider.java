package de.teamlapen.werewolves.data;

import de.teamlapen.werewolves.core.ModBlocks;
import de.teamlapen.werewolves.core.ModItems;
import de.teamlapen.werewolves.core.ModTags;
import de.teamlapen.werewolves.util.REFERENCE;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModTagsProvider {

    public static void addProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        BlockTagsProvider blocks = new ModBlockTagsProvider(generator, existingFileHelper);
        generator.addProvider(blocks);
        generator.addProvider(new ModItemTagsProvider(generator, blocks, existingFileHelper));
    }

    private static class ModBlockTagsProvider extends BlockTagsProvider {
        public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
            super(generatorIn, REFERENCE.MODID, existingFileHelper);
        }

        @Override
        protected void registerTags() {
            this.getOrCreateBuilder(ModTags.Blocks.SILVER_ORE).add(ModBlocks.silver_ore);
            this.getOrCreateBuilder(BlockTags.LOGS).add(ModBlocks.jacaranda_log, ModBlocks.magic_log);
            this.getOrCreateBuilder(BlockTags.SAPLINGS).add(ModBlocks.jacaranda_sapling, ModBlocks.magic_sapling);
            this.getOrCreateBuilder(BlockTags.LEAVES).add(ModBlocks.jacaranda_leaves, ModBlocks.magic_leaves);
            this.getOrCreateBuilder(BlockTags.PLANKS).add(ModBlocks.magic_planks);
        }
    }

    private static class ModItemTagsProvider extends ItemTagsProvider {
        public ModItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
            super(generatorIn, blockTagsProvider, REFERENCE.MODID, existingFileHelper);
        }

        @Override
        protected void registerTags() {
            this.copy(ModTags.Blocks.SILVER_ORE, ModTags.Items.SILVER_ORE);
            this.getOrCreateBuilder(ModTags.Items.SILVER_INGOT).add(ModItems.silver_ingot);
            this.getOrCreateBuilder(ModTags.Items.RAWMEATS).add(Items.BEEF, Items.CHICKEN, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, ModItems.liver);
        }
    }
}
