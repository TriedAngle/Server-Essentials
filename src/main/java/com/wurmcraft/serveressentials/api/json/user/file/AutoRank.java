package com.wurmcraft.serveressentials.api.json.user.file;

import com.wurmcraft.serveressentials.api.json.user.DataType;

public class AutoRank implements DataType {

  private int playTime;
  private int balance;
  private int exp;
  private String rank;
  private String nextRank;

  public AutoRank() {}

  public AutoRank(AutoRank auto) {
    this.playTime = auto.playTime;
    this.balance = auto.balance;
    this.exp = auto.exp;
    this.rank = auto.rank;
    this.nextRank = auto.nextRank;
  }

  /**
   * Used as storage to hold values about th way a player rank's up
   *
   * @param playTime How long the player has been online
   * @param balance The amount of money the player currently has
   * @param exp The amount of levels in experience the player has
   * @param rank The player's current rank
   * @param nextRank The rank the player will get when these values are true
   */
  public AutoRank(int playTime, int balance, int exp, String rank, String nextRank) {
    this.playTime = playTime;
    this.balance = balance;
    this.exp = exp;
    this.rank = rank;
    this.nextRank = nextRank;
  }

  public int getPlayTime() {
    return playTime;
  }

  public void setPlayTime(int playTime) {
    this.playTime = playTime;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public int getExp() {
    return exp;
  }

  public void setExp(int exp) {
    this.exp = exp;
  }

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public String getNextRank() {
    return nextRank;
  }

  public void setNextRank(String nextRank) {
    this.nextRank = nextRank;
  }

  @Override
  public String getID() {
    return nextRank + "_" + rank;
  }
}