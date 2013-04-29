package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import net.minecraft.entity.ai.EntityAIBase;

public class NpcAISit extends EntityAIBase
{
    private EntityNpc npc;

    public NpcAISit(EntityNpc par1Entity)
    {
        this.npc = par1Entity;
        setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute()
    {
        return npc.isMode(EnumMode.SIT);
    }
    
    @Override
    public void startExecuting()
    {
        this.npc.getNavigator().clearPathEntity();
    }
    
    @Override
    public void resetTask()
    {
    }
}
