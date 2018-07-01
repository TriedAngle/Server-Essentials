package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.IDataType;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO Implement
public class GlobalData implements IDataType {

  private SpawnPoint spawn;
  private String[] rules;
  private String[] motd;
  private String website;
  private boolean lockdown;
  private String[] bannedMods;

  public GlobalData(SpawnPoint spawn, String[] rules, String[] motd, String website) {
    this.spawn = spawn;
    this.rules = rules;
    this.motd = motd;
    this.website = website;
    bannedMods = new String[0];
  }

  public SpawnPoint getSpawn() {
    return spawn;
  }

  public void setSpawn(SpawnPoint spawn) {
    this.spawn = spawn;
    DataHelper.forceSave(new File(ConfigHandler.saveLocation), this);
  }

  public String[] getRules() {
    return rules;
  }

  public void setRules(String[] rules) {
    this.rules = rules;
    DataHelper.forceSave(new File(ConfigHandler.saveLocation), this);
  }

  public void addRule(String rule) {
    String[] rules = getRules();
    if (rules != null) {
      List<String> listRules = new ArrayList<>();
      Collections.addAll(listRules, rules);
      listRules.add(rule);
      setRules(listRules.toArray(new String[0]));
    } else {
      setRules(new String[]{rule});
    }
  }

  public void removeRule(int id) {
    String[] rules = getRules();
    if (id < rules.length) {
      rules[id] = null;
      List<String> temp = new ArrayList<>();
      for (String rule : rules) {
        if (rule != null && rules.length > 0) {
          temp.add(rule);
        }
      }
      setRules(temp.toArray(new String[0]));
    }
  }

  public void removeMotd(int id) {
    String[] motd = getMotd();
    if (id < motd.length) {
      motd[id] = null;
      List<String> temp = new ArrayList<>();
      for (String mot : motd) {
        if (mot != null && rules.length > 0) {
          temp.add(mot);
        }
      }
      setMotd(temp.toArray(new String[0]));
    }
  }

  public String[] getMotd() {
    return motd;
  }

  public void setMotd(String[] motd) {
    this.motd = motd;
    DataHelper
        .forceSave(new File(ConfigHandler.saveLocation + File.separator + "Global.json"), this);
  }

  public void addMotd(String motd) {
    String[] motds = getMotd();
    if (motds != null) {
      List<String> listmotd = new ArrayList<>();
      Collections.addAll(listmotd, motds);
      listmotd.add(motd);
      setMotd(listmotd.toArray(new String[0]));
    } else {
      setMotd(new String[]{motd});
    }
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getWebsite() {
    return website;
  }

  public boolean getLockDown() {
    return lockdown;
  }

  public void setLockDown(boolean lock) {
    lockdown = lock;
  }

  public String[] getBannedMods() {
    return bannedMods;
  }

  @Override
  public String getID() {
    return "Global";
  }
}
