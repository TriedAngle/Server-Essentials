package wurmcraft.serveressentials.common.commands.info;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import org.apache.commons.lang3.time.DurationFormatUtils;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.commands.EssentialsCommand;
import wurmcraft.serveressentials.common.utils.DataHelper;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class OnlineTimeCommand extends EssentialsCommand {

	public OnlineTimeCommand (String perm) {
		super (perm);
	}

	@Override
	public String getCommandName () {
		return "onlineTime";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {
		return "/onlineTime | /onlineTime <username>";
	}

	private static final class AbstractUsernameCollection<T extends String> extends AbstractCollection<T> {
		List<T> names;
		{
			names = new LinkedList<>();
		}
		public AbstractUsernameCollection(Collection<T> values) {
			for (T value : values) add(value);
		}
		@Override
		public boolean contains(Object s) {
			Iterator<T> itr = this.iterator();
			if(s == null) {
				while(itr.hasNext()) {
					if(itr.next() == null) {
						return true;
					}
				}
			} else {
				while(itr.hasNext()) {
					if(((String)s).equalsIgnoreCase(itr.next())) {
						return true;
					}
				}
			}
			return false;
		}
		@Override
		public Iterator<T> iterator() {return names.iterator();}
		@Override
		public boolean add(T s) {
			if (!contains(s)) {
				names.add(s);
				return true;
			} else return false;
		}
		@Override
		public int size() {
			return 0;
		}
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		super.execute (server,sender,args);
			List<String> unknownPlayers = new ArrayList<>();
			new HashMap<UUID, PlayerData>() {
				{
					if (args.length==0) {
						UsernameCache.getMap().forEach((uuid, s) -> put(uuid, DataHelper.loadPlayerData(uuid)));
					} else {
						AbstractUsernameCollection<String> usernames = new AbstractUsernameCollection<String>(UsernameCache.getMap().values());
						for (String arg : args) {
							UsernameCache.getMap().forEach((uuid, s) -> {
								if (s.equalsIgnoreCase(arg)) put(uuid, DataHelper.loadPlayerData(uuid));
								else if (!usernames.contains(s)) unknownPlayers.add(s);
							});
						}
					}
				}
			}.forEach((uuid, pd) -> {
				unknownPlayers.forEach(s -> ChatHelper.sendMessageTo(sender, TextFormatting.RED + "Unknown Player: '" + s + "'"));
				String formatted = DurationFormatUtils
						.formatDuration(pd.getOnlineTime()*60000, "d%:H$:m#:s@")
						.replace('%', 'D')
						.replace('$', 'H')
						.replace('#', 'M')
						.replace('@', 'S')
						.replaceAll(":", ", ");
				ChatHelper.sendMessageTo(sender, TextFormatting.GREEN + UsernameCache.getLastKnownUsername(uuid) +
				TextFormatting.DARK_AQUA + " : " + formatted);
			});

	}

	@Override
	public Boolean isPlayerOnly () {
		return false;
	}

	@Override
	public String getDescription () {
		return "Find out how long you have played on the server.";
	}

	@Override
	public List <String> getCommandAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("onlinetime");
		aliases.add ("OnlineTime");
		aliases.add ("ONLINETIME");
		aliases.add ("OT");
		aliases.add ("ot");
		return aliases;
	}

	@Override
	public List <String> getTabCompletionOptions (MinecraftServer server,ICommandSender sender,String[] args,@Nullable BlockPos pos) {
		List <String> usernames = new ArrayList <> ();
		Collections.addAll (usernames,server.getAllUsernames ());
		return usernames;
	}
}
