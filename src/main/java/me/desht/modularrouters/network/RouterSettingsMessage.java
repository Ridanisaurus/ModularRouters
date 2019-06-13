package me.desht.modularrouters.network;

import io.netty.buffer.ByteBuf;
import me.desht.modularrouters.block.tile.TileEntityItemRouter;
import me.desht.modularrouters.logic.RouterRedstoneBehaviour;
import me.desht.modularrouters.util.MiscUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Received on: SERVER
 *
 * Used when a player updates settings on an item router via its GUI.
 */
public class RouterSettingsMessage {
    private boolean eco;
    private TileEntityItemRouter router;
    private RouterRedstoneBehaviour redstoneBehaviour;

    public RouterSettingsMessage() {
    }

    public RouterSettingsMessage(TileEntityItemRouter router, RouterRedstoneBehaviour redstoneBehaviour, boolean ecoMode) {
        this.router = router;
        this.redstoneBehaviour = redstoneBehaviour;
        this.eco = ecoMode;
    }

    public RouterSettingsMessage(PacketBuffer buffer) {
        BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        ServerWorld world = MiscUtil.getWorldForDimensionId(buffer.readInt());
        if (world != null) {
            router = TileEntityItemRouter.getRouterAt(world, pos);
        }
        redstoneBehaviour = RouterRedstoneBehaviour.values()[buffer.readByte()];
        eco = buffer.readBoolean();
    }

    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(router.getPos().getX());
        byteBuf.writeInt(router.getPos().getY());
        byteBuf.writeInt(router.getPos().getZ());
        byteBuf.writeInt(MiscUtil.getDimensionForWorld(router.getWorld()));
        byteBuf.writeByte(redstoneBehaviour.ordinal());
        byteBuf.writeBoolean(eco);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (router != null) {
                router.setRedstoneBehaviour(redstoneBehaviour);
                router.setEcoMode(eco);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
