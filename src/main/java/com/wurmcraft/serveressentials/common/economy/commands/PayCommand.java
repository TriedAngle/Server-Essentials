package com.wurmcraft.serveressentials.common.economy.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import com.wurmcraft.serveressentials.api.json.user.optional.Coin;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;

@Command(moduleName = "Economy")
public class PayCommand extends SECommand {

  @Override
  public String getName() {
    return "pay";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 2 || args.length == 3) {
      GlobalUser user = forceUserFromUUID(UsernameResolver.getUUIDFromName(args[0]));
      GlobalUser global =
          (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
      if (user != null) {
        try {
          double amount = Double.parseDouble(args[1]);
          String currency = ConfigHandler.serverCurrency.replaceAll(" ", "_");
          if (args.length == 3) {
            currency = args[2];
          }
          if (setCurrency(global.getBank(), currency, amount, false)) {
            setCurrency(user.getBank(), currency, amount, true);
            RequestHelper.UserResponses.overridePlayerData(user);
            RequestHelper.UserResponses.overridePlayerData(global);
            UserManager.playerData.put(
                player.getGameProfile().getId(),
                new Object[] {
                  global,
                  DataHelper.load(Keys.LOCAL_USER, new LocalUser(player.getGameProfile().getId()))
                });
            player.sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                        .PAYED
                        .replaceAll(
                            "%PLAYER%",
                            UsernameCache.getLastKnownUsername(
                                UsernameResolver.getUUIDFromName(args[0])))
                        .replaceAll("%COIN%", currency)
                        .replaceAll("%AMOUNT%", "" + amount)));
          }
        } catch (NumberFormatException e) {
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).INVALID_NUMBER.replaceAll("%NUMBER%", args[1])));
        }
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  public static boolean setCurrency(Bank bank, String currencyName, double amount, boolean add) {
    boolean completed = false;
    if (bank.currency.length == 0) {
      bank = initBank(bank);
    }
    for (Coin c : bank.currency) {
      if (c.name.equalsIgnoreCase(currencyName)) {
        if (!add && c.amount - amount > 0) return false;
        if (add) {
          c.amount += amount;
        } else {
          c.amount -= amount;
        }
        completed = true;
        break;
      }
    }
    return completed;
  }

  private static Bank initBank(Bank bank) {
    Coin[] coins = new Coin[ConfigHandler.activeCurrency.length];
    for (int index = 0; index < ConfigHandler.activeCurrency.length; index++) {
      coins[index] = new Coin(ConfigHandler.activeCurrency[index], 0);
    }
    bank.currency = coins;
    return bank;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}