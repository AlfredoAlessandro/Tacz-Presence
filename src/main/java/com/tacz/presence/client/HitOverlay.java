package com.tacz.presence.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.tacz.presence.PresenceConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Hit Overlay - Efecto visual de impacto con bordes radiales rojos oscuros
 * en los lados izquierdo y derecho que aparecen casi instantáneamente
 */
public class HitOverlay implements IGuiOverlay {

    public static final HitOverlay INSTANCE = new HitOverlay();

    // Estado de la animación
    private static float intensity = 0.0f;
    private static float slideProgress = 0.0f;
    private static float scaleProgress = 0.0f;

    // Fase de la animación
    private static boolean isEntering = false;
    private static boolean isExiting = false;

    /**
     * Activa el efecto Hit Overlay cuando el jugador recibe un impacto
     */
    public static void trigger() {
        // Verificar si está habilitado
        if (PresenceConfig.HIT_OVERLAY_MODE.get() == PresenceConfig.Mode.OFF) {
            return;
        }

        intensity = 1.0f;
        slideProgress = 0.8f; // Empezar casi completamente visible (casi instantáneo)
        scaleProgress = 0.7f; // Empezar con escala ya avanzada
        isEntering = true;
        isExiting = false;
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (intensity <= 0.01f && slideProgress <= 0.01f) {
            return;
        }

        // Actualizar animación
        updateAnimation();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        // Renderizar bordes radiales en izquierda y derecha
        renderSideBorders(screenWidth, screenHeight);

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void updateAnimation() {
        // Obtener fade speed de la configuración
        float fadeSpeed = PresenceConfig.HIT_OVERLAY_FADE_SPEED.get().floatValue();

        if (isEntering) {
            // Fase de entrada: MUY RÁPIDA (casi instantánea)
            slideProgress = Math.min(1.0f, slideProgress + 0.5f);
            scaleProgress = Math.min(1.0f, scaleProgress + 0.4f);

            // Cuando llega al máximo, comenzar a salir inmediatamente
            if (slideProgress >= 0.95f) {
                isEntering = false;
                isExiting = true;
            }
        } else if (isExiting) {
            // Fase de salida: usa el fade speed de config
            intensity = Math.max(0.0f, intensity - fadeSpeed);
            slideProgress = Math.max(0.0f, slideProgress - fadeSpeed * 0.6f);
            scaleProgress = Math.max(0.0f, scaleProgress - fadeSpeed * 0.3f);

            if (intensity <= 0.01f) {
                isExiting = false;
                slideProgress = 0.0f;
                scaleProgress = 0.0f;
            }
        }
    }

    private void renderSideBorders(int screenWidth, int screenHeight) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        // Colores rojos oscuros para el efecto de impacto
        float red = 0.5f;
        float green = 0.02f;
        float blue = 0.02f;

        // Obtener opacidad de la configuración
        float configOpacity = PresenceConfig.HIT_OVERLAY_OPACITY.get().floatValue();

        // Alpha basado en intensidad y slide progress
        float alpha = intensity * slideProgress * configOpacity;

        // Dimensiones del borde
        float borderWidth = screenWidth * 0.15f * (0.6f + scaleProgress * 0.4f);
        float slideOffset = borderWidth * (1.0f - slideProgress);

        // Curva del borde radial
        int segments = 40;
        float curveDepth = screenWidth * 0.12f * scaleProgress;

        // === BORDE IZQUIERDO ===
        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            float y = t * screenHeight;

            // Curva sinusoidal para el borde radial
            float curve = (float) Math.sin(t * Math.PI) * curveDepth;
            curve += (float) Math.sin(t * Math.PI * 2) * curveDepth * 0.15f;

            float outerX = -slideOffset;
            float innerX = borderWidth - slideOffset + curve;

            buffer.vertex(outerX, y, 0.0)
                    .color(red, green, blue, alpha)
                    .endVertex();

            buffer.vertex(innerX, y, 0.0)
                    .color(red, green, blue, 0.0f)
                    .endVertex();
        }
        tesselator.end();

        // === BORDE DERECHO ===
        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            float y = t * screenHeight;

            float curve = (float) Math.sin(t * Math.PI) * curveDepth;
            curve += (float) Math.sin(t * Math.PI * 2) * curveDepth * 0.15f;

            float innerX = screenWidth - borderWidth + slideOffset - curve;
            float outerX = screenWidth + slideOffset;

            buffer.vertex(innerX, y, 0.0)
                    .color(red, green, blue, 0.0f)
                    .endVertex();

            buffer.vertex(outerX, y, 0.0)
                    .color(red, green, blue, alpha)
                    .endVertex();
        }
        tesselator.end();
    }
}
