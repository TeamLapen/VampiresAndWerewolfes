package de.teamlapen.werewolves.entities.werewolf;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.entity.IDefaultTaskMasterEntity;
import de.teamlapen.vampirism.entity.goals.ForceLookEntityGoal;
import de.teamlapen.vampirism.entity.goals.LookAtClosestVisibleGoal;
import de.teamlapen.vampirism.entity.hunter.HunterBaseEntity;
import de.teamlapen.vampirism.inventory.container.TaskBoardContainer;
import de.teamlapen.werewolves.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class WerewolfTaskMasterEntity extends WerewolfBaseEntity implements IDefaultTaskMasterEntity {

    private static final DataParameter<String> BIOME_TYPE = EntityDataManager.createKey(WerewolfTaskMasterEntity.class, DataSerializers.STRING);
    @Nullable
    private PlayerEntity interactor;

    public WerewolfTaskMasterEntity(EntityType<? extends WerewolfTaskMasterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute getAttributeBuilder() {
        return WerewolfBaseEntity.getAttributeBuilder();
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.interactor != null && (!this.interactor.isAlive() || !(this.interactor.openContainer instanceof TaskBoardContainer))) {
            this.interactor = null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return Helper.isWerewolf(Minecraft.getInstance().player);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, CreatureEntity.class, 10.0F, 1.0D, 1.1D, VampirismAPI.factionRegistry().getPredicate(this.getFaction(), false, true, false, false, null)));
        this.goalSelector.addGoal(2, new ForceLookEntityGoal<>(this));
        this.goalSelector.addGoal(8, new MoveThroughVillageGoal(this, 0.6D, true, 600, () -> false));
        this.goalSelector.addGoal(9, new RandomWalkingGoal(this, 0.7D));
        this.goalSelector.addGoal(10, new LookAtClosestVisibleGoal(this, PlayerEntity.class, 20.0F, 0.6F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, HunterBaseEntity.class, 17.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(BIOME_TYPE, Registry.VILLAGER_TYPE.getDefaultKey().toString());
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld worldIn, @Nonnull DifficultyInstance difficultyIn, @Nonnull SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setBiomeType(VillagerType.func_242371_a(worldIn.func_242406_i(this.getPosition())));
        return data;
    }

    @Nonnull
    @Override
    protected ActionResultType func_230254_b_(@Nonnull PlayerEntity playerEntity, @Nonnull Hand hand) {
        if (!this.world.isRemote) {
            if (Helper.isWerewolf(playerEntity) && this.interactor == null && this.processInteraction(playerEntity, this)) {
                this.getNavigator().clearPath();
                this.interactor = playerEntity;
            }

        }
        return ActionResultType.SUCCESS;
    }

    @Nonnull
    @Override
    public Optional<PlayerEntity> getForceLookTarget() {
        return Optional.ofNullable(this.interactor);
    }

    public VillagerType getBiomeType() {
        String key = this.dataManager.get(BIOME_TYPE);
        ResourceLocation id = new ResourceLocation(key);
        return Registry.VILLAGER_TYPE.getOrDefault(id);
    }

    protected void setBiomeType(VillagerType type) {
        this.dataManager.set(BIOME_TYPE, Registry.VILLAGER_TYPE.getKey(type).toString());
    }
}
