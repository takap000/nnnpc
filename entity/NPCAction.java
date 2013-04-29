package takap.mods.nnnpc.entity;

import net.minecraft.nbt.NBTTagCompound;

public class NPCAction
{
    private boolean isEnable;
    private int currentActionIndex;
    private INPCAction[] actionTypeList;
    
    public NPCAction()
    {
        this.isEnable = true;
        // なにかもっといい管理方法ないかなぁ...
        this.actionTypeList = new INPCAction[2];
        this.actionTypeList[0] = new NPCActionWait();
        this.actionTypeList[1] = new NPCActionMoveTo();
        this.currentActionIndex = 0;
    }
    
    public boolean isEnable()
    {
        return this.isEnable;
    }
    
    public void switchEnable()
    {
        this.isEnable = !this.isEnable;
    }
    
    public void switchAction()
    {
        this.currentActionIndex = (this.currentActionIndex + 1) % this.actionTypeList.length;
    }
    
    public INPCAction getCurrentAction()
    {
        return this.actionTypeList[this.currentActionIndex];
    }

    public static NPCAction loadNPCActionFromNBT(NBTTagCompound nbtTagCompound)
    {
        NPCAction action = new NPCAction();
        action.readFromNBT(nbtTagCompound);
        return action;
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        this.currentActionIndex = nbtTagCompound.getInteger("actionTypeIndex");
        this.isEnable = nbtTagCompound.getBoolean("actionEnable");
        this.actionTypeList[0] = NPCActionWait.loadNPCActionWaitFromNBT(nbtTagCompound);
        this.actionTypeList[1] = NPCActionMoveTo.loadNPCActionMoveToFromNBT(nbtTagCompound);
    }
    
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setInteger("actionTypeIndex", this.currentActionIndex);
        nbtTagCompound.setBoolean("actionEnable", this.isEnable);
        ((NPCActionWait)this.actionTypeList[0]).writeToNBT(nbtTagCompound);
        ((NPCActionMoveTo)this.actionTypeList[1]).writeToNBT(nbtTagCompound);
    }
}
