package wurmcraft.serveressentials.common.utils;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import wurmcraft.serveressentials.common.claim.ChunkManager;
import wurmcraft.serveressentials.common.commands.admin.*;
import wurmcraft.serveressentials.common.commands.chat.*;
import wurmcraft.serveressentials.common.commands.claim.ClaimCommand;
import wurmcraft.serveressentials.common.commands.claim.RemoveClaimCommand;
import wurmcraft.serveressentials.common.commands.eco.BalTopCommand;
import wurmcraft.serveressentials.common.commands.eco.MoneyCommand;
import wurmcraft.serveressentials.common.commands.eco.PayCommand;
import wurmcraft.serveressentials.common.commands.info.*;
import wurmcraft.serveressentials.common.commands.item.KitCommand;
import wurmcraft.serveressentials.common.commands.item.RenameCommand;
import wurmcraft.serveressentials.common.commands.item.SendItemCommand;
import wurmcraft.serveressentials.common.commands.item.SkullCommand;
import wurmcraft.serveressentials.common.commands.player.*;
import wurmcraft.serveressentials.common.commands.teleport.*;
import wurmcraft.serveressentials.common.reference.Perm;

public class LoadHelper {

	public static void registerCommands (FMLServerStartingEvent e) {
		e.registerServerCommand (new SetHomeCommand (Perm.SET_HOME));
		e.registerServerCommand (new HomeCommand (Perm.HOME));
		e.registerServerCommand (new DelHome (Perm.DEL_HOME));
		e.registerServerCommand (new SetWarpCommand (Perm.SET_WARP));
		e.registerServerCommand (new WarpCommand (Perm.WARP));
		e.registerServerCommand (new DelWarp (Perm.DEL_WARP));
		e.registerServerCommand (new SetSpawnCommand (Perm.SET_SPAWN));
		e.registerServerCommand (new SpawnCommand (Perm.SPAWN));
		e.registerServerCommand (new InvseeCommand (Perm.INVSEE));
		e.registerServerCommand (new EnderChestCommand (Perm.ENDER_CHEST));
		e.registerServerCommand (new SudoCommand (Perm.SUDO));
		e.registerServerCommand (new SeenCommand (Perm.SEEN));
		e.registerServerCommand (new HealCommand (Perm.HEAL));
		e.registerServerCommand (new GameModeCommand (Perm.GAMEMODE));
		e.registerServerCommand (new RulesCommand (Perm.RULES));
		e.registerServerCommand (new AddRuleCommand (Perm.ADD_RULE));
		e.registerServerCommand (new DeleteRuleCommand (Perm.DEL_RULE));
		e.registerServerCommand (new MotdCommand (Perm.MOTD));
		e.registerServerCommand (new AddMotdCommand (Perm.ADD_MOTD));
		e.registerServerCommand (new DeleteMotdCommand (Perm.DEL_MOTD));
		e.registerServerCommand (new TpaCommand (Perm.TPA));
		e.registerServerCommand (new TpacceptCommand (Perm.TPA_ACCEPT));
		e.registerServerCommand (new TpdenyCommand (Perm.TPA_DENY));
		e.registerServerCommand (new TeamCommand (Perm.TEAM));
		e.registerServerCommand (new TeamAdminCommand (Perm.TEAM_ADMIN));
		e.registerServerCommand (new ClaimCommand (Perm.CLAIM));
		e.registerServerCommand (new RemoveClaimCommand (Perm.DEL_CLAIM));
		e.registerServerCommand (new FlyCommand (Perm.FLY));
		e.registerServerCommand (new AfkCommand (Perm.AFK));
		e.registerServerCommand (new BroadcastCommand (Perm.BROADCAST));
		e.registerServerCommand (new PingCommand (Perm.PING));
		e.registerServerCommand (new SkullCommand (Perm.SKULL));
		e.registerServerCommand (new BackCommand (Perm.BACK));
		e.registerServerCommand (new DPFCommand (Perm.DEL_PLAYER_FILE));
		e.registerServerCommand (new ReloadPlayerDataCommand (Perm.RELOAD_PLAYER_DATA));
		e.registerServerCommand (new TpCommand (Perm.TELEPORT));
		e.registerServerCommand (new FreezeCommand (Perm.FREEZE));
		e.registerServerCommand (new TopCommand (Perm.TOP));
		e.registerServerCommand (new SuicideCommand (Perm.SUICIDE));
		e.registerServerCommand (new ListCommand (Perm.LIST));
		e.registerServerCommand (new RenameCommand (Perm.RENAME));
		e.registerServerCommand (new ChannelCommand (Perm.CHANNEL));
		e.registerServerCommand (new MuteCommand (Perm.MUTE));
		e.registerServerCommand (new MoneyCommand (Perm.MONEY));
		e.registerServerCommand (new PayCommand (Perm.PAY));
		e.registerServerCommand (new VaultCommand (Perm.VAULT));
		e.registerServerCommand (new HelpCommand (Perm.HELP));
		e.registerServerCommand (new WebsiteCommand (Perm.WEBSITE));
		e.registerServerCommand (new SpeedCommand (Perm.SPEED));
		e.registerServerCommand (new MsgCommand (Perm.MESSAGE));
		e.registerServerCommand (new MailCommand (Perm.MAIL));
		e.registerServerCommand (new SendItemCommand (Perm.SEND_ITEM));
		e.registerServerCommand (new KitAdminCommand (Perm.KIT_ADMIN));
		e.registerServerCommand (new KitCommand (Perm.KIT));
		e.registerServerCommand (new ReplyCommand (Perm.REPLY));
		e.registerServerCommand (new SpyCommand (Perm.SPY));
		e.registerServerCommand (new NickCommand (Perm.NICK));
		e.registerServerCommand (new SetGroup (Perm.SET_GROUP));
		e.registerServerCommand (new OnlineTimeCommand (Perm.ONLINE_TIME));
		e.registerServerCommand (new AutoRankCommand (Perm.AUTORANK));
		e.registerServerCommand (new TpHereCommand (Perm.TP_HERE));
		e.registerServerCommand (new TpLockCommand (Perm.TPLOCK));
		e.registerServerCommand (new PreGenCommand (Perm.PREGEN));
		e.registerServerCommand (new RandomTeleportCommand (Perm.RTP));
		e.registerServerCommand (new BalTopCommand (Perm.BAL_TOP));
	}

	public static void loadData () {
		DataHelper.createDefaultRank ();
		DataHelper.loadWarps ();
		DataHelper.loadGlobal ();
		DataHelper.loadRanks ();
		DataHelper.loadAllTeams ();
		ChunkManager.loadAllClaims ();
		DataHelper.createDefaultChannels ();
		DataHelper.loadAllChannels ();
		DataHelper.loadAllKits ();
		DataHelper.loadAllAutoRanks ();
		DataHelper.createDefaultAutoRank ();
	}
}
