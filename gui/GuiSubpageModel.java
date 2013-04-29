package takap.mods.nnnpc.gui;

import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.FormManager;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSubpageModel extends GuiSubpageBase
{
    private GuiUtilityButton formPreviousButton;
    private GuiUtilityButton formNextButton;
    private GuiUtilityButton texturePreviousButton;
    private GuiUtilityButton textureNextButton;
    private GuiUtilityButton textureListButton;
    private GuiUtilityButton rolePreviousButton;
    private GuiUtilityButton roleNextButton;
    private GuiUtilityButton modePreviousButton;
    private GuiUtilityButton modeNextButton;
    private GuiUtilityButton scalePlusPlusButton;
    private GuiUtilityButton scalePlusButton;
    private GuiUtilityButton scaleMinusButton;
    private GuiUtilityButton scaleMinusMinusButton;
    private GuiUtilityButton dominantHandPreviousButton;
    private GuiUtilityButton dominantHandNextButton;
    private GuiUtilityButton textureListPreviousButton;
    private GuiUtilityButton textureListNextButton;
    private GuiUtilityButton[] textureElementButton;
    private final int lineHeight = 16;
    private final int buttonHeight = 12;
    private final int switchButtonWidth = 16;
    private final int listButtonWidth = 32;
    private final int labelOffsetX = 10;
    private final int valueOffsetX = 120;
    private final int stringValueLabelWidth = 100;
    private final int intValueLabelWidth = 28;
    private final int valueMarginX = 3;
    private final int stringValueWidth = this.stringValueLabelWidth - this.valueMarginX - this.valueMarginX;
    private final int intValueWidth = this.intValueLabelWidth - this.valueMarginX - this.valueMarginX;
    private final int indexLabelWidth = 80;
    private final int indexLabelOffsetX = 244;
    private int delayAfterClick;
    private final int defaultDelayAfterClick = 16;
    private final int elementButtonWidth = 160;
    private final int listPageLabelWidth = 100;
    private int leftColumnX;
    private int rightColumnX;
    
    private int listPage;
    private boolean isListMode;
    private final int numberOfElements = 20;
    
    private enum EnumButton
    {
        FORM_PREVIOUS,
        FORM_NEXT,
        TEXTURE_PREVIOUS,
        TEXTURE_NEXT,
        TEXTURE_LIST,
        TEXTURE_LIST_PREVIOUS,
        TEXTURE_LIST_NEXT,
        ROLE_PREVIOUS,
        ROLE_NEXT,
        MODE_PREVIOUS,
        MODE_NEXT,
        SCALE_PLUSPLUS,
        SCALE_PLUS,
        SCALE_MINUS,
        SCALE_MINUSMINUS,
        DOMINANTHAND_PREVIOUS,
        DOMINANTHAND_NEXT
    }
    
    public GuiSubpageModel(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        super(mainPage, player, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, left, right);
        this.isListMode = false;
        this.delayAfterClick = 0;
        updateOverwrappingButtons();
    }
    
    @Override
    public void createButtonList()
    {
        this.leftColumnX = (this.guiWidth - elementButtonWidth*2) / 4;
        this.rightColumnX = (this.guiWidth*3 - elementButtonWidth*2) / 4;
        
        int offsetY;
        
        // TODO: 目盛操作用ボタンまわり, 使い勝手に問題がなければ"<<"と">>"の記述を削除する
        // ボタン作成
        // name
        offsetY = 10;
        this.formPreviousButton = new GuiUtilityButton(this.numberOfElements+EnumButton.FORM_PREVIOUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "<");
        this.formNextButton = new GuiUtilityButton(this.numberOfElements+EnumButton.FORM_NEXT.ordinal(), this.valueOffsetX+this.stringValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, ">");
        // texture
        offsetY += this.lineHeight;
        this.texturePreviousButton = new GuiUtilityButton(this.numberOfElements+EnumButton.TEXTURE_PREVIOUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "<");
        this.textureNextButton = new GuiUtilityButton(this.numberOfElements+EnumButton.TEXTURE_NEXT.ordinal(), this.valueOffsetX+this.stringValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, ">");
        this.textureListButton = new GuiUtilityButton(this.numberOfElements+EnumButton.TEXTURE_LIST.ordinal(), this.textureNextButton.xPosition+20, offsetY, this.listButtonWidth, this.buttonHeight, "List");
        // role
        offsetY += this.lineHeight;
        this.rolePreviousButton = new GuiUtilityButton(this.numberOfElements+EnumButton.ROLE_PREVIOUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "<");
        this.roleNextButton = new GuiUtilityButton(this.numberOfElements+EnumButton.ROLE_NEXT.ordinal(), this.valueOffsetX+this.stringValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, ">");
        // mode
        offsetY += this.lineHeight;
        this.modePreviousButton = new GuiUtilityButton(this.numberOfElements+EnumButton.MODE_PREVIOUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "<");
        this.modeNextButton = new GuiUtilityButton(this.numberOfElements+EnumButton.MODE_NEXT.ordinal(), this.valueOffsetX+this.stringValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, ">");
        // dominant hand
        offsetY += this.lineHeight;
        this.dominantHandPreviousButton = new GuiUtilityButton(this.numberOfElements+EnumButton.DOMINANTHAND_PREVIOUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "<");
        this.dominantHandNextButton = new GuiUtilityButton(this.numberOfElements+EnumButton.DOMINANTHAND_NEXT.ordinal(), this.valueOffsetX+this.stringValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, ">");
        // scale
        offsetY += this.lineHeight;
/*  // "<<",">>"も表示する場合
        this.scalePlusPlusButton = new GuiUtilityButton(this.numberOfElements+EnumButton.SCALE_PLUSPLUS.ordinal(), this.valueOffsetX-31, yOffset, 16, 12, "++");
        this.scalePlusButton = new GuiUtilityButton(this.numberOfElements+EnumButton.SCALE_PLUS.ordinal(), this.valueOffsetX-14, yOffset, 10, 12, "+");
        this.scaleMinusButton = new GuiUtilityButton(this.numberOfElements+EnumButton.SCALE_MINUS.ordinal(), this.valueOffsetX+this.intValueLabelWidth+4, yOffset, 10, 12, "-");
        this.scaleMinusMinusButton = new GuiUtilityButton(this.numberOfElements+EnumButton.SCALE_MINUSMINUS.ordinal(), this.valueOffsetX+this.intValueLabelWidth+15, yOffset, 16, 12, "--");
*/
        // "+","-"のみ表示
        this.scalePlusButton = new GuiUtilityButton(this.numberOfElements+EnumButton.SCALE_PLUS.ordinal(), this.valueOffsetX-20, offsetY, this.switchButtonWidth, this.buttonHeight, "+");
        this.scaleMinusButton = new GuiUtilityButton(this.numberOfElements+EnumButton.SCALE_MINUS.ordinal(), this.valueOffsetX+this.intValueLabelWidth+4, offsetY, this.switchButtonWidth, this.buttonHeight, "-");
        
        // ボタン登録
        addButtonControl(this.formPreviousButton);
        addButtonControl(this.formNextButton);
        addButtonControl(this.texturePreviousButton);
        addButtonControl(this.textureNextButton);
        addButtonControl(this.textureListButton);
        addButtonControl(this.rolePreviousButton);
        addButtonControl(this.roleNextButton);
        addButtonControl(this.modePreviousButton);
        addButtonControl(this.modeNextButton);
        addButtonControl(this.dominantHandPreviousButton);
        addButtonControl(this.dominantHandNextButton);
//        addButtonControl(this.scalePlusPlusButton);
        addButtonControl(this.scalePlusButton);
        addButtonControl(this.scaleMinusButton);
//        addButtonControl(this.scaleMinusMinusButton);
        
        // オーバレイ表示用のボタン作成, 登録
        // ページ切り替え
        this.textureListPreviousButton = new GuiUtilityButton(this.numberOfElements+EnumButton.TEXTURE_LIST_PREVIOUS.ordinal(), (this.guiWidth-this.listPageLabelWidth)/2-20, 12, this.switchButtonWidth, this.buttonHeight, "<");
        this.textureListNextButton = new GuiUtilityButton(this.numberOfElements+EnumButton.TEXTURE_LIST_NEXT.ordinal(), (this.guiWidth+this.listPageLabelWidth)/2+4, 12, this.switchButtonWidth, this.buttonHeight, ">");
        addOverwrappingButtonControl(this.textureListPreviousButton);
        addOverwrappingButtonControl(this.textureListNextButton);
        // texture用個別ボタン
        this.textureElementButton = new GuiUtilityButton[this.numberOfElements];
        for ( int i=0,j=0,elementButtonY=48; i<this.numberOfElements/2; i++ )
        {
            j = i + this.numberOfElements/2;
            this.textureElementButton[i] = new GuiUtilityButton(i, this.leftColumnX, elementButtonY, this.elementButtonWidth, this.buttonHeight, "invalid value");
            this.textureElementButton[j] = new GuiUtilityButton(j, this.rightColumnX, elementButtonY, this.elementButtonWidth, this.buttonHeight, "invalid value");
            addOverwrappingButtonControl(this.textureElementButton[i]);
            addOverwrappingButtonControl(this.textureElementButton[j]);
            elementButtonY += 18;
        }
    }
    
    @Override
    public void drawForegroundLayer()
    {
        if ( this.delayAfterClick > 0 )
        {
            this.delayAfterClick--;
        }
        
        int offsetY;
        String formName = this.npc.getForm().getName();
        String textureName = this.npc.getNpcTexture().getNameWithoutExtension();
        String roleName = this.npc.getRole().getName();
        String modeName = this.npc.getMode().getModeName();
        String indexLabel;
        
        // model(form)
        offsetY = 12;  // 10+2
        indexLabel = (FormManager.getInstance().getFormIndex(formName)+1) + "/" + FormManager.getInstance().getNumberOfForms();
        drawString("Model", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawCenteredString(formName, this.valueOffsetX+this.valueMarginX, offsetY, this.stringValueWidth, Utility.colorWhite, false);
        drawRightAlignedString(indexLabel, this.indexLabelOffsetX, offsetY, this.indexLabelWidth, Utility.colorWhite, true);
        // texture
        offsetY += this.lineHeight;
        indexLabel = (this.npc.getForm().getTextureIndex(this.npc.getNpcTexture())+1) + "/" + this.npc.getForm().getNumberOfTextures();
        drawString("Texture", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawCenteredString(textureName, this.valueOffsetX+this.valueMarginX, offsetY, this.stringValueWidth, Utility.colorWhite, false);
        drawRightAlignedString(indexLabel, this.indexLabelOffsetX, offsetY, this.indexLabelWidth, Utility.colorWhite, true);
        // role
        offsetY += this.lineHeight;
        indexLabel = (this.npc.getForm().getRoleIndex(roleName)+1) + "/" + this.npc.getForm().getNumberOfRoles();
        drawString("Role", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawCenteredString(roleName, this.valueOffsetX+this.valueMarginX, offsetY, this.stringValueWidth, Utility.colorWhite, false);
        drawRightAlignedString(indexLabel, this.indexLabelOffsetX, offsetY, this.indexLabelWidth, Utility.colorWhite, true);
        // mode
        offsetY += this.lineHeight;
        indexLabel = (this.npc.getRole().getModeIndex(this.npc.getMode())+1) + "/" + this.npc.getRole().getNumberOfModes();
        drawString("Mode", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawCenteredString(modeName, this.valueOffsetX+this.valueMarginX, offsetY, this.stringValueWidth, Utility.colorWhite, false);
        drawRightAlignedString(indexLabel, this.indexLabelOffsetX, offsetY, this.indexLabelWidth, Utility.colorWhite, true);
        // dominant hand
        offsetY += this.lineHeight;
        drawString("Dominant Hand", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawCenteredString(this.npc.getDominantHand(), this.valueOffsetX+this.valueMarginX, offsetY, this.stringValueWidth, Utility.colorWhite, false);
        // scale
        offsetY += this.lineHeight;
        drawString("Scale", this.labelOffsetX, offsetY, Utility.colorWhite, true);
        drawRightAlignedString(String.format("%1.2f", this.npc.getScaleAmount()), this.valueOffsetX+this.valueMarginX, offsetY, this.intValueWidth, Utility.colorWhite, false);
    }
    
    @Override
    public void drawOverwrappingLayer()
    {
        if ( this.isListMode )
        {
            // 背面の表示をぼかす
            drawOverwrappingShade();
            
            // ラベル描画
            String label = (this.listPage+1) + " / " + (this.npc.getForm().getNumberOfTextures()/this.numberOfElements+(this.npc.getForm().getNumberOfTextures()%this.numberOfElements>0?1:0));
            drawCenteredString(label, this.guiWidth/2-50, 14, 100, Utility.colorWhite, false);
        }
    }
    
    @Override
    public boolean actionPerformed(int id)
    {
        // ボタンが重なっている場合，クリック時に連続でイベントが発生してしまうのを防止, "List"クリック時が対象
        if ( this.delayAfterClick > 0 )
        {
            return true;
        }
        
        if ( this.isListMode )
        {
            // ページ切り替え (前)
            if ( id == this.numberOfElements+EnumButton.TEXTURE_LIST_PREVIOUS.ordinal() )
            {
                int numberOfPages = this.npc.getForm().getNumberOfTextures()/this.numberOfElements+(this.npc.getForm().getNumberOfTextures()%this.numberOfElements>0?1:0);
                this.listPage = (this.listPage-1+numberOfPages) % numberOfPages;
                updateOverwrappingButtons();
                return true;
            }
            // ページ切り替え (次)
            else if ( id == this.numberOfElements+EnumButton.TEXTURE_LIST_NEXT.ordinal() )
            {
                this.listPage = (this.listPage+1) % (this.npc.getForm().getNumberOfTextures()/this.numberOfElements+(this.npc.getForm().getNumberOfTextures()%this.numberOfElements>0?1:0));
                updateOverwrappingButtons();
                return true;
            }
            // textureボタンのクリックイベントチェック
            else if ( id >= 0 && id < this.numberOfElements )
            {
                this.npc.setNpcTexture(this.npc.getForm().getTexture(id+this.numberOfElements*this.listPage));
                setListMode(false);
                return true;
            }
            // メニューボタンなどの無効化
            return true;
        }
        else
        {
            if ( id == this.numberOfElements+EnumButton.FORM_PREVIOUS.ordinal() )
            {
                // 新しいFormでNPCを生成
                EntityNpc newNpc = FormManager.getInstance().getPreviousForm(this.npc.getForm().getName()).getDefaultNpc(this.npc.worldObj, this.npc.posX, this.npc.posY, this.npc.posZ, 0f, 0f);
                // パラメタ移し替え & 親ページにNPC変更通知, NPCの差し替え処理はmainPage側で実施されるので，this.npcを更新する必要なし
                newNpc.copyCoreParameterFrom(this.npc);
                this.mainPage.switchNpc(newNpc);
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.FORM_NEXT.ordinal() )
            {
                // 新しいFormでNPCを生成
                EntityNpc newNpc = FormManager.getInstance().getNextForm(this.npc.getForm().getName()).getDefaultNpc(this.npc.worldObj, this.npc.posX, this.npc.posY, this.npc.posZ, 0f, 0f);
                // パラメタ移し替え & 親ページにNPC変更通知, NPCの差し替え処理はmainPage側で実施されるので，this.npcを更新する必要なし
                newNpc.copyCoreParameterFrom(this.npc);
                this.mainPage.switchNpc(newNpc);
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.TEXTURE_PREVIOUS.ordinal() )
            {
                this.npc.setNpcTexture(this.npc.getForm().getPreviousTexture(this.npc.getNpcTexture().getRelativePath()));
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.TEXTURE_NEXT.ordinal() )
            {
                this.npc.setNpcTexture(this.npc.getForm().getNextTexture(this.npc.getNpcTexture().getRelativePath()));
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.TEXTURE_LIST.ordinal() )
            {
                this.listPage = 0;
                setListMode(true);
                this.delayAfterClick = this.defaultDelayAfterClick;
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.ROLE_PREVIOUS.ordinal() )
            {
                this.npc.setRole(this.npc.getForm().getPreviousRole(this.npc.getRole().getName()));
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.ROLE_NEXT.ordinal() )
            {
                this.npc.setRole(this.npc.getForm().getNextRole(this.npc.getRole().getName()));
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.MODE_PREVIOUS.ordinal() )
            {
                this.npc.changePreviousMode();
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.MODE_NEXT.ordinal() )
            {
                this.npc.changeNextMode();
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.SCALE_PLUSPLUS.ordinal() )
            {
                this.npc.increaseScaleAmount(0.1f);
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.SCALE_PLUS.ordinal() )
            {
            	// "++"保留のため，暫定的に0.1操作
    //            this.npc.increaseScaleAmount(0.01f);
                this.npc.increaseScaleAmount(0.1f);
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.SCALE_MINUS.ordinal() )
            {
            	// "--"保留のため，暫定的に0.1操作
    //            this.npc.increaseScaleAmount(-0.01f);
                this.npc.increaseScaleAmount(-0.1f);
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.SCALE_MINUSMINUS.ordinal() )
            {
                this.npc.increaseScaleAmount(-0.1f);
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.DOMINANTHAND_PREVIOUS.ordinal() )
            {
                this.npc.setPreviousDominantHandIndex();
                return true;
            }
            else if ( id == this.numberOfElements+EnumButton.DOMINANTHAND_NEXT.ordinal() )
            {
                this.npc.setNextDominantHandIndex();
                return true;
            }
        }
        return false;
    }

    private void setListMode(boolean flag)
    {
        this.isListMode = flag;
        updateOverwrappingButtons();
    }
    
    private void updateOverwrappingButtons()
    {
        if ( this.isListMode )
        {
            this.textureListPreviousButton.drawButton = true;
            this.textureListNextButton.drawButton = true;
            for ( int i=0; i<this.numberOfElements; i++ )
            {
                if ( i + this.listPage*this.numberOfElements >= this.npc.getForm().getNumberOfTextures() )
                {
                    this.textureElementButton[i].drawButton = false;
                }
                else
                {
                    String textureFileName = this.npc.getForm().getTexture(i+this.numberOfElements*this.listPage).getName();
                    this.textureElementButton[i].displayString = textureFileName.substring(0, textureFileName.length()-4);
                    this.textureElementButton[i].drawButton = true;
                }
            }
        }
        else
        {
            this.textureListPreviousButton.drawButton = false;
            this.textureListNextButton.drawButton = false;
            for ( int i=0; i<this.numberOfElements; i++ )
            {
                this.textureElementButton[i].drawButton = false;
            }
        }
    }

    @Override
    public String getSubpageTitle()
    {
        return "General";
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
