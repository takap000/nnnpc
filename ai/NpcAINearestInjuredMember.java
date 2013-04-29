package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.RoleBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITarget;

public class NpcAINearestInjuredMember extends EntityAITarget
{
    EntityLiving targetEntity;
    private EntityNpc npc;

    public NpcAINearestInjuredMember(EntityNpc npc, float par3, boolean par5)
    {
        this(npc, par3, par5, false);
    }

    public NpcAINearestInjuredMember(EntityNpc npc, float par3, boolean par5, boolean par6)
    {
        super(npc, par3, par5, par6);
        this.npc = npc;
        this.targetDistance = par3;
        setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute()
    {
        RoleBase role = this.npc.getRole();
        if ( (role == null) || (!role.canHeal()) )
        {
            return false;
        }
        
        // TODO: "Wait以外"に変更すべき？ 要検討
        if ( !this.npc.isMode(EnumMode.FOLLOW) )
        {
            return false;
        }
        
        this.targetEntity = this.npc.getEntityToHeal();
        if ( this.targetEntity != null )
        {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
