package com.enotakin.library.entity;

import com.enotakin.library.entity.type.LibRenderedEntity;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.*;

public class WorldRepository {

    private final Long2ObjectMap<ChunkRepository> chunks = new Long2ObjectOpenHashMap<>();
    private final Map<Player, PlayerRepository> players = new HashMap<>();
    private final Set<LibRenderedEntity> entities = new HashSet<>();

    public void onTick() {
        for (LibRenderedEntity entity : entities) {
            entity.onTick();
        }
    }

    public ChunkRepository getChunkRepository(int chunkX, int chunkZ, boolean createIfNotExists) {
        long key = Chunk.getChunkKey(chunkX, chunkZ);

        ChunkRepository repository = chunks.get(key);
        if (repository == null && createIfNotExists) {
            repository = new ChunkRepository(chunkX, chunkZ);
            chunks.put(key, repository);
        }

        return repository;
    }

    public PlayerRepository createPlayerRepository(Player player) {
        PlayerRepository repository = new PlayerRepository(player);
        players.put(player, repository);
        return repository;
    }

    public void addPlayerRepository(PlayerRepository repository) {
        players.put(repository.player, repository);
    }

    public PlayerRepository getPlayerRepository(Player player) {
        return players.get(player);
    }

    public PlayerRepository removePlayerRepository(Player player) {
        PlayerRepository playerRepository = players.remove(player);

        for (ChunkRepository chunkRepository : playerRepository.repositories) {
            chunkRepository.removePlayer(player, false);
        }
        playerRepository.repositories.clear();

        return playerRepository;
    }

    public void addEntity(LibRenderedEntity entity) {
        if (entities.add(entity)) {
            ChunkRepository chunkRepository = getChunkRepository(NumberConversions.floor(entity.getX()) >> 4, NumberConversions.floor(entity.getZ()) >> 4, true);
            chunkRepository.addEntity(entity);
        }
    }

    public void removeEntity(LibRenderedEntity entity) {
        if (entities.remove(entity)) {
            ChunkRepository chunkRepository = getChunkRepository(NumberConversions.floor(entity.getX()) >> 4, NumberConversions.floor(entity.getZ()) >> 4, false);
            if (chunkRepository != null) {
                chunkRepository.removeEntity(entity, true);
            }
        }
    }

    public class PlayerRepository {

        private final Player player;
        private final Set<ChunkRepository> repositories = new HashSet<>();

        PlayerRepository(Player player) {
            this.player = player;
        }

        void addChunkRepository(ChunkRepository repository) {
            repositories.add(repository);
        }

        void removeChunkRepository(ChunkRepository repository) {
            repositories.remove(repository);
        }

    }

    public class ChunkRepository {

        private final int x;
        private final int z;

        private final Set<Player> players = new HashSet<>();
        private final Set<LibRenderedEntity> entities = new HashSet<>();

        ChunkRepository(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public Set<Player> getPlayers() {
            return players;
        }

        public void addEntity(LibRenderedEntity entity) {
            if (entities.add(entity)) {
                for (Player player : players) {
                    if (entity.isVisibleTo(player)) {
                        entity.addPlayer(player);
                    }
                }
            }
        }

        public void removeEntity(LibRenderedEntity entity, boolean updateForPlayers) {
            if (entities.remove(entity)) {
                if (updateForPlayers) {
                    for (Player player : players) {
                        if (entity.isVisibleTo(player)) {
                            entity.removePlayer(player);
                        }
                    }
                }

                removeChunkIfNecessary();
            }
        }

        public void addPlayer(Player player) {
            if (players.add(player)) {
                PlayerRepository repository = getPlayerRepository(player);
                repository.addChunkRepository(this);

                for (LibRenderedEntity entity : entities) {
                    if (entity.isVisibleTo(player)) {
                        entity.addPlayer(player);
                    }
                }
            }
        }

        public void removePlayer(Player player, boolean clearPlayerRepository) {
            if (players.remove(player)) {
                if (clearPlayerRepository) {
                    PlayerRepository repository = getPlayerRepository(player);
                    if (repository != null) {
                        repository.removeChunkRepository(this);
                    }
                }

                for (LibRenderedEntity entity : entities) {
                    entity.removePlayer(player);
                }

                removeChunkIfNecessary();
            }
        }

        private void removeChunkIfNecessary() {
            if (players.isEmpty() && entities.isEmpty()) {
                chunks.remove(Chunk.getChunkKey(x, z));
            }
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            ChunkRepository that = (ChunkRepository) object;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }

    }

}
