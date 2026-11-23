package com.velorise.cameralockon;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Consumer;

final class LineRenderHelper {
    private LineRenderHelper() {}

    static void drawLines(Minecraft minecraft, Consumer<VertexConsumer> drawer) {
        RenderType renderType = RenderTypes.lines();
        StagedVertexBuffer staged = minecraft.gameRenderer.renderBuffers().stagedVertexBuffer();
        StagedVertexBuffer.Draw draw = staged.appendDraw(renderType.format(), renderType.primitiveTopology());
        VertexConsumer consumer = staged.getVertexBuilder(draw);
        drawer.accept(consumer);
        staged.upload();
        try {
            StagedVertexBuffer.ExecuteInfo executeInfo = staged.getExecuteInfo(draw);
            if (executeInfo == null) return;
            RenderTarget target = minecraft.gameRenderer.mainRenderTarget();
            GpuTextureView colorView = target.getColorTextureView();
            if (colorView == null) return;
            GpuTextureView depthView = target.getDepthTextureView();
            PreparedRenderType prepared = renderType.prepare();
            try (RenderPass pass = depthView != null
                    ? RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                            () -> "camera_lockon_lines", colorView, Optional.empty(), depthView, OptionalDouble.empty())
                    : RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                            () -> "camera_lockon_lines", colorView, Optional.empty())) {
                prepared.drawFromBuffer(executeInfo, pass);
            }
        } finally {
            staged.endDraw();
        }
    }
}
