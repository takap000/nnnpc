package takap.mods.nnnpc.gui;

import takap.mods.nnnpc.entity.EntityNpc;
import takap.mods.nnnpc.entity.INPCAction;
import takap.mods.nnnpc.entity.NPCAction;
import takap.mods.nnnpc.entity.NPCActionMoveTo;
import takap.mods.nnnpc.entity.NPCActionWait;
import takap.mods.nnnpc.location.NamedLocation;
import takap.mods.nnnpc.utility.Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSubpageRoleForRouteTracer extends GuiSubpageBase
{
    // 寝床確保用に予備を残しておく
    private final int numberOfActions = EntityNpc.maxRouteLimit;
    
    private final int upButtonMarginRight = 5;
    private final int upButtonWidth = 20;
    // innerクラスからも利用される値
    private final int lineHeight = 15;
    private final int buttonHeight = 12;
    private final int smallButtonWidth = 16;
    private final int horizontalMargin = 12;
    
    private GuiNPCActionControl[] actionControls;
    private GuiUtilityButton[] upButtons;
    
    private class GuiNPCActionControl
    {
        private int nextButtonId;
        private boolean isEnable;
        private GuiUtilityButton enableButton;
        private GuiUtilityButton actionTypeButton;
        private GuiTextField textField;
        private GuiUtilityButton previousButton;
        private GuiUtilityButton nextButton;
        private String locationLabel;
        private int locationLabelColor;
        private boolean hasTextField;
        private boolean hasLabel;
        
        // 個別の値
        private final int actionTypeButtonWidth = 48;
        private final int textFieldWidth = 48;
        private final int labelWidth = 104;
        private final int enableButtonWidth = 48;
        // offset, margin
        private final int actionTypeButtonOffsetX = 5;
        private int previousButtonOffsetX = this.actionTypeButtonOffsetX + this.actionTypeButtonWidth + horizontalMargin;
        private int textFieldOffsetX = this.previousButtonOffsetX;
        private int labelOffsetX = this.previousButtonOffsetX + smallButtonWidth + horizontalMargin;
        private int nextButtonOffsetX = this.labelOffsetX + this.labelWidth + horizontalMargin;
        
        private GuiNPCActionControl(int startId, int yOffset)
        {
            int enableButtonOffsetX = right - upButtonMarginRight - upButtonWidth - left - horizontalMargin - this.enableButtonWidth;
            this.isEnable = true;
            this.textField = new GuiTextField(mc.fontRenderer, this.textFieldOffsetX, yOffset, this.textFieldWidth, buttonHeight);
            this.actionTypeButton = new GuiUtilityButton(startId++, this.actionTypeButtonOffsetX, yOffset, this.actionTypeButtonWidth, buttonHeight, "Wait");
            this.previousButton = new GuiUtilityButton(startId++, this.previousButtonOffsetX, yOffset, smallButtonWidth, buttonHeight, "<<");
            this.nextButton = new GuiUtilityButton(startId++, this.nextButtonOffsetX, yOffset, smallButtonWidth, buttonHeight, ">>");
            this.enableButton = new GuiUtilityButton(startId++, enableButtonOffsetX, yOffset, this.enableButtonWidth, buttonHeight, this.isEnable?"Enable":"Disable");
            this.nextButtonId = startId;
            // ボタン登録
            addButtonControl(this.actionTypeButton);
            addButtonControl(this.previousButton);
            addButtonControl(this.nextButton);
            addButtonControl(this.enableButton);
        }
        
        private int getNextId()
        {
            return this.nextButtonId;
        }
        
        private boolean checkActionPerformed(int actionIndex, int buttonId)
        {
            if ( buttonId == this.actionTypeButton.id )
            {
                npc.getNPCAction(actionIndex).switchAction();
                updateNPCActionType(actionIndex);
                return true;
            }
            else if ( buttonId == this.previousButton.id )
            {
                // "MoveTo"のときのみ表示されるボタンのため，型チェックはせずにキャスティング
                NPCActionMoveTo action = (NPCActionMoveTo)npc.getNPCAction(actionIndex).getCurrentAction();
                action.setPreviousLocationIndex();
                updateNPCActionType(actionIndex);
                return true;
            }
            else if ( buttonId == this.nextButton.id )
            {
                // "MoveTo"のときのみ表示されるボタンのため，型チェックはせずにキャスティング
                NPCActionMoveTo action = (NPCActionMoveTo)npc.getNPCAction(actionIndex).getCurrentAction();
                action.setNextLocationIndex();
                updateNPCActionType(actionIndex);
                return true;
            }
            else if ( buttonId == this.enableButton.id )
            {
                npc.getNPCAction(actionIndex).switchEnable();
                updateNPCActionType(actionIndex);
                return true;
            }
            return false;
        }
        
        private void initializeControls(int index)
        {
            // "Enable"ボタンの設定
            if ( npc.getNPCAction(index).isEnable() )
            {
                this.enableButton.displayString = "Enable";
                this.enableButton.setActive(true);
            }
            else
            {
                this.enableButton.displayString = "Disable";
                this.enableButton.setActive(false);
            }
            
            // アクション個別の設定
            INPCAction action = npc.getNPCAction(index).getCurrentAction();
            // "Wait"側の設定
            if ( action instanceof NPCActionWait )
            {
                this.textField.setText(String.valueOf(((NPCActionWait)action).getWaitTime()));
            }
            // "MoveTo"側の設定
            else
            {
                this.locationLabelColor = Utility.colorWhite;
                NamedLocation location = npc.getLocation(((NPCActionMoveTo)action).getLocationIndex());
                if ( location.isAvailable() )
                {
                    this.locationLabel = String.valueOf(location.getName());
                }
                else
                {
                    this.locationLabel = "No Entry";
                    this.locationLabelColor = Utility.colorDimGray;
                }
            }
            updateNPCActionType(index);
        }
        
        private void updateNPCActionType(int index)
        {
            NPCAction action = npc.getNPCAction(index);
            if ( action == null )
            {
                setNPCActionDisabledAll();
                return;
            }
            else if ( !action.isEnable() )
            {
                setNPCActionDisabledAll();
            }
            else if ( action.getCurrentAction() instanceof NPCActionWait )
            {
                setNPCActionWait((NPCActionWait)action.getCurrentAction());
            }
            else if ( action.getCurrentAction() instanceof NPCActionMoveTo )
            {
                setNPCActionMoveTo((NPCActionMoveTo)action.getCurrentAction());
            }
            
            if ( action.isEnable() )
            {
                this.enableButton.displayString = "Enable";
                this.enableButton.setActive(true);
            }
            else
            {
                this.enableButton.displayString = "Disable";
                this.enableButton.setActive(false);
            }
        }
        
        private void setNPCActionWait(NPCActionWait action)
        {
            this.actionTypeButton.displayString = action.getLabel();
            this.actionTypeButton.drawButton = true;
            this.previousButton.drawButton = false;
            this.nextButton.drawButton = false;
            this.enableButton.drawButton = true;
            
            String textFieldValue = "";
            if ( action.getWaitTime() > 0 )
            {
                textFieldValue = String.valueOf(action.getWaitTime());
            }
            this.textField.setText(textFieldValue);
            this.hasTextField = true;
            this.hasLabel = false;
        }
        
        private void setNPCActionMoveTo(NPCActionMoveTo action)
        {
            this.actionTypeButton.displayString = action.getLabel();
            this.actionTypeButton.drawButton = true;
            this.previousButton.drawButton = true;
            this.nextButton.drawButton = true;
            this.enableButton.drawButton = true;
            
            this.locationLabelColor = Utility.colorWhite;
            NamedLocation location = npc.getLocation(action.getLocationIndex());
            if ( location.isAvailable() )
            {
                this.locationLabel = String.valueOf(location.getName());
            }
            else
            {
                this.locationLabel = "No Entry";
                this.locationLabelColor = Utility.colorDimGray;
            }
            this.hasTextField = false;
            this.hasLabel = true;
        }
        
        private void setNPCActionDisabledAll()
        {
            this.actionTypeButton.drawButton = false;
            this.previousButton.drawButton = false;
            this.nextButton.drawButton = false;
            this.enableButton.drawButton = true;
            this.hasTextField = false;
            this.hasLabel = false;
        }
        
        private void drawTextField()
        {
            if ( this.hasTextField )
            {
                this.textField.drawTextBox();
            }
        }
        
        private void drawLocationLabel(int offsetY)
        {
            if ( this.hasLabel )
            {
                drawCenteredString(this.locationLabel, this.labelOffsetX, offsetY, this.labelWidth, this.locationLabelColor, true);
            }
        }
    }
    
    public GuiSubpageRoleForRouteTracer(GuiNpcSetting mainPage, EntityPlayer player, EntityNpc npc, Minecraft mc, int guiTop, int guiBottom, int guiLeft, int guiRight, int top, int bottom, int left, int right)
    {
        super(mainPage, player, npc, mc, guiTop, guiBottom, guiLeft, guiRight, top, bottom, left, right);
    }

    @Override
    public void createButtonList()
    {
        // ボタン作成
        // name
        int offsetY = 10;
        int id = 0;
        this.actionControls = new GuiNPCActionControl[numberOfActions];
        for ( int i=0; i<this.actionControls.length; i++ )
        {
            this.actionControls[i] = new GuiNPCActionControl(id, offsetY);
            id = this.actionControls[i].getNextId();
            this.actionControls[i].initializeControls(i);
            offsetY += this.lineHeight;
        }
        this.upButtons = new GuiUtilityButton[numberOfActions-1];
        offsetY = 10 + this.lineHeight;
        int xOffset = this.right - this.upButtonMarginRight - this.upButtonWidth - this.left;
        for ( int i=0; i<this.upButtons.length; i++ )
        {
            this.upButtons[i] = new GuiUtilityButton(id++, xOffset, offsetY, this.upButtonWidth, this.buttonHeight, "up");
            addButtonControl(this.upButtons[i]);
            offsetY += this.lineHeight;
        }
    }
    
    @Override
    public void drawForegroundLayer()
    {
        int offsetY;
        
        // y座標はボタンのy座標+2がよさげ
        offsetY = 10+2;
        for ( int i=0; i<this.numberOfActions; i++ )
        {
            this.actionControls[i].drawLocationLabel(offsetY);
            offsetY += this.lineHeight;
        }
    }
    
    @Override
    public void drawBackgroundLayer()
    {
        super.drawBackgroundLayer();
        for ( int i=0; i<this.numberOfActions; i++ )
        {
            this.actionControls[i].drawTextField();
        }
    }
    
    @Override
    public boolean actionPerformed(int id)
    {
        for ( int i=0; i<this.numberOfActions; i++ )
        {
            if ( this.actionControls[i].checkActionPerformed(i, id) )
            {
                return true;
            }
        }
        for ( int i=0; i<this.upButtons.length; i++ )
        {
            if ( id == this.upButtons[i].id )
            {
                NPCAction swap = this.npc.getNPCAction(i);
                this.npc.setNPCAction(i, this.npc.getNPCAction(i+1));
                this.npc.setNPCAction(i+1, swap);
                this.actionControls[i].updateNPCActionType(i);
                this.actionControls[i+1].updateNPCActionType(i+1);
            }
        }
        return false;
    }

    @Override
    public String getSubpageTitle()
    {
        return "General";
    }

    @Override
    public boolean keyTyped(char par1, int par2)
    {
        for ( int i=0; i<this.numberOfActions; i++ )
        {
            GuiTextField textField = this.actionControls[i].textField;
            if ( textField.isFocused() )
            {
                String textFieldValue = textField.getText();
                textField.textboxKeyTyped(par1, par2);
                try
                {
                    int waitTime = Integer.parseInt(textField.getText());
                    String newValue = String.valueOf(waitTime);
                    textFieldValue = newValue;
                    NPCAction action = npc.getNPCAction(i);
                    ((NPCActionWait)action.getCurrentAction()).setWaitTime(waitTime);
                    if ( waitTime == 0 )
                    {
                        textFieldValue = "";
                    }
                }
                catch ( NumberFormatException e )
                {
                    // すべて削除されていた場合は0をセット
                    if ( textField.getText().equals("") )
                    {
                        NPCAction action = npc.getNPCAction(i);
                        ((NPCActionWait)action.getCurrentAction()).setWaitTime(0);
                        textFieldValue = "";
                    }
                }
                textField.setText(textFieldValue);
            }
        }
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
        for ( int i=0; i<this.numberOfActions; i++ )
        {
            this.actionControls[i].textField.setFocused(false);
        }
        for ( int i=0; i<this.numberOfActions; i++ )
        {
            this.actionControls[i].textField.mouseClicked(x - this.left, y - this.top, z);
        }
        return false;
    }
}
