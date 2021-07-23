package me.desht.modularrouters.datagen;

import me.desht.modularrouters.ModularRouters;
import me.desht.modularrouters.ModularRoutersTags;
import me.desht.modularrouters.core.ModItems;
import me.desht.modularrouters.item.augment.ItemAugment;
import me.desht.modularrouters.item.module.ItemModule;
import me.desht.modularrouters.item.smartfilter.ItemSmartFilter;
import me.desht.modularrouters.item.upgrade.ItemUpgrade;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.Arrays;
import java.util.function.Supplier;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, new BlockTagsProvider(generatorIn, ModularRouters.MODID, existingFileHelper), ModularRouters.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (RegistryObject<Item> ro : ModItems.ITEMS.getEntries()) {
            if (ro.get() instanceof ItemModule) {
                addItemsToTag(ModularRoutersTags.Items.MODULES, ro);
            } else if (ro.get() instanceof ItemUpgrade) {
                addItemsToTag(ModularRoutersTags.Items.UPGRADES, ro);
            } else if (ro.get() instanceof ItemAugment) {
                addItemsToTag(ModularRoutersTags.Items.AUGMENTS, ro);
            } else if (ro.get() instanceof ItemSmartFilter) {
                addItemsToTag(ModularRoutersTags.Items.FILTERS, ro);
            }
        }
    }

    @SafeVarargs
    private final void addItemsToTag(Tag.Named<Item> tag, Supplier<? extends ItemLike>... items) {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(ItemLike::asItem).toArray(Item[]::new));
    }

    @Override
    public String getName() {
        return "Modular Routers Item Tags";
    }
}
