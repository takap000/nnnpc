package takap.mods.nnnpc.inventory;

import takap.mods.nnnpc.entity.EntityNpc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


public class InventoryNpc implements IInventory
{
    public ItemStack mainInventory[];
    public ItemStack weaponInventory[];

    public int currentItem;
    public EntityNpc npc;

    public boolean inventoryChanged;

    public InventoryNpc(EntityNpc npc)
    {
        mainInventory = new ItemStack[24];
        weaponInventory = new ItemStack[2];
        currentItem = 0;
        inventoryChanged = false;
        this.npc = npc;
    }

    /**
     * Returns a slot index in main inventory containing a specific itemID
     */
    private int getInventorySlotContainItem(int par1)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null && mainInventory[i].itemID == par1)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * stores an itemstack in the users inventory
     */
    private int storeItemStack(ItemStack par1ItemStack)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null && mainInventory[i].itemID == par1ItemStack.itemID && mainInventory[i].isStackable() && mainInventory[i].stackSize < mainInventory[i].getMaxStackSize() && mainInventory[i].stackSize < getInventoryStackLimit() && (!mainInventory[i].getHasSubtypes() || mainInventory[i].getItemDamage() == par1ItemStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(mainInventory[i], par1ItemStack))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the first item stack that is empty.
     */
    private int getFirstEmptyStack()
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * This function stores as many items of an ItemStack as possible in a matching slot and returns the quantity of
     * left over items.
     */
    private int storePartialItemStack(ItemStack par1ItemStack)
    {
        int i = par1ItemStack.itemID;
        int j = par1ItemStack.stackSize;

        if (par1ItemStack.getMaxStackSize() == 1)
        {
            int k = getFirstEmptyStack();

            if (k < 0)
            {
                return j;
            }

            if (mainInventory[k] == null)
            {
                mainInventory[k] = ItemStack.copyItemStack(par1ItemStack);
            }

            return 0;
        }

        int l = storeItemStack(par1ItemStack);

        if (l < 0)
        {
            l = getFirstEmptyStack();
        }

        if (l < 0)
        {
            return j;
        }

        if (mainInventory[l] == null)
        {
            mainInventory[l] = new ItemStack(i, 0, par1ItemStack.getItemDamage());

            if (par1ItemStack.hasTagCompound())
            {
                mainInventory[l].setTagCompound((NBTTagCompound)par1ItemStack.getTagCompound().copy());
            }
        }

        int i1 = j;

        if (i1 > mainInventory[l].getMaxStackSize() - mainInventory[l].stackSize)
        {
            i1 = mainInventory[l].getMaxStackSize() - mainInventory[l].stackSize;
        }

        if (i1 > getInventoryStackLimit() - mainInventory[l].stackSize)
        {
            i1 = getInventoryStackLimit() - mainInventory[l].stackSize;
        }

        if (i1 == 0)
        {
            return j;
        }
        else
        {
            j -= i1;
            mainInventory[l].stackSize += i1;
            mainInventory[l].animationsToGo = 5;
            return j;
        }
    }

    /**
     * Decrement the number of animations remaining. Only called on client side. This is used to handle the animation of
     * receiving a block.
     */
    public void decrementAnimations()
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null)
            {
                mainInventory[i].updateAnimation(npc.worldObj, npc, i, currentItem == i);
            }
        }
    }

    /**
     * removed one item of specified itemID from inventory (if it is in a stack, the stack size will reduce with 1)
     */
    public boolean consumeInventoryItem(int par1)
    {
        int i = getInventorySlotContainItem(par1);

        if (i < 0)
        {
            return false;
        }
        
        ItemStack aitemstack[] = mainInventory;
        if (i >= mainInventory.length)
        {
            aitemstack = weaponInventory;
            i -= mainInventory.length;
        }
        if (--aitemstack[i].stackSize <= 0)
        {
            aitemstack[i] = null;
        }

        return true;
    }

    /**
     * Get if a specifiied item id is inside the inventory.
     */
    public boolean hasItem(int par1)
    {
        int i = getInventorySlotContainItem(par1);
        return i >= 0;
    }

    /**
     * Adds the item stack to the inventory, returns false if it is impossible.
     */
    public boolean addItemStackToInventory(ItemStack par1ItemStack)
    {
        if (!par1ItemStack.isItemDamaged())
        {
            int i;

            do
            {
                i = par1ItemStack.stackSize;
                par1ItemStack.stackSize = storePartialItemStack(par1ItemStack);
            }
            while (par1ItemStack.stackSize > 0 && par1ItemStack.stackSize < i);
            return par1ItemStack.stackSize < i;
        }

        int j = getFirstEmptyStack();

        if (j >= 0)
        {
            mainInventory[j] = ItemStack.copyItemStack(par1ItemStack);
            mainInventory[j].animationsToGo = 5;
            par1ItemStack.stackSize = 0;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        ItemStack aitemstack[] = mainInventory;

        if (par1 >= mainInventory.length)
        {
            aitemstack = weaponInventory;
            par1 -= mainInventory.length;
        }

        if (aitemstack[par1] != null)
        {
            if (aitemstack[par1].stackSize <= par2)
            {
                ItemStack itemstack = aitemstack[par1];
                aitemstack[par1] = null;
                return itemstack;
            }

            ItemStack itemstack1 = aitemstack[par1].splitStack(par2);

            if (aitemstack[par1].stackSize == 0)
            {
                aitemstack[par1] = null;
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        ItemStack aitemstack[] = mainInventory;
        
        if ( par1 >= this.getSizeInventory() )
        {
            return null;
        }
        else if ( par1 >= mainInventory.length )
        {
            aitemstack = weaponInventory;
            par1 -= mainInventory.length;
        }

        if (aitemstack[par1] != null)
        {
            ItemStack itemstack = aitemstack[par1];
            aitemstack[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        ItemStack aitemstack[] = mainInventory;

        if (par1 >= aitemstack.length)
        {
            par1 -= aitemstack.length;
            aitemstack = weaponInventory;
        }

        aitemstack[par1] = par2ItemStack;
    }

    /**
     * Gets the strength of the current item (tool) against the specified block, 1.0f if not holding anything.
     */
    public float getStrVsBlock(Block par1Block)
    {
        float f = 1.0F;

/*
        if (mainInventory[currentItem] != null)
        {
            f *= mainInventory[currentItem].getStrVsBlock(par1Block);
        }
*/

        return f;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public NBTTagList writeToNBT(NBTTagList par1NBTTagList)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                mainInventory[i].writeToNBT(nbttagcompound);
                par1NBTTagList.appendTag(nbttagcompound);
            }
        }

        for (int j = 0; j < weaponInventory.length; j++)
        {
            if (weaponInventory[j] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)(j + 100));
                weaponInventory[j].writeToNBT(nbttagcompound1);
                par1NBTTagList.appendTag(nbttagcompound1);
            }
        }

        return par1NBTTagList;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void readFromNBT(NBTTagList par1NBTTagList)
    {
        mainInventory = new ItemStack[24];
        weaponInventory = new ItemStack[2];

        for (int i = 0; i < par1NBTTagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)par1NBTTagList.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xff;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack == null)
            {
                continue;
            }

            if (j >= 0 && j < mainInventory.length)
            {
                mainInventory[j] = itemstack;
            }

            if (j >= 100 && j < weaponInventory.length + 100)
            {
                weaponInventory[j - 100] = itemstack;
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return mainInventory.length + weaponInventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        ItemStack aitemstack[] = mainInventory;

        if (par1 >= aitemstack.length)
        {
            par1 -= aitemstack.length;
            aitemstack = weaponInventory;
        }

        return aitemstack[par1];
    }

    /**
     * Returns the name of the inventory.
     */
    @Override
    public String getInvName()
    {
        return "container.inventory";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Return damage vs an entity done by the current held weapon, or 1 if nothing is held
     */
    public int getDamageVsEntity(Entity par1Entity)
    {
        ItemStack itemstack = getStackInSlot(currentItem);

        if (itemstack != null)
        {
            return itemstack.getDamageVsEntity(par1Entity);
        }
        else
        {
            return 1;
        }
    }

    /**
     * Returns whether the current item (tool) can harvest from the specified block (actually get a result).
     */
    public boolean canHarvestBlock(Block par1Block)
    {
        if (par1Block.blockMaterial.isToolNotRequired())
        {
            return true;
        }

        ItemStack itemstack = getStackInSlot(currentItem);

        if (itemstack != null)
        {
            return itemstack.canHarvestBlock(par1Block);
        }
        else
        {
            return false;
        }
    }

    /**
     * Drop all items.
     */
    public void dropAllItems()
    {
//    	cnpc.dropAllItems();
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    @Override
    public void onInventoryChanged()
    {
        inventoryChanged = true;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        if (npc.isDead)
        {
            return false;
        }

        return par1EntityPlayer.getDistanceSqToEntity(npc) <= 64D;
    }

    /**
     * Returns true if the specified ItemStack exists in the inventory.
     */
    public boolean hasItemStack(ItemStack par1ItemStack)
    {
        for (int i = 0; i < weaponInventory.length; i++)
        {
            if (weaponInventory[i] != null && weaponInventory[i].isItemEqual(par1ItemStack))
            {
                return true;
            }
        }

        for (int j = 0; j < mainInventory.length; j++)
        {
            if (mainInventory[j] != null && mainInventory[j].isItemEqual(par1ItemStack))
            {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public void openChest()
    {
    }
    
    @Override
    public void closeChest()
    {
    }
    
    public void copyInventoryFrom(InventoryNpc inventory)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            mainInventory[i] = ItemStack.copyItemStack(inventory.mainInventory[i]);
        }

        for (int j = 0; j < weaponInventory.length; j++)
        {
            weaponInventory[j] = ItemStack.copyItemStack(inventory.weaponInventory[j]);
        }
    }

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}
}
