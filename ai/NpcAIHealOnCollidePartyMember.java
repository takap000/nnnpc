package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.RoleBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;


public class NpcAIHealOnCollidePartyMember extends EntityAIBase
{
    private EntityNpc npc;
    private EntityLiving targetEntity;
    private int actionTick;
    private PathEntity npcPathEntity;
    private int pathUpdateTick;
    private final int actionInterval = 30;

    public NpcAIHealOnCollidePartyMember(EntityNpc par1Entity)
    {
        this.actionTick = 0;
        this.npc = par1Entity;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        RoleBase role = this.npc.getRole();
        
        // サポート機能をもっていないNPCやフォロー不可のNPCを除外
        if ( (role == null) || (!role.canHeal()) || (!role.canFollow()) ) // TODO: "Follow"をもたないroleで発動させることを検討
        {
            return false;
        }
        
        // modeが"Follow"のときのみ機能, TODO: modeが"Wait"以外の場合にすべき? "Follow"をもたないroleで発動させることを検討
        if ( !this.npc.isMode(EnumMode.FOLLOW) )
        {
            return false;
        }
        
        EntityLiving entityliving = this.npc.getEntityToHeal();
        if (entityliving == null)
        {
            return false;
        }

        this.targetEntity = entityliving;
        this.npcPathEntity = this.npc.getNavigator().getPathToEntityLiving(this.targetEntity);
        return npcPathEntity != null;
    }

    @Override
    public boolean continueExecuting()
    {
        if ( this.targetEntity == null )
        {
            EntityLiving entityLiving = this.npc.getEntityToHeal();
            if (entityLiving == null)
            {
                return false;
            }
            this.targetEntity = entityLiving;
        }
        else
        {
            if (!this.targetEntity.isEntityAlive())
            {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void startExecuting()
    {
        pathUpdateTick = 0;
    }

    @Override
    public void resetTask()
    {
        this.targetEntity = null;
        this.npc.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask()
    {
        this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30f, 30f);
        
        if ( this.npc.getEntitySenses().canSee(this.targetEntity) && (--pathUpdateTick<=0) )
        {
            this.pathUpdateTick = 4 + this.npc.getRNG().nextInt(7);
            if ( this.npc.getDistanceSqToEntity(this.targetEntity) < 5d )  // TODO: 5dは決め打ちではなく，heightとwidthから算出するように
            {
                this.npc.getNavigator().clearPathEntity();
            }
            else
            {
                this.npc.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.npc.getMoveSpeed());
            }
        }

        this.actionTick = Math.max(this.actionTick - 1, 0);
        double healRange = 2.5d;    // TODO: npcの設定項目にすべき？heightとwidthで判定？ ちょっと遠めに
        double healArea = (healRange + this.npc.width) * (healRange + this.npc.width);
        
        if ( this.npc.getDistanceSq(this.targetEntity.posX, this.targetEntity.boundingBox.minY, this.targetEntity.posZ) > healArea )
        {
            return;
        }

        if ( this.actionTick > 0 )
        {
            return;
        }
        else
        {
            this.actionTick = this.actionInterval;
            if ( this.npc.healEntity(targetEntity) )
            {
                this.npc.swingItem();
            }
            // アイテム不足などでhealに失敗しても，targetとpathをリセットする
            resetTask();
            return;
        }
    }
}
