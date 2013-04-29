package takap.mods.nnnpc.utility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class ScreenShotHelper
{
    private static IntBuffer field_74293_b;
    private static int[] field_74294_c;
    
    public static BufferedImage saveScreenshot(int par2, int par3)
    {
        try
        {
            int var5 = par2 * par3;

            if (field_74293_b == null || field_74293_b.capacity() < var5)
            {
                field_74293_b = BufferUtils.createIntBuffer(var5);
                field_74294_c = new int[var5];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            field_74293_b.clear();
            GL11.glReadPixels(0, 0, par2, par3, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, field_74293_b);
            field_74293_b.get(field_74294_c);
            func_74289_a(field_74294_c, par2, par3);
            BufferedImage bufferedImage = new BufferedImage(par2, par3, 1);
            bufferedImage.setRGB(0, 0, par2, par3, field_74294_c, 0, par2);
            return bufferedImage;
        }
        catch (Exception var8)
        {
            var8.getStackTrace();
            return null;
        }
    }

    private static void func_74289_a(int[] par0ArrayOfInteger, int par1, int par2)
    {
        int[] var3 = new int[par1];
        int var4 = par2 / 2;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            System.arraycopy(par0ArrayOfInteger, var5 * par1, var3, 0, par1);
            System.arraycopy(par0ArrayOfInteger, (par2 - 1 - var5) * par1, par0ArrayOfInteger, var5 * par1, par1);
            System.arraycopy(var3, 0, par0ArrayOfInteger, (par2 - 1 - var5) * par1, par1);
        }
    }
}
