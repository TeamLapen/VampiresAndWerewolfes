package de.teamlapen.werewolves.items;

import de.teamlapen.werewolves.WerewolvesMod;
import de.teamlapen.werewolves.api.items.ISilverFactionSlayerItem;
import de.teamlapen.werewolves.util.REFERENCE;

import net.minecraft.item.ItemSpade;

public class ItemSilverShovel extends ItemSpade implements ISilverFactionSlayerItem {
    public static final String regName = "silver_shovel";

    public ItemSilverShovel() {
        super(WerewolvesMod.TOOL_SILVER);
        this.setCreativeTab(WerewolvesMod.creativeTab);
        this.setRegistryName(REFERENCE.MODID, regName);
        this.setUnlocalizedName(REFERENCE.MODID + "." + regName);
    }


}
