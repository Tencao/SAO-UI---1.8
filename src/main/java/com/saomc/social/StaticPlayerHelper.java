package com.saomc.social;

import com.saomc.SAOCore;
import com.saomc.util.OptionCore;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Part of SAOUI
 *
 * @author Bluexin
 */
public class StaticPlayerHelper {
    public static final float HEALTH_ANIMATION_FACTOR = 0.075F;
    public static final float HEALTH_FRAME_FACTOR = HEALTH_ANIMATION_FACTOR * HEALTH_ANIMATION_FACTOR * 0x40 * 0x64;
    public static Map<UUID, Float> healthSmooth = new HashMap<>();
    public static Map<UUID, Float> hungerSmooth = new HashMap<>();
    public static final double MAX_RANGE = 512.0D;

    public static List<EntityPlayer> listOnlinePlayers(Minecraft mc, boolean search, double range) {
        if (!search) range = MAX_RANGE;

        final AxisAlignedBB box = AxisAlignedBB.fromBounds(
                mc.thePlayer.posX - range, mc.thePlayer.posY - range, mc.thePlayer.posZ - range,
                mc.thePlayer.posX + range, mc.thePlayer.posY + range, mc.thePlayer.posZ + range
        );

        @SuppressWarnings("unchecked")
        final List<EntityPlayer> entities = mc.theWorld.getEntitiesWithinAABB(EntityPlayer.class, box);

        return entities;
    }

    @SuppressWarnings("unchecked")
    public static List<EntityPlayer> listOnlinePlayers(Minecraft mc) {
        return mc.theWorld.getEntities(EntityPlayer.class, (o) -> true);
    }

    public static EntityPlayer findOnlinePlayer(Minecraft mc, String username) {
        return listOnlinePlayers(mc).stream().filter(player -> getName(player).equals(username)).findAny().orElse(null);
    }

    public static boolean[] isOnline(Minecraft mc, String[] names) { // TODO: update a boolean[] upon player join server? (/!\ client-side)
        final List<EntityPlayer> players = listOnlinePlayers(mc);
        final boolean[] online = new boolean[names.length];

        for (int i = 0; i < names.length; i++) {
            final int index = i;
            online[i] = players.stream().anyMatch(player -> getName(player).equals(names[index]));
        }

        return online;
    }

    public static boolean isOnline(Minecraft mc, String name) {
        return isOnline(mc, new String[]{name})[0];
    }

    public static String getName(EntityPlayer player) {
        return player == null ? "" : player.getCommandSenderName();
    }

    public static String getName(Minecraft mc) {
        return getName(mc.thePlayer);
    }

    public static String unformatName(String name) {
        int index = name.indexOf("�");

        while (index != -1) {
            if (index + 1 < name.length()) name = name.replace(name.substring(index, index + 2), "");
            else name = name.replace("�", "");

            index = name.indexOf("�");
        }

        return name;
    }

    public static float getHealth(final Minecraft mc, final Entity entity, final float time) {
        if (OptionCore.SMOOTH_HEALTH.getValue()) {
            final float healthReal;
            final UUID uuid = entity.getUniqueID();

            if (entity instanceof EntityLivingBase) healthReal = ((EntityLivingBase) entity).getHealth();
            else healthReal = entity.isDead ? 0F : 1F;

            if (healthSmooth.containsKey(uuid)) {
                float healthValue = healthSmooth.get(uuid);

                if ((healthReal <= 0) && (entity instanceof EntityLivingBase)) {
                    final float value = (float) (18 - ((EntityLivingBase) entity).deathTime) / 18;

                    if (value <= 0) healthSmooth.remove(uuid);

                    return healthValue * value;
                } else if (Math.round(healthValue * 10) != Math.round(healthReal * 10))
                    healthValue = healthValue + (healthReal - healthValue) * (gameTimeDelay(mc, time) * HEALTH_ANIMATION_FACTOR);
                else healthValue = healthReal;

                healthSmooth.put(uuid, healthValue);
                return healthValue;
            } else {
                healthSmooth.put(uuid, healthReal);
                return healthReal;
            }
        } else
            return (entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHealth() : (entity.isDead ? 0F : 1F));
    }

    public static float getMaxHealth(final Entity entity) {
        return entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getMaxHealth() : 1F;
    }

    public static float getHungerFract(final Minecraft mc, final Entity entity, final float time) {
        if (!(entity instanceof EntityPlayer)) return 1.0F;
        EntityPlayer player = (EntityPlayer) entity;
        final float hunger;
        if (OptionCore.SMOOTH_HEALTH.getValue()) {
            final UUID uuid = entity.getUniqueID();

            hunger = player.getFoodStats().getFoodLevel();

            if (hungerSmooth.containsKey(uuid)) {
                float hungerValue = hungerSmooth.get(uuid);

                if (hunger <= 0) {
                    final float value = (float) (18 - player.deathTime) / 18;

                    if (value <= 0) hungerSmooth.remove(uuid);

                    return hungerValue * value;
                } else if (Math.round(hungerValue * 10) != Math.round(hunger * 10))
                    hungerValue = hungerValue + (hunger - hungerValue) * (gameTimeDelay(mc, time) * HEALTH_ANIMATION_FACTOR);
                else hungerValue = hunger;

                hungerSmooth.put(uuid, hungerValue);
                return hungerValue / 20.0F;
            } else {
                hungerSmooth.put(uuid, hunger);
                return hunger / 20.0F;
            }
        } else return player.getFoodStats().getFoodLevel() / 20.0F;
    }

    private static float gameTimeDelay(Minecraft mc, float time) {
        return time >= 0F ? time : HEALTH_FRAME_FACTOR / gameFPS(mc);
    }

    public static boolean isCreative(EntityPlayer player) { // TODO: test this!
        return player.capabilities.isCreativeMode;
    }

    private static int gameFPS(Minecraft mc) {
        return mc.getLimitFramerate();
    }
}
