package com.enotakin.library.api.util;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.enotakin.library.api.entity.MineRenderedEntity;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityStatus;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public final class MCHelper {

    private static final String TEXTURES_KEY = "textures";
    private static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger();
    private static final Location TEMP_LOCATION = new Location(null, 0D, 0D, 0D);
    private static final BlockPosition.MutableBlockPosition TEMP_POSITION = new BlockPosition.MutableBlockPosition();

    public static final BlockFace[] AXIS = {
            BlockFace.SOUTH,
            BlockFace.WEST,
            BlockFace.NORTH,
            BlockFace.EAST
    };

    private MCHelper() {
    }

    public static int nextEntityID() {
        return ENTITY_ID_COUNTER.decrementAndGet();
    }

    @SuppressWarnings("deprecation")
    public static int getEntityTypeID(EntityType type) {
        Optional<EntityTypes<?>> optional = EntityTypes.getByName(type.getName());
        return optional
                .map(IRegistry.ENTITY_TYPE::a)
                .orElse(-1);
    }

    public static net.minecraft.server.v1_16_R3.Chunk toHandle(Chunk chunk) {
        return ((CraftChunk) chunk).getHandle();
    }

    public static net.minecraft.server.v1_16_R3.WorldServer toHandle(World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static net.minecraft.server.v1_16_R3.Entity toHandle(Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }

    public static net.minecraft.server.v1_16_R3.EntityPlayer toHandle(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static boolean isDebugWorld(World world) {
        WorldServer handle = toHandle(world);
        return handle.isDebugWorld();
    }

    public static void updateScaledHealth(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.updateScaledHealth();
    }

    @SuppressWarnings("deprecation")
    public static GameMode getPreviousGameMode(Player player) {
        EntityPlayer handle = toHandle(player);
        return GameMode.getByValue(handle.playerInteractManager.c().getId());
    }

    public static void dropItem(Player player, ItemStack itemStack) {
        EntityPlayer handle = toHandle(player);
        handle.drop(CraftItemStack.asNMSCopy(itemStack), false);
    }

    public static void playTotemAnimation(Player player, int customModelData) {
        ItemStack itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);

        playTotemAnimation(player, itemStack);
    }

    public static void playTotemAnimation(Player player, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.TOTEM_OF_UNDYING) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();

        inventory.setItemInMainHand(itemStack);

        WrapperPlayServerEntityStatus wrapper = new WrapperPlayServerEntityStatus();
        wrapper.setEntityID(player.getEntityId());
        wrapper.setEntityStatus((byte) 35);
        wrapper.sendPacket(player);

        inventory.setItemInMainHand(itemInMainHand);
    }

    public static Material getTypeAt(Location location) {
        return getTypeAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Material getTypeAt(org.bukkit.World world, int blockX, int blockY, int blockZ) {
        TEMP_POSITION.setValues(blockX, blockY, blockZ);

        WorldServer handle = toHandle(world);
        IBlockData blockData = handle.getType(TEMP_POSITION);
        return blockData.getBukkitMaterial();
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        EntityPlayer handle = toHandle(player);
        handle.playerConnection.sendPacket(packet);
    }

    public static void clearPathfinding(EntityInsentient entity) {
        clearPathfinding(entity.goalSelector);
        clearPathfinding(entity.targetSelector);
    }

    private static void clearPathfinding(PathfinderGoalSelector goalSelector) {
        try {
            Map<PathfinderGoal.Type, PathfinderGoalWrapped> lockedFlags = Reflect.get(goalSelector, "c");
            lockedFlags.clear();

            Set<PathfinderGoalWrapped> availableGoals = Reflect.get(goalSelector, "d");
            availableGoals.clear();
        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to clear pathfinding:", exception);
        }
    }

    public static Entity spawnEntity(net.minecraft.server.v1_16_R3.Entity entity, Location location) {
        location.getChunk(); // Load the chunk before adding the entity

        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        if (entity instanceof EntityLiving livingEntity) {
            livingEntity.aA = entity.yaw;
            livingEntity.aB = entity.yaw;
            livingEntity.aC = entity.yaw;
            livingEntity.aD = entity.yaw;
        }

        WorldServer handle = toHandle(location.getWorld());
        handle.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity.getBukkitEntity();
    }

    public static Location getTempLocation(Entity entity) {
        return entity.getLocation(TEMP_LOCATION);
    }

    public static Location getTempLocation(Block block) {
        return block.getLocation(TEMP_LOCATION);
    }

    public static Location getTempLocation(MineRenderedEntity entity) {
        return entity.getLocation(TEMP_LOCATION);
    }

    public static ItemStack setSkullValue(ItemStack itemStack, String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.put(TEXTURES_KEY, new Property(TEXTURES_KEY, value));
        return setGameProfile(itemStack, gameProfile);
    }

    public static ItemStack setSkullPlayer(ItemStack itemStack, Player player) {
        EntityPlayer handle = toHandle(player);
        GameProfile profile = handle.getProfile();

        GameProfile cloneProfile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap properties = cloneProfile.getProperties();

        for (Property property : profile.getProperties().get(TEXTURES_KEY)) {
            if (TEXTURES_KEY.equals(property.getName())) {
                properties.put(TEXTURES_KEY, new Property(TEXTURES_KEY, property.getValue()));
            }
        }

        return setGameProfile(itemStack, cloneProfile);
    }

    private static ItemStack setGameProfile(ItemStack itemStack, GameProfile profile) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof SkullMeta skullMeta) {
            Class<?> craftMetaSkull = MinecraftReflection.getCraftBukkitClass("inventory.CraftMetaSkull");
            Reflect.set(craftMetaSkull, skullMeta, "profile", profile);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public static int getTotalExperience(int level) {
        if (level < 0) {
            throw new IllegalStateException("Level cannot be negative: " + level);
        }

        if (level <= 16) {
            return Mth.square(level) + 6 * level;
        } else if (level <= 31) {
            return Mth.floor(2.5F * Mth.square(level) - 40.5F * level + 360);
        } else {
            return Mth.floor(4.5F * Mth.square(level) - 162.5F * level + 2220);
        }
    }

    public static int getLevel(int experience) {
        if (experience < 0) {
            throw new IllegalStateException("Experience cannot be negative: " + experience);
        }

        if (experience <= 352) {
            return (int) Mth.floor(Math.sqrt(experience + 9) - 3);
        } else if (experience <= 1507) {
            return (int) Mth.floor(81F / 10F + Math.sqrt((2F / 5F) * (experience - 7839F / 40F)));
        } else {
            return (int) Mth.floor(325F / 18F + Math.sqrt(2F / 9F * (experience - 54215F / 72F)));
        }
    }

    public static int getRequiredExperience(int level) {
        if (level <= 0) {
            throw new IllegalStateException("Level cannot be zero or negative: " + level);
        }

        level = level - 1;

        if (level <= 14) {
            return 7 + level * 2;
        } else if (level <= 29) {
            return 37 + (level - 15) * 5;
        } else {
            return 112 + (level - 30) * 9;
        }
    }

    public static BlockFace toBlockFace(float yaw) {
        return AXIS[Mth.floor(yaw / 90F + 0.5F) & 3];
    }

    public static BlockFace nextBlockFace(BlockFace face, int offset) {
        int ordinal = face.ordinal();
        if (ordinal >= AXIS.length) {
            throw new IllegalArgumentException();
        }
        return AXIS[(ordinal + offset + 2) & 3];
    }

    public static Vector rotateAroundAxisX(Vector vector, double angle) {
        return rotateAroundAxisX(vector, Math.cos(angle), Math.sin(angle));
    }

    public static Vector rotateAroundAxisX(Vector vector, double cos, double sin) {
        double y = vector.getY() * cos - vector.getZ() * sin;
        double z = vector.getY() * sin + vector.getZ() * cos;

        return vector
                .setY(y)
                .setZ(z);
    }

    public static Vector rotateAroundAxisY(Vector vector, double angle) {
        return rotateAroundAxisY(vector, Math.cos(angle), Math.sin(angle));
    }

    public static Vector rotateAroundAxisY(Vector vector, double cos, double sin) {
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector
                .setX(x)
                .setZ(z);
    }

    public static Vector rotateAroundAxisZ(Vector vector, double angle) {
        return rotateAroundAxisZ(vector, Math.cos(angle), Math.sin(angle));
    }

    public static Vector rotateAroundAxisZ(Vector vector, double cos, double sin) {
        double x = vector.getX() * cos - vector.getY() * sin;
        double y = vector.getX() * sin + vector.getY() * cos;

        return vector
                .setX(x)
                .setY(y);
    }

}
