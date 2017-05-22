package wurmcraft.serveressentials.common.commands.info;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.storage.Mail;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.List;

public class MailCommand extends EssentialsCommand {

	public MailCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "mail";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/mail send <name> <message> | /mail read | /mail delete <#>";
	}

	@Override
	public Boolean isPlayerOnly () {
		return true;
	}

	@Override
	public String getDescription () {
		return "Send and read messages from offline players";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Mail");
		aliases.add ("MAIL");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase ("send")) {
				if (args.length >= 2) {
					for (EntityPlayer p : server.getPlayerList ().getPlayerList ())
						if (UsernameCache.getLastKnownUsername (p.getGameProfile ().getId ()).equalsIgnoreCase (args[1])) {
							if (args.length >= 3) {
								List <String> messageLines = new ArrayList <> ();
								for (int index = 2; index < args.length; index++)
									messageLines.add (args[index]);
								Mail mail = new Mail (player.getGameProfile ().getId (),p.getGameProfile ().getId (),Strings.join (messageLines," "));
								DataHelper.addMail (mail);
								ChatHelper.sendMessageTo (player,Local.MAIL_SENT);
							} else
								ChatHelper.sendMessageTo (player,Local.MISSING_MESSAGE);
						}
				} else
					ChatHelper.sendMessageTo (player,Local.PLAYER_NOT_FOUND.replaceAll ("#","None"));
			} else if (args[0].equalsIgnoreCase ("read")) {
				List <Mail> playerMail = DataHelper.getPlayerData (player.getGameProfile ().getId ()).getMail ();
				if (playerMail.size () > 0) {
					ChatHelper.sendMessageTo (player,Local.SPACER);
					for (int index = 0; index < playerMail.size (); index++)
						ChatHelper.sendMessageTo (player,"[" + index + "] " + UsernameCache.getLastKnownUsername (playerMail.get (index).getSender ()) + " " + playerMail.get (index).getMessage ());
					ChatHelper.sendMessageTo (player,Local.SPACER);
				} else
					ChatHelper.sendMessageTo (player,Local.NO_MAIL);
			} else if (args[0].equalsIgnoreCase ("delete") || args[0].equalsIgnoreCase ("del")) {
				if (args.length >= 2) {
					Integer mailNo = Integer.valueOf (args[1]);
					DataHelper.removeMail (player.getGameProfile ().getId (),mailNo);
					ChatHelper.sendMessageTo (player,Local.MAIL_REMOVED);
				} else
					ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
			}
		} else
			ChatHelper.sendMessageTo (sender,getCommandUsage (sender));
	}
}
