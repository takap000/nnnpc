package takap.mods.nnnpc.location;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

public class NamedLocation
{
    private String name;
    private String coordinateX;
    private String coordinateY;
    private String coordinateZ;
    private static int maxNameLength = 16;
    
    public NamedLocation()
    {
        this(null, null);
    }
    
    public NamedLocation(String name, ChunkCoordinates coordinate)
    {
        this.setName(name);
        this.setCoordinate(coordinate);
    }
    
    public NamedLocation(String name, String x, String y, String z)
    {
        this.setName(name);
        this.setCoordinateX(x);
        this.setCoordinateY(y);
        this.setCoordinateZ(z);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public boolean isAvailable()
    {
        try
        {
            int value;
            value = Integer.parseInt(this.coordinateX);
            value = Integer.parseInt(this.coordinateY);
            value = Integer.parseInt(this.coordinateZ);
            return true;
        }
        catch ( NumberFormatException e )
        {
            return false;
        }
    }
    
    public ChunkCoordinates getCoordinate()
    {
        try
        {
            int x = Integer.parseInt(this.coordinateX);
            int y = Integer.parseInt(this.coordinateY);
            int z = Integer.parseInt(this.coordinateZ);
            return new ChunkCoordinates(x, y, z);
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    public void setCoordinate(ChunkCoordinates coordinate)
    {
        if ( coordinate != null )
        {
            setCoordinateX(String.valueOf(coordinate.posX));
            setCoordinateY(String.valueOf(coordinate.posY));
            setCoordinateZ(String.valueOf(coordinate.posZ));
        }
    }

    public String getCoordinateX()
    {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX)
    {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY()
    {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY)
    {
        this.coordinateY = coordinateY;
    }

    public String getCoordinateZ()
    {
        return coordinateZ;
    }

    public void setCoordinateZ(String coordinateZ)
    {
        this.coordinateZ = coordinateZ;
    }

    public static int getMaxNameLength()
    {
        return maxNameLength;
    }
    
    public static NamedLocation loadLocationFromNBT(NBTTagCompound nbtTagCompound)
    {
        NamedLocation location = new NamedLocation();
        location.readFromNBT(nbtTagCompound);
        return location;
    }
    
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        setName(nbtTagCompound.getString("locationName"));
        setCoordinateX(nbtTagCompound.getString("locationX"));
        setCoordinateY(nbtTagCompound.getString("locationY"));
        setCoordinateZ(nbtTagCompound.getString("locationZ"));
    }
    
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setString("locationName", getName());
        nbtTagCompound.setString("locationX", getCoordinateX());
        nbtTagCompound.setString("locationY", getCoordinateY());
        nbtTagCompound.setString("locationZ", getCoordinateZ());
    }
}
