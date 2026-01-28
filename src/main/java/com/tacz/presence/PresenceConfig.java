package com.tacz.presence;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PresenceConfig {
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
    }

    public enum Mode {
        ALL, GUNS_ONLY, OFF
    }

    public enum HudType {
        TACZ_HUD, // Original TACZ HUD
        TACZ_P_HUD // Custom immersive HUD from this mod
    }

    // === SCREEN SHAKE ===
    public static ForgeConfigSpec.DoubleValue SHAKE_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue MAX_SHAKE;

    // === DAMAGE OVERLAY ===
    public static ForgeConfigSpec.EnumValue<Mode> DAMAGE_OVERLAY_MODE;
    public static ForgeConfigSpec.DoubleValue DAMAGE_OVERLAY_OPACITY;
    public static ForgeConfigSpec.DoubleValue VIGNETTE_DECAY_RATE;

    // === HUD ===
    public static ForgeConfigSpec.EnumValue<HudType> GUN_HUD_TYPE;

    // === HUD ALERTS ===
    public static ForgeConfigSpec.IntValue HUD_ALERT_Y_OFFSET;
    public static ForgeConfigSpec.DoubleValue HUD_ALERT_SCALE;
    public static ForgeConfigSpec.BooleanValue HUD_NO_AMMO_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> HUD_NO_AMMO_COLOR;
    public static ForgeConfigSpec.BooleanValue HUD_RELOAD_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> HUD_RELOAD_COLOR;
    public static ForgeConfigSpec.BooleanValue HUD_LOW_AMMO_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> HUD_LOW_AMMO_COLOR;

    // === DAMAGE INDICATOR ===
    public static ForgeConfigSpec.EnumValue<Mode> DAMAGE_INDICATOR_MODE;
    public static ForgeConfigSpec.DoubleValue DAMAGE_INDICATOR_RADIUS;
    public static ForgeConfigSpec.DoubleValue DAMAGE_INDICATOR_OPACITY;

    // === HIT OVERLAY ===
    public static ForgeConfigSpec.EnumValue<Mode> HIT_OVERLAY_MODE;
    public static ForgeConfigSpec.DoubleValue HIT_OVERLAY_OPACITY;
    public static ForgeConfigSpec.DoubleValue HIT_OVERLAY_FADE_SPEED;

    // === SOUNDS ===
    public static ForgeConfigSpec.BooleanValue ENABLE_HIT_SOUND;
    public static ForgeConfigSpec.BooleanValue ENABLE_SUPER_HIT_SOUND;

    // === SNIPER GLARE (Server) ===
    public static ForgeConfigSpec.BooleanValue GLARE_ENABLED;
    public static ForgeConfigSpec.DoubleValue GLARE_MIN_ZOOM;
    public static ForgeConfigSpec.DoubleValue GLARE_MIN_DISTANCE;
    public static ForgeConfigSpec.DoubleValue GLARE_BASE_SIZE;
    public static ForgeConfigSpec.DoubleValue GLARE_SCALE_CLOSE;
    public static ForgeConfigSpec.DoubleValue GLARE_SCALE_MID;
    public static ForgeConfigSpec.DoubleValue GLARE_SCALE_FAR;
    public static ForgeConfigSpec.DoubleValue GLARE_VIEW_ANGLE;
    public static ForgeConfigSpec.DoubleValue GLARE_OPACITY_DAY;
    public static ForgeConfigSpec.DoubleValue GLARE_OPACITY_NIGHT;
    public static ForgeConfigSpec.DoubleValue GLARE_OPACITY_RAIN;
    public static ForgeConfigSpec.DoubleValue GLARE_OPACITY_NIGHT_RAIN;

    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {
            // === SCREEN SHAKE ===
            builder.push("screen_shake");
            builder.comment("Camera shake intensity multiplier (0.0-5.0)");
            SHAKE_MULTIPLIER = builder.defineInRange("ShakeMultiplier", 0.2D, 0.0D, 5.0D);
            builder.comment("Max camera shake intensity");
            MAX_SHAKE = builder.defineInRange("MaxShake", 15.0D, 0.0D, 100.0D);
            builder.pop();

            // === DAMAGE OVERLAY ===
            builder.push("damage_overlay");
            builder.comment("Damage overlay mode: ALL, GUNS_ONLY, OFF");
            DAMAGE_OVERLAY_MODE = builder.defineEnum("Mode", Mode.ALL);
            builder.comment("Global opacity for all damage overlay textures (0.0-1.0)");
            DAMAGE_OVERLAY_OPACITY = builder.defineInRange("Opacity", 0.8D, 0.0D, 1.0D);
            builder.comment("Vignette fade rate per tick (lower = slower fade)");
            VIGNETTE_DECAY_RATE = builder.defineInRange("VignetteDecayRate", 0.0015D, 0.001D, 0.1D);
            builder.pop();

            // === HUD ===
            builder.push("hud");
            builder.comment("Gun HUD style: TACZ_HUD (original), TACZ_P_HUD (custom immersive)");
            GUN_HUD_TYPE = builder.defineEnum("GunHudType", HudType.TACZ_P_HUD);
            builder.pop();

            // === HUD ALERTS ===
            builder.push("hud_alerts");
            builder.comment("Vertical offset for HUD alerts from center");
            HUD_ALERT_Y_OFFSET = builder.defineInRange("AlertYOffset", 60, -500, 500);
            builder.comment("Scale of HUD alert text");
            HUD_ALERT_SCALE = builder.defineInRange("AlertScale", 1.2, 0.1, 5.0);
            builder.comment("Show 'NO AMMO' alert");
            HUD_NO_AMMO_ENABLED = builder.define("NoAmmoEnabled", true);
            builder.comment("Color for 'NO AMMO' (Hex format: #RRGGBB)");
            HUD_NO_AMMO_COLOR = builder.define("NoAmmoColor", "#FF5555");
            builder.comment("Show 'RELOAD' alert");
            HUD_RELOAD_ENABLED = builder.define("ReloadEnabled", true);
            builder.comment("Color for 'RELOAD' (Hex format: #RRGGBB)");
            HUD_RELOAD_COLOR = builder.define("ReloadColor", "#FF5555");
            builder.comment("Show 'LOW AMMO' alert");
            HUD_LOW_AMMO_ENABLED = builder.define("LowAmmoEnabled", true);
            builder.comment("Color for 'LOW AMMO' (Hex format: #RRGGBB)");
            HUD_LOW_AMMO_COLOR = builder.define("LowAmmoColor", "#FFAA00");
            builder.pop();

            // === DAMAGE INDICATOR ===
            builder.push("damage_indicator");
            builder.comment("Damage indicator mode: ALL, GUNS_ONLY, OFF");
            DAMAGE_INDICATOR_MODE = builder.defineEnum("Mode", Mode.ALL);
            builder.comment("Indicator distance from crosshair (0.05-0.5)");
            DAMAGE_INDICATOR_RADIUS = builder.defineInRange("Radius", 0.25, 0.05, 0.5);
            builder.comment("Indicator base opacity (0.0-1.0)");
            DAMAGE_INDICATOR_OPACITY = builder.defineInRange("Opacity", 1.0, 0.0, 1.0);
            builder.pop();

            // === HIT OVERLAY ===
            builder.push("hit_overlay");
            builder.comment("Hit overlay mode: ALL, GUNS_ONLY, OFF");
            HIT_OVERLAY_MODE = builder.defineEnum("Mode", Mode.ALL);
            builder.comment("Hit overlay opacity (0.0-1.0)");
            HIT_OVERLAY_OPACITY = builder.defineInRange("Opacity", 0.8D, 0.0D, 1.0D);
            builder.comment("Fade out speed (higher = faster fade, 0.01-0.2)");
            HIT_OVERLAY_FADE_SPEED = builder.defineInRange("FadeOutSpeed", 0.05D, 0.01D, 0.2D);
            builder.pop();

            // === SOUNDS ===
            builder.push("sounds");
            builder.comment("Enable standard hit sound");
            ENABLE_HIT_SOUND = builder.define("EnableHitSound", true);
            builder.comment("Enable super hit sound (high damage)");
            ENABLE_SUPER_HIT_SOUND = builder.define("EnableSuperHitSound", true);
            builder.pop();
        }
    }

    public static class Server {
        public Server(ForgeConfigSpec.Builder builder) {
            // === SNIPER GLARE ===
            builder.push("sniper_glare");
            builder.comment("Enable sniper glare effect visible to other players");
            GLARE_ENABLED = builder.define("EnableSniperGlare", true);
            builder.comment("Minimum scope zoom level to be considered a sniper scope (e.g., 4.0 for 4x zoom)");
            GLARE_MIN_ZOOM = builder.defineInRange("MinZoomLevel", 4.0D, 1.0D, 20.0D);
            builder.comment("Minimum distance (blocks) to see the glare");
            GLARE_MIN_DISTANCE = builder.defineInRange("MinDistance", 5.0D, 1.0D, 100.0D);
            builder.comment("Base glare size in blocks");
            GLARE_BASE_SIZE = builder.defineInRange("BaseSize", 3.0D, 0.5D, 20.0D);
            builder.comment("Size scale when close (at minimum distance)");
            GLARE_SCALE_CLOSE = builder.defineInRange("ScaleClose", 0.5D, 0.1D, 5.0D);
            builder.comment("Size scale at medium distance (50 blocks)");
            GLARE_SCALE_MID = builder.defineInRange("ScaleMid", 1.0D, 0.1D, 5.0D);
            builder.comment("Size scale when far (100+ blocks)");
            GLARE_SCALE_FAR = builder.defineInRange("ScaleFar", 1.6D, 0.1D, 10.0D);
            builder.comment("Viewing angle (degrees) from sniper's aim direction to see glare");
            GLARE_VIEW_ANGLE = builder.defineInRange("ViewAngle", 45.0D, 10.0D, 180.0D);
            builder.comment("Glare opacity during clear day (0.0-1.0)");
            GLARE_OPACITY_DAY = builder.defineInRange("OpacityDay", 1.0D, 0.0D, 1.0D);
            builder.comment("Glare opacity during clear night (0.0-1.0)");
            GLARE_OPACITY_NIGHT = builder.defineInRange("OpacityNight", 0.8D, 0.0D, 1.0D);
            builder.comment("Glare opacity during rain (0.0-1.0)");
            GLARE_OPACITY_RAIN = builder.defineInRange("OpacityRain", 0.65D, 0.0D, 1.0D);
            builder.comment("Glare opacity during night + rain (0.0-1.0)");
            GLARE_OPACITY_NIGHT_RAIN = builder.defineInRange("OpacityNightRain", 0.5D, 0.0D, 1.0D);
            builder.pop();
        }
    }
}
