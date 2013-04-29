package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.RoleBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


public class NpcAIFollowOwner extends EntityAIBase
{
    private EntityNpc npc;
    private World world;
    private EntityLiving owner;
    private PathNavigate pathNavigate;
    private int pathUpdateTick;
    float maxDist;
    float minDist;
    private boolean canAvoidsWater;

    public NpcAIFollowOwner(EntityNpc par1Entity, float par3, float par4)
    {
        this.npc = par1Entity;
        this.world = this.npc.worldObj;
        this.pathNavigate = par1Entity.getNavigator();
        this.minDist = par3;
        this.maxDist = par4;
        setMutexBits(3);
    }
    
    public boolean shouldExecute()
    {
        EntityLiving entityliving = this.npc.getOwner();
        if (entityliving == null)
        {
            return false;
        }
        
        RoleBase role = this.npc.getRole();
        if ( (role == null) || (!role.canFollow()) )
        {
            return false;
        }
        
        if (!this.npc.isMode(EnumMode.FOLLOW))
        {
            return false;
        }

        double distanceSq = this.npc.getDistanceSqToEntity(entityliving);
        if (distanceSq < (double)(minDist * minDist))
        {
            return false;
        }
        else
        {
            this.owner = entityliving;
            return true;
        }
    }
    
    public boolean continueExecuting()
    {
        if ( !this.pathNavigate.noPath() )
        {
            if ( this.npc.getDistanceSqToEntity(this.owner) > (double)(this.maxDist * this.maxDist) )
            {
                return true;
            }
            this.pathNavigate.clearPathEntity();
        }
        return false;
    }
    
    public void startExecuting()
    {
        this.pathUpdateTick = 0;
        this.canAvoidsWater = this.npc.getNavigator().getAvoidsWater();
        this.npc.getNavigator().setAvoidsWater(false);
    }
    
    public void resetTask()
    {
        this.owner = null;
        this.pathNavigate.clearPathEntity();
        this.npc.getNavigator().setAvoidsWater(this.canAvoidsWater);
    }
    
    public void updateTask()
    {
        this.npc.getLookHelper().setLookPositionWithEntity(this.owner, 10f, this.npc.getVerticalFaceSpeed());
        
        if (--this.pathUpdateTick > 0)
        {
            return;
        }
        
        this.pathUpdateTick = 10;
        
        if ( this.pathNavigate.tryMoveToEntityLiving(this.owner, this.npc.getMoveSpeed()) )
        {
            return;
        }

        if ( this.npc.getDistanceSqToEntity(this.owner) < 144D )
        {
            return;
        }

        int i = MathHelper.floor_double(this.owner.posX) - 2;
        int j = MathHelper.floor_double(this.owner.posZ) - 2;
        int k = MathHelper.floor_double(this.owner.boundingBox.minY);

        for ( int l = 0; l <= 4; l++ )
        {
            for ( int i1 = 0; i1 <= 4; i1++ )
            {
                if ( (l<1 || i1<1 || l>3 || i1>3) && this.world.isBlockNormalCube(i + l, k - 1, j + i1) && !this.world.isBlockNormalCube(i+l, k, j+i1) && !this.world.isBlockNormalCube(i+l, k+1, j+i1) )
                {
                    this.npc.setLocationAndAngles((float)(i+l) + 0.5F, k, (float)(j+i1) + 0.5F, this.npc.rotationYaw, this.npc.rotationPitch);
                    this.pathNavigate.clearPathEntity();
                    return;
                }
            }
        }
    }
}
