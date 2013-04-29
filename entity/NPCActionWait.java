package takap.mods.nnnpc.entity;

import net.minecraft.nbt.NBTTagCompound;

public class NPCActionWait implements INPCAction
{
    private int waitTime;
    
    public NPCActionWait()
    {
        this.waitTime = 0;
    }
    
    @Override
    public String getLabel()
    {
        return "Wait";
    }

    @Override
    public boolean isAvailable()
    {
        if ( this.waitTime < 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public int getWaitTime()
    {
        return this.waitTime;
    }
    
    public void setWaitTime(int time)
    {
        this.waitTime = time;
    }

    public static NPCActionWait loadNPCActionWaitFromNBT(NBTTagCompound nbtTagCompound)
    {
        NPCActionWait action = new NPCActionWait();
        action.readFromNBT(nbtTagCompound);
        return action;
    }
    
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        this.waitTime = nbtTagCompound.getInteger("waitTime");
    }
    
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setInteger("waitTime", this.waitTime);
    }
}
