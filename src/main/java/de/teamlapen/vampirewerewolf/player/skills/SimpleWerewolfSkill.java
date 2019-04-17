package de.teamlapen.vampirewerewolf.player.skills;

import de.teamlapen.vampirewerewolf.api.VReference;
import de.teamlapen.vampirewerewolf.api.entities.player.werewolf.IWerewolfPlayer;
import de.teamlapen.vampirism.player.skills.VampirismSkill;
import net.minecraft.util.ResourceLocation;

public class SimpleWerewolfSkill extends VampirismSkill<IWerewolfPlayer> {

    private final int u, v;

    /**
     * @param id
     *            Registry name
     * @param desc
     *            Enable description using the default unlocalized key
     */
    public SimpleWerewolfSkill(ResourceLocation id, int u, int v, boolean desc) {
        super(VReference.WEREWOLF_FACTION);
        this.setRegistryName(id);
        this.u = u;
        this.v = v;
        if (desc) setHasDefaultDescription();
    }

    @Override
    public int getMinU() {
        return u;
    }

    @Override
    public int getMinV() {
        return v;
    }

}