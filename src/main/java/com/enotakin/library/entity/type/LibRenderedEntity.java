package com.enotakin.library.entity.type;

import com.enotakin.library.api.entity.MineRenderedEntity;
import com.enotakin.library.api.protocol.PacketWrapper;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.entity.Interact;
import com.enotakin.library.api.util.MCHelper;
import com.enotakin.library.api.util.Mth;
import com.enotakin.library.entity.EntityManager;
import com.enotakin.library.entity.WorldRepository;

import java.util.*;

public class LibRenderedEntity implements MineRenderedEntity {

    private static final double TRUNCATION_STEPS = 32D * 128D;

    protected final MineLibraryPlugin plugin;

    protected World world;

    private double newX;
    private double newY;
    private double newZ;
    private float newYaw;
    private float newPitch;

    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;

    protected boolean onGround;
    private boolean updateLocation;

    private boolean autoVisible;
    private double renderDistance;
    private double renderDistanceSq;
    private Interact interact;

    protected int ticks;
    protected boolean removed;

    private final Set<Player> canSee = new HashSet<>();
    protected final Set<Player> viewers = new HashSet<>();

    public LibRenderedEntity(MineLibraryPlugin plugin, Location location) {
        this.plugin = plugin;

        world = location.getWorld();

        newX = x = location.getX();
        newY = y = location.getY();
        newZ = z = location.getZ();
        newYaw = yaw = location.getYaw();
        newPitch = pitch = location.getPitch();

        onGround = false;
    }

    @Override
    public boolean isAutoVisible() {
        return autoVisible;
    }

    @Override
    public void setAutoVisible(boolean autoVisible) {
        if (this.autoVisible != autoVisible) {
            this.autoVisible = autoVisible;
            this.updateVisibility();
        }
    }

    @Override
    public double getRenderDistance() {
        return renderDistance;
    }

    @Override
    public void setRenderDistance(double renderDistance) {
        renderDistance = Mth.clamp(renderDistance, 0D, 120D);

        if (this.renderDistance != renderDistance) {
            this.renderDistance = renderDistance;
            this.renderDistanceSq = Mth.square(renderDistance);
            this.updateVisibility();
        }
    }

    @Override
    public Interact getInteract() {
        return interact;
    }

    @Override
    public void setInteract(Interact interact) {
        this.interact = interact;
    }

    @Override
    public boolean showTo(Player player) {
        if (!autoVisible && canSee.add(player)) {
            addPlayer(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean isVisibleTo(Player player) {
        return autoVisible || canSee.contains(player);
    }

    @Override
    public boolean hideTo(Player player) {
        if (!autoVisible && canSee.remove(player)) {
            removePlayer(player);
            return true;
        }
        return false;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public Location getLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public Location getLocation(Location location) {
        if (location != null) {
            location.setWorld(world);
            location.setX(x);
            location.setY(y);
            location.setZ(z);
            location.setYaw(yaw);
            location.setPitch(pitch);
        }
        return location;
    }

    @Override
    public void teleport(World world, double x, double y, double z, float yaw, float pitch) {
        Preconditions.checkArgument(world != null, "");

        if (this.world.equals(world)) {
            this.newX = x;
            this.newY = y;
            this.newZ = z;
            this.newYaw = yaw;
            this.newPitch = pitch;
        } else {
            EntityManager manager = plugin.getManager(EntityManager.class);
            manager.removeEntity(this);

            this.world = world;
            this.newX = this.x = x;
            this.newY = this.y = y;
            this.newZ = this.z = z;
            this.newYaw = this.yaw = yaw;
            this.newPitch = this.pitch = pitch;

            manager.addEntity(this);
        }
    }

    @Override
    public boolean isRemoved() {
        return removed;
    }

    @Override
    public boolean remove() {
        if (removed) {
            return false;
        }

        EntityManager manager = plugin.getManager(EntityManager.class);
        manager.removeEntity(this);

        removed = true;
        return true;
    }

    public void addPlayer(Player player) {
        if (isOutOfRadius(player)) {
            return;
        }

        if (viewers.add(player)) {
            onShowTo(player);
        }
    }

    public void removePlayer(Player player) {
        if (viewers.remove(player)) {
            onHideTo(player);
        }
    }

    private void updateVisibility() {
        WorldRepository.ChunkRepository repository = getChunkRepository(x, z, true);
        Collection<Player> chunkPlayers = repository.getPlayers();

        // Удаляем наблюдателя и скрываем сущность
        // всем игрокам кто ее видит сейчас, но не должен
        List<Player> players = new ArrayList<>(viewers);
        for (Player player : players) {
            if (isOutOfRadius(player) || !isVisibleTo(repository, player)) {
                removePlayer(player);
            }
        }

        // Пытаемся показать всем игрокам,
        // которые могли бы видеть эту сущность
        for (Player player : chunkPlayers) {
            if (isVisibleTo(repository, player)) {
                // Метод addPlayer() проверяет дистанцию радиуса внутри
                addPlayer(player);
            }
        }
    }

    private boolean isVisibleTo(WorldRepository.ChunkRepository repository, Player player) {
        return repository.getPlayers().contains(player) && isVisibleTo(player);
    }

    private boolean isOutOfRadius(Player player) {
        if (renderDistance > 0D) {
            Location location = MCHelper.getTempLocation(player);
            return distanceSq(location) > renderDistanceSq;
        }
        return false;
    }

    public void onShowTo(Player player) {
    }

    public void onHideTo(Player player) {
    }

    public void onTick() {
        if (renderDistance > 0D) {
            updateVisibility();
        }

        // Проверка изменения локации сущности
        boolean moved = newX != x || newY != y || newZ != z;
        boolean rotated = newYaw != yaw || newPitch != pitch;

        if (moved) {
            WorldRepository.ChunkRepository fromRepository = getChunkRepository(x, z, false);
            WorldRepository.ChunkRepository toRepository = getChunkRepository(newX, newZ, true);

            if (fromRepository != null && !fromRepository.equals(toRepository)) {
                List<Player> players = new ArrayList<>(viewers);
                for (Player player : players) {
                    boolean outOfRadiusNew = false;
                    if (renderDistance > 0D) {
                        Location location = MCHelper.getTempLocation(player);
                        outOfRadiusNew = distanceSqNew(location) > renderDistanceSq;
                    }

                    if (outOfRadiusNew || !isVisibleTo(toRepository, player)) {
                        removePlayer(player);
                    }
                }

                fromRepository.removeEntity(this, false);
            }

            long deltaX = Mth.floor((newX - x) * TRUNCATION_STEPS);
            long deltaY = Mth.floor((newY - y) * TRUNCATION_STEPS);
            long deltaZ = Mth.floor((newZ - z) * TRUNCATION_STEPS);

            boolean teleported = deltaX < Short.MIN_VALUE || deltaX > Short.MAX_VALUE
                    || deltaY < Short.MIN_VALUE || deltaY > Short.MAX_VALUE
                    || deltaZ < Short.MIN_VALUE || deltaZ > Short.MAX_VALUE;

            if (teleported) {
                onTeleport(newX, newY, newZ, newYaw, newPitch);
                // TODO: Rotate head ?
                updateLocation = false;
            } else {
                if (rotated) {
                    onMoveAndLook((short) deltaX, (short) deltaY, (short) deltaZ, newYaw, newPitch);
                    if (newYaw != yaw) {
                        onHead(newYaw);
                    }
                } else {
                    onMove((short) deltaX, (short) deltaY, (short) deltaZ);
                }
                updateLocation = true;
            }

            // Добавляем сущность после всех телепортов,
            // чтобы игроки, которые ее только что увидят,
            // не видел все телепортации
            toRepository.addEntity(this);
        } else {
            if (rotated) {
                onLook(newYaw, newPitch);
                if (newYaw != yaw) {
                    onHead(newYaw);
                }
            }
        }

        if (moved) {
            x = newX;
            y = newY;
            z = newZ;
        }

        if (rotated) {
            yaw = newYaw;
            pitch = newPitch;
        }

        if (updateLocation && ticks % 20 == 0) {
            onTeleport(x, y, z, yaw, pitch);
            updateLocation = false;
        }

        ticks++;
    }

    public void onHead(float yaw) {
    }

    public void onLook(float yaw, float pitch) {
    }

    public void onMove(short deltaX, short deltaY, short deltaZ) {
    }

    public void onMoveAndLook(short deltaX, short deltaY, short deltaZ, float yaw, float pitch) {
    }

    public void onTeleport(double x, double y, double z, float yaw, float pitch) {
    }

    boolean hasViewers() {
        return viewers.size() > 0;
    }

    void sendPacket(PacketWrapper wrapper) {
        for (Player player : viewers) {
            wrapper.sendPacket(player);
        }
    }

    double distanceSq(Location location) {
        return Mth.square(x - location.getX())
                + Mth.square(y - location.getY())
                + Mth.square(z - location.getZ());
    }

    double distanceSqNew(Location location) {
        return Mth.square(newX - location.getX())
                + Mth.square(newY - location.getY())
                + Mth.square(newZ - location.getZ());
    }

    WorldRepository.ChunkRepository getChunkRepository(double x, double z, boolean createIfNotExists) {
        EntityManager manager = plugin.getManager(EntityManager.class);
        WorldRepository worldRepository = manager.getWorld(world);
        return worldRepository.getChunkRepository(NumberConversions.floor(x) >> 4, NumberConversions.floor(z) >> 4, createIfNotExists);
    }

}
