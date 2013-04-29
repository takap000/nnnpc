package takap.mods.nnnpc.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.INPCAction;
import takap.mods.nnnpc.entity.NPCActionMoveTo;
import takap.mods.nnnpc.location.NamedLocation;
import takap.mods.nnnpc.location.RecordedLocation;
import takap.mods.nnnpc.texture.Texture;
import takap.mods.nnnpc.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSubpageLocation extends GuiSubpageBase
{
    private GuiTextField[] nameTextField;
    private GuiTextField[] coordinateXField;
    private GuiTextField[] coordinateYField;
    private GuiTextField[] coordinateZField;
    private GuiUtilityButton[] loadButton;
    private String[] locationStatus;
    private GuiUtilityButton loadOkButton;
    private GuiUtilityButton loadCancelButton;
    private GuiUtilityButton previousLocationButton;
    private GuiUtilityButton nextLocationButton;
    
    protected int locationSize;
    private final String locationNotUsed = "";
    private final String locationUsed = "used";
    private final String locationInvalid = "invalid";
    protected int savedLocationSize;
    private int locationIndex;
    
    // 配置位置関連，定数
    private final int topMargin = 6;
    private final int lineHeight = 15;
    private final int nameFieldWidth = 104;
    private final int coordinateFieldWidth = 39;
    private final int fieldInterval = 4;
    private final int labelFieldOffsetX = 5;
    private final int labelFieldWidth = 12;
    private final int locationStatusFieldWidth = 36;
    private final int loadButtonWidth = 36;
    private final int loadCancelButtonWidth = 48;
    private final int loadButtonHeight = 12;
    private final int imageWidth = 288;
    private final int imageHeight = 144;
    private final int loadButtonMarginX = 16;
    private final int loadButtonMarginTop = 8;
    private final int locationSelectButtonMarginX = 2;
    private final int locationSelectButtonWidth = 32;
    private final int locationSelectButtonHeight = 12;
    private final int locationSelectButtonMarginBottom = 4;
    private final int locationLabelMarginLeft = 12;
    // offset関連，通常時用
    private int nameFieldOffsetX;
    private int coordinateXFieldOffsetX;
    private int coordinateYFieldOffsetX;
    private int coordinateZFieldOffsetX;
    private int loadButtonOffsetX;
    private int locationStatusFieldOffsetX;
    private boolean isLoadMode;
    private int savedLocationButtonIdOffset;
    private int pushedLoadButtonId;
    // オーバレイ表示するコントロールのoffset関連
    private int locationImageOffsetX;
    private int locationImageOffsetY;
    private int locationLabelOffsetX;
    private int locationLabelOffsetY;
    
    private enum EnumButtonId
    {
        PREVIOUS,
        NEXT,
        OK,
        CANCEL
    }
    
    public GuiSubpageLocation(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        super(mainPage, player, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, left, right);
    }

    @Override
    public void createButtonList()
    {
        this.locationSize = EntityNpc.maxLocationLimit;
        this.nameTextField = new GuiTextField[this.locationSize];
        this.coordinateXField = new GuiTextField[this.locationSize];
        this.coordinateYField = new GuiTextField[this.locationSize];
        this.coordinateZField = new GuiTextField[this.locationSize];
        this.loadButton = new GuiUtilityButton[this.locationSize];
        this.locationStatus = new String[this.locationSize];
        for ( int i=0; i<this.locationSize; i++ )
        {
            this.locationStatus[i] = this.locationNotUsed;
        }
        
        this.savedLocationButtonIdOffset = this.locationSize;
        this.savedLocationSize = RecordedLocation.numberOfSlots;
        
        int yOffset;
        this.nameFieldOffsetX = this.labelFieldOffsetX + this.labelFieldWidth + this.fieldInterval;
        this.coordinateXFieldOffsetX = this.nameFieldOffsetX + this.nameFieldWidth + this.fieldInterval;
        this.coordinateYFieldOffsetX = this.coordinateXFieldOffsetX + this.coordinateFieldWidth + this.fieldInterval;
        this.coordinateZFieldOffsetX = this.coordinateYFieldOffsetX + this.coordinateFieldWidth + this.fieldInterval;
        this.loadButtonOffsetX = this.coordinateZFieldOffsetX + this.coordinateFieldWidth + this.fieldInterval;
        this.locationStatusFieldOffsetX = this.loadButtonOffsetX + this.loadButtonWidth + this.fieldInterval;
        
        // "Load"ボタン作成
        yOffset = this.topMargin;
        for ( int i=0; i<this.locationSize; i++ )
        {
            yOffset += this.lineHeight;
            this.nameTextField[i] = new GuiTextField(this.mc.fontRenderer, this.nameFieldOffsetX, yOffset, this.nameFieldWidth, 12);
            this.nameTextField[i].setEnableBackgroundDrawing(true);
            this.nameTextField[i].setMaxStringLength(NamedLocation.getMaxNameLength());
            this.coordinateXField[i] = new GuiTextField(this.mc.fontRenderer, this.coordinateXFieldOffsetX, yOffset, this.coordinateFieldWidth, 12);
            this.coordinateXField[i].setEnableBackgroundDrawing(true);
            this.coordinateYField[i] = new GuiTextField(this.mc.fontRenderer, this.coordinateYFieldOffsetX, yOffset, this.coordinateFieldWidth, 12);
            this.coordinateYField[i].setEnableBackgroundDrawing(true);
            this.coordinateZField[i] = new GuiTextField(this.mc.fontRenderer, this.coordinateZFieldOffsetX, yOffset, this.coordinateFieldWidth, 12);
            this.coordinateZField[i].setEnableBackgroundDrawing(true);
            this.loadButton[i] = new GuiUtilityButton(i, this.loadButtonOffsetX, yOffset, this.loadButtonWidth, 12, "Load");
            // ボタン登録
            addButtonControl(this.loadButton[i]);
        }
        
/*
        // locationボタン作成
        for ( int i=0; i<this.savedLocationSize; i++ )
        {
            // IDにはoffsetをつける点に注意!
            this.locationButton[i] = new GuiUtilityButton(i+this.savedLocationButtonIdOffset, this.loadButtonOffsetX, 40, this.loadButtonWidth, 12, "test");
            addOverwrappingButtonControl(this.locationButton[i]);
        }
*/
        // 座標Load時のコントロール関連, offset設定
        this.locationImageOffsetX = this.guiLeft + (this.guiWidth - this.imageWidth) / 2;
        this.locationImageOffsetY = this.guiTop + (this.guiHeight - this.imageHeight) / 2;
        int loadOkButtonOffsetX = this.guiLeft + (this.guiWidth - this.loadCancelButtonWidth*2 - this.loadButtonMarginX)/2;
        int loadCancelButtonOffsetX = loadOkButtonOffsetX + this.loadButtonMarginX + this.loadCancelButtonWidth;
        int loadButtonOffsetY = this.locationImageOffsetY + this.imageHeight + this.loadButtonMarginTop ;
        int previousLocationButtonOffsetX = this.locationImageOffsetX;
        int nextLocationButtonOffsetX = previousLocationButtonOffsetX + this.locationSelectButtonWidth + this.locationSelectButtonMarginX;
        this.locationLabelOffsetX = nextLocationButtonOffsetX + this.locationSelectButtonWidth + this.locationLabelMarginLeft;
        int locationSelectButtonOffsetY = this.locationImageOffsetY - this.locationSelectButtonMarginBottom - this.locationSelectButtonHeight;
        this.locationLabelOffsetY = locationSelectButtonOffsetY + 2;  // ラベル用微調整で+2
        // 座標Load時のコントロール関連, コントロール作成
        this.previousLocationButton = new GuiUtilityButton(this.savedLocationButtonIdOffset+EnumButtonId.PREVIOUS.ordinal(), previousLocationButtonOffsetX, locationSelectButtonOffsetY, this.locationSelectButtonWidth, this.locationSelectButtonHeight, "<<");
        this.nextLocationButton = new GuiUtilityButton(this.savedLocationButtonIdOffset+EnumButtonId.NEXT.ordinal(), nextLocationButtonOffsetX, locationSelectButtonOffsetY, this.locationSelectButtonWidth, this.locationSelectButtonHeight, ">>");
        this.loadOkButton = new GuiUtilityButton(this.savedLocationButtonIdOffset+EnumButtonId.OK.ordinal(), loadOkButtonOffsetX, loadButtonOffsetY, this.loadCancelButtonWidth, this.loadButtonHeight, "Load");
        this.loadCancelButton = new GuiUtilityButton(this.savedLocationButtonIdOffset+EnumButtonId.CANCEL.ordinal(), loadCancelButtonOffsetX, loadButtonOffsetY, this.loadCancelButtonWidth, this.loadButtonHeight, "Cancel");
        
        addOverwrappingButtonControl(this.loadOkButton);
        addOverwrappingButtonControl(this.loadCancelButton);
        addOverwrappingButtonControl(this.previousLocationButton);
        addOverwrappingButtonControl(this.nextLocationButton);
        
        setLoadMode(false);
    }
    
    @Override
    public void drawForegroundLayer()
    {
        int yOffset;
        NamedLocation[] locationList = this.npc.getLocationList();
        
        // locationの利用可否をチェック
        for ( int i=0; i<this.locationSize; i++ )
        {
            NamedLocation location = locationList[i];
            if ( location != null )
            {
                if ( location.isAvailable() )
                {
                    this.locationStatus[i] = this.locationNotUsed;
                    continue;
                }
            }
            this.locationStatus[i] = this.locationInvalid;
        }
        // locationが"Route"にて利用されているかチェック，無効なlocationの更新はしない
        for ( int i=0; i<EntityNpc.maxRouteLimit; i++ )
        {
            // "Enable"ボタンの状態チェック
            if ( this.npc.getNPCAction(i).isEnable() )
            {
                // "MoveTo"が選択されているかチェック
                INPCAction action = this.npc.getNPCAction(i).getCurrentAction();
                if ( action instanceof NPCActionMoveTo )
                {
                    // 参照しているindexのOBチェック
                    NPCActionMoveTo npcAction = (NPCActionMoveTo)action;
                    if ( npcAction.isAvailable() )
                    {
                        // 参照先locationに設定されている座標が適正かチェック
                        int locationIndex = npcAction.getLocationIndex();
                        if ( this.npc.getLocation(locationIndex).isAvailable() )
                        {
                            // おめでとう! 使用中です
                            this.locationStatus[npcAction.getLocationIndex()] = this.locationUsed;
                        }
                    }
                }
            }
        }
        
        // y座標はボタンのy座標+2がよさげ
        yOffset = this.topMargin+2;
        this.drawCenteredString("memo", this.nameFieldOffsetX, yOffset, this.nameFieldWidth, Utility.colorWhite, true);
        this.drawCenteredString("x", this.coordinateXFieldOffsetX, yOffset, this.coordinateFieldWidth, Utility.colorWhite, true);
        this.drawCenteredString("y", this.coordinateYFieldOffsetX, yOffset, this.coordinateFieldWidth, Utility.colorWhite, true);
        this.drawCenteredString("z", this.coordinateZFieldOffsetX, yOffset, this.coordinateFieldWidth, Utility.colorWhite, true);
        for ( int i=0; i<this.locationSize; i++ )
        {
            yOffset += this.lineHeight;
            drawRightAlignedString(String.valueOf(i+1), this.labelFieldOffsetX, yOffset, this.labelFieldWidth, Utility.colorWhite, true);
            drawCenteredString(this.locationStatus[i], this.locationStatusFieldOffsetX, yOffset, this.locationStatusFieldWidth, this.locationStatus[i].equals(this.locationInvalid)?Utility.colorRed:Utility.colorWhite, true);
            this.nameTextField[i].setText(locationList[i].getName());
            this.coordinateXField[i].setText(locationList[i].getCoordinateX());
            this.coordinateYField[i].setText(locationList[i].getCoordinateY());
            this.coordinateZField[i].setText(locationList[i].getCoordinateZ());
        }
    }
    
    @Override
    public void drawBackgroundLayer()
    {
        super.drawBackgroundLayer();
        for ( int i=0; i<this.locationSize; i++ )
        {
            this.nameTextField[i].drawTextBox();
            this.coordinateXField[i].drawTextBox();
            this.coordinateYField[i].drawTextBox();
            this.coordinateZField[i].drawTextBox();
        }
    }
    
    @Override
    public void drawOverwrappingLayer()
    {
        if ( this.isLoadMode )
        {
            // 背面の表示をぼかす
            drawOverwrappingShade();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            
            // 座標登録状況確認, 未登録の場合はloadボタンを非アクティブ化して終了
            Texture texture = RecordedLocation.getInstance().getTexture(this.locationIndex);
            NamedLocation location = RecordedLocation.getInstance().getLocation(this.locationIndex);
            if ( (texture==null) || (location==null) )
            {
                this.loadOkButton.setActive(false);
                return;
            }
            Tessellator tessellator = Tessellator.instance;
            if ( texture.getImageIndex() < 0 )
            {
                RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
                texture.setImageIndex(renderEngine.allocateAndSetupTexture(texture.getTextureImage()));
                if ( texture.getImageIndex() < 0 )
                {
                    this.loadOkButton.setActive(false);
                    return;
                }
//                renderEngine.bindTexture(texture.getImageIndex());
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
            }
            this.loadOkButton.setActive(true);
            
            // ラベル設定
            this.drawString(location.getName(), this.locationLabelOffsetX, this.locationLabelOffsetY, Utility.colorWhite, true);
            
            // 登録されているスクリーンショット描画
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)this.locationImageOffsetX, (double)this.locationImageOffsetY+(double)this.imageHeight, 0.0d, 0.0d, 1.0d);
            tessellator.addVertexWithUV((double)this.locationImageOffsetX+(double)this.imageWidth, (double)this.locationImageOffsetY+(double)this.imageHeight, 0.0D, 1.0d, 1.0d);
            tessellator.addVertexWithUV((double)this.locationImageOffsetX+(double)this.imageWidth, (double)this.locationImageOffsetY, 0.0d, 1.0d, 0.0d);
            tessellator.addVertexWithUV((double)this.locationImageOffsetX, (double)this.locationImageOffsetY, 0.0d, 0.0d, 0.0d);
            tessellator.draw();
        }
    }
    
    @Override
    public boolean actionPerformed(int id)
    {
        if ( (id>=this.locationSize) && this.isLoadMode )
        {
            int normalizedIndex = id - this.savedLocationButtonIdOffset;
            if ( normalizedIndex == EnumButtonId.PREVIOUS.ordinal() )
            {
                this.locationIndex = (this.locationIndex + RecordedLocation.numberOfSlots - 1) % RecordedLocation.numberOfSlots;
            }
            else if ( normalizedIndex == EnumButtonId.NEXT.ordinal() )
            {
                this.locationIndex = (this.locationIndex + 1) % RecordedLocation.numberOfSlots;
            }
            else if ( normalizedIndex == EnumButtonId.OK.ordinal() )
            {
                if ( this.loadOkButton.isActive() )
                {
                    NamedLocation location = RecordedLocation.getInstance().getLocation(this.locationIndex);
                    if ( location != null )
                    {
                        this.npc.setLocation(this.pushedLoadButtonId, location);
                    }
                    setLoadMode(false);
                }
            }
            else if ( normalizedIndex == EnumButtonId.CANCEL.ordinal() )
            {
                setLoadMode(false);
            }
            return true;
        }
        else if ( (id>=0) && (id<this.locationSize) && !this.isLoadMode )
        {
            this.pushedLoadButtonId = id;
            setLoadMode(true);
            return true;
        }
        return false;
    }
    
    public void setLoadMode(boolean flag)
    {
        this.isLoadMode = flag;
        this.locationIndex = 0;
        // オーバレイ表示のon/off切り替え
        this.loadOkButton.drawButton = this.isLoadMode;
        this.loadCancelButton.drawButton = this.isLoadMode;
        this.previousLocationButton.drawButton = this.isLoadMode;
        this.nextLocationButton.drawButton = this.isLoadMode;
    }
    
    @Override
    public String getSubpageTitle()
    {
        return "General";
    }

    @Override
    public boolean mouseClicked(int par1, int par2, int par3)
    {
        if ( this.isLoadMode )
        {
            return false;
        }
        
        for ( int i=0; i<this.locationSize; i++ )
        {
            this.nameTextField[i].mouseClicked(par1-this.left, par2-this.top, par3);
            this.coordinateXField[i].mouseClicked(par1-this.left, par2-this.top, par3);
            this.coordinateYField[i].mouseClicked(par1-this.left, par2-this.top, par3);
            this.coordinateZField[i].mouseClicked(par1-this.left, par2-this.top, par3);
        }
        return false;
    }
    
    @Override
    public boolean updateScreen()
    {
        if ( this.isLoadMode )
        {
            return true;
        }
        
        for ( int i=0; i<this.locationSize; i++ )
        {
            this.nameTextField[i].updateCursorCounter();
            this.coordinateXField[i].updateCursorCounter();
            this.coordinateYField[i].updateCursorCounter();
            this.coordinateZField[i].updateCursorCounter();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char par1, int par2)
    {
        // Loadモードの場合はEscのみが処理対象
        if ( this.isLoadMode )
        {
            if ( par2 == Keyboard.KEY_ESCAPE )
            {
                setLoadMode(false);
            }
            return true;
        }
        
        if ( par2 == Keyboard.KEY_ESCAPE )
        {
            return false;
        }
        else
        {
            for ( int i=0; i<this.locationSize; i++ )
            {
                if ( this.nameTextField[i].isFocused() )
                {
                    this.nameTextField[i].textboxKeyTyped(par1, par2);
                    this.npc.getLocationList()[i].setName(this.nameTextField[i].getText());
                    return true;
                }
                else if ( this.coordinateXField[i].isFocused() )
                {
                    this.coordinateXField[i].textboxKeyTyped(par1, par2);
                    this.npc.getLocationList()[i].setCoordinateX(this.coordinateXField[i].getText());
                    return true;
                }
                else if ( this.coordinateYField[i].isFocused() )
                {
                    this.coordinateYField[i].textboxKeyTyped(par1, par2);
                    this.npc.getLocationList()[i].setCoordinateY(this.coordinateYField[i].getText());
                    return true;
                }
                else if ( this.coordinateZField[i].isFocused() )
                {
                    this.coordinateZField[i].textboxKeyTyped(par1, par2);
                    this.npc.getLocationList()[i].setCoordinateZ(this.coordinateZField[i].getText());
                    return true;
                }
            }
        }
        return false;
    }
}
