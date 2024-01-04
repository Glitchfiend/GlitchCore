package glitchcore.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface CustomPacket<T extends CustomPacket<T>>
{
    void encode(FriendlyByteBuf buf);

    T decode(FriendlyByteBuf buf);

    void handle(T data, Context context);

    interface Context
    {
        boolean isClientSide();
        default boolean isServerSide()
        {
            return !isClientSide();
        }
        Optional<Player> getPlayer();
    }
}
