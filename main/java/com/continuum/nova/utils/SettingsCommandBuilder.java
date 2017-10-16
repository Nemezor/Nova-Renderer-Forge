package com.continuum.nova.utils;

import com.continuum.nova.NovaNative;
import com.continuum.nova.injector.Interface;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;

public class SettingsCommandBuilder
{
    public NovaNative.mc_settings getSettings(Minecraft mc)
    {
        NovaNative.mc_settings settings = new NovaNative.mc_settings();
        settings.view_bobbing = mc.gameSettings.viewBobbing;
        settings.render_distance = mc.gameSettings.renderDistanceChunks;
        settings.fog_color_red = Interface.entityRenderer_getFogColorRed(mc.entityRenderer);
        settings.fog_color_green = Interface.entityRenderer_getFogColorGreen(mc.entityRenderer);
        settings.fog_color_blue = Interface.entityRenderer_getFogColorBlue(mc.entityRenderer);
        settings.anaglyph = mc.gameSettings.anaglyph;
        settings.display_height = mc.displayHeight;
        settings.display_width = mc.displayWidth;

        if (Utils.exists(mc.theWorld))
        {
            settings.should_render_clouds = mc.theWorld.provider.isSurfaceWorld() ? mc.gameSettings.shouldRenderClouds() : 0;
        }

        Entity viewEntity = mc.getRenderViewEntity();

        if (Utils.exists(viewEntity))
        {
            settings.has_blindness = ((EntityLivingBase) viewEntity).isPotionActive(MobEffects.BLINDNESS);
        }

        return settings;
    }
}
