package com.wurmcraft.serveressentials.common.teleport.commands.user.tpa;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "Teleportation")
public class TpDenyCommand extends SECommand {

  @Override
  public String getName() {
    return "tpDeny";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    long playerRequest = 0;
    for (long requestTime : TeleportationModule.activeRequests.keySet()) {
      if (TeleportationModule.activeRequests
          .get(requestTime)[1]
          .getGameProfile()
          .getId()
          .equals(player.getGameProfile().getId())) {
        playerRequest = requestTime;
        break;
      }
    }
    TeleportationModule.activeRequests.remove(playerRequest);
    sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).TPA_DENY));
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}