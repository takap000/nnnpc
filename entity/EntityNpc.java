package takap.mods.nnnpc.entity;

import java.util.List;

import takap.mods.nnnpc.mod_NNNPC;
import takap.mods.nnnpc.ai.NpcAIAttackOnCollide;
import takap.mods.nnnpc.ai.NpcAIAvoidPrimedEntity;
import takap.mods.nnnpc.ai.NpcAIFollowOwner;
import takap.mods.nnnpc.ai.NpcAIHealOnCollidePartyMember;
import takap.mods.nnnpc.ai.NpcAINearestAttackableTarget;
import takap.mods.nnnpc.ai.NpcAINearestInjuredMember;
import takap.mods.nnnpc.ai.NpcAISit;
import takap.mods.nnnpc.ai.NpcAITraceRoute;
import takap.mods.nnnpc.ai.NpcAIWander;
import takap.mods.nnnpc.ai.NpcAIWatchOwner;
import takap.mods.nnnpc.gui.EnumGuiPage;
import takap.mods.nnnpc.inventory.InventoryNpc;
import takap.mods.nnnpc.location.NamedLocation;
import takap.mods.nnnpc.packet.PacketNpcInitializeEvent;
import takap.mods.nnnpc.packet.PacketNpcLifeSettingEvent;
import takap.mods.nnnpc.packet.PacketNpcSwingEvent;
import takap.mods.nnnpc.party.PartyManager;
import takap.mods.nnnpc.texture.Texture;
import takap.mods.nnnpc.texture.TextureManager;
import takap.mods.nnnpc.utility.Utility;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
//import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;

public abstract class EntityNpc extends EntityCreature implements IEntityAdditionalSpawnData
{
    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private final int initialHealth = 20;
    private int maxHealth;
    private int lastHealth;
    private final int limitMinHealth = 10;
    private final int limitMaxHealth = 99;
    private final int initialMaxHealth = this.limitMaxHealth;
    private float scaleAmount;
    private boolean isRespawnable;
    private String npcName;
    private final int maxNameLength = 24;
    private boolean isNameVisible;
    private boolean isLifeVisible;
    private String ownerName;
    private int attackStrength;
    private final int minAttackStrength = 1;
    private final int maxAttackStrength = 20;
    private PathNavigate pathNavigator;         // navigatorのかわりに使用, pathSearchRangeを変更するため
    private boolean isSwinging;
    private int swingProgressInt;
    private String npcId;
    private EnumMode mode;
    private FormBase form;
    private RoleBase role;
    private Texture npcTexture;
    private InventoryNpc inventory;
    private int dominantHandIndex;
    private int healOtherThreshold;
    private boolean canGenerateExp;
    // Location関連
    private NamedLocation[] locationList;
    private NPCAction[] actionList;
    
    // 以下，NBTで保存する必要のないもの
    private String guiHolder;
    
    public static final int maxLocationLimit = 12;
    public static final int maxActionLimit = 12;
    public static final int maxRouteLimit = maxActionLimit - 2;
    
    public EntityNpc(World world)
    {
        super(world);
        this.pathNavigator = new PathNavigate(this, this.worldObj, 64f);
        setMaxHealth(this.initialMaxHealth);
        setEntityHealth(this.initialHealth);
        this.lastHealth = getHealth();
        this.texture = null;
        setNpcName("");
        setOwnerName("");
        setAttackStrength(1);
        setNameVisible(false);
        setLifeVisible(false);
        setScaleAmount(1.0f);
        setMoveSpeed(0.25f);
        this.inventory = new InventoryNpc(this);
        setEntityAI();
        clearGuiHolder();
        this.locationList = new NamedLocation[maxLocationLimit];
        for ( int i=0; i<maxLocationLimit; i++ )
        {
            locationList[i] = new NamedLocation("", "", "", "");
        }
        this.actionList = new NPCAction[maxActionLimit];
        for ( int i=0; i<maxActionLimit; i++ )
        {
            actionList[i] = new NPCAction();
        }
        setDominantHandIndex(0);
        setHealOtherThreshold(50);
        setCanGenerateExp(false);
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }
    
    @Override
    public void setDead()
    {
        if ( !this.worldObj.isRemote )
        {
            dropAllItems();
        }
        super.setDead();
    }
    
    @Override
    protected boolean canDespawn()
    {
        return false;
    }
    
    @Override
    public PathNavigate getNavigator()
    {
        return this.pathNavigator;
    }
    
    @Override
    protected void updateAITasks()
    {
        ++this.entityAge;
        this.worldObj.theProfiler.startSection("checkDespawn");
        this.despawnEntity();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("sensing");
        this.getEntitySenses().clearSensingCache();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("navigation");
        this.getNavigator().onUpdateNavigation();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("mob tick");
        this.updateAITick();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("controls");
        this.worldObj.theProfiler.startSection("move");
        this.getMoveHelper().onUpdateMoveHelper();
        this.worldObj.theProfiler.endStartSection("look");
        this.getLookHelper().onUpdateLook();
        this.worldObj.theProfiler.endStartSection("jump");
        this.getJumpHelper().doJump();
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.endSection();
    }
    
    public void setEntityAI()
    {
        getNavigator().setAvoidsWater(true);
        getNavigator().setBreakDoors(true);
        
        int priority = 1;
        this.tasks.addTask(priority++, new EntityAISwimming(this));
        this.tasks.addTask(priority++, new NpcAISit(this));
        this.tasks.addTask(priority++, new NpcAIAvoidPrimedEntity(this, 25f, 4));
        this.tasks.addTask(priority++, new NpcAIHealOnCollidePartyMember(this));
        this.tasks.addTask(priority++, new NpcAIAttackOnCollide(this, false));
        this.tasks.addTask(priority++, new EntityAIMoveIndoors(this));
        this.tasks.addTask(priority++, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(priority++, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(priority++, new NpcAITraceRoute(this));
        this.tasks.addTask(priority++, new NpcAIFollowOwner(this, 5f, 3.0f));
        this.tasks.addTask(priority++, new NpcAIWander(this));
        this.tasks.addTask(priority++, new NpcAIWatchOwner(this, 16f));
        this.tasks.addTask(priority++, new EntityAIWatchClosest(this, net.minecraft.entity.EntityLiving.class, 8f));
        this.tasks.addTask(priority++, new EntityAILookIdle(this));
        priority = 1;
        this.targetTasks.addTask(priority++, new NpcAINearestInjuredMember(this, 12f, true));
        this.targetTasks.addTask(priority++, new NpcAINearestAttackableTarget(this, 12f, true));
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setString("npcId", getNpcId());
        nbtTagCompound.setString("form", getForm().getName());
        nbtTagCompound.setString("role", getRole().getName());
        nbtTagCompound.setString("mode", getMode().toString());
        nbtTagCompound.setString("npcTexture", getNpcTexture().getRelativePath());
        nbtTagCompound.setDouble("spawnX", getSpawnX());
        nbtTagCompound.setDouble("spawnY", getSpawnY());
        nbtTagCompound.setDouble("spawnZ", getSpawnZ());
        nbtTagCompound.setString("npcName", getNpcName());
        nbtTagCompound.setBoolean("isNameVisible", isNameVisible());
        nbtTagCompound.setInteger("maxHealth", getMaxHealth());
        nbtTagCompound.setBoolean("isLifeVisible", isLifeVisible());
        nbtTagCompound.setBoolean("isRespawnable", isRespawnable());
        nbtTagCompound.setInteger("attackStrength", getAttackStrength());
        nbtTagCompound.setFloat("scaleAmount", getScaleAmount());
        nbtTagCompound.setFloat("moveSpeed", getMoveSpeed());
        nbtTagCompound.setString("ownerName", getOwnerName());
        nbtTagCompound.setTag("inventory", getInventory().writeToNBT(new NBTTagList()));
        nbtTagCompound.setTag("locationList", getLocationNBTTagList());
        nbtTagCompound.setTag("actionList", this.getNPCActionNBTTagList());
        nbtTagCompound.setInteger("dominantHandIndex", this.dominantHandIndex);
        nbtTagCompound.setBoolean("canGenerateExp", canGenerateExp());
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);
        setNpcId(nbtTagCompound.getString("npcId"));
        setFormByName(nbtTagCompound.getString("form"));
        setRoleByName(nbtTagCompound.getString("role"));
        setModeByName(nbtTagCompound.getString("mode"));
        setNpcTextureByName(nbtTagCompound.getString("npcTexture"));
        updateSpawnCoordinate(nbtTagCompound.getDouble("spawnX"), nbtTagCompound.getDouble("spawnY"), nbtTagCompound.getDouble("spawnZ"));
        setNpcName(nbtTagCompound.getString("npcName"));
        setNameVisible(nbtTagCompound.getBoolean("isNameVisible"));
        setMaxHealth(nbtTagCompound.getInteger("maxHealth"));
        setLifeVisible(nbtTagCompound.getBoolean("isLifeVisible"));
        setRespawnable(nbtTagCompound.getBoolean("isRespawnable"));
        setAttackStrength(nbtTagCompound.getInteger("attackStrength"));
        setScaleAmount(nbtTagCompound.getFloat("scaleAmount"));
        setMoveSpeed(nbtTagCompound.getFloat("moveSpeed"));
        setOwnerName(nbtTagCompound.getString("ownerName"));
        setInventory(nbtTagCompound.getTagList("inventory"));
        setLocationList(nbtTagCompound.getTagList("locationList"));
        setNPCActionList(nbtTagCompound.getTagList("actionList"));
        setDominantHandIndex(nbtTagCompound.getInteger("dominantHandIndex"));
        setCanGenerateExp(nbtTagCompound.getBoolean("canGenerateExp"));
    }
    
    public void writeCoreParameterToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setString("npcId", getNpcId());
        nbtTagCompound.setDouble("spawnX", getSpawnX());
        nbtTagCompound.setDouble("spawnY", getSpawnY());
        nbtTagCompound.setDouble("spawnZ", getSpawnZ());
        nbtTagCompound.setString("npcName", getNpcName());
        nbtTagCompound.setBoolean("isNameVisible", isNameVisible());
        nbtTagCompound.setInteger("maxHealth", getMaxHealth());
        nbtTagCompound.setBoolean("isLifeVisible", isLifeVisible());
        nbtTagCompound.setBoolean("isRespawnable", isRespawnable());
        nbtTagCompound.setInteger("attackStrength", getAttackStrength());
        nbtTagCompound.setFloat("scaleAmount", getScaleAmount());
        nbtTagCompound.setFloat("moveSpeed", getMoveSpeed());
        nbtTagCompound.setString("ownerName", getOwnerName());
        nbtTagCompound.setTag("inventory", getInventory().writeToNBT(new NBTTagList()));
        nbtTagCompound.setTag("locationList", getLocationNBTTagList());
        nbtTagCompound.setTag("actionList", this.getNPCActionNBTTagList());
        nbtTagCompound.setInteger("dominantHandIndex", this.dominantHandIndex);
        nbtTagCompound.setBoolean("canGenerateExp", canGenerateExp());
    }

    public void readCoreParameterFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readEntityFromNBT(nbtTagCompound);
        setNpcId(nbtTagCompound.getString("npcId"));
        updateSpawnCoordinate(nbtTagCompound.getDouble("spawnX"), nbtTagCompound.getDouble("spawnY"), nbtTagCompound.getDouble("spawnZ"));
        setNpcName(nbtTagCompound.getString("npcName"));
        setNameVisible(nbtTagCompound.getBoolean("isNameVisible"));
        setMaxHealth(nbtTagCompound.getInteger("maxHealth"));
        setLifeVisible(nbtTagCompound.getBoolean("isLifeVisible"));
        setRespawnable(nbtTagCompound.getBoolean("isRespawnable"));
        setAttackStrength(nbtTagCompound.getInteger("attackStrength"));
        setScaleAmount(nbtTagCompound.getFloat("scaleAmount"));
        setMoveSpeed(nbtTagCompound.getFloat("moveSpeed"));
        setOwnerName(nbtTagCompound.getString("ownerName"));
        setInventory(nbtTagCompound.getTagList("inventory"));
        setLocationList(nbtTagCompound.getTagList("locationList"));
        setNPCActionList(nbtTagCompound.getTagList("actionList"));
        setDominantHandIndex(nbtTagCompound.getInteger("dominantHandIndex"));
        setCanGenerateExp(nbtTagCompound.getBoolean("canGenerateExp"));
    }
    
    public void copyCoreParameterFrom(EntityNpc targetNpc)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        targetNpc.writeCoreParameterToNBT(nbtTagCompound);
        this.readCoreParameterFromNBT(nbtTagCompound);
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer)
    {
    	// NPCの状態確認, デバッグ用
    	if ( mod_NNNPC.debugLv > 0 )
    	{
	        showNpcInformation();
    	}
        
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        
        // 体力回復, ItemFood
        if ( (itemStack != null) && (Item.itemsList[itemStack.itemID] instanceof ItemFood) )
        {
            if ( getHealth() != getMaxHealth() )
            {
                ItemFood itemFood = (ItemFood)Item.itemsList[itemStack.itemID];
                heal(itemFood.getHealAmount());
                if ( getHealth() >= getMaxHealth() )
                {
                    setEntityHealth(getMaxHealth());
                    showHearts(5);
                    // うざいかも・・・
//                    this.worldObj.playSoundAtEntity(this, "random.levelup", 0.5f, 1.0f);
                }
                else
                {
                    showNote((double)getHealth()/getMaxHealth(), 0.0d, 0.0d);
                    // 鬱陶しいかも・・・
//                    this.worldObj.playSoundAtEntity(this, "random.eat", this.rand.nextFloat() * 0.8f, 1.0f);
                }
                itemStack.stackSize--;
                if ( itemStack.stackSize <= 0 )
                {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, (ItemStack)null);
                }
                return true;
            }
        }
        // モード変更, 松明
        else if ( (itemStack != null) && itemStack.itemID == Block.torchWood.blockID )
        {
            // Shift押しの場合は前のモードへ
            if ( entityPlayer.isSneaking() )
            {
                // サーバ側，クライアント側の両方で処理
                changePreviousModeWithNotify(entityPlayer);
            }
            // Shiftなしの場合は次のモードへ
            else
            {
                // サーバ側，クライアント側の両方で処理
                changeNextModeWithNotify(entityPlayer);
            }
            return true;
        }
        // NPC操作
        else if ( (itemStack != null) && ((itemStack.itemID == mod_NNNPC.niceStick.itemID) || (itemStack.itemID == mod_NNNPC.soulStone.itemID)) )
        {
            setGuiHolder(entityPlayer.username);
            entityPlayer.openGui(mod_NNNPC.instance, EnumGuiPage.SETTING.ordinal(), worldObj, (int)posX, (int)posY, (int)posZ);
            return true;
        }
        // 無効なアイテム or 素手の場合はインベントリ
        setGuiHolder(entityPlayer.username);
        entityPlayer.openGui(mod_NNNPC.instance, EnumGuiPage.INVENTORY.ordinal(), worldObj, (int)posX, (int)posY, (int)posZ);
        return true;
    }
    
    @Override
    public void onLivingUpdate()
    {
    	// 体力変更有無チェック, サーバ側のみ実施
        if ( !this.worldObj.isRemote )
        {
            if ( getHealth() != this.lastHealth )
            {
                PacketNpcLifeSettingEvent packet = new PacketNpcLifeSettingEvent(getMaxHealth(), isLifeVisible());
                packet.sendPacketToClient(this.worldObj, this, false);
            }
            this.lastHealth = getHealth();
        }
    	
        updateSwingState();
        super.onLivingUpdate();
        pickupItem();
    }
    
    @Override
    public void onDeathUpdate()
    {
        if ( isRespawnable() )
        {
            this.deathTime++;
            if ( this.deathTime >= 20 )
            {
                showSmoke(5);
                setEntityHealth(getInitialHealth());
                this.deathTime = 0;
                setMode(getRole().getDefaultMode());
                moveToRespawnPoint();
                // 初期化
                if ( !this.worldObj.isRemote )
                {
                    PacketNpcInitializeEvent packet = new PacketNpcInitializeEvent();
                    packet.sendPacketToClient(this.worldObj, this, false);
                }
            }
        }
        else
        {
            super.onDeathUpdate();
        }
    }
    
    public void moveToRespawnPoint()
    {
        setLocationAndAngles(getSpawnX(), getSpawnY(), getSpawnZ(), this.rotationYaw, this.rotationPitch);
    }
    
    private int getSwingSpeedModifier()
    {
        if (isPotionActive(Potion.digSpeed))
        {
            return 6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1;
        }

        if (isPotionActive(Potion.digSlowdown))
        {
            return 6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
        }
        else
        {
            return 6;
        }
    }

    public void updateSwingState()
    {
        int i = getSwingSpeedModifier();
        
        if ( this.isSwinging )
        {
            this.swingProgressInt++;

            if ( this.swingProgressInt >= i )
            {
                this.swingProgressInt = 0;
                this.isSwinging = false;
            }
        }
        else
        {
            this.swingProgressInt = 0;
        }
        
        this.swingProgress = (float)this.swingProgressInt / (float)i;
    }
    
    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int i = this.getAttackStrength();

        if (isPotionActive(Potion.damageBoost))
        {
            i += 3 << getActivePotionEffect(Potion.damageBoost).getAmplifier();
        }

        if (isPotionActive(Potion.weakness))
        {
            i -= 2 << getActivePotionEffect(Potion.weakness).getAmplifier();
        }
        
        // TODO: 経験値ドロップは設定でon/off切り替え？
        // "Exp on"の場合はExpオーブを出すため，オーナーからの0ポイントダメージ付加
        if ( canGenerateExp() )
        {
            par1Entity.attackEntityFrom(DamageSource.causePlayerDamage(getOwner()), 0);
        }
        // ターゲットにダメージ付与 (mobとして)
        boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        if ( flag )
        {
            // TODO: モーションの実装，ノックバックの検討
            if ( this.rand.nextInt(100) == 0 )
            {
                par1Entity.motionY += 0.4d;
            }
        }
        
        return flag;
    }

    @Override
    protected void attackEntity(Entity par1Entity, float par2)
    {
        if ( this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY )
        {
            this.attackTime = 20;
            attackEntityAsMob(par1Entity);
        }
    }
    
    @Override
    public void swingItem()
    {
        if ( !this.worldObj.isRemote )
        {
            PacketNpcSwingEvent packet = new PacketNpcSwingEvent();
            packet.sendPacketToClient(this.worldObj, this, false);
            return;
        }
        
        if ( !this.isSwinging || (this.swingProgressInt >= getSwingSpeedModifier() / 2) || (this.swingProgressInt < 0) )
        {
            this.swingProgressInt = -1;
            this.isSwinging = true;
        }
    }
    
    @Override
    public int getMaxHealth()
    {
        return this.maxHealth;
    }

    public double getSpawnX()
    {
        return spawnX;
    }

    public void setSpawnX(double spawnX)
    {
        this.spawnX = spawnX;
    }

    public double getSpawnY()
    {
        return spawnY;
    }

    public void setSpawnY(double spawnY)
    {
        this.spawnY = spawnY;
    }

    public double getSpawnZ()
    {
        return spawnZ;
    }

    public void setSpawnZ(double spawnZ)
    {
        this.spawnZ = spawnZ;
    }

    public int getInitialHealth()
    {
        return initialHealth;
    }

    public int getInitialMaxHealth()
    {
        return initialMaxHealth;
    }

    public void setMaxHealth(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    public float getScaleAmount()
    {
        return scaleAmount;
    }

    public void setScaleAmount(float scaleAmount)
    {
        this.scaleAmount = scaleAmount;
        updateNpcSize();
    }
    
    public void updateNpcSize()
    {
        FormBase form = getForm();
        if ( form != null )
        {
            // サイズ更新，form.getScaleWeight()はbaseHeightに適用済みなので不要
            float weight = this.scaleAmount;
            setSize(weight*form.getBaseWidth(), weight*form.getBaseHeight() );
        }
    }

    public boolean isRespawnable()
    {
        return isRespawnable;
    }

    public void setRespawnable(boolean isRespawnable)
    {
        this.isRespawnable = isRespawnable;
    }

    public String getNpcName()
    {
        return npcName;
    }

    public void setNpcName(String npcName)
    {
        // 最大長までに制限
        this.npcName = npcName.length()>getMaxNameLength() ? npcName.substring(0, getMaxNameLength()) : npcName;
    }

    public boolean isNameVisible()
    {
        return isNameVisible;
    }

    public void setNameVisible(boolean isNameVisible)
    {
        this.isNameVisible = isNameVisible;
    }

    public boolean isLifeVisible()
    {
        return isLifeVisible;
    }

    public void setLifeVisible(boolean isLifeVisible)
    {
        this.isLifeVisible = isLifeVisible;
    }

    public String getOwnerName()
    {
        return ownerName;
    }
    
    public EntityPlayer getOwner()
    {
        return this.worldObj.getPlayerEntityByName(getOwnerName());
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public int getAttackStrength()
    {
        return attackStrength;
    }

    public void setAttackStrength(int attackStrength)
    {
        this.attackStrength = attackStrength;
    }

    public boolean isSwinging()
    {
        return isSwinging;
    }

    public void setSwinging(boolean isSwinging)
    {
        this.isSwinging = isSwinging;
    }

    public int getSwingProgressInt()
    {
        return swingProgressInt;
    }

    public void setSwingProgressInt(int swingProgressInt)
    {
        this.swingProgressInt = swingProgressInt;
    }
    
    public String getNpcId()
    {
        return npcId;
    }

    public void generateNpcId()
    {
        this.npcId = new StringBuilder(Long.toString(System.currentTimeMillis())).append("_").append(Integer.toString(entityId)).toString();
    }

    public void setNpcId(String npcId)
    {
        this.npcId = npcId;
    }
    
    @Override
    public void writeSpawnData(ByteArrayDataOutput data)
    {
        // Clientに空のデータを送信
    }

    @Override
    public void readSpawnData(ByteArrayDataInput data)
    {
        // Serverに初期化用packet(Request)を返信
        PacketNpcInitializeEvent packet = new PacketNpcInitializeEvent();
        packet.sendPacketToServer(this.worldObj, this, false);
    }

    public EnumMode getMode()
    {
        return mode;
    }
    
    public boolean isMode(EnumMode mode)
    {
        return getMode().equals(mode);
    }
    
    public boolean setMode(EnumMode mode)
    {
        if ( mode == null )
        {
            return false;
        }
        this.mode = mode;
        return true;
    }
    
    public boolean setModeByName(String name)
    {
        return setMode(EnumMode.valueOf(name));
    }
    
    public void changePreviousMode()
    {
        setMode(getRole().getPreviousMode(getMode()));
    }
    
    public void changeNextMode()
    {
        setMode(getRole().getNextMode(getMode()));
    }
    
    public void changePreviousModeWithNotify(EntityPlayer player)
    {
        String modeBefore = getMode().getModeName();
        changePreviousMode();
        if ( this.worldObj.isRemote )
        {
            addChatMessage(player, "Mode: " + modeBefore + " -> " + getMode().getModeName());
        }
    }
    
    public void changeNextModeWithNotify(EntityPlayer player)
    {
        String modeBefore = getMode().getModeName();
        changeNextMode();
        if ( this.worldObj.isRemote )
        {
            addChatMessage(player, "Mode: " + modeBefore + " -> " + getMode().getModeName());
        }
    }
    
    public RoleBase getRole()
    {
        return role;
    }

    public boolean setRole(RoleBase role)
    {
        if ( role == null )
        {
            return false;
        }
        
        this.role = role;
        if ( setMode(role.getDefaultMode()) )
        {
            if ( role.canFollow() )
            {
                PartyManager.getInstance().registerNpc(this);
            }
            else
            {
                PartyManager.getInstance().deleteNpc(this);
            }
            return true;
        }
        return false;
    }
    
    public boolean setRoleByName(String name)
    {
        RoleBase role = getForm().getRoleByName(name);
        return setRole(role);
    }
    
    public FormBase getForm()
    {
        return form;
    }
    
    public boolean setForm(FormBase form)
    {
        this.form = form;
        if ( form != null )
        {
            RoleBase role = form.getDefaultRole();
            if ( setRole(role) && setNpcTexture(form.getDefaultTexture()) )
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean setFormByName(String name)
    {
        return setForm(FormManager.getInstance().getForm(name));
    }
    
    public Texture getNpcTexture()
    {
        return npcTexture;
    }
    
    public boolean setNpcTexture(Texture texture)
    {
        this.npcTexture = texture;
        this.texture = texture.getRelativePath();
        return this.npcTexture != null;
    }
    
    public boolean setNpcTextureByName(String name)
    {
        return setNpcTexture(TextureManager.getInstance().getTexture(name));
    }

    public void updateSpawnCoordinate(double x, double y, double z)
    {
        setSpawnX(x);
        setSpawnY(y);
        setSpawnZ(z);
    }
    
    public int getEntityId()
    {
        return entityId;
    }
    
    public float getMoveSpeed()
    {
        return moveSpeed;
    }
    
    public void setMoveSpeed(float moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }
    
    public InventoryNpc getInventory()
    {
        return this.inventory;
    }
    
    private void setInventory(NBTTagList tagList)
    {
        if ( this.inventory == null )
        {
            this.inventory = new InventoryNpc(this);
        }
        this.inventory.readFromNBT(tagList);
    }
    
    public void setInventory(ItemStack[] itemStacks)
    {
        if ( this.inventory == null )
        {
            this.inventory = new InventoryNpc(this);
        }
        int size = itemStacks.length;
        for ( int i=0; i<size; i++ )
        {
            this.inventory.setInventorySlotContents(i, itemStacks[i]);
        }
    }
    
    public void setInventory(InventoryNpc inventory)
    {
        this.inventory = inventory;
    }
    
    public void addChatMessage(EntityPlayer player, String s)
    {
        if ( (player!=null) && this.worldObj.isRemote )
        {
            player.addChatMessage(s);
        }
    }
    
    public void showHearts(int num)
    {
        for ( int i=0; i<num; i++ )
        {
            showParticle("heart", 0.0D, 0.0D, 0.0D);
        }
    }

    public void showSmoke(int num)
    {
        for ( int i=0; i<num; i++ )
        {
            showParticle("smoke", this.rand.nextGaussian() * 0.02D, this.rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D);
        }
    }

    public void showNote(double d1, double d2, double d3)
    {
        showParticle("note", d1, d2, d3);
    }

    private void showParticle(String s, double d, double d1, double d2)
    {
        if ( !this.worldObj.isRemote )
        {
            this.worldObj.spawnParticle(s, (this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F)) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), (this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F)) - (double)this.width, d, d1, d2);
        }
    }
    
    public int getLimitMaxHealth()
    {
        return limitMaxHealth;
    }

    public int getLimitMinHealth()
    {
        return limitMinHealth;
    }
    
    public int getMinAttackStrength()
    {
        return minAttackStrength;
    }
    
    public int getMaxAttackStrength()
    {
        return maxAttackStrength;
    }
    
    public void increaseMaxHealth(int increment)
    {
        int maxHealth = getMaxHealth() + increment;
        if ( maxHealth < getLimitMinHealth() )
        {
            maxHealth = getLimitMinHealth();
        }
        else if ( maxHealth > getLimitMaxHealth() )
        {
            maxHealth = getLimitMaxHealth();
        }
        setMaxHealth(maxHealth);
    }
    
    public void increaseMoveSpeed(float increment)
    {
        float moveSpeed = getMoveSpeed() + increment;
        if ( moveSpeed < getForm().getMinMoveSpeed() )
        {
            moveSpeed = getForm().getMinMoveSpeed();
        }
        else if ( moveSpeed > getForm().getMaxMoveSpeed() )
        {
            moveSpeed = getForm().getMaxMoveSpeed();
        }
        moveSpeed = (float)((int)(moveSpeed*100 + 0.5f)) / 100;
        setMoveSpeed(moveSpeed);
    }
    
    public void increaseAttackStrength(int increment)
    {
        int attackStrength = getAttackStrength() + increment;
        if ( attackStrength < getMinAttackStrength() )
        {
            attackStrength = getMinAttackStrength();
        }
        else if ( attackStrength > getMaxAttackStrength() )
        {
            attackStrength = getMaxAttackStrength();
        }
        setAttackStrength(attackStrength);
    }
    
    public void increaseScaleAmount(float increment)
    {
        float scaleAmount = getScaleAmount() + increment;
        if ( scaleAmount < getForm().getMinScaleAmount() )
        {
            scaleAmount = getForm().getMinScaleAmount();
        }
        else if ( scaleAmount > getForm().getMaxScaleAmount() )
        {
            scaleAmount = getForm().getMaxScaleAmount();
        }
        scaleAmount = (float)((int)(scaleAmount*100 + 0.5f)) / 100;
        setScaleAmount(scaleAmount);
    }
    
    public void switchNameVisibility()
    {
        setNameVisible(!isNameVisible());
    }
    
    public void switchLifeVisibility()
    {
        setLifeVisible(!isLifeVisible());
    }
    
    public void switchRespawnable()
    {
        setRespawnable(!isRespawnable());
    }
    
    public void deleteByPlayer(boolean canSpawnEffect, boolean canDropItems)
    {
        if ( canSpawnEffect )
        {
            showSmoke(5);
        }
        // アイテムドロップなしで削除したい場合(form変更時など)
        if ( !canDropItems )
        {
            // TODO: 手抜きはやめよう！
            this.inventory = new InventoryNpc(this);
        }
        this.setDead();
    }
    
    public EntityLiving getEntityToHeal()
    {
        // ownerを優先的に負傷判定
        EntityPlayer owner = this.getOwner();
        if ( owner == null )
        {
            // ownerがnullならパーティー編成されていないので終了
            return null;
        }
        if ( isInjured(owner.getHealth(), owner.getMaxHealth()) )
        {
            return (EntityLiving)owner;
        }
        
        // パーティーメンバーの負傷判定
        EntityLiving targetEntity = null;
        List partyList = PartyManager.getInstance().getPartyMemberList();
        int minHealth = 1000;
        for ( int i=0; i<partyList.size(); i++ )
        {
            EntityNpc npc = (EntityNpc)partyList.get(i);
            if ( (npc == null) || (npc.equals(this)) )    // 自分自身は除外
            {
                continue;
            }
            // 負傷判定
            if ( isInjured(npc.getHealth(), npc.getMaxHealth()) )
            {
                // 最小値判定
                if ( npc.getHealth() < minHealth )
                {
                    minHealth = npc.getHealth();
                    List loadedEntityList = this.worldObj.getLoadedEntityList();
                    for ( int j=0; j<loadedEntityList.size(); j++ )
                    {
                        if ( loadedEntityList.get(j) instanceof EntityNpc )
                        {
                            EntityNpc serverNpc = (EntityNpc)loadedEntityList.get(j);
                            if ( serverNpc.getNpcId().equals(npc.getNpcId()) )
                            {
                                targetEntity = (EntityLiving)serverNpc;
                            }
                        }
                    }
                }
            }
        }
        
        return targetEntity;
    }
    
    public boolean isInjured(int health, int maxHealth)
    {
        if ( (health*100 / maxHealth) < getHealOtherThreshold() )
        {
            return true;
        }
        return false;
    }
    
    public boolean healEntity(EntityLiving entity)
    {
        InventoryNpc inventory = this.getInventory();
        for ( int i=0; i<inventory.getSizeInventory(); i++ )
        {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if ( itemStack == null )
            {
                continue;
            }
            if ( itemStack.getItem() instanceof ItemFood )
            {
                ItemFood itemFood = (ItemFood)itemStack.getItem();
                int healAmount = MathHelper.floor_double((itemFood.getHealAmount()*0.75d));
                entity.heal(healAmount>0?healAmount:1);
                inventory.consumeInventoryItem(itemFood.itemID);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Entity getEntityToAttack()
    {
        return getClosestAttackableMob(16d);
    }

    protected Entity getClosestAttackableMob(double distance)
    {
        Entity targetEntity = null;
        List entityList = this.worldObj.loadedEntityList;
        double minDistance = distance;
        
        for ( int i = 0; i < entityList.size(); i++ )
        {
            Entity entity = (Entity)entityList.get(i);
            if ( canEntityBeSeen(entity) && (entity.getDistanceToEntity(this) < minDistance) )
            {
                if ( entity instanceof EntityMob )
                {
                    if ( !(entity instanceof EntityCreeper) && !(entity instanceof EntityPigZombie) )
                    {
                        targetEntity = entity;
                        minDistance = entity.getDistanceToEntity(this);
                    }
                }
            }
        }
        return targetEntity;
    }
    
    public void showNpcInformation()
    {
        if ( this.worldObj.isRemote )
        {
        	// TODO: 項目足りないー
            Utility.printInformation("Npc Information");
            Utility.printInformation("----------------------------------------");
            Utility.printInformation("npcId          : " + getNpcId());
            Utility.printInformation("form           : " + getForm().getName());
            Utility.printInformation("role           : " + getRole().getName());
            Utility.printInformation("mode           : " + getMode().getModeName());
            Utility.printInformation("npcTexture     : " + getNpcTexture().getRelativePath());
            Utility.printInformation("spawn          : " + getSpawnX() + "," + getSpawnY() + "," + getSpawnZ());
            Utility.printInformation("npcName        : " + getNpcName());
            Utility.printInformation("isNameVisible  : " + String.valueOf(isNameVisible()));
            Utility.printInformation("maxHealth      : " + getMaxHealth());
            Utility.printInformation("isLifeVisible  : " + String.valueOf(isLifeVisible()));
            Utility.printInformation("attackStrength : " + getAttackStrength());
            Utility.printInformation("scaleAmount    : " + getScaleAmount());
            Utility.printInformation("ownerName      : " + getOwnerName());
            Utility.printInformation("----------------------------------------");
        }
    }

    public String getGuiHolder()
    {
        return guiHolder;
    }

    public void setGuiHolder(String guiHolder)
    {
        this.guiHolder = guiHolder;
    }
    
    public void clearGuiHolder()
    {
        setGuiHolder(null);
    }

    public int getMaxNameLength()
    {
        return maxNameLength;
    }

    public NamedLocation[] getLocationList()
    {
        return locationList;
    }
    
    public NamedLocation getLocation(int index)
    {
        return this.locationList[index];
    }
    
    public void setLocation(int index, NamedLocation location)
    {
        this.locationList[index] = location;
    }
    
    public void setLocationList(NBTTagList tagList)
    {
        if ( this.locationList == null )
        {
            this.locationList = new NamedLocation[maxLocationLimit];
        }
        
        for ( int i=0; i<tagList.tagCount(); i++ )
        {
            NBTTagCompound nbtTagCompound = (NBTTagCompound)tagList.tagAt(i);
            int locationIndex = nbtTagCompound.getByte("location") & 0xff;
            NamedLocation location = NamedLocation.loadLocationFromNBT(nbtTagCompound);

            if ( location == null )
            {
                continue;
            }
            setLocation(locationIndex, location);
        }
    }
    
    private NBTTagList getLocationNBTTagList()
    {
        NBTTagList nbtTagList = new NBTTagList();
        for ( int i=0; i<maxLocationLimit; i++ )
        {
            if ( getLocation(i) != null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("location", (byte)i);
                getLocation(i).writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        return nbtTagList;
    }
    
    public NPCAction[] getNPCActionList()
    {
        return this.actionList;
    }
    
    public NPCAction getNPCAction(int index)
    {
        return this.actionList[index];
    }
    
    public void setNPCAction(int index, NPCAction action)
    {
        this.actionList[index] = action;
    }
    
    public void setNPCActionList(NBTTagList tagList)
    {
        if ( this.actionList == null )
        {
            this.actionList = new NPCAction[maxActionLimit];
        }
        
        for ( int i=0; i<tagList.tagCount(); i++ )
        {
            NBTTagCompound nbtTagCompound = (NBTTagCompound)tagList.tagAt(i);
            int actionIndex = nbtTagCompound.getByte("action") & 0xff;
            NPCAction action = NPCAction.loadNPCActionFromNBT(nbtTagCompound);

            if ( action == null )
            {
                continue;
            }
            setNPCAction(actionIndex, action);
        }
    }
    
    private NBTTagList getNPCActionNBTTagList()
    {
        NBTTagList nbtTagList = new NBTTagList();
        for ( int i=0; i<maxActionLimit; i++ )
        {
            if ( getNPCAction(i) != null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("action", (byte)i);
                getNPCAction(i).writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        return nbtTagList;
    }
    
    private void pickupItem()
    {
        // サーバ側のみで実施
        if ( this.worldObj.isRemote )
        {
            return;
        }
        
        if (getHealth() > 0)
        {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0d, 0.0d, 1.0d));
            if (list != null)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    Entity entity = (Entity)list.get(i);

                    if (!entity.isDead && (entity instanceof EntityItem))
                    {
                        EntityItem entityItem = (EntityItem)entity;
                        // func_92014 return ItemStack
//                        if (entityItem.delayBeforeCanPickup == 0 && this.inventory.addItemStackToInventory(entityItem.func_92014_d()))
                        if (entityItem.delayBeforeCanPickup == 0 && this.inventory.addItemStackToInventory(entityItem.getEntityItem()))
                        {
                            this.worldObj.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
//                            if (entityItem.func_92014_d().stackSize <= 0)
                            if ( entityItem.getEntityItem().stackSize <= 0)
                            {
                                entityItem.setDead();
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setDominantHandIndex(int index)
    {
        this.dominantHandIndex = index;
    }
    
    public int getDominantHandIndex()
    {
        return this.dominantHandIndex;
    }
    
    public void setPreviousDominantHandIndex()
    {
        setDominantHandIndex(getForm().getPreviousDominantHandIndex(getDominantHandIndex()));
    }
    
    public void setNextDominantHandIndex()
    {
        setDominantHandIndex(getForm().getNextDominantHandIndex(getDominantHandIndex()));
    }
    
    public String getDominantHand()
    {
        return getForm().getDominantHand(getDominantHandIndex());
    }
    
    public int getLifeColor()
    {
        // 満タン!
        if ( getHealth() == getMaxHealth() )
        {
            return Utility.colorBlue;
        }
        // ピンチ!!
        else if ( getHealth() < getMaxHealth() * 0.2F )
        {
            return Utility.colorRed;
        }
        // 半分未満
        else if ( getHealth() < getMaxHealth() * 0.5F )
        {
            return Utility.colorYellow;
        }
        // 半分以上
        else
        {
            return Utility.colorGreen;
        }
    }

    public float getLifeColorR()
    {
        return (float)((getLifeColor()>>16) & 0xff) / 256F;
    }

    public float getLifeColorG()
    {
        return (float)((getLifeColor()>>8) & 0xff) / 256F;
    }

    public float getLifeColorB()
    {
        return (float)(getLifeColor() & 0x0000ff) / 256F;
    }
    
    public void teleportToOwner()
    {
        EntityPlayer owner = this.worldObj.getPlayerEntityByName(getOwnerName());

        if ( this.getDistanceSqToEntity(owner) < 36D )
        {
            return;
        }

        int i = MathHelper.floor_double(owner.posX) - 2;
        int j = MathHelper.floor_double(owner.posZ) - 2;
        int k = MathHelper.floor_double(owner.boundingBox.minY);

        for ( int l = 0; l <= 4; l++ )
        {
            for ( int i1 = 0; i1 <= 4; i1++ )
            {
                if ( (l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !this.worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1) )
                {
                    this.setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, this.rotationYaw, this.rotationPitch);
                    this.getNavigator().clearPathEntity();
                    return;
                }
            }
        }
    }
    
    public ItemStack getHeldItemOnLeftHand()
    {
        return this.inventory.weaponInventory[0];
    }

    public ItemStack getHeldItemOnRightHand()
    {
        return this.inventory.weaponInventory[1];
    }
    
    public void dropAllItems()
    {
        if ( this.worldObj.isRemote )
        {
            return;
        }

        if ( this.inventory == null )
        {
            return;
        }

        for ( int i=0; i<this.inventory.getSizeInventory(); i++ )
        {
            ItemStack itemStack = this.inventory.getStackInSlot(i);
            if ( itemStack == null )
            {
                continue;
            }

            EntityItem entityItem = new EntityItem(this.worldObj, this.posX, (this.posY + (double)getEyeHeight()), posZ, itemStack);
            float motionWeight = 0.05F;
            entityItem.motionX = (double)((float)this.rand.nextGaussian() * motionWeight);
            entityItem.motionY = (double)((float)this.rand.nextGaussian() * motionWeight + 0.2F);
            entityItem.motionZ = (double)((float)this.rand.nextGaussian() * motionWeight);
            if ( itemStack.hasTagCompound() )
            {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }
            this.worldObj.spawnEntityInWorld(entityItem);
            this.inventory.setInventorySlotContents(i, null);
        }
    }
    
    public int getHealOtherThreshold()
    {
        return healOtherThreshold;
    }

    public void setHealOtherThreshold(int healOtherThreshold)
    {
        this.healOtherThreshold = healOtherThreshold;
    }

    public boolean canGenerateExp()
    {
        return canGenerateExp;
    }

    public void setCanGenerateExp(boolean canGenerateExp)
    {
        this.canGenerateExp = canGenerateExp;
    }
    
    public void switchExpDrop()
    {
        setCanGenerateExp(!canGenerateExp());
    }
}
