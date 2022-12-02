package com.enotakin.library.entity.type;

import com.comphenix.protocol.wrappers.*;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.entity.MineHuman;
import com.enotakin.library.api.protocol.*;
import com.enotakin.library.api.scoreboard.ScoreboardTeam;
import com.enotakin.library.api.scoreboard.Team;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class LibHuman extends LibEquippableEntity implements MineHuman {

    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_PLAYER_MODE_CUSTOMISATION = new WrappedDataWatcher.WrappedDataWatcherObject(16, WrappedDataWatcher.Registry.get(Byte.class));

    private static final String SKIN_KEY = "textures";
    private static final int PLAYER_LIST_NAME_HIDE_DELAY = 20 * 5;

    private final String name = ChatColor.DARK_GRAY + "NPC [" + entityID + "]";
    private final WrappedGameProfile gameProfile = new WrappedGameProfile(uniqueID, name);
    private final ScoreboardTeam scoreboardTeam = new ScoreboardTeam(name);

    private Skin skin;
    private ChatColor glowColor;

    private final Map<Player, PlayerTabList> tabLists = new HashMap<>();

    public LibHuman(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);

        scoreboardTeam.setVisibility(Team.Visibility.NEVER);
        scoreboardTeam.setPush(Team.Push.NEVER);
        scoreboardTeam.addEntry(name);

        dataWatcher.setObject(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0x7F); // Turn on the display of volumetric skins
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Skin getSkin() {
        return skin;
    }

    @Override
    public void setSkin(Skin skin) {
        if (!Objects.equals(this.skin, skin)) {
            this.skin = skin;
            this.gameProfile.getProperties().put(SKIN_KEY, new WrappedSignedProperty(SKIN_KEY, skin.getValue(), skin.getSignature()));

            if (hasViewers()) {
                for (Player viewer : viewers) {
                    onHideTo(viewer);
                    onShowTo(viewer);
                }
            }
        }
    }

    @Override
    public ChatColor getGlowColor() {
        return glowColor;
    }

    @Override
    public void setGlowColor(ChatColor glowColor) {
        if (this.glowColor != glowColor) {
            this.glowColor = glowColor;
            this.scoreboardTeam.setColor(glowColor);
        }
    }

    @Override
    public boolean remove() {
        boolean removed = super.remove();
        if (removed) {
            scoreboardTeam.clear();
        }
        return removed;
    }

    @Override
    public void onShowTo(Player player) {
        WrapperPlayServerPlayerInfo infoWrapper = new WrapperPlayServerPlayerInfo();
        infoWrapper.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        infoWrapper.setData(Collections.singletonList(new PlayerInfoData(gameProfile, 0, EnumWrappers.NativeGameMode.NOT_SET, null)));
        infoWrapper.sendPacket(player);

        WrapperPlayServerNamedEntitySpawn spawnWrapper = new WrapperPlayServerNamedEntitySpawn();
        spawnWrapper.setEntityID(entityID);
        spawnWrapper.setPlayerUUID(gameProfile.getUUID());
        spawnWrapper.setX(x);
        spawnWrapper.setY(y);
        spawnWrapper.setZ(z);
        spawnWrapper.setYaw(yaw);
        spawnWrapper.setPitch(pitch);
        spawnWrapper.sendPacket(player);

        WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
        metadataWrapper.setEntityID(entityID);
        metadataWrapper.setMetadata(dataWatcher.getWatchableObjects());
        metadataWrapper.sendPacket(player);

        WrapperPlayServerEntityHeadRotation headRotationWrapper = new WrapperPlayServerEntityHeadRotation();
        headRotationWrapper.setEntityID(entityID);
        headRotationWrapper.setHeadYaw(yaw);
        headRotationWrapper.sendPacket(player);

        WrapperPlayServerAnimation animationWrapper = new WrapperPlayServerAnimation();
        animationWrapper.setEntityID(entityID);
        animationWrapper.setAnimation(0);
        animationWrapper.sendPacket(player);

        onShowViewTo(player);
        onShowEquipmentTo(player);

        scoreboardTeam.showTo(player);
        tabLists.put(player, new PlayerTabList(player, PLAYER_LIST_NAME_HIDE_DELAY));
    }

    @Override
    public void onHideTo(Player player) {
        WrapperPlayServerPlayerInfo infoWrapper = new WrapperPlayServerPlayerInfo();
        infoWrapper.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        infoWrapper.setData(Collections.singletonList(new PlayerInfoData(gameProfile, 0, EnumWrappers.NativeGameMode.NOT_SET, null)));
        infoWrapper.sendPacket(player);

        WrapperPlayServerEntityDestroy destroyWrapper = new WrapperPlayServerEntityDestroy();
        destroyWrapper.setEntityIDs(entityID);
        destroyWrapper.sendPacket(player);

        onHideViewTo(player);

        scoreboardTeam.hideTo(player);
        tabLists.remove(player);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (tabLists.size() > 0) {
            Iterator<PlayerTabList> iterator = tabLists.values().iterator();
            while (iterator.hasNext()) {
                PlayerTabList tabList = iterator.next();
                if (--tabList.ticks <= 0) {
                    WrapperPlayServerPlayerInfo infoWrapper = new WrapperPlayServerPlayerInfo();
                    infoWrapper.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                    infoWrapper.setData(Collections.singletonList(new PlayerInfoData(gameProfile, 0, EnumWrappers.NativeGameMode.NOT_SET, null)));
                    infoWrapper.sendPacket(tabList.player);

                    iterator.remove();
                }
            }
        }
    }

    private static class PlayerTabList {

        private final Player player;
        private int ticks;

        public PlayerTabList(Player player, int ticks) {
            this.player = player;
            this.ticks = ticks;
        }

    }

}
