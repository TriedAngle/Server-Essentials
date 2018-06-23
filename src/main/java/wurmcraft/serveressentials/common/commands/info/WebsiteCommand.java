package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.utils.SECommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.reference.Perm;
import wurmcraft.serveressentials.common.utils.DataHelper2;

public class WebsiteCommand extends SECommand {

  public WebsiteCommand(Perm perm) {
    super(perm);
  }

  @Override
  public String getName() {
    return "website";
  }

  @Override
  public String[] getAltNames() {
    return new String[]{"web"};
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/website";
  }

  @Override
  public String getDescription() {
    return "Displays the server's website";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    ChatHelper.sendMessageTo(sender, TextFormatting.RED + Local.WEBSITE
        .replaceAll("#", TextFormatting.GOLD + DataHelper2.globalSettings.getWebsite()));
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
