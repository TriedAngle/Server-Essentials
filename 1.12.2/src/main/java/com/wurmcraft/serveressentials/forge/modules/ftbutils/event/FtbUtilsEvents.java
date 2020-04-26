package com.wurmcraft.serveressentials.forge.modules.ftbutils.event;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.event.NewPlayerJoin;
import com.wurmcraft.serveressentials.forge.api.event.RankChangeEvent;
import com.wurmcraft.serveressentials.forge.modules.ftbutils.FTBUtilsUtils;
import java.util.concurrent.TimeUnit;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class FtbUtilsEvents {

  @SubscribeEvent
  public void newPlayer(NewPlayerJoin e) {
    SECore.executors.schedule(() -> {
      FTBUtilsUtils.setPlayerClaimBlocks(e.player,
          FTBUtilsUtils.getMaxClaims(e.playerData, (Rank) SERegistry.getStoredData(
              DataKey.RANK, e.playerData.global.rank)));
    }, 500, TimeUnit.MILLISECONDS);
  }

  @SubscribeEvent
  public void onRankChange(RankChangeEvent e) {
    SECore.executors.schedule(() -> {
      FTBUtilsUtils
          .updatePlayerClaimBlocks(e.player,
              FTBUtilsUtils.getMaxClaims(e.data, e.newRank));
    }, 500, TimeUnit.MILLISECONDS);
  }

  @SubscribeEvent
  public void onPlayerLogin(PlayerLoggedInEvent e) {
    SECore.executors.schedule(() -> {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, e.player.getGameProfile().getId().toString());
      FTBUtilsUtils.updatePlayerClaimBlocks(e.player,
          FTBUtilsUtils.getMaxClaims(playerData, (Rank) SERegistry.getStoredData(
              DataKey.RANK, playerData.global.rank)));
    }, 500, TimeUnit.MILLISECONDS);
  }
}