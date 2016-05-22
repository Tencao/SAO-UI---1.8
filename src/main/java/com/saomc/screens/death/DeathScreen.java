package com.saomc.screens.death;

import com.saomc.GLCore;
import com.saomc.colorstates.CursorStatus;
import com.saomc.events.ConfigHandler;
import com.saomc.screens.Alert;
import com.saomc.screens.Elements;
import com.saomc.screens.buttons.Actions;
import com.saomc.screens.menu.Categories;
import com.saomc.screens.window.ScreenGUI;
import com.saomc.util.ColorUtil;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class DeathScreen extends ScreenGUI {

    private final GuiGameOver gameOver;
    private final CursorStatus oldCursorStatus;

    public DeathScreen(GuiGameOver guiGamOver) {
        super();
        gameOver = guiGamOver;
        oldCursorStatus = CURSOR_STATUS;

        CURSOR_STATUS = CursorStatus.HIDDEN;
    }

    @Override
    protected void init() {
        super.init();

        elements.add(new Alert(this, 0, 0, ConfigHandler._DEAD_ALERT, this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? ColorUtil.HARDCORE_DEAD_COLOR : ColorUtil.DEAD_COLOR));
    }

    @Override
    public int getX(boolean relative) {
        return super.getX(relative) + width / 2;
    }

    @Override
    public int getY(boolean relative) {
        return super.getY(relative) + height / 2;
    }

    @Override
    public void drawScreen(int cursorX, int cursorY, float f) {
        drawDefaultBackground();

        GLCore.glTranslatef(-width / 2, -height / 2, 0.0F);
        GLCore.glScalef(2.0F, 2.0F, 2.0F);

        super.drawScreen(cursorX, cursorY, f);

    }

    @Override
    public void actionPerformed(Elements element, Actions action, int data) {
        gameOver.confirmClicked(this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled(), -1);
    }

    @Override
    protected void backgroundClicked(int cursorX, int cursorY, int button) {
        gameOver.confirmClicked(this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled(), -1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    public void close() {
        super.close();

        CURSOR_STATUS = oldCursorStatus;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
