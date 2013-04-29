package takap.mods.nnnpc.inventory;

import net.minecraft.inventory.IInventory;


public class ContainerNpcInventoryForSimple extends ContainerNpcInventory
{
    public ContainerNpcInventoryForSimple()
    {
        this(null, null);
    }
    
    public ContainerNpcInventoryForSimple(IInventory par1IInventory, IInventory par2IInventory)
    {
        super(par1IInventory, par2IInventory);
    }
    
    public void setInventoryOffset()
    {
        inventoryOffset = 55;
    }
}
