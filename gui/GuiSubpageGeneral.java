package takap.mods.nnnpc.gui;

import org.lwjgl.input.Keyboard;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSubpageGeneral extends GuiSubpageBase
{
    private GuiTextField nameTextField;
    private GuiUtilityButton nameShowButton;
//    private GuiUtilityButton speedPlusPlusButton;
    private GuiUtilityButton speedPlusButton;
    private GuiUtilityButton speedMinusButton;
//    private GuiUtilityButton speedMinusMinusButton;
    private GuiUtilityButton respawnButton;
    private GuiUtilityButton respawnUpdateButton;
//    private GuiUtilityButton lifePlusPlusButton;
    private GuiUtilityButton lifePlusButton;
    private GuiUtilityButton lifeMinusButton;
//    private GuiUtilityButton lifeMinusMinusButton;
    private GuiUtilityButton lifeShowButton;
//    private GuiUtilityButton strengthPlusPlusButton;
    private GuiUtilityButton strengthPlusButton;
    private GuiUtilityButton strengthMinusButton;
//    private GuiUtilityButton strengthMinusMinusButton;
    private GuiUtilityButton expButton;
    private final int lineHeight = 16;
    private final int labelOffsetX = 10;
    private final int valueOffsetX = 96;
    private final int defaultValueLabelWidth = 48;
    private final int valueMarginX = 3;
    private final int valueWidth = this.defaultValueLabelWidth - this.valueMarginX - this.valueMarginX;
    private final int nameFieldWidth = 160;
    private final int defaultRespawnUpdateTick = 60;
    private int respawnUpdateTick;
    
    private enum EnumButton
    {
        NAME_SHOW,
        SPEED_PLUSPLUS,
        SPEED_PLUS,
        SPEED_MINUS,
        SPEED_MINUSMINUS,
        RESPAWN,
        RESPAWN_UPDATE,
        MAXLIFE_PLUSPLUS,
        MAXLIFE_PLUS,
        MAXLIFE_MINUS,
        MAXLIFE_MINUSMINUS,
        LIFE_SHOW,
        STRENGTH_PLUSPLUS,
        STRENGTH_PLUS,
        STRENGTH_MINUS,
        STRENGTH_MINUSMINUS,
        EXP
    }
    
    public GuiSubpageGeneral(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        super(mainPage, player, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, left, right);
        this.respawnUpdateTick = -1;
    }

    @Override
    public void createButtonList()
    {
        int yOffset;
        
        // TODO: 目盛操作用ボタンまわり, 使い勝手に問題がなければ"<<"と">>"の記述を削除する
        // ボタン作成
        // name
        yOffset = 10;
        this.nameTextField = new GuiTextField(this.mc.fontRenderer, this.valueOffsetX-31, yOffset, this.nameFieldWidth, 12);
        this.nameTextField.setEnableBackgroundDrawing(true);
        this.nameTextField.setMaxStringLength(this.npc.getMaxNameLength());
        this.nameShowButton = new GuiUtilityButton(EnumButton.NAME_SHOW.ordinal(), this.width-42, yOffset, 32, 12, this.npc.isNameVisible()?"Show":"Hide");
        
        // speed
        yOffset += this.lineHeight;
/*  // "<",">"表示の場合
        this.speedPlusPlusButton = new GuiUtilityButton(EnumButton.SPEED_PLUSPLUS.ordinal(), valueOffsetX-31, yOffset, 16, 12, "<<");
        this.speedPlusButton = new GuiUtilityButton(EnumButton.SPEED_PLUS.ordinal(), valueOffsetX-14, yOffset, 10, 12, "+");
        this.speedMinusButton = new GuiUtilityButton(EnumButton.SPEED_MINUS.ordinal(), valueOffsetX+this.defaultValueLabelWidth+4, yOffset, 10, 12, "-");
        this.speedMinusMinusButton = new GuiUtilityButton(EnumButton.SPEED_MINUSMINUS.ordinal(), valueOffsetX+this.defaultValueLabelWidth+15, yOffset, 16, 12, ">>");
*/
        // "+","-"表示
        this.speedPlusButton = new GuiUtilityButton(EnumButton.SPEED_PLUS.ordinal(), valueOffsetX-20, yOffset, 16, 12, "+");
        this.speedMinusButton = new GuiUtilityButton(EnumButton.SPEED_MINUS.ordinal(), valueOffsetX+this.defaultValueLabelWidth+4, yOffset, 16, 12, "-");
        
        // life
        yOffset += this.lineHeight;
/*  // "<",">"表示の場合
        this.lifePlusPlusButton = new GuiUtilityButton(EnumButton.MAXLIFE_PLUSPLUS.ordinal(), this.valueOffsetX-31, yOffset, 16, 12, "<<");
        this.lifePlusButton = new GuiUtilityButton(EnumButton.MAXLIFE_PLUS.ordinal(), this.valueOffsetX-14, yOffset, 10, 12, "<");
        this.lifeMinusButton = new GuiUtilityButton(EnumButton.MAXLIFE_MINUS.ordinal(), this.valueOffsetX+this.defaultValueLabelWidth+4, yOffset, 10, 12, ">");
        this.lifeMinusMinusButton = new GuiUtilityButton(EnumButton.MAXLIFE_MINUSMINUS.ordinal(), this.valueOffsetX+this.defaultValueLabelWidth+15, yOffset, 16, 12, ">>");
        this.lifeShowButton = new GuiUtilityButton(EnumButton.LIFE_SHOW.ordinal(), this.width-42, yOffset, 32, 12, this.npc.isLifeVisible()?"Show":"Hide");
*/
        // "+","-"表示
        this.lifePlusButton = new GuiUtilityButton(EnumButton.MAXLIFE_PLUS.ordinal(), this.valueOffsetX-20, yOffset, 16, 12, "+");
        this.lifeMinusButton = new GuiUtilityButton(EnumButton.MAXLIFE_MINUS.ordinal(), this.valueOffsetX+this.defaultValueLabelWidth+4, yOffset, 16, 12, "-");
        this.lifeShowButton = new GuiUtilityButton(EnumButton.LIFE_SHOW.ordinal(), this.width-42, yOffset, 32, 12, this.npc.isLifeVisible()?"Show":"Hide");
        
        // strength
        yOffset += this.lineHeight;
/*  // "<",">"表示の場合
        this.strengthPlusPlusButton = new GuiUtilityButton(EnumButton.STRENGTH_PLUSPLUS.ordinal(), this.valueOffsetX-31, yOffset, 16, 12, "<<");
        this.strengthPlusButton = new GuiUtilityButton(EnumButton.STRENGTH_PLUS.ordinal(), this.valueOffsetX-14, yOffset, 10, 12, "<");
        this.strengthMinusButton = new GuiUtilityButton(EnumButton.STRENGTH_MINUS.ordinal(), this.valueOffsetX+this.defaultValueLabelWidth+4, yOffset, 10, 12, ">");
        this.strengthMinusMinusButton = new GuiUtilityButton(EnumButton.STRENGTH_MINUSMINUS.ordinal(), this.valueOffsetX+this.defaultValueLabelWidth+15, yOffset, 16, 12, ">>");
*/
        // "+","-"表示
        this.strengthPlusButton = new GuiUtilityButton(EnumButton.STRENGTH_PLUS.ordinal(), this.valueOffsetX-20, yOffset, 16, 12, "+");
        this.strengthMinusButton = new GuiUtilityButton(EnumButton.STRENGTH_MINUS.ordinal(), this.valueOffsetX+this.defaultValueLabelWidth+4, yOffset, 16, 12, "-");
        
        // respawn
        yOffset += this.lineHeight;
// x座標変更, 仮
//        this.respawnButton = new GuiUtilityButton(EnumButton.RESPAWN.ordinal(), this.valueOffsetX, yOffset, this.defaultValueLabelWidth, 12, this.npc.isRespawnable()?"Enable":"Disable");
        this.respawnButton = new GuiUtilityButton(EnumButton.RESPAWN.ordinal(), this.valueOffsetX-20, yOffset, this.defaultValueLabelWidth, 12, this.npc.isRespawnable()?"Enable":"Disable");
        this.respawnUpdateButton = new GuiUtilityButton(EnumButton.RESPAWN_UPDATE.ordinal(), this.valueOffsetX-20+this.defaultValueLabelWidth+4, yOffset, this.defaultValueLabelWidth, 12, "Update");
        
        // exp
        yOffset += this.lineHeight;
        this.expButton = new GuiUtilityButton(EnumButton.EXP.ordinal(), this.valueOffsetX-20, yOffset, this.defaultValueLabelWidth, 12, this.npc.canGenerateExp()?"Enable":"Disable");
        
        // ボタン登録
        // name
        addButtonControl(this.nameShowButton);
        // speed
//        addButtonControl(this.speedPlusPlusButton);
        addButtonControl(this.speedPlusButton);
        addButtonControl(this.speedMinusButton);
//        addButtonControl(this.speedMinusMinusButton);
        // life
//        addButtonControl(this.lifePlusPlusButton);
        addButtonControl(this.lifePlusButton);
        addButtonControl(this.lifeMinusButton);
//        addButtonControl(this.lifeMinusMinusButton);
        addButtonControl(this.lifeShowButton);
        // strength
//        addButtonControl(this.strengthPlusPlusButton);
        addButtonControl(this.strengthPlusButton);
        addButtonControl(this.strengthMinusButton);
//        addButtonControl(this.strengthMinusMinusButton);
        // respawn
        addButtonControl(this.respawnButton);
        addButtonControl(this.respawnUpdateButton);
        // exp
        addButtonControl(this.expButton);
    }
    
    @Override
    public void drawForegroundLayer()
    {
        int offsetY;
        
        // y座標はボタンのy座標+2がよさげ
        // name
        offsetY = 10+2;
        drawString("Name", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        this.nameTextField.setText(npc.getNpcName());
        // speed
        offsetY += this.lineHeight;
        drawString("Speed", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawRightAlignedString(String.format("%1.2f", this.npc.getMoveSpeed()), this.valueOffsetX+this.valueMarginX, offsetY, this.valueWidth, Utility.colorWhite, true);
        // Life
        offsetY += this.lineHeight;
        drawString("MaxLife", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawRightAlignedString(String.valueOf(this.npc.getMaxHealth()), this.valueOffsetX+this.valueMarginX, offsetY, this.valueWidth, Utility.colorWhite, true);
        // Strength
        offsetY += this.lineHeight;
        drawString("Strength", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawRightAlignedString(String.valueOf(this.npc.getAttackStrength()), this.valueOffsetX+this.valueMarginX, offsetY, this.valueWidth, Utility.colorWhite, true);
        // Respawn
        offsetY += this.lineHeight;
        drawString("Respawn", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        if ( this.respawnUpdateTick > 0 )
        {
            int offsetX = this.valueOffsetX + this.defaultValueLabelWidth + this.defaultValueLabelWidth + 4;
            drawString("updated!", offsetX, offsetY, Utility.colorRed, true);
            this.respawnUpdateTick--;
        }
        // Exp
        offsetY += this.lineHeight;
        drawString("Drop Exp", this.labelOffsetX, offsetY, Utility.colorWhite, true);
    }
    
    @Override
    public void drawBackgroundLayer()
    {
        super.drawBackgroundLayer();
        this.nameTextField.drawTextBox();
    }
    
    @Override
    public boolean actionPerformed(int id)
    {
        if ( id == EnumButton.NAME_SHOW.ordinal() )
        {
            this.npc.switchNameVisibility();
            this.nameShowButton.displayString = this.npc.isNameVisible()?"Show":"Hide";
            return true;
        }
        else if ( id == EnumButton.SPEED_PLUSPLUS.ordinal() )
        {
            this.npc.increaseMoveSpeed(0.1f);
            return true;
        }
        else if ( id == EnumButton.SPEED_PLUS.ordinal() )
        {
            this.npc.increaseMoveSpeed(0.01f);
            return true;
        }
        else if ( id == EnumButton.SPEED_MINUS.ordinal() )
        {
            this.npc.increaseMoveSpeed(-0.01f);
            return true;
        }
        else if ( id == EnumButton.SPEED_MINUSMINUS.ordinal() )
        {
            this.npc.increaseMoveSpeed(-0.1f);
            return true;
        }
        else if ( id == EnumButton.RESPAWN.ordinal() )
        {
            this.npc.switchRespawnable();
            this.respawnButton.displayString = this.npc.isRespawnable()?"Enable":"Disable";
            return true;
        }
        else if ( id == EnumButton.RESPAWN_UPDATE.ordinal() )
        {
            // 地面埋まり防止
            this.npc.updateSpawnCoordinate(this.npc.posX, this.npc.posY+0.5f, this.npc.posZ);
            this.respawnUpdateTick = this.defaultRespawnUpdateTick;
            return true;
        }
        else if ( id == EnumButton.MAXLIFE_PLUSPLUS.ordinal() )
        {
            this.npc.increaseMaxHealth(10);
            return true;
        }
        else if ( id == EnumButton.MAXLIFE_PLUS.ordinal() )
        {
            this.npc.increaseMaxHealth(1);
            return true;
        }
        else if ( id == EnumButton.MAXLIFE_MINUS.ordinal() )
        {
            this.npc.increaseMaxHealth(-1);
            return true;
        }
        else if ( id == EnumButton.MAXLIFE_MINUSMINUS.ordinal() )
        {
            this.npc.increaseMaxHealth(-10);
            return true;
        }
        else if ( id == EnumButton.LIFE_SHOW.ordinal() )
        {
            this.npc.switchLifeVisibility();
            this.lifeShowButton.displayString = this.npc.isLifeVisible()?"Show":"Hide";
            return true;
        }
        else if ( id == EnumButton.STRENGTH_PLUSPLUS.ordinal() )
        {
            this.npc.increaseAttackStrength(10);
            return true;
        }
        else if ( id == EnumButton.STRENGTH_PLUS.ordinal() )
        {
            this.npc.increaseAttackStrength(1);
            return true;
        }
        else if ( id == EnumButton.STRENGTH_MINUS.ordinal() )
        {
            this.npc.increaseAttackStrength(-1);
            return true;
        }
        else if ( id == EnumButton.STRENGTH_MINUSMINUS.ordinal() )
        {
            this.npc.increaseAttackStrength(-10);
            return true;
        }
        else if ( id == EnumButton.EXP.ordinal() )
        {
            this.npc.switchExpDrop();
            this.expButton.displayString = this.npc.canGenerateExp()?"Enable":"Disable";
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public String getSubpageTitle()
    {
        return "General";
    }

    @Override
    public boolean mouseClicked(int par1, int par2, int par3)
    {
        // subpageのoffset分も加味する
        this.nameTextField.mouseClicked(par1-this.left, par2-this.top, par3);
        return false;
    }
    
    @Override
    public boolean updateScreen()
    {
        this.nameTextField.updateCursorCounter();
        return false;
    }

    @Override
    public boolean keyTyped(char par1, int par2)
    {
        if ( par2 == Keyboard.KEY_ESCAPE )
        {
            return false;
        }
        else if ( this.nameTextField.isFocused() )
        {
            this.nameTextField.textboxKeyTyped(par1, par2);
            this.npc.setNpcName(this.nameTextField.getText());
            return true;
        }
        return false;
    }
}
