package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class NpcAIWander extends EntityAIBase
{
    private EntityNpc npc;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public NpcAIWander(EntityNpc npc)
    {
        this.npc = npc;
        setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute()
    {
        if ( !this.npc.isMode(EnumMode.FREEDOM) && !this.npc.isMode(EnumMode.SWIM) )
        {
            return false;
        }

        if ( this.npc.getRNG().nextInt(120) != 0 )
        {
            return false;
        }

        Vec3 vec3d = RandomPositionGenerator.findRandomTarget(this.npc, 10, 7);

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            this.xPosition = vec3d.xCoord;
            this.yPosition = vec3d.yCoord;
            this.zPosition = vec3d.zCoord;
            return true;
        }
    }
    
    @Override
    public boolean continueExecuting()
    {
        return !this.npc.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting()
    {
        this.npc.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.npc.getMoveSpeed());
    }
}
