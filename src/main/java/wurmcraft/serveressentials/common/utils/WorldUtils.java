package wurmcraft.serveressentials.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldUtils {

  // TODO NON DEV Suppport / Fix NBT Save Issue
  public static void setSignText(World world, BlockPos pos, String[] sign) {
    if (world.getTileEntity(pos) instanceof TileEntitySign) {
      TileEntitySign signTile = (TileEntitySign) world.getTileEntity(pos);
      try {
        TextComponentString[] text = new TextComponentString[4];
				for (int index = 0; index < 4; index++) {
					if (sign.length > index) {
						text[index] = new TextComponentString(sign[index]);
					} else {
						text[index] = new TextComponentString("");
					}
				}
        Field field = setFinalStatic(signTile.getClass(), "signText", text);
        field.set(signTile, text);
        signTile.markDirty();
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
      } catch (Exception f) {
        f.printStackTrace();
      }
    }
  }

  private static Field setFinalStatic(Class clazz, String fieldName, Object newValue)
      throws NoSuchFieldException, IllegalAccessException {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    Field modifiers = field.getClass().getDeclaredField("modifiers");
    modifiers.setAccessible(true);
    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    return field;
  }

  public static double getTPS(int dimID) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    long sum = 0L;
    long[] ticks = server.worldTickTimes.get(dimID);
		for (int i = 0; i < ticks.length; i++) {
			sum += ticks[i];
		}
    double tps = (double) sum / (double) ticks.length * 1.0E-6D;
		if (tps < 50) {
			return 20;
		} else {
			return 1000 / tps;
		}
  }

  public static double getTPS() {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    double tickSum = 0;
		for (int i = 0; i < server.tickTimeArray.length; ++i) {
			tickSum += server.tickTimeArray[i];
		}
    tickSum /= server.tickTimeArray.length;
    return 1000000000 / tickSum;
  }
}
