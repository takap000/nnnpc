package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class NpcAIWatchOwner extends EntityAIBase
{
    private EntityNpc npc;
    private EntityPlayer owner;
    
    private Entity closestEntity;
    private float range;
    private int watchDuration;
    private float threshold;

    public NpcAIWatchOwner(EntityNpc npc, float range)
    {
        this.npc = npc;
        this.owner = null;
        this.closestEntity = null;
        this.range = range;
        this.threshold = 0.02F;
        setMutexBits(2);
    }

    public NpcAIWatchOwner(EntityNpc npc, float range, float threshold)
    {
        this.npc = npc;
        this.owner = null;
        this.closestEntity = null;
        this.range = range;
        this.threshold = threshold;
        setMutexBits(2);
    }
    
    @Override
    public boolean shouldExecute()
    {
        this.owner = this.npc.getOwner();
        if ( this.owner == null )
        {
            return false;
        }
        if ( this.npc.getRNG().nextFloat() >= this.threshold )
        {
            return false;
        }

        if ( this.npc.getDistanceToEntity(this.owner) < this.range )
        {
            this.closestEntity = this.owner.worldObj.getClosestPlayerToEntity(this.owner, this.range);
        }

        return this.closestEntity != null;
    }
    
    @Override
    public boolean continueExecuting()
    {
        if ( !this.closestEntity.isEntityAlive() )
        {
            return false;
        }

        if ( this.npc.getDistanceSqToEntity(this.closestEntity) > (double)(this.range * this.range) )
        {
            return false;
        }
        else
        {
            return this.watchDuration > 0;
        }
    }
    
    @Override
    public void startExecuting()
    {
        this.watchDuration = 40 + this.npc.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask()
    {
        this.closestEntity = null;
    }
    
    @Override
    public void updateTask()
    {
        this.npc.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + (double)closestEntity.getEyeHeight(), this.closestEntity.posZ, 10F, this.npc.getVerticalFaceSpeed());
        this.watchDuration--;
    }
}
