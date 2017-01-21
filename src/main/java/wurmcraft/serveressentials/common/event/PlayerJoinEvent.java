package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerJoinEvent {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        DataHelper.registerPlayer(e.player);
    }
}
