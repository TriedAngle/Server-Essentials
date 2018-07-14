package com.wurmcraft.serveressentials.common.security.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.security.SecurityModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.List;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Security")
public class ModsCommand extends SECommand {

  @Override
  public String getName() {
    return "mods";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        sender.sendMessage(
            new TextComponentString(
                TextFormatting.AQUA + Strings.join(SecurityModule.getPlayerMods(player), ",")));
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/mods \u00A7b<player>";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_MODS.replaceAll("&", "\u00A7");
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return autoCompleteUsername(args, 0);
  }
}
