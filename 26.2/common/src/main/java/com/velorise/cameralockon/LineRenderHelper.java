package com.velorise.cameralockon;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

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
            if (executeInfo != null) {
                renderType.prepare().drawFromBuffer(executeInfo);
            }
        } finally {
            staged.endDraw();
        }
    }
}
