package takap.mods.nnnpc.entity;

import net.minecraft.nbt.NBTTagCompound;

public class NPCActionMoveTo implements INPCAction
{
    private int locationIndex;
    
    public NPCActionMoveTo()
    {
        this.locationIndex = 0;
    }
    
    @Override
    public String getLabel()
    {
        return "MoveTo";
    }

    @Override
    public boolean isAvailable()
    {
        if ( (this.locationIndex<0) || (this.locationIndex >= EntityNpc.maxLocationLimit) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public int getLocationIndex()
    {
        return this.locationIndex;
    }
    
    public void setPreviousLocationIndex()
    {
        this.locationIndex = (this.locationIndex+EntityNpc.maxLocationLimit-1) % EntityNpc.maxLocationLimit;
    }
    
    public void setNextLocationIndex()
    {
        this.locationIndex = (this.locationIndex+1) % EntityNpc.maxLocationLimit;
    }

    public static NPCActionMoveTo loadNPCActionMoveToFromNBT(NBTTagCompound nbtTagCompound)
    {
        NPCActionMoveTo action = new NPCActionMoveTo();
        action.readFromNBT(nbtTagCompound);
        return action;
    }
    
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        this.locationIndex = nbtTagCompound.getInteger("moveToLocationIndex");
    }
    
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setInteger("moveToLocationIndex", this.locationIndex);
    }
}
