package com.wurmcraft.serveressentials.common.teleport.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class TopCommand extends SECommand {

  @Override
  public String getName() {
    return "top";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/top";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    TeleportUtils.teleportTo(
        (EntityPlayerMP) player,
        new LocationWrapper(
            player.world.getTopSolidOrLiquidBlock(player.getPosition()), player.dimension),
        true);
    sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).TELEPORT_TOP));
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TOP;
  }
}
