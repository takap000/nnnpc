package takap.mods.nnnpc.extension.majokko;

import takap.mods.nnnpc.entity.FormBase;
import takap.mods.nnnpc.entity.RenderBipedNpc;
import takap.mods.nnnpc.entity.RoleFollower;
import takap.mods.nnnpc.entity.RoleMedic;
import takap.mods.nnnpc.entity.RoleVillager;

public final class FormMajokko extends FormBase
{
    private static FormMajokko instance = new FormMajokko();

    private FormMajokko()
    {
        super("majokko", EntityMajokko.class, ModelMajokko.class, RenderBipedNpc.class);
        setBaseSize(0.9f, 0.5f, 1.4f, 0.4f);
        setBaseTextureSize(64, 32);
        setIconInformation(5, 5, 15, 25);
        // TODO: 要調整
//        setItemOffset(0.025f, 0.35f, 0.0625f);
        setItemOffset(0.03125f, 0.3125f, 0.0625f);
        setMoveSpeedLimit(0.01f, 0.4f);
    }

    public static FormMajokko getInstance()
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
