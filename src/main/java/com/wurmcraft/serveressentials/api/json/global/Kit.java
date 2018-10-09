package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.common.utils.StackConverter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class Kit implements DataType {

  private String name;
  private String[] items;
  private int time;

  public Kit() {}

  public Kit(String name, ItemStack[] items, int time) {
    this.name = name;
    List<String> stack = new ArrayList<>();
    for (ItemStack item : items) {
      stack.add(StackConverter.toString(item));
    }
    this.items = stack.toArray(new String[0]);
    this.time = time;
  }

  public ItemStack[] getItems() {
    List<ItemStack> stacks = new ArrayList<>();
    for (String item : items) {
      stacks.add(StackConverter.getData(item));
    }
    return stacks.toArray(new ItemStack[0]);
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setItems(ItemStack[] items) {
    List<String> stack = new ArrayList<>();
    for (ItemStack item : items) {
      stack.add(StackConverter.toString(item));
    }
    this.items = stack.toArray(new String[0]);
  }

  @Override
  public String getID() {
    return name;
  }
}
