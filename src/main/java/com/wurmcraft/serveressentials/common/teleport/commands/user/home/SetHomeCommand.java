package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Teleportation")
public class SetHomeCommand extends SECommand {

  private static int getMaxHomes(GlobalUser user) {
    for (String perk : user.getPerks()) {
      if (perk.startsWith("home.amount.")) {
        return Integer.parseInt(perk.substring(perk.lastIndexOf('.') + 1));
      }
    }
    return 1;
  }

  @Override
  public String getName() {
    return "setHome";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    String homeName = args.length > 0 ? args[0] : ConfigHandler.defaultHome;
    if (homeName.isEmpty()) {
      homeName = ConfigHandler.defaultHome;
    }
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (!homeName.equalsIgnoreCase("list") && setHome(player, homeName)) {
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .HOME_CREATED
              .replaceAll("%NAME%", homeName)
              .replaceAll("&", FORMATTING_CODE));
    } else {
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .HOME_FAILED
              .replaceAll("%NAME%", homeName)
              .replaceAll("&", FORMATTING_CODE));
    }
  }

  public boolean setHome(EntityPlayer player, String name) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player)[0];
      if (homeExists(name, data.getHomes())) {
        return false;
      }
      if (data.getMaxHomes() > data.getHomes().length) {
        data.addHome(new Home(name, new LocationWrapper(player.getPosition(), player.dimension)));
        return true;
      } else {
        return false;
      }
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser data = (LocalUser) UserManager.getPlayerData(player)[1];
      if (homeExists(name, data.getHomes())) {
        data.delHome(name);
        data.addHome(new Home(name, new LocationWrapper(player.getPosition(), player.dimension)));
        return true;
      }
      GlobalUser global = (GlobalUser) UserManager.getPlayerData(player)[0];
      if (getMaxHomes(global) > data.getHomes().length) {
        data.addHome(new Home(name, new LocationWrapper(player.getPosition(), player.dimension)));
        DataHelper.forceSave(Keys.LOCAL_USER, data);
        UserManager.PLAYER_DATA.put(player.getGameProfile().getId(), new Object[] {global, data});
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/setHome \u00A7b<name>";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("setH");
    alts.add("sHome");
    return alts;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_SETHOME.replaceAll("&", FORMATTING_CODE);
  }

  private static boolean homeExists(String name, Home[] homes) {
    for (Home home : homes) {
      if (home.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }
}
