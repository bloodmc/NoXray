package com.thomas15v.noxray.modifications.mixins;

import com.thomas15v.noxray.modifications.internal.InternalBlockStateContainer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SPacketChunkData.class)
public class MixinPacketChunkData {

    @Redirect(method = "extractChunkData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/BlockStateContainer;write(Lnet/minecraft/network/PacketBuffer;)V"))
    public void writeModified(BlockStateContainer storage, PacketBuffer buffer, PacketBuffer methodbuffer, Chunk chunk, boolean aboolean, int anunknownint){
        ((InternalBlockStateContainer)storage).writeModified(buffer);
    }

    @Redirect(method = "calculateChunkSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getData()Lnet/minecraft/world/chunk/BlockStateContainer;"))
    public BlockStateContainer setYValue(ExtendedBlockStorage extendedBlockStorage){
        BlockStateContainer container = extendedBlockStorage.getData();
        ((InternalBlockStateContainer)container).setY(extendedBlockStorage.getYLocation());
        return container;
    }

    @Redirect(method = "calculateChunkSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/BlockStateContainer;getSerializedSize()I"))
    public int caclulateModifiedSize(BlockStateContainer blockStateContainer, Chunk chunk, boolean p_189556_2_, int p_189556_3_){
        InternalBlockStateContainer container = (InternalBlockStateContainer) blockStateContainer;
        container.updateModified(chunk);
        return container.modifiedSize();
    }

}