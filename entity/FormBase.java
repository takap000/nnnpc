package takap.mods.nnnpc.entity;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import takap.mods.nnnpc.mod_NNNPC;
import takap.mods.nnnpc.texture.Texture;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.world.World;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public abstract class FormBase {
    private String name;
    private Class entityClass;
    private Class modelClass;
    private Class rendererClass;
    
    private float shadowSize = 0.5f;

    private int baseTextureWidth = 64;
    private int baseTextureHeight = 32;
    private int iconTextureWidth = 0;
    private int iconTextureHeight = 0;
    private int iconTextureOffsetX = 0;
    private int iconTextureOffsetY = 0;
    
    private float itemOffsetX = 0f;
    private float itemOffsetY = 0f;
    private float itemOffsetZ = 0f;
    
    private float scaleWeight = 1f;
    private float baseHeight = 1.8f;
    private float baseWidth = 0.6f;
    
    private float minMoveSpeed = 0.4f;
    private float maxMoveSpeed = 0.01f;
    private float minScaleAmount = 0.5f;
    private float maxScaleAmount = 2.0f;
    
    private boolean isAvailable;
    private List<Texture> textureList;
    protected List<RoleBase> roleList;
//    private List accessoryList;
    protected List<String> dominantHandList;
    
    protected FormBase(String name, Class entity, Class model, Class renderer)
    {
        this.name = name;
        this.entityClass = entity;
        this.modelClass = model;
        this.rendererClass = renderer;
        initializeList();
        setRoleList();
    }
    
    protected void setBaseSize(float scaleWeight, float baseWidth, float baseHeight, float shadowSize)
    {
        this.scaleWeight = scaleWeight;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.shadowSize = shadowSize;
    }
    
    protected void setBaseTextureSize(int baseTextureWidth, int baseTextureHeight)
    {
        this.baseTextureWidth = baseTextureWidth;
        this.baseTextureHeight = baseTextureHeight;
    }
    
    protected void setIconInformation(int iconTextureWidth, int iconTextureHeight, int iconTextureOffsetX, int iconTextureOffsetY)
    {
        this.iconTextureWidth = iconTextureWidth;
        this.iconTextureHeight = iconTextureHeight;
        this.iconTextureOffsetX = iconTextureOffsetX;
        this.iconTextureOffsetY = iconTextureOffsetY;
    }
    
    protected void setItemOffset(float offsetX, float offsetY, float offsetZ)
    {
        this.itemOffsetX = offsetX;
        this.itemOffsetY = offsetY;
        this.itemOffsetZ = offsetZ;
    }
    
    protected void setMoveSpeedLimit(float minMoveSpeed, float maxMoveSpeed)
    {
        this.minMoveSpeed = minMoveSpeed;
        this.maxMoveSpeed = maxMoveSpeed;
    }
    
    private void initializeList()
    {
        this.isAvailable = false;
        this.textureList = new ArrayList<Texture>();
        this.roleList = new ArrayList<RoleBase>();
//        this.accessoryList = new ArrayList();
        this.dominantHandList = new ArrayList<String>();
        String path = new StringBuilder(mod_NNNPC.textureDirectoryPath).append("/").append(this.getName()).toString();
        File directory = new File(path);
        if ( !directory.exists() || !directory.isDirectory() )
        {
            return;
        }
        
        // textureのlist作成
        File[] files = directory.listFiles();
        if ( files == null || files.length == 0 )
        {
            return;
        }
        int numberOfFiles = files.length;
        for ( int i=0; i<numberOfFiles; i++ )
        {
            String relativePath = new StringBuilder(this.getName()).append("/").append(files[i].getName()).toString();
            Texture texture = new Texture(relativePath, files[i]);
            if ( texture.isAvailable() )
            {
                this.textureList.add(texture);
            }
        }
        if ( getNumberOfTextures() <= 0 )
        {
            return;
        }
        initializeDominantHandList();
        this.isAvailable = true;
    }
    
    public void initializeDominantHandList()
    {
        this.dominantHandList.add(Utility.rightHanded);
        this.dominantHandList.add(Utility.leftHanded);
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public float getShadowSize()
    {
        return this.shadowSize;
    }
    
    public Class getEntityClass()
    {
        return this.entityClass;
    }
    
    public Class getModelClass()
    {
        return this.modelClass;
    }
    
    public Class getRendererClass()
    {
        return this.rendererClass;
    }
    
    protected RenderLiving getRenderer()
    {
        try {
            Class[] parameters = new Class[]{ ModelBase.class, Float.TYPE };
            Constructor constructor = rendererClass.getConstructor(parameters);
            return (RenderLiving)constructor.newInstance(modelClass.newInstance(), getShadowSize());
        }
        catch ( Exception e ) {
            Utility.printWarning("failed to get constructor in getRenderer() @ CustomableNpcModel");
            return null;
        }
    }

    public boolean isAvailable()
    {
        if ( !this.isAvailable )
        {
            // TODO: テクスチャがなくても使用不能にしないで，デフォルトテクスチャをmod内に配置し，
            //       それを利用するようにする？
            return false;
        }
        
        return true;
    }
    
    public void registerForm()
    {
        EntityRegistry.registerModEntity(getEntityClass(), getName(), FormManager.getInstance().getFormIndex(getName()), mod_NNNPC.instance, 128, 1, true);
        RenderingRegistry.registerEntityRenderingHandler(getEntityClass(), getRenderer());
    }
    
    protected abstract void setRoleList();
    
    public RoleBase getRole(int i)
    {
        if ( i >= 0 && i < getNumberOfRoles() )
        {
            return this.roleList.get(i);
        }
        return null;
    }
    
    public RoleBase getRoleByName(String name)
    {
        int size = getNumberOfRoles();
        for ( int i=0; i<size; i++ )
        {
            RoleBase role = getRole(i);
            if ( role != null && role.getName().equals(name) )
            {
                return role;
            }
        }
        return null;
    }
    
    public int getRoleIndex(String name)
    {
        int size = getNumberOfRoles();
        for ( int i=0; i<size; i++ )
        {
            RoleBase role = getRole(i);
            if ( role != null && role.getName().equals(name) )
            {
                return i;
            }
        }
        return -1;
    }
    
    public RoleBase getPreviousRole(String name)
    {
        int index = getRoleIndex(name);
        if ( index == -1 )
        {
            return getDefaultRole();
        }
        int numberOfRoles = getNumberOfRoles();
        return getRole((index-1+numberOfRoles) % numberOfRoles);
    }
    
    public RoleBase getNextRole(String name)
    {
        int index = getRoleIndex(name);
        if ( index == -1 )
        {
            return getDefaultRole();
        }
        return getRole((index+1) % getNumberOfRoles());
    }
    
    public int getNumberOfRoles()
    {
        return this.roleList.size();
    }
    
    public Texture getTexture(int i)
    {
        if ( i >= 0 && i < getNumberOfTextures() )
        {
            return this.textureList.get(i);
        }
        return null;
    }
    
    public int getNumberOfTextures()
    {
        return this.textureList.size();
    }
    
    public int getTextureIndex(String name)
    {
        int size = getNumberOfTextures();
        for ( int i=0; i<size; i++ )
        {
            Texture texture = this.textureList.get(i);
            if ( texture.getRelativePath().equals(name) )
            {
                return i;
            }
        }
        return -1;
    }
    
    public int getTextureIndex(Texture texture)
    {
        int size = getNumberOfTextures();
        for ( int i=0; i<size; i++ )
        {
            if ( texture.equals(this.textureList.get(i)) )
            {
                return i;
            }
        }
        return -1;
    }
    
    public Texture getPreviousTexture(String name)
    {
        int index = getTextureIndex(name);
        if ( index == -1 )
        {
            return null;
        }
        int size = getNumberOfTextures();
        return this.textureList.get((index+size-1)%size);
    }
    
    public Texture getNextTexture(String name)
    {
        int index = getTextureIndex(name);
        if ( index == -1 )
        {
            return null;
        }
        return this.textureList.get((index+1)%getNumberOfTextures());
    }
    
    public EntityNpc getDefaultNpc(World world, double posX, double posY, double posZ, float yaw, float pitch)
    {
        EntityNpc npc = null;
        try
        {
            Class[] parameters = new Class[]{ World.class };
            Constructor constructor = getEntityClass().getConstructor(parameters);
            Utility.printInformation("generate entity \"" + this.getName() + "\" @ FormBase");
            npc = (EntityNpc)constructor.newInstance(world);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Utility.printWarning("failed to get constructor in getEntity() @ FormBase");
        }
        // TODO: 警告出力，呼び出し元でも何か処理？
        if ( !npc.setForm(this) )
        {
            return null;
        }
        npc.setLocationAndAngles(posX, posY, posZ, yaw, pitch);
        npc.updateSpawnCoordinate(posX, posY, posZ);
        npc.generateNpcId();
        
        return npc;
    }
    
    public void showFormInformation()
    {
        Utility.printInformation("Name    : " + getName());
        Utility.printInformation("Entity  : " + getEntityClass().getName());
        Utility.printInformation("Model   : " + getModelClass().getName());
        Utility.printInformation("Renderer: " + getRendererClass().getName());
        int size = this.textureList.size();
        Utility.printInformation("Texture : " + size + " file(s)");
        for ( int i=0; i<size; i++ )
        {
            this.textureList.get(i).showTextureInformation();
            Utility.printInformation("    --------------------");
        }
    }
    
    public int getIconTextureWidth()
    {
        return this.iconTextureWidth;
    }
    
    public int getIconTextureHeight()
    {
        return this.iconTextureHeight;
    }
    
    public int getIconTextureOffsetX()
    {
        return this.iconTextureOffsetX;
    }
    
    public int getIconTextureOffsetY()
    {
        return this.iconTextureOffsetY;
    }
    
    public int getBaseTextureWidth()
    {
        return this.baseTextureWidth;
    }
    
    public int getBaseTextureHeight()
    {
        return this.baseTextureHeight;
    }
    
    public float getItemOffsetX()
    {
        return this.itemOffsetX;
    }
    
    public float getItemOffsetY()
    {
        return this.itemOffsetY;
    }
    
    public float getItemOffsetZ()
    {
        return this.itemOffsetZ;
    }
    
    public float getBaseWidth()
    {
        return baseWidth;
    }

    public float getBaseHeight()
    {
        return baseHeight;
    }

    public RoleBase getDefaultRole()
    {
        if ( getNumberOfRoles() > 0 )
        {
            return getRole(0);
        }
        return null;
    }

    public Texture getDefaultTexture()
    {
        if ( getNumberOfTextures() > 0 )
        {
            return getTexture(0);
        }
        return null;
    }
    
    public float getMinMoveSpeed()
    {
        return minMoveSpeed;
    }
    
    public float getMaxMoveSpeed()
    {
        return maxMoveSpeed;
    }
    
    public float getScaleWeight()
    {
        return scaleWeight;
    }

    public float getMinScaleAmount()
    {
        return minScaleAmount;
    }

    public void setMinScaleAmount(float minScaleAmount)
    {
        this.minScaleAmount = minScaleAmount;
    }

    public float getMaxScaleAmount()
    {
        return maxScaleAmount;
    }

    public void setMaxScaleAmount(float maxScaleAmount)
    {
        this.maxScaleAmount = maxScaleAmount;
    }
    
    public String getDefaultDominantHand()
    {
        return this.dominantHandList.get(0);
    }
    
    public int getDominantHandListSize()
    {
        return this.dominantHandList.size();
    }
    
    public String getDominantHand(int index)
    {
        return this.dominantHandList.get(index);
    }
    
    public int getPreviousDominantHandIndex(int index)
    {
        int dominantHandListSize = getDominantHandListSize();
        return (index-1+dominantHandListSize)%dominantHandListSize;
    }
    
    public int getNextDominantHandIndex(int index)
    {
        return (index+1)%getDominantHandListSize();
    }
    
    public boolean canUseHands()
    {
        return IModelWithHands.class.isAssignableFrom(getModelClass());
    }
}
