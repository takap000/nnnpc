package takap.mods.nnnpc.entity;


public final class FormDefault extends FormBase
{
    private static FormDefault instance = new FormDefault();

    private FormDefault()
    {
        super("default", EntityDefault.class, ModelBipedNpc.class, RenderBipedNpc.class);
        setBaseSize(1f, 0.6f, 1.8f, 0.5f);
        setBaseTextureSize(64, 32);
        setIconInformation(8, 8, 8, 8);
        // 要調整
//        setItemOffset(0.0625f, 0.4375f, 0.0625f);
        setItemOffset(0.0625f, 0.375f, 0.0625f);
        setMoveSpeedLimit(0.01f, 0.4f);
    }

    public static FormDefault getInstance()
    {
        return instance;
    }

    @Override
    protected void setRoleList()
    {
        this.roleList.add(new RoleVillager());
        this.roleList.add(new RoleFollower());
        this.roleList.add(new RoleMedic());
    }
}
