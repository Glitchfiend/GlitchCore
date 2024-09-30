package glitchcore.network;

import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

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
