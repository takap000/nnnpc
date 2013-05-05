package takap.mods.nnnpc.location;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.mod_NNNPC;
import takap.mods.nnnpc.texture.Texture;

public class RecordedLocation
{
    public static final int numberOfSlots = 16;
    
    private static RecordedLocation instance = new RecordedLocation();
    private NamedLocation[] locationList;
    private Texture[] textureList;
    
    private RecordedLocation()
    {
        loadLocation();
    }
    
    public static RecordedLocation getInstance()
    {
        return instance;
    }
    
    private void loadLocation()
    {
        this.locationList = new NamedLocation[numberOfSlots];
        this.textureList = new Texture[numberOfSlots];
        for ( int i=0; i<numberOfSlots; i++ )
        {
            this.locationList[i] = null;
            this.textureList[i] = null;
        }
    }
    
    public NamedLocation getLocation(int index)
    {
        if ( (index<0) || (index>=numberOfSlots) )
        {
            return null;
        }
        return this.locationList[index];
    }
    
    public void setLocation(int index, NamedLocation location)
    {
        if ( (index<0) || (index>=numberOfSlots) )
        {
            return;
        }
        this.locationList[index] = location;
    }
    
    public Texture getTexture(int index)
    {
        if ( (index<0) || (index>=numberOfSlots) )
        {
            return null;
        }
        return this.textureList[index];
    }
    
    public void overwriteTexture(int index, BufferedImage image)
    {
        if ( (index<0) || (index>=numberOfSlots) )
        {
            return;
        }
        String fileName = String.format("%02d.png", index);
        File destinationFile = new File(mod_NNNPC.locationSSDirectoryPath, fileName);
        if ( destinationFile.exists() )
        {
            destinationFile.delete();
        }
        
        try
        {
            destinationFile.createNewFile();
            ImageIO.write(image, "png", destinationFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        
        // 登録済みのテクスチャがあった場合は開放
        if ( this.textureList[index] != null )
        {
	        int oldIndex = this.textureList[index].getImageIndex();
	        if ( oldIndex >= 0 )
	        {
	        	GL11.glDeleteTextures(oldIndex);
	        }
        }
        
        // 新しいファイルをテクスチャとして登録
        Texture texture = new Texture(fileName, destinationFile);
        this.textureList[index] = texture;
    }
}
