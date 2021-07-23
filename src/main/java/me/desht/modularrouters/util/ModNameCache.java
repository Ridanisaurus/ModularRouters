package me.desht.modularrouters.util;

import com.google.common.collect.Maps;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.Map;

/**
 * Maintains a cache of mod id's to "friendly" mod names.
 */
public class ModNameCache {
    private static final Map<String, String> ID_2_NAME = Maps.newHashMap();

    public static void init() {
        ModList.get().forEachModFile(modFile -> {
            for (IModInfo info : modFile.getModInfos()) {
                ID_2_NAME.put(info.getModId(), info.getDisplayName());
                ID_2_NAME.put(info.getModId().toLowerCase(), info.getDisplayName());
            }
        });
        ID_2_NAME.put("minecraft", "Minecraft");
    }

    public static String getModName(ItemStack stack) {
        return getModName(stack.getItem().getRegistryName().getNamespace());
    }

    public static String getModName(String modId) {
        return ID_2_NAME.getOrDefault(modId, modId);
    }
}
