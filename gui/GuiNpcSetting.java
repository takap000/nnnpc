package takap.mods.nnnpc.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.packet.PacketNpcDeleteEvent;
import takap.mods.nnnpc.packet.PacketNpcGuiCloseEvent;
import takap.mods.nnnpc.packet.PacketNpcSettingEvent;
import takap.mods.nnnpc.packet.PacketNpcSpawnEvent;
import takap.mods.nnnpc.utility.Utility;

public class GuiNpcSetting extends GuiScreen
{
    protected float xSize_lo;
    protected float ySize_lo;
    protected EntityPlayer entityPlayer;
    protected EntityNpc npc;
    protected EntityNpc oldNpc;
    protected int updateCoordinateTick;
    protected int npcSearchTick;
    protected String npcId;
    
    protected int guiTop;
    protected int guiBottom;
    protected int guiLeft;
    protected int guiRight;
    
    private Contents selectedContents;
    private HashMap<Integer, Contents> contentsMap;
    
    protected final int xSize = 396;
    protected final int ySize = 216;
    protected final int menuButtonWidth = 48;
    protected final int menuButtonHeight = 20;
    protected final int subpageMarginX = 5;
    protected final int subpageMarginY = 5;
    protected final int menuButtonMarginX = 5;
    protected final int menuButtonMarginY = 5;
    protected final int menuButtonIdOffset = 128;
    
    private List<GuiButton> overwrappingButtonList;
    
    private enum EnumSubpage
    {
        GENERAL,
        MODEL,
        ROLE,
        LOCATION,
        DELETE;
    }
    
    private class Contents
    {
        private GuiMenuButton button;
        private GuiSubpageBase subpage;
        
        public Contents(GuiMenuButton button, GuiSubpageBase subpage)
        {
            this.button = button;
            this.subpage = subpage;
        }
        
        private GuiMenuButton getButton()
        {
            return button;
        }
        
        private GuiSubpageBase getSubpage()
        {
            return subpage;
        }
        
        private void setSubpage(GuiSubpageBase subpage)
        {
            this.subpage = subpage;
        }
    }
    
    public GuiNpcSetting(EntityPlayer entityplayer, EntityNpc npc)
    {
        this.npc = npc;
        this.oldNpc = null;
        this.entityPlayer = entityplayer;
        updateCoordinateTick = 0;
        npcSearchTick = 0;
        Keyboard.enableRepeatEvents(true);
        this.buttonList = new ArrayList();
        this.overwrappingButtonList = new ArrayList();
    }
    
    private void setContentsMap()
    {
        int selectedId = -1;
        if ( this.selectedContents != null )
        {
            selectedId = this.selectedContents.getButton().id - this.menuButtonIdOffset;
        }
        
        // 初期化時の他，Model変更でnpcが再生成されたときに呼び出すように
        this.contentsMap = new HashMap<Integer, Contents>();
        this.contentsMap.put(EnumSubpage.GENERAL.ordinal(), new Contents(new GuiMenuButton(EnumSubpage.GENERAL.ordinal() + menuButtonIdOffset, "General"), new GuiSubpageGeneral(this, this.entityPlayer, this.npc, this.mc, this.guiTop, this.guiBottom, this.guiLeft, this.guiRight, this.guiTop + subpageMarginY, this.guiBottom - subpageMarginY, this.guiLeft + subpageMarginX + menuButtonWidth + this.subpageMarginX, this.guiRight - this.subpageMarginX)));
        this.contentsMap.put(EnumSubpage.MODEL.ordinal(), new Contents(new GuiMenuButton(EnumSubpage.MODEL.ordinal() + menuButtonIdOffset, "Model"), new GuiSubpageModel(this, this.entityPlayer, this.npc, this.mc, this.guiTop, this.guiBottom, this.guiLeft, this.guiRight,  this.guiTop + subpageMarginY, this.guiBottom - subpageMarginY, this.guiLeft + subpageMarginX + menuButtonWidth + this.subpageMarginX, this.guiRight - this.subpageMarginX)));
        this.contentsMap.put(EnumSubpage.ROLE.ordinal(), new Contents(new GuiMenuButton(EnumSubpage.ROLE.ordinal() + menuButtonIdOffset, "Role"), new GuiSubpageRoleForRouteTracer(this, this.entityPlayer, this.npc, this.mc, this.guiTop, this.guiBottom, this.guiLeft, this.guiRight, this.guiTop + subpageMarginY, this.guiBottom - subpageMarginY, this.guiLeft + subpageMarginX + menuButtonWidth + this.subpageMarginX, this.guiRight - this.subpageMarginX)));
        this.contentsMap.put(EnumSubpage.LOCATION.ordinal(), new Contents(new GuiMenuButton(EnumSubpage.LOCATION.ordinal() + menuButtonIdOffset, "Location"), new GuiSubpageLocation(this, this.entityPlayer, this.npc, this.mc, this.guiTop, this.guiBottom, this.guiLeft, this.guiRight, this.guiTop + subpageMarginY, this.guiBottom - subpageMarginY, this.guiLeft + subpageMarginX + menuButtonWidth + this.subpageMarginX, this.guiRight - this.subpageMarginX)));
        this.contentsMap.put(EnumSubpage.DELETE.ordinal(), new Contents(new GuiMenuButton(EnumSubpage.DELETE.ordinal() + menuButtonIdOffset, "Delete"), new GuiSubpageGeneral(this, this.entityPlayer, this.npc, this.mc, this.guiTop, this.guiBottom, this.guiLeft, this.guiRight, this.guiTop + subpageMarginY, this.guiBottom - subpageMarginY, this.guiLeft + subpageMarginX + menuButtonWidth + this.subpageMarginX, this.guiRight - this.subpageMarginX)));
        
        if ( selectedId != -1 )
        {
            selectContents(selectedId);
        }
        else
        {
            selectContents(EnumSubpage.GENERAL.ordinal());
        }
    }
    
    private void selectContents(int id)
    {
    	for ( Map.Entry<Integer, Contents> entry : contentsMap.entrySet() )
        {
            Contents contents = entry.getValue();
            if ( contents == null )
            {
                break;
            }
            contents.getButton().setIsSelected(false);
        }
        this.selectedContents = this.contentsMap.get(id);
        if ( id == EnumSubpage.ROLE.ordinal() )
        {
            this.selectedContents.setSubpage(this.npc.getRole().getGuiSubpage(this, this.entityPlayer, this.npc, this.mc, this.guiTop, this.guiBottom, this.guiLeft, this.guiRight, this.guiTop + this.subpageMarginY, this.guiBottom - this.subpageMarginY, this.guiLeft + this.subpageMarginX + this.menuButtonWidth + this.subpageMarginX, this.guiRight - this.subpageMarginX));
        }
        this.selectedContents.getButton().setIsSelected(true);
    }
    
    private void updateSubpageNpc()
    {
        for ( Map.Entry<Integer, Contents> entry : contentsMap.entrySet() )
        {
            Contents contents = entry.getValue();
            if ( contents == null )
            {
                break;
            }
            contents.getSubpage().setNpc(this.npc);
        }
    }

    @Override
    public void initGui()
    {
        if ( this.npc == null )
        {
            this.mc.displayGuiScreen(null);
            return;
        }
        this.buttonList.clear();
        this.overwrappingButtonList.clear();
        super.initGui();
        this.guiTop = (this.height - this.ySize) / 2;
        this.guiBottom = this.guiTop + this.ySize;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiRight = this.guiLeft + this.xSize;
        setContentsMap();
        
        // メニュー部にsubpage間の遷移を行うためのボタンを追加
        int buttonPosX = this.guiLeft + this.menuButtonMarginX;
        int buttonPosY = this.guiTop + 30;
        for ( Map.Entry<Integer, Contents> entry : contentsMap.entrySet() )
        {
            Contents contents = entry.getValue();
            if ( contents == null )
            {
                // TODO: バグルート，警告出力追加
                break;
            }
            GuiMenuButton button = contents.getButton();
            if ( entry.getKey() == EnumSubpage.DELETE.ordinal() )
            {
                button.setButtonProperty(buttonPosX, this.guiBottom-menuButtonHeight-this.menuButtonMarginY, menuButtonWidth, menuButtonHeight);
                this.buttonList.add(button);
                continue;
            }
            button.setButtonProperty(buttonPosX, buttonPosY, menuButtonWidth, menuButtonHeight);
            this.buttonList.add(button);
            buttonPosY += this.menuButtonHeight;
        }
        
        // subpageのボタン追加
        this.buttonList.addAll(this.selectedContents.getSubpage().getButtonList());
        this.overwrappingButtonList.addAll(this.selectedContents.getSubpage().getOverwrappingButtonList());
    }
    
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        // TODO: メニュー部あたりを装飾する？
        
        // subpage用の領域へ描画
    	GuiSubpageBase subpage = this.selectedContents.getSubpage();
        subpage.translateToSubpageDomain();
        subpage.drawForegroundLayer();
        subpage.resetTranslation();
    }
    
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        // 共通処理，背景用画像の読み込み他
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator var2 = Tessellator.instance;
        this.mc.renderEngine.bindTexture(Utility.settingPageBackgroundImage);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float var3 = 16.0f;
        var2.startDrawingQuads();
        var2.addVertexWithUV(this.guiLeft, this.guiBottom, 0.0d, 0.0d, (this.ySize/var3 + 0.0f));
        var2.addVertexWithUV(this.guiRight, this.guiBottom, 0.0d, (this.xSize/var3), (this.ySize/var3 + 0.0f));
        var2.addVertexWithUV(this.guiRight, this.guiTop, 0.0d, (this.xSize/var3), 0.0f);
        var2.addVertexWithUV(this.guiLeft, this.guiTop, 0.0d, 0.0d, 0.0d);
        var2.draw();
        
        // subpageのbackgroundを重ねて描画
        GuiSubpageBase subpage = this.selectedContents.getSubpage();
        subpage.translateToSubpageDomain();
        subpage.drawBackgroundLayer();
        subpage.resetTranslation();
    }
    
    protected void drawGuiContainerOverwrappingLayer(int par1, int par2, float par3)
    {
    	GL11.glPushMatrix();
        GL11.glTranslatef(0f, 0f, -1f);
        // subpage側の処理で全体を覆うような描画(ダイアログ表示etc.)を行う場合の処理
        this.selectedContents.getSubpage().drawOverwrappingLayer();
        int size = this.overwrappingButtonList.size();
        for ( int i=0; i<size; i++ )
        {
            GuiButton button = this.overwrappingButtonList.get(i);
            button.drawButton(this.mc, par1, par2);
        }
        GL11.glTranslatef(0f, 0f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground(); // 黒のフィルタ
//        drawBackground(0); // 土ブロックの連続パターン
        this.drawGuiContainerBackgroundLayer(par3, par1, par2);
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
        this.drawGuiContainerOverwrappingLayer(par1, par2, par3);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        // subpage側のイベントチェック，subpageでイベント発生済みの場合は判定終了
        if ( this.selectedContents.getSubpage().actionPerformed(guiButton.id) )
        {
            return;
        }
        
        // メニューボタンのイベントチェック
        if ( guiButton.id == EnumSubpage.GENERAL.ordinal() + menuButtonIdOffset )
        {
            selectContents(EnumSubpage.GENERAL.ordinal());
            initGui();
        }
        else if ( guiButton.id == EnumSubpage.MODEL.ordinal() + menuButtonIdOffset )
        {
            selectContents(EnumSubpage.MODEL.ordinal());
            initGui();
        }
        else if ( guiButton.id == EnumSubpage.ROLE.ordinal() + menuButtonIdOffset )
        {
            selectContents(EnumSubpage.ROLE.ordinal());
            initGui();
        }
        else if ( guiButton.id == EnumSubpage.LOCATION.ordinal() + menuButtonIdOffset )
        {
            selectContents(EnumSubpage.LOCATION.ordinal());
            initGui();
        }
        else if ( guiButton.id == EnumSubpage.DELETE.ordinal() + menuButtonIdOffset )
        {
            PacketNpcDeleteEvent deletePacket = new PacketNpcDeleteEvent(true, true);
            deletePacket.sendPacketToServer(this.mc.theWorld, this.npc, false);
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        // 先にsubpage側の判定
        if ( this.selectedContents.getSubpage().keyTyped(par1, par2) )
        {
            return;
        }
        
        // Esc or inventory key
        if ( par2 == Keyboard.KEY_ESCAPE || par2 == mc.gameSettings.keyBindInventory.keyCode )
        {
            if ( this.oldNpc != null )
            {
                // form変更があった場合は古いNPC(表示中のNPC)を削除して新しいNPCをspawnさせる
                PacketNpcSpawnEvent spawnPacket = new PacketNpcSpawnEvent();
                spawnPacket.sendPacketToServer(this.mc.theWorld, this.npc, false);
                PacketNpcDeleteEvent deletePacket = new PacketNpcDeleteEvent(false, false);
                deletePacket.sendPacketToServer(this.mc.theWorld, this.oldNpc, false);
            }
            else
            {
                // form変更がなかった場合はNPCの設定値を変更
                PacketNpcSettingEvent packet = new PacketNpcSettingEvent();
                packet.sendPacketToServer(this.mc.theWorld, this.npc, false);
            }
            closeScreen();
        }
    }
    
    @Override
    public void updateScreen()
    {
    	if ( !this.selectedContents.getSubpage().updateScreen() )
        {
            super.updateScreen();
        }
    }
    
    @Override
    public void mouseClicked(int x, int y, int z)
    {
        if ( !this.selectedContents.getSubpage().mouseClicked(x, y, z) )
        {
            for ( int i=0; i<this.buttonList.size(); i++ )
            {
                GuiButton button = (GuiButton)this.buttonList.get(i);

                if (button.mousePressed(this.mc, x, y))
                {
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    this.actionPerformed(button);
                }
            }
            for ( int i=0; i<this.overwrappingButtonList.size(); i++ )
            {
                GuiButton button = this.overwrappingButtonList.get(i);
                if ( button.mousePressed(this.mc, x, y) )
                {
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    this.actionPerformed(button);
                }
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return true;
    }
    
    public void switchNpc(EntityNpc newNpc)
    {
        if ( this.oldNpc == null )
        {
            this.oldNpc = this.npc;
        }
        this.npc = newNpc;
        updateSubpageNpc();
    }
    
    public void closeScreen()
    {
        this.mc.thePlayer.closeScreen();
    }
    
    @Override
    public void onGuiClosed()
    {
        this.npc.clearGuiHolder();
        PacketNpcGuiCloseEvent packet = new PacketNpcGuiCloseEvent();
        packet.sendPacketToServer(this.mc.theWorld, this.npc, false);
    	super.onGuiClosed();
    }
}
