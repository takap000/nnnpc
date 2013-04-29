package takap.mods.nnnpc.entity;

public enum EnumMode
{
    WAIT,
    SWIM,
    SIT,
    FREEDOM,
    FOLLOW,
    TRACEROUTE,
    WORK
    ;
    
    // Gui表示で利用
    public String getModeName()
    {
        return name().toLowerCase();
    }
}
