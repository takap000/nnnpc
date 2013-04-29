package takap.mods.nnnpc.ai;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.EnumMode;
import takap.mods.nnnpc.entity.INPCAction;
import takap.mods.nnnpc.entity.NPCAction;
import takap.mods.nnnpc.entity.NPCActionMoveTo;
import takap.mods.nnnpc.entity.NPCActionWait;
import takap.mods.nnnpc.entity.RoleBase;
import takap.mods.nnnpc.entity.RoleVillager;
import takap.mods.nnnpc.location.NamedLocation;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.ChunkCoordinates;

public class NpcAITraceRoute extends EntityAIBase
{
    private EntityNpc npc;
    private PathNavigate pathNavigate;
    private int actionIndex;
    private int waitTick;
    private final int invalidWaitTick = -100;

    public NpcAITraceRoute(EntityNpc par1Entity)
    {
        this.npc = par1Entity;
        this.pathNavigate = par1Entity.getNavigator();
        this.actionIndex = 0;
        this.waitTick = this.invalidWaitTick;
        setMutexBits(3);
    }
    
    public boolean shouldExecute()
    {
        // roleのチェックはなくてもよいかも
        RoleBase role = this.npc.getRole();
        if ( (role == null) || !(role instanceof RoleVillager) )
        {
            return false;
        }
        
        EnumMode mode = this.npc.getMode();
        if ( (mode==null) || (!this.npc.isMode(EnumMode.TRACEROUTE)) )
        {
            return false;
        }
        
        return true;
    }
    
    public boolean continueExecuting()
    {
        // roleのチェックは(ry
        RoleBase role = this.npc.getRole();
        if ( (role == null) || !(role instanceof RoleVillager) )
        {
            return false;
        }
        
        EnumMode mode = this.npc.getMode();
        if ( (mode==null) || (!this.npc.isMode(EnumMode.TRACEROUTE)) )
        {
            return false;
        }
        
        return true;
    }
    
    public void startExecuting()
    {
        resetTask();
    }
    
    public void resetTask()
    {
        this.actionIndex = 0;
        this.waitTick = this.invalidWaitTick;
        this.pathNavigate.clearPathEntity();
    }
    
    public void updateTask()
    {
        NPCAction action = this.npc.getNPCAction(actionIndex);
        if ( !action.isEnable() )
        {
            // すべてのアクションが無効の場合，無限ループにおちいってしまうのでインクリメントだけさせてreturn
            incrementActionIndex();
            return;
        }
        
        INPCAction currentAction = action.getCurrentAction();
        if ( currentAction instanceof NPCActionWait )
        {
            updateWaitState((NPCActionWait)currentAction);
        }
        else if ( currentAction instanceof NPCActionMoveTo )
        {
            updateMoveToState((NPCActionMoveTo)currentAction);
        }
    }
    
    private void updateWaitState(NPCActionWait action)
    {
        // 不正な値が設定されていないかチェック
        if ( !action.isAvailable() )
        {
            incrementActionIndex();
            return;
        }
        
        if ( this.waitTick == this.invalidWaitTick )
        {
            this.waitTick = action.getWaitTime();
        }
        
        // tickのデクリメント処理, 0を下回るまで(0の場合も)実施
        if ( this.waitTick >= 0 )
        {
            this.waitTick--;
        }
        
        // wait終了判定
        if ( this.waitTick < 0 )
        {
            incrementActionIndex();
            return;
        }
    }
    
    private void updateMoveToState(NPCActionMoveTo action)
    {
        if ( !action.isAvailable() )
        {
            incrementActionIndex();
            return;
        }
        
        // 経路未設定の場合は経路を設定
        if ( this.pathNavigate.noPath() )
        {
            NamedLocation location = this.npc.getLocation(action.getLocationIndex());
            if ( location == null )
            {
                incrementActionIndex();
                return;
            }
            ChunkCoordinates coordinate = location.getCoordinate();
            if ( coordinate == null )
            {
                incrementActionIndex();
                return;
            }
            
            if ( this.pathNavigate.tryMoveToXYZ(coordinate.posX, coordinate.posY, coordinate.posZ, this.npc.getMoveSpeed()) )
            {
                if ( this.pathNavigate.getPath().isFinished() )
                {
                    incrementActionIndex();
                    return;
                }
                return;
            }
            // falseを返した場合はスキップ。
            else
            {
                if ( this.npc.getDistanceSq(coordinate.posX, coordinate.posY, coordinate.posZ) > 4d )
                {
                    String goal;
                    String locationName = location.getName();
                    if ( (locationName == null) || locationName.equals("") )
                    {
                        goal = "specified location";
                    }
                    else
                    {
                        goal = locationName;
                    }
                    this.npc.addChatMessage(this.npc.getOwner(), "I could not find path to " + goal);
                    Utility.printInformation("  could not find path to " + goal);
                }
                incrementActionIndex();
                return;
            }
        }
    }
    
    private void incrementActionIndex()
    {
        this.actionIndex = (this.actionIndex + 1) % EntityNpc.maxRouteLimit;
        this.waitTick = this.invalidWaitTick;
    }
}
