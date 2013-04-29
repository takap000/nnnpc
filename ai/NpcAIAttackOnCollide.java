package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.RoleBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;


public class NpcAIAttackOnCollide extends EntityAIBase
{
    private EntityNpc npc;
    private EntityLiving targetEntity;
    private int attackTick;
    private boolean canCheckLocation;
    private PathEntity npcPathEntity;
    private int pathUpdateTick;

    public NpcAIAttackOnCollide(EntityNpc par1Entity, boolean par3)
    {
        this.attackTick = 0;
        this.npc = par1Entity;
        this.canCheckLocation = par3;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        RoleBase role = this.npc.getRole();
        if ( (role == null) || (!role.canAttack()) )
        {
            return false;
        }
        
        EntityLiving entityliving = this.npc.getAttackTarget();
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
        RoleBase role = this.npc.getRole();
        if ( (role == null) || (!role.canAttack()) )
        {
            return false;
        }
        
        EntityLiving entityliving = this.npc.getAttackTarget();
        if (entityliving == null)
        {
            return false;
        }

        if (!this.targetEntity.isEntityAlive())
        {
            return false;
        }

        if (this.canCheckLocation)
        {
            return this.npc.isWithinHomeDistance(MathHelper.floor_double(this.targetEntity.posX), MathHelper.floor_double(this.targetEntity.posY), MathHelper.floor_double(this.targetEntity.posZ));
        }

        return !this.npc.getNavigator().noPath();
    }

    @Override
    public void startExecuting()
    {
        this.npc.getNavigator().setPath(this.npcPathEntity, this.npc.getMoveSpeed());
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
        this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30F, 30F);

        if ( (this.canCheckLocation||this.npc.getEntitySenses().canSee(this.targetEntity)) && (--pathUpdateTick<=0) )
        {
            this.pathUpdateTick = 4 + this.npc.getRNG().nextInt(7);
            this.npc.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.npc.getMoveSpeed());
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        double attackRange = 1.5d;    // TODO: npcの設定項目にすべき？
        double attackArea = (attackRange + this.npc.width) * (attackRange + this.npc.width);

        if ( this.npc.getDistanceSq(this.targetEntity.posX, this.targetEntity.boundingBox.minY, this.targetEntity.posZ) > attackArea )
        {
            return;
        }

        if ( this.attackTick > 0 )
        {
            return;
        }
        else
        {
            this.attackTick = 12;
            this.npc.attackEntityAsMob(targetEntity);
            this.npc.swingItem();
            return;
        }
    }
}
