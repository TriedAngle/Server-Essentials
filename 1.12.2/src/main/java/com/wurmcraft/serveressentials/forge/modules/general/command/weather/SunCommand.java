package com.wurmcraft.serveressentials.forge.modules.general.command.weather;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General", name = "Sun")
public class SunCommand {

  @Command(inputArguments = {})
  public void enableSun(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.sun") || !SERegistry
        .isModuleLoaded("Rank")) {
      WorldServer world = null;
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        world = FMLCommonHandler.instance().getMinecraftServerInstance()
            .getWorld(sender.getCommandSenderEntity().dimension);
      } else {
        world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
      }
      world.getWorldInfo().setRaining(false);
      world.getWorldInfo().setThundering(false);
      world.getWorldInfo().setRainTime(0);
      sender.sendMessage(
          new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_SUN));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
