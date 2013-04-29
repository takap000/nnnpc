package takap.mods.nnnpc.texture;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import takap.mods.nnnpc.utility.Utility;

public class Texture
{
    private String fileName;
    private String relativePath;
    private String absolutePath;
    private BufferedImage textureImage;
    private int imageIndex;
    private int width;
    private int height;
    private boolean isAvailable;

    public Texture(String relativePath, File file)
    {
        this.isAvailable = initializeTexture(relativePath, file);
        if ( isAvailable )
        {
            registerTexture();
        }
        setImageIndex(-1);
    }
    
    private boolean initializeTexture(String relativePath, File file)
    {
        this.relativePath = relativePath;
        this.absolutePath = file.getAbsolutePath();
        this.fileName = file.getName();
        try
        {
            this.textureImage = ImageIO.read(file);
        }
        catch ( IOException e )
        {
            e.getStackTrace();
            return false;
        }
        if ( this.textureImage == null )
        {
            return false;
        }
        this.width = this.textureImage.getWidth();
        this.height = this.textureImage.getHeight();
        return true;
    }

    private void registerTexture()
    {
        TextureManager.getInstance().putTexture(getRelativePath(), this);
    }

    public String getRelativePath()
    {
        return this.relativePath;
    }

    public String getAbsolutePath()
    {
        return this.absolutePath;
    }

    public int getWidth()
    {
        return this.width;
    }
    
    public int getHeight()
    {
        return this.height;
    }

    public boolean isAvailable()
    {
        return this.isAvailable;
    }

    public void showTextureInformation()
    {
        Utility.printInformation("    AbsolutePath: " + this.getAbsolutePath());
        Utility.printInformation("    RelativePath: " + this.getRelativePath());
        Utility.printInformation("    Width       : " + this.getWidth());
        Utility.printInformation("    Height      : " + this.getHeight());
    }
    
    public BufferedImage getTextureImage()
    {
        return textureImage;
    }
    
    public void setTextureImage(BufferedImage textureImage)
    {
        this.textureImage = textureImage;
    }

    public int getImageIndex()
    {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex)
    {
        this.imageIndex = imageIndex;
    }
    
    public String getName()
    {
        return fileName;
    }
    
    public String getNameWithoutExtension()
    {
        return fileName.substring(0, fileName.length()-4);
    }
}
