package takap.mods.nnnpc.extension.slim;

import takap.mods.nnnpc.entity.FormBase;
import takap.mods.nnnpc.entity.RenderBipedNpc;
import takap.mods.nnnpc.entity.RoleFollower;
import takap.mods.nnnpc.entity.RoleMedic;
import takap.mods.nnnpc.entity.RoleVillager;

public final class FormSlim extends FormBase
{
    private static FormSlim instance = new FormSlim();

    private FormSlim()
    {
        super("slim", EntitySlim.class, ModelSlim.class, RenderBipedNpc.class);
        setBaseSize(0.8f, 0.6f, 1.8f, 0.5f);
        setBaseTextureSize(64, 64);
        setIconInformation(8, 8, 8, 8);
        // TODO: 要調整
//        setItemOffset(0f, 0.1875f, 0f);
        setItemOffset(0.0625f, 0.375f, 0.0625f);  // 0.0625f
        setMoveSpeedLimit(0.01f, 0.4f);
    }

    public static FormSlim getInstance()
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
