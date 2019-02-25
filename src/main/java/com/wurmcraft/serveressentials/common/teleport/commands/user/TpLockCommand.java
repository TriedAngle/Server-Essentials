package com.wurmcraft.serveressentials.common.teleport.commands.user;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@Command(moduleName = "Teleportation")
public class TpLockCommand extends SECommand {

  @Override
  public String getName() {
    return "tpLock";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      if (user.isTpLock()) {
        setLock(player, false);
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).TPLOCK_DISABLED.replaceAll("&", FORMATTING_CODE));
      } else {
        setLock(player, true);
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).TPLOCK_ENABLED.replaceAll("&", FORMATTING_CODE));
      }
    } else if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        PlayerData data =
            (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
        if (data.isTpLock()) {
          setLock(player, false);
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).TPLOCK_DISABLED.replaceAll("&", FORMATTING_CODE));
        } else {
          setLock(player, true);
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).TPLOCK_ENABLED.replaceAll("&", FORMATTING_CODE));
        }
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/tplock \u00A7b<player>";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TPLOCK.replaceAll("&", FORMATTING_CODE);
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }

  private static void setLock(EntityPlayer player, boolean lock) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
      user.setTpLock(lock);
      DataHelper.forceSave(Keys.LOCAL_USER, user);
      UserManager.PLAYER_DATA.put(
          player.getGameProfile().getId(),
          new Object[] {UserManager.getPlayerData(player.getGameProfile().getId())[0], user});
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      data.setTpLock(lock);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
      UserManager.PLAYER_DATA.put(player.getGameProfile().getId(), new Object[] {data});
    }
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("lock");
    return alts;
  }
}
