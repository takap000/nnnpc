package takap.mods.nnnpc.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import takap.mods.nnnpc.location.NamedLocation;
import takap.mods.nnnpc.location.RecordedLocation;
import takap.mods.nnnpc.texture.Texture;
import takap.mods.nnnpc.utility.ScreenShotHelper;
import takap.mods.nnnpc.utility.Utility;

public class GuiLocationRecorder extends GuiScreen
{
    // 右クリックされた地点の座標
    private int posX;
    private int posY;
    private int posZ;
    
    private BufferedImage tempImage;
    private int tempImageIndex;
    
    // コントロール関係
    private GuiTextField newLocationTextField;
    private GuiTextField loadedLocationTextField;
    private GuiButton okButton;
    private GuiButton cancelButton;
    private GuiUtilityButton previousButton;
    private GuiUtilityButton nextButton;
    
    // GUIの縁
    private int guiTop;
    @SuppressWarnings("unused")
    private int guiBottom;
    private int guiLeft;
    private int guiRight;
    
    // サイズやoffsetの固定値
    private final int xSize = 396;
    private final int ySize = 216;
    private final int mainButtonWidth = 56;
    private final int mainButtonHeight = 20;
    private final int mainButtonMarginTop = 20;
    private final int mainButtonHorizontalInterval = 32;
    private final int subButtonWidth = 24;
    private final int subButtonHeight = 12;
    private final int subButtonMarginBottom = 12;
    private final int imageWidth = 176;
    private final int imageHeight = 96;
    private final int textFieldWidth = 104;
    private final int textFieldHeight = 12;
    private final int textFieldMarginTop = 24;
    private final int slotLabelWidth = 32;
    private final int slotLabelMarginX = 4;
    private final int slotLabelOffsetYAdjuster = 2;
    private final int imageOffsetYAdjuster = 6;
    private final String newImageLabel = "New Location";
    private final String loadedImageLabel = "Save Slot";
    
    // サイズやoffset，計算するもの
    private int slotLabelOffsetX;
    private int slotLabelOffsetY;
    private int newLocationImageOffsetX;
    private int loadedLocationImageOffsetX;
    private int imageOffsetY;
    private int imageLabelOffsetY;
    private int newLocationImageLabelOffsetX;
    private int loadedLocationImageLabelOffsetX;
    
    private int locationIndex;
    
    public GuiLocationRecorder(int x, int y, int z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        Keyboard.enableRepeatEvents(true);
        this.buttonList = new ArrayList();
        this.locationIndex = 0;
        this.tempImageIndex = -1;
    }
    
    private enum EnumButtonId
    {
        OK,
        CANCEL,
        PREVIOUS,
        NEXT
    }
    
    @Override
    public void initGui()
    {
        this.tempImage = ScreenShotHelper.saveScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        
        this.buttonList.clear();
        super.initGui();
        this.guiTop = (this.height - this.ySize) / 2;
        this.guiBottom = this.guiTop + this.ySize;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiRight = this.guiLeft + this.xSize;
        
        // 画像関連offset
        int imageMarginX = (this.xSize-this.imageWidth*2)/4;
        this.newLocationImageOffsetX = this.guiLeft + imageMarginX;
        this.loadedLocationImageOffsetX = this.guiRight - imageMarginX - this.imageWidth;
        this.imageOffsetY = this.guiTop + (this.ySize-this.imageHeight)/2 - this.imageOffsetYAdjuster;
        // GuiTextField関連offset
        int textFieldMarginX = (this.xSize-this.textFieldWidth*2)/4;
        int newLocationTextFieldOffsetX = this.guiLeft + textFieldMarginX;
        int loadedLocationTextFieldOffsetX = this.guiRight - textFieldMarginX - this.textFieldWidth;
        int textFieldOffsetY = imageOffsetY + this.imageHeight + this.textFieldMarginTop;
        // スロット変更ボタン関連offset
        int previousButtonOffsetX = loadedLocationImageOffsetX;
        this.slotLabelOffsetX = previousButtonOffsetX + this.subButtonWidth + this.slotLabelMarginX;
        int nextButtonOffsetX = this.slotLabelOffsetX + this.slotLabelWidth + this.slotLabelMarginX;
        int subButtonOffsetY = imageOffsetY - this.subButtonMarginBottom - this.subButtonHeight;
        this.slotLabelOffsetY = subButtonOffsetY + this.slotLabelOffsetYAdjuster;
        // 画像のラベルのoffset
        this.newLocationImageLabelOffsetX = this.newLocationImageOffsetX;
        this.loadedLocationImageLabelOffsetX = this.loadedLocationImageOffsetX;
        this.imageLabelOffsetY = this.imageOffsetY;  // 画像にオーバレイ
        // "OK"，"Cancel"関連offset
        int okButtonOffsetX = this.guiLeft + (this.xSize - this.mainButtonHorizontalInterval)/2 - this.mainButtonWidth;
        int cancelButtonOffsetX = this.guiLeft + (this.xSize + this.mainButtonHorizontalInterval)/2;
        int mainButtonOffsetY = textFieldOffsetY + this.textFieldHeight + this.mainButtonMarginTop;
        
        this.previousButton = new GuiUtilityButton(EnumButtonId.PREVIOUS.ordinal(), previousButtonOffsetX, subButtonOffsetY, this.subButtonWidth, this.subButtonHeight, "<<");
        this.nextButton = new GuiUtilityButton(EnumButtonId.NEXT.ordinal(), nextButtonOffsetX, subButtonOffsetY, this.subButtonWidth, this.subButtonHeight, ">>");
        this.okButton = new GuiButton(EnumButtonId.OK.ordinal(), okButtonOffsetX, mainButtonOffsetY, this.mainButtonWidth, this.mainButtonHeight, "Save");
        this.cancelButton = new GuiButton(EnumButtonId.CANCEL.ordinal(), cancelButtonOffsetX, mainButtonOffsetY, this.mainButtonWidth, this.mainButtonHeight, "Cancel");
        
        this.newLocationTextField = new GuiTextField(this.fontRenderer, newLocationTextFieldOffsetX, textFieldOffsetY, this.textFieldWidth, this.textFieldHeight);
        this.newLocationTextField.setFocused(true);
        this.newLocationTextField.setText("");
        this.loadedLocationTextField = new GuiTextField(this.fontRenderer, loadedLocationTextFieldOffsetX, textFieldOffsetY, this.textFieldWidth, this.textFieldHeight);
        this.loadedLocationTextField.setFocused(false);
        NamedLocation loadedLocation = RecordedLocation.getInstance().getLocation(this.locationIndex);
        this.loadedLocationTextField.setText(loadedLocation!=null?loadedLocation.getName():"");
        
        this.buttonList.add(this.previousButton);
        this.buttonList.add(this.nextButton);
        this.buttonList.add(this.okButton);
        this.buttonList.add(this.cancelButton);
    }
    
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        drawString(this.fontRenderer, this.newImageLabel, this.newLocationImageLabelOffsetX, this.imageLabelOffsetY, Utility.colorWhite);
        drawString(this.fontRenderer, this.loadedImageLabel, this.loadedLocationImageLabelOffsetX, this.imageLabelOffsetY, Utility.colorWhite);
        drawCenteredString(String.format("%2d", this.locationIndex+1)+"/"+RecordedLocation.numberOfSlots, this.slotLabelOffsetX, this.slotLabelOffsetY, this.slotLabelWidth, Utility.colorWhite, true);
        this.newLocationTextField.drawTextBox();
        // 右側のテキストボックスは選択中スロットのlocationが非nullのときのみ表示
        if ( RecordedLocation.getInstance().getLocation(this.locationIndex) != null )
        {
            this.loadedLocationTextField.drawTextBox();
        }
    }
    
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        // 右クリック時にキャプチャした画像を左側に表示
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        if ( this.tempImageIndex < 0 )
        {
            RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
            this.tempImageIndex = renderEngine.allocateAndSetupTexture(tempImage);
            if ( this.tempImageIndex < 0 )
            {
                return;
            }
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.tempImageIndex);
        Minecraft.getMinecraft().renderEngine.resetBoundTexture();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(this.newLocationImageOffsetX, this.imageOffsetY+this.imageHeight, 0.0d, 0.0d, 1.0d);
        tessellator.addVertexWithUV(this.newLocationImageOffsetX+this.imageWidth, this.imageOffsetY+this.imageHeight, 0.0d, 1.0d, 1.0d);
        tessellator.addVertexWithUV(this.newLocationImageOffsetX+this.imageWidth, this.imageOffsetY, 0.0d, 1.0d, 0.0d);
        tessellator.addVertexWithUV(this.newLocationImageOffsetX, this.imageOffsetY, 0.0d, 0.0d, 0.0d);
        tessellator.draw();
        
        // 保存されているLocationの画像を表示
        Tessellator var2 = Tessellator.instance;
        NamedLocation location = RecordedLocation.getInstance().getLocation(this.locationIndex);
        Texture texture = RecordedLocation.getInstance().getTexture(this.locationIndex);
        if ( location == null || texture == null )
        {
            // TODO: Emptyの場合の画像を準備する
/*
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.tempFile.getImageIndex());
            var2.startDrawingQuads();
            var2.addVertexWithUV((double)this.newLocationImageOffsetX, (double)this.imageOffsetY+(double)this.imageHeight, 0.0d, 0.0d, 1.0d);
            var2.addVertexWithUV((double)this.newLocationImageOffsetX+(double)this.imageWidth, (double)this.imageOffsetY+(double)this.imageHeight, 0.0D, 1.0d, 1.0d);
            var2.addVertexWithUV((double)this.newLocationImageOffsetX+(double)this.imageWidth, (double)this.imageOffsetY, 0.0d, 1.0d, 0.0d);
            var2.addVertexWithUV((double)this.newLocationImageOffsetX, (double)this.imageOffsetY, 0.0d, 0.0d, 0.0d);
            var2.draw();
*/
        }
        else
        {
            if ( texture.getImageIndex() < 0 )
            {
                RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
                int textureIndex = renderEngine.allocateAndSetupTexture(texture.getTextureImage());
                if ( textureIndex < 0 )
                {
                    return;
                }
//                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
//                renderEngine.resetBoundTexture();
                texture.setImageIndex(textureIndex);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getImageIndex());
            var2.startDrawingQuads();
            var2.addVertexWithUV(this.loadedLocationImageOffsetX, this.imageOffsetY+this.imageHeight, 0.0d, 0.0d, 1.0d);
            var2.addVertexWithUV(this.loadedLocationImageOffsetX+this.imageWidth, this.imageOffsetY+this.imageHeight, 0.0d, 1.0d, 1.0d);
            var2.addVertexWithUV(this.loadedLocationImageOffsetX+this.imageWidth, this.imageOffsetY, 0.0d, 1.0d, 0.0d);
            var2.addVertexWithUV(this.loadedLocationImageOffsetX, this.imageOffsetY, 0.0d, 0.0d, 0.0d);
            var2.draw();
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground(); // 黒のフィルタ
//        drawBackground(0); // 土ブロックの連続パターン
        GL11.glPushMatrix();
        this.drawGuiContainerBackgroundLayer(par3, par1, par2);
        GL11.glPopMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(par1, par2, par3);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        this.drawGuiContainerForegroundLayer(par1, par2);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if ( guiButton.id == EnumButtonId.OK.ordinal() )
        {
            RecordedLocation.getInstance().overwriteTexture(this.locationIndex, this.tempImage);
            RecordedLocation.getInstance().setLocation(this.locationIndex, new NamedLocation(this.newLocationTextField.getText(), String.valueOf(this.posX), String.valueOf(this.posY), String.valueOf(this.posZ)));
            closeScreen();
        }
        else if ( guiButton.id == EnumButtonId.CANCEL.ordinal() )
        {
            closeScreen();
        }
        else if ( guiButton.id == EnumButtonId.PREVIOUS.ordinal() )
        {
            this.locationIndex = (RecordedLocation.numberOfSlots+this.locationIndex-1) % RecordedLocation.numberOfSlots;
            NamedLocation loadedLocation = RecordedLocation.getInstance().getLocation(this.locationIndex);
            this.loadedLocationTextField.setText(loadedLocation!=null?loadedLocation.getName():"");
        }
        else if ( guiButton.id == EnumButtonId.NEXT.ordinal() )
        {
            this.locationIndex = (this.locationIndex+1) % RecordedLocation.numberOfSlots;
            NamedLocation loadedLocation = RecordedLocation.getInstance().getLocation(this.locationIndex);
            this.loadedLocationTextField.setText(loadedLocation!=null?loadedLocation.getName():"");
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        // Esc or inventory key
        if ( par2 == Keyboard.KEY_ESCAPE )
        {
            closeScreen();
        }
        
        else if ( this.newLocationTextField.isFocused() )
        {
            this.newLocationTextField.textboxKeyTyped(par1, par2);
        }
        else if ( this.loadedLocationTextField.isFocused() )
        {
            this.loadedLocationTextField.textboxKeyTyped(par1, par2);
            RecordedLocation.getInstance().getLocation(this.locationIndex).setName(this.loadedLocationTextField.getText());
        }
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }
    
    @Override
    public void mouseClicked(int x, int y, int z)
    {
        this.newLocationTextField.mouseClicked(x, y, z);
        this.loadedLocationTextField.mouseClicked(x, y, z);
        for ( int i=0; i<this.buttonList.size(); i++ )
        {
            GuiButton button = (GuiButton)this.buttonList.get(i);
            if (button.mousePressed(this.mc, x, y))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.actionPerformed(button);
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return true;
    }
    
    private void closeScreen()
    {
        this.mc.thePlayer.closeScreen();
    }
    
    public void drawCenteredString(String string, int posX, int posY, int width, int color, boolean isShadowed)
    {
        if ( isShadowed )
        {
            this.fontRenderer.drawStringWithShadow(string, posX+(width-this.fontRenderer.getStringWidth(string))/2, posY, color);
        }
        else
        {
            this.fontRenderer.drawString(string, posX+(width-this.fontRenderer.getStringWidth(string))/2, posY, color);
        }
    }
}
