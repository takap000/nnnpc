package takap.mods.nnnpc.ai;

import java.util.List;

import takap.mods.nnnpc.entity.EntityNpc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

public class NpcAIAvoidPrimedEntity extends EntityAIBase
{
    private EntityNpc npc;
    private Entity targetEntity;
    private float distanceFromEntity;

    private PathEntity npcPathEntity;
    private PathNavigate npcPathNavigate;

    private int checkInterval;
    private int interval;

    public NpcAIAvoidPrimedEntity(EntityNpc par1Entity, float par2, int par3)
    {
        this.npc = par1Entity;
        this.targetEntity = null;
        this.distanceFromEntity = par2;
        this.checkInterval = par3;
        this.interval = 0;
        this.npcPathNavigate = this.npc.getNavigator();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
    	if ( isTargetPrimed(this.targetEntity) )
    	{
    		return true;
    	}
        this.interval = (this.interval+1) % this.checkInterval;
        if ( this.interval != 0 )
        {
        	return false;
        }

        this.targetEntity = this.searchPrimedEntity();
        if (this.targetEntity == null)
        {
            return false;
        }

        Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.npc, 16, 7, this.npc.worldObj.getWorldVec3Pool().getVecFromPool(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));

        if (var2 == null)
        {
            return false;
        }
        else if (this.targetEntity.getDistanceSq(var2.xCoord, var2.yCoord, var2.zCoord) < this.targetEntity.getDistanceSqToEntity(this.npc))
        {
            return false;
        }
        else
        {
            this.npcPathEntity = this.npcPathNavigate.getPathToXYZ(var2.xCoord, var2.yCoord, var2.zCoord);
            return this.npcPathEntity == null ? false : this.npcPathEntity.isDestinationSame(var2);
        }
    }
    
    private boolean isTargetPrimed(Entity entity)
    {
        if ( entity == null )
        {
            return false;
        }
    	else if ( entity instanceof EntityTNTPrimed )
    	{
            return true;
    	}
    	else if ( entity instanceof EntityCreeper )
    	{
    	    EntityCreeper creeper = (EntityCreeper)entity;
    	    if ( creeper.getCreeperState() == 1 )
    	    {
    	    	return true;
    	    }
    	    else
    	    {
    	    	return false;
    	    }
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private Entity searchPrimedEntity()
    {
        List entityList = this.npc.worldObj.getLoadedEntityList();
        for ( int i=0; i<entityList.size(); i++ )
        {
        	Entity entity = (Entity)entityList.get(i);
        	if ( isTargetPrimed(entity) )
        	{
        		if ( this.npc.getDistanceSqToEntity(entity) < this.distanceFromEntity )
        		{
        			return entity;
        		}
        	}
        }
        return null;
    }
    
    @Override
    public boolean continueExecuting()
    {
        return !this.npcPathNavigate.noPath();
    }
    
    @Override
    public void startExecuting()
    {
        this.npcPathNavigate.setPath(this.npcPathEntity, this.npc.getMoveSpeed()*1.5f);
    }
    
    @Override
    public void resetTask()
    {
        this.targetEntity = null;
    }
    
    @Override
    public void updateTask()
    {
        if (this.npc.getDistanceSqToEntity(this.targetEntity) < 49.0D)
        {
            this.npc.getNavigator().setSpeed(this.npc.getMoveSpeed()*1.5f);
        }
        else
        {
            this.npc.getNavigator().setSpeed(this.npc.getMoveSpeed()*1.2f);
        }
    }
}
