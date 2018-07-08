package com.wurmcraft.serveressentials.common.chat.events;

import com.wurmcraft.serveressentials.api.json.global.Channel;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.api.json.user.team.fileOnly.Team;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerChat {

  public static HashMap<UUID, String[]> lastChat = new HashMap<>();

  public static boolean proccessRest(ServerChatEvent e) {
    GlobalUser global =
        (GlobalUser) UserManager.getPlayerData(e.getPlayer().getGameProfile().getId())[0];
    LocalUser local =
        (LocalUser) UserManager.getPlayerData(e.getPlayer().getGameProfile().getId())[1];
    Channel currentChannel = (Channel) DataHelper.get(Keys.CHANNEL, local.getCurrentChannel());
    if (DataHelper.get(Keys.CHANNEL, local.getCurrentChannel()) == null) {
      local.setCurrentChannel(ConfigHandler.defaultChannel);
      DataHelper.forceSave(Keys.LOCAL_USER, local);
      currentChannel = (Channel) DataHelper.get(Keys.CHANNEL, local.getCurrentChannel());
    }
    String username =
        global.getNick().isEmpty()
            ? e.getUsername()
            : TextFormatting.GREEN + "*" + TextFormatting.GRAY + global.getNick();
    e.setComponent(
        new TextComponentString(
            ChatHelper.format(
                username,
                UserManager.getRank(global.rank),
                currentChannel,
                e.getPlayer().dimension,
                UserManager.getTeam(global.getTeam()).length > 0
                    ? (ITeam) UserManager.getTeam(global.getTeam())[0]
                    : new Team(),
                e.getMessage())));
    if (currentChannel.getName().equalsIgnoreCase(ConfigHandler.defaultChannel)) {
      return false;
    }
    return true;
  }

  public static boolean proccessFile(ServerChatEvent e) {
    return false;
  }

  private static List<EntityPlayerMP> getPlayersInChannel(Channel channel) {
    List<EntityPlayerMP> players = new ArrayList<>();
    for (EntityPlayerMP player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      Channel ch = getUserChannel(player.getGameProfile().getId());
      if (channel.getName().equalsIgnoreCase(ch.getName())) {
        players.add(player);
      }
    }
    return players;
  }

  private static Channel getUserChannel(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(uuid)[1];
      return (Channel) DataHelper.get(Keys.CHANNEL, user.getCurrentChannel());
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return (Channel) DataHelper.get(Keys.CHANNEL, data.getCurrentChannel());
    }
    return (Channel) DataHelper.get(Keys.CHANNEL, ConfigHandler.defaultChannel);
  }

  private static boolean handleMessage(UUID name, String message) {
    if (lastChat.containsKey(name)) {
      String[] chat = lastChat.get(name);
      if (chat[0].equalsIgnoreCase(message)) {
        int count = 0;
        for (int index = 0; index < chat.length; index++) {
          if (message.equalsIgnoreCase(chat[index])) {
            count++;
          } else if (chat[index] == null) {
            chat[index] = message;
            count++;
            break;
          }
        }
        return count < ConfigHandler.spamLimit;
      } else {
        lastChat.remove(name);
        return true;
      }
    } else {
      String[] chat = new String[ConfigHandler.spamLimit];
      chat[0] = message;
      lastChat.put(name, chat);
    }
    return true;
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onChat(ServerChatEvent e) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      if (handleMessage(e.getPlayer().getGameProfile().getId(), e.getMessage())) {
        if (proccessRest(e)) {
          e.setCanceled(true);
          Channel ch = getUserChannel(e.getPlayer().getGameProfile().getId());
          for (EntityPlayerMP player : getPlayersInChannel(ch)) {
            player.sendMessage(e.getComponent());
          }
        }
      } else {
        e.getPlayer()
            .sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                        .CHAT_SPAM));
        e.setCanceled(true);
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      if (handleMessage(e.getPlayer().getGameProfile().getId(), e.getMessage())) {
        if (proccessFile(e)) {
          e.setCanceled(true);
        }
      } else {
        e.getPlayer()
            .sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                        .CHAT_SPAM));
        e.setCanceled(true);
      }
    }
  }
}
