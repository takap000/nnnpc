package takap.mods.nnnpc.item;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.FormManager;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class ItemSoulStone extends Item
{
    public ItemSoulStone(int i)
    {
        super(i);
        setMaxStackSize(64);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int i, int j, int k, int l, float par8, float par9, float par10)
    {
        if (world.isRemote)
        {
            return true;
        }

        if ( FormManager.getInstance().getNumberOfForms() <= 0 )
        {
            Utility.printInformation("texture directory is not found ... @ ItemSoulStone");
            return false;
        }
        double x = i + Facing.offsetsXForSide[l] + 0.5d;
        double y = j + Facing.offsetsYForSide[l];
        double z = k + Facing.offsetsZForSide[l] + 0.5d;
        EntityNpc npc = FormManager.getInstance().getDefaultForm().getDefaultNpc(world, x, y, z, 0.0f, 0.0f);
        if ( npc == null )
        {
            Utility.printInformation("no models are available ... @ ItemCustomableNpcEgg");
            return false;
        }
        // ownerは召喚時にだけ設定するので，getDefaultNpc()内ではなく，ここでのみ実施
        npc.setOwnerName(entityPlayer.username);
        world.spawnEntityInWorld(npc);
        itemStack.stackSize--;
        world.playSoundAtEntity(entityPlayer, "random.glass", itemRand.nextFloat() * 1.0f, 1.0f);
        return true;
    }
}
