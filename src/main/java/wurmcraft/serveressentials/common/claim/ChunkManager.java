package wurmcraft.serveressentials.common.claim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.Location;
import wurmcraft.serveressentials.common.api.storage.RegionData;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
	* Utility class used for Claiming
	*
	* @author DemiSan
	*/
public class ChunkManager {

		//              Region Location, Data
		private static HashMap<Location, RegionData> claimData     = new HashMap<>();
		private static File                          SAVE_LOCATION = new File(DataHelper.saveLocation + File.separator + "Claims" + File.separator + "0");
		private static Gson                          gson          = new GsonBuilder().setPrettyPrinting().create();

		/**
			* Converts the In-Game position into its chunk counterpart
			*
			* @param pos In-Game location within the chunk
			*
			* @return Chunk from In-Game location
			*/
		public static final Location convertToChunk(BlockPos pos) {
				return new Location(pos.getX() >> 4, pos.getZ() >> 4);
		}

		/**
			* Converts a Chunk (Location) into a region variant
			*
			* @param loc Chunk location within the world
			*
			* @return Location of region in the world
			*/
		public static final Location convertToRegion(Location loc) {
				return new Location(loc.getX() >> 5, loc.getZ() >> 5);
		}

		/**
			* Converts the In-Game position into a region variant
			*
			* @param pos In-Game location within the region
			*
			* @return Region from In-Game Location
			*
			* @see #convertToChunk(BlockPos)
			* @see #convertToRegion(Location)
			*/
		public static final Location getRegionLocation(BlockPos pos) {
				return convertToRegion(convertToChunk(pos));
		}

		/**
			* Gives the RegionData for the specific region location
			*
			* @param loc Region Location within the world
			*
			* @return RegionData for the specified Location
			*
			* @see RegionData
			*/
		@Nullable
		public static final RegionData getRegion(Location loc) {
				for (Location l : claimData.keySet())
						if (l.getX() == loc.getX() && l.getZ() == loc.getZ()) return claimData.get(l); return null;
		}

		/**
			* Gives the RegionData for a specific region Location
			*
			* @param pos In-Game position within the Region
			*
			* @return RegionData for the specified In-Game position
			*
			* @see #getRegion(Location)
			*/
		public static final RegionData getRegion(BlockPos pos) {

				return getRegion(getRegionLocation(pos));
		}

		/**
			* Translates a In-Game Position into a chunk based index value used for data storage
			*
			* @param pos In-Game position used to translate and find Claim Data
			*
			* @return index number used within the region file to store the claim
			*
			* @see RegionData
			* @see Claim
			*/
		public static final int getIndexForClaim(BlockPos pos) {
				Location chunkLoc = convertToChunk(pos); Location regionLoc = convertToRegion(chunkLoc);
				int      x        = Math.abs(chunkLoc.getX() - (regionLoc.getX() * 32));
				int      z        = Math.abs(chunkLoc.getZ() - (regionLoc.getZ() * 32)); return (x * 32) + z;
		}

		public static final int getIndexForClaim(Location loc) {
				Location regionLoc = convertToRegion(loc); int x = Math.abs(loc.getX() - (regionLoc.getX() * 32));
				int      z         = Math.abs(loc.getZ() - (regionLoc.getZ() * 32)); return (x * 32) + z;
		}

		/**
			* Finds the Claim related to the In-Game position
			*
			* @param pos In-Game Position to look for the Claim
			*
			* @return Claim for the In-Game Position
			*
			* @see Claim
			*/
		@Nullable
		public static final Claim getClaim(BlockPos pos) {
				RegionData regionData = getRegion(pos); if (regionData != null) return regionData.getClaim(getIndexForClaim(pos));
				return null;
		}

		/**
			* Loads a specific Region File
			*
			* @param loc Region Location to look for Data
			*
			* @see RegionData
			*/
		public static void loadRegion(Location loc) {
				File regionFile = getFileForRegion(loc); try {
						RegionData data = gson.fromJson(Strings.join(IOUtils.readLines(new FileInputStream(regionFile)), ""), RegionData.class);
						addLoadedRegion(loc, data);
				} catch (FileNotFoundException e) {} catch (IOException e) {}
		}

		/**
			* Saves Region Data to a file / json
			*
			* @param loc    Location where the Region is located
			* @param region RegionData to save to a file
			*
			* @see RegionData
			*/
		public static void saveRegion(Location loc, RegionData region) {
				if (!SAVE_LOCATION.exists()) SAVE_LOCATION.mkdirs(); File regionFile = getFileForRegion(loc); try {
						regionFile.createNewFile(); FileUtils.writeStringToFile(regionFile, gson.toJson(region), "UTF-8");
				} catch (IOException e) {}
		}

		/**
			* Saves Region Data to a file / json
			*
			* @param pos    In-Game position where the Region is located
			* @param region RegionData to save to a file
			*
			* @see RegionData
			*/
		public static void saveRegion(BlockPos pos, RegionData region) {
				saveRegion(getRegionLocation(pos), region);
		}

		/**
			* Helps create a file for Region Data Storage
			*
			* @param loc Location / name of the Region being stored
			*
			* @return A file named based on its location
			*/
		public static File getFileForRegion(Location loc) {
				return new File(SAVE_LOCATION + File.separator + "c." + loc.getX() + "." + loc.getZ() + ".json");
		}

		/**
			* Helps load RegionData into the database
			*
			* @param loc  Location of the RegionData
			* @param data RegionData to add to the database
			*
			* @see #saveRegion(Location, RegionData)
			*/
		public static void addLoadedRegion(Location loc, RegionData data) {
				if (!claimData.containsKey(loc)) claimData.put(loc, data);
		}

		/**
			* Removes RegionData from the database
			*
			* @param loc Location of the Region to remove from memory
			*/
		public static void removeLoadedRegion(Location loc) {
				if (claimData.containsKey(loc)) claimData.remove(loc);
		}

		/**
			* Saves and reloads RegionData for the database
			*
			* @param loc  Location of the Region
			* @param data RegionData to save and reload / override
			*/
		public static void handleRegionUpdate(Location loc, RegionData data) {
				saveRegion(loc, data); removeLoadedRegion(loc); loadRegion(loc);
		}

		public static void forceSaveAll() {
				for (Location loc : claimData.keySet())
						saveRegion(loc, claimData.get(loc));
		}
}