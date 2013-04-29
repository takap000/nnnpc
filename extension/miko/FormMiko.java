package takap.mods.nnnpc.extension.miko;

import takap.mods.nnnpc.entity.FormBase;
import takap.mods.nnnpc.entity.RenderBipedNpc;
import takap.mods.nnnpc.entity.RoleFollower;
import takap.mods.nnnpc.entity.RoleMedic;
import takap.mods.nnnpc.entity.RoleVillager;

public final class FormMiko extends FormBase
{
    private static FormMiko instance = new FormMiko();

    private FormMiko()
    {
        super("miko", EntityMiko.class, ModelMiko.class, RenderBipedNpc.class);
        setBaseSize(1f, 0.4f, 1.35f, 0.4f);
        setBaseTextureSize(64, 32);
        setIconInformation(5, 5, 15, 25);
        // TODO: 要調整
//        setItemOffset(0.025f, 0.35f, 0.0625f);
        setItemOffset(0.03125f, 0.3125f, 0.0625f);
        setMoveSpeedLimit(0.01f, 0.4f);
    }

    public static FormMiko getInstance()
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
