package net.apolloclient.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author BossCode45
 * @see GuiButton
 */
public class ToggleItemPhysicsGuiButton extends GuiButton {
    //buttonTextures is the texture location for the button texture
    protected static final ResourceLocation buttonTextures = new ResourceLocation("ItemPhysicsButton.png");
    public ToggleItemPhysicsGuiButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public ToggleItemPhysicsGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    /**
     * @see GuiButton#drawButton(Minecraft, int, int)
     * this is an override of it with a few changes to make it have a custom texture
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, 0, this.width , this.height, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
        }
    }
}
