package fr.minemobs.aetheruhcutils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = MainAetherUHC.MODID)
public class Events {

    public static Block[] dangerBlockArray = { Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.AIR, Blocks.MAGMA };

    @SubscribeEvent
    public void onChangedDim(PlayerEvent.PlayerChangedDimensionEvent event){
        if(event.fromDim == 4 && event.toDim == 0){
            EntityPlayer player = event.player;
            World world = player.getServer().getWorld(DimensionType.NETHER.getId());
            randomTeleport(world, player);
        }
    }


    private void randomTeleport(World world, EntityPlayer p) {
        Random r = new Random();
        int low = 20;
        int high = 2500;
        int x = r.nextInt(high-low) + low;
        int y = r.nextInt(255);
        int z = r.nextInt(high-low) + low;
        int maxTries = 500;
        while (!isSafe(world, p, x, y, z) && (maxTries == -1 || maxTries > 0)) {
            y++;
            if(y >= 120) {
                x = r.nextInt(high-low) + low;
                y = 50;
                z = r.nextInt(high-low) + low;
                continue;
            }
            if(maxTries > 0){
                maxTries--;
            }
            if(maxTries == 0) {
                TextComponentString msg = new TextComponentString("&cTimed out trying to find a safe location to warp to.");
                p.sendMessage(msg);
                return;
            }
        }

        p.changeDimension(DimensionType.NETHER.getId(), new Teleporter(new BlockPos(x, y, z)));
        TextComponentString successful = new TextComponentString("&aYou have been teleported");
        p.sendMessage(successful);
    }

    private boolean isSafe(World world, EntityPlayer player, int newX, int newY, int newZ) {
        return (isEmpty(world, newX, newY, newZ)) &&
                (!isDangerBlock(world, newX, newY - 1, newZ));
    }

    private boolean isEmpty(World world, int newX, int newY, int newZ) {
        if ((world.isAirBlock(new BlockPos(newX, newY, newZ))) && (world.isAirBlock(new BlockPos(newX, newY + 1, newZ))) &&
                (world.isAirBlock(new BlockPos(newX + 1, newY, newZ))) && (world.isAirBlock(new BlockPos(newX - 1, newY, newZ))) &&
                (world.isAirBlock(new BlockPos(newX, newY, newZ + 1))) && (world.isAirBlock(new BlockPos(newX, newY, newZ - 1)))) {
            return true;
        }
        return false;
    }

    private boolean isDangerBlock(World world, int newX, int newY, int newZ) {
        for (Block block : dangerBlockArray) {
            if (block.equals(world.getBlockState(new BlockPos(newX, newY, newZ)).getBlock())) {
                return true;
            }
        }
        return false;
    }

    private static class Teleporter implements ITeleporter
    {
        private final BlockPos targetPos;

        private Teleporter(BlockPos targetPos)
        {
            this.targetPos = targetPos;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw)
        {
            entity.moveToBlockPosAndAngles(targetPos, yaw, entity.rotationPitch);
        }
    }
}
