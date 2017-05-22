package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import wurmcraft.serveressentials.common.api.storage.Global;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesCommand extends EssentialsCommand {

	private static final int LIST_SIZE = 7;

	public RulesCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "rules";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "rules";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("Rules");
		aliases.add ("RULES");
		return aliases;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		Global globalSettings = DataHelper.globalSettings;
		if (globalSettings.getRules () != null && globalSettings.getRules ().length > 0) {
			if (globalSettings.getRules ().length > LIST_SIZE) {
				if (args.length == 0) {
					String[] temp = Arrays.copyOfRange (globalSettings.getRules (),0,LIST_SIZE);
					for (String rule : temp)
						ChatHelper.sendMessageTo (sender,rule.replaceAll ("&","\u00A7"));
				} else {
					int pageNo = Integer.valueOf (args[0]);
					if (pageNo <= (globalSettings.getRules ().length / LIST_SIZE)) {
						String[] temp = Arrays.copyOfRange (globalSettings.getRules (),(pageNo * LIST_SIZE),(pageNo * LIST_SIZE) + LIST_SIZE);
						for (String rule : temp)
							if (rule != null && rule.length () > 0)
								ChatHelper.sendMessageTo (sender,rule.replaceAll ("&","\u00A7"));
					} else
						ChatHelper.sendMessageTo (sender,Local.PAGE_NONE.replaceAll ("#",args[0]).replaceAll ("$",(globalSettings.getRules ().length / LIST_SIZE) + ""));
				}
			} else
				for (String rule : globalSettings.getRules ())
					ChatHelper.sendMessageTo (sender,rule.replaceAll ("&","\u00A7"));
		} else
			ChatHelper.sendMessageTo (sender,Local.NO_RULES);
	}

	@Override
	public String getDescription () {
		return "Displays the server's Rules";
	}
}
