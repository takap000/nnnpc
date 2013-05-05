package takap.mods.nnnpc.gui;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSubpageRoleForMedic extends GuiSubpageBase
{
    private GuiUtilityButton thresholdPlusButton;
    private GuiUtilityButton thresholdMinusButton;
    private final int labelOffsetX = 10;
    private final int labelInitialOffsetY = 12;
    private final int buttonInitialOffsetY = 10;
    private final int valueOffsetX = 120;
    private final int lineHeight = 16;
    private final int buttonHeight = 12;
    private final int switchButtonWidth = 16;
    private final int stringValueLabelWidth = 100;
    private final int valueMarginX = 3;
    private final int stringValueWidth = this.stringValueLabelWidth - this.valueMarginX - this.valueMarginX;
    
    private enum EnumButton
    {
        HEAL_THRESHOLD_PLUS,
        HEAL_THRESHOLD_MINUS
    }
    
    public GuiSubpageRoleForMedic(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        super(mainPage, player, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, left, right);
    }

    @Override
    public void createButtonList()
    {
        int offsetY = this.buttonInitialOffsetY;
        this.thresholdPlusButton = new GuiUtilityButton(EnumButton.HEAL_THRESHOLD_PLUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "+");
        this.thresholdMinusButton = new GuiUtilityButton(EnumButton.HEAL_THRESHOLD_MINUS.ordinal(), this.valueOffsetX+this.stringValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, "-");
        
        offsetY += this.lineHeight;    // 次のパラメタ追加用
        
        addButtonControl(this.thresholdPlusButton);
        addButtonControl(this.thresholdMinusButton);
    }
    
    @Override
    public void drawForegroundLayer()
    {
//        drawString("Not used", this.labelOffsetX , this.labelInitialOffsetY, Utility.colorWhite, true);
        
        // Heal Threshold
        int offsetY = this.labelInitialOffsetY;
        drawString("Heal Threshold", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawCenteredString(String.valueOf(this.npc.getHealOtherThreshold()), this.valueOffsetX+this.valueMarginX, offsetY, this.stringValueWidth, Utility.colorWhite, false);
    }
    
    @Override
    public boolean actionPerformed(int id)
    {
        if ( id == EnumButton.HEAL_THRESHOLD_PLUS.ordinal() )
        {
            this.npc.increaseHealOtherThreshold(10);
            return true;
        }
        else if ( id == EnumButton.HEAL_THRESHOLD_MINUS.ordinal() )
        {
            this.npc.increaseHealOtherThreshold(-10);
            return true;
        }
        return false;
    }

    @Override
    public String getSubpageTitle()
    {
        return "Role";
    }

    @Override
    public boolean keyTyped(char par1, int par2)
    {
        return false;
    }

    @Override
    public boolean updateScreen()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(int x, int y, int z)
    {
        return false;
    }
}
