package com.wurmcraft.serveressentials.common.protect;

import com.wurmcraft.serveressentials.api.protection.Town;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TownCache extends Town {

  public Town town;
  public List<AxisAlignedBB> claimedAreas;

  public TownCache(Town town) {
    this.town = town;
    updateClaimedAreaCache();
  }

  public void updateClaimedAreaCache() {
    town.getClaimedArea()
        .forEach(
            area ->
                claimedAreas.add(
                    new AxisAlignedBB(area.locationA.getPos(), area.locationB.getPos())));
  }

  public boolean isAreaClaimed(BlockPos pos) {
    for (AxisAlignedBB bb : claimedAreas) {
      if (bb.contains(new Vec3d(pos.getX(), pos.getY(), pos.getZ()))) {
        return true;
      }
    }
    return false;
  }
}
