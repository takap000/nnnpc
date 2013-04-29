package takap.mods.nnnpc.item;

import takap.mods.nnnpc.mod_NNNPC;
import takap.mods.nnnpc.gui.EnumGuiPage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class ItemNiceStick extends Item
{
    public ItemNiceStick(int i)
    {
        super(i);
        setMaxStackSize(1);
    }
    
    @Override
    public int getSpriteNumber()
    {
    	return 1;
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int i, int j, int k, int l, float par8, float par9, float par10)
    {
        // path設定の都合上，上面のみ許可
        if ( ForgeDirection.getOrientation(l).equals(ForgeDirection.UP) )
        {
            // solidか判定
            if ( world.isBlockSolidOnSide(i, j, k, ForgeDirection.getOrientation(l), false) )
            {
                entityPlayer.openGui(mod_NNNPC.instance, EnumGuiPage.LOCATION.ordinal(), world, i, j, k);
            }
        }
        
        return true;
    }
}
