package takap.mods.nnnpc.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerNpcInventory extends Container
{
    private int numRows;
    protected int inventoryOffset;

    public ContainerNpcInventory()
    {
        this(null, null);
    }
    
    public ContainerNpcInventory(IInventory par1IInventory, IInventory par2IInventory)
    {
        this.numRows = 3;
//        numRows = par2IInventory.getSizeInventory() / 8;
        int i = (numRows - 4) * 18;
        setInventoryOffset();

        // npc inventory -- main
        for (int j = 0; j < numRows; j++)
        {
            for (int i1 = 0; i1 < 8; i1++)
            {
                addSlotToContainer(new Slot(par2IInventory, i1 + j * 8, 8 + i1 * 18, 18 + j * 18 + inventoryOffset));
            }
        }
        // npc inventory -- weapon
        addSlotToContainer(new Slot(par2IInventory, 24, 153, 24 + inventoryOffset));  // left
        addSlotToContainer(new Slot(par2IInventory, 25, 153, 54 + inventoryOffset));  // right

        // player inventory
        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
                addSlotToContainer(new Slot(par1IInventory, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i + inventoryOffset));
            }
        }

        for (int l = 0; l < 9; l++)
        {
            addSlotToContainer(new Slot(par1IInventory, l, 8 + l * 18, 161 + i + inventoryOffset));
        }
    }

    public void setInventoryOffset()
    {
        inventoryOffset = 0;
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }
    
    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < numRows * 8)
            {
                if (!mergeItemStack(itemstack1, numRows * 8, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, numRows * 8, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
