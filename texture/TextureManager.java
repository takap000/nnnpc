package takap.mods.nnnpc.texture;

import java.util.HashMap;

public class TextureManager
{
    private static TextureManager instance = new TextureManager();
    private HashMap<String, Texture> textureMap;
    
    private TextureManager()
    {
        createTextureMap();
    }
    
    private void createTextureMap()
    {
        this.textureMap = new HashMap<String, Texture>();
    }
    
    public static TextureManager getInstance()
    {
        return instance;
    }
    
    public void putTexture(String key, Texture texture)
    {
        this.textureMap.put(key, texture);
    }
    
    public void updateTexture(String key, Texture texture)
    {
        this.textureMap.remove(key);
        putTexture(key, texture);
    }
    
    public Texture getTexture(String key)
    {
        return this.textureMap.get(key);
    }
}
