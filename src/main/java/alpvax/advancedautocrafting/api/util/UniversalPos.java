package alpvax.advancedautocrafting.api.util;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Objects;

class UniversalPos extends ProxyBlockSource implements Comparable<IBlockSource> {
  public static final Comparator<IBlockSource> COMPARATOR = Comparator.comparing((IBlockSource loc) -> loc.getWorld().func_234923_W_())
                                                                 .thenComparing(loc -> new BlockPos(loc.getX(), loc.getY(), loc.getZ()));

  public static UniversalPos from(@Nonnull IWorldPosCallable c) {
    return c.apply(UniversalPos::new).orElseThrow(() -> new NullPointerException("Failed to create UniversalPos from IWorldPosCallable: " + c.toString()));
  }
  public UniversalPos(@Nonnull World world, @Nonnull BlockPos pos) {
    super((ServerWorld) world, pos); //TODO: Fix ServerWorld not on Client
  }

  public boolean isLoaded() {
    return this.isLoaded(0);
  }
  public boolean isLoaded(int adjacentBlocks) {
    return getWorld().isAreaLoaded(getBlockPos(), adjacentBlocks);
  }

  /**
   * Equivalent of {@link BlockPos#offset}
   * @return a new UniversalPos with the same world, and the offset pos
   */
  public UniversalPos offset(Direction d) {
    return new UniversalPos(getWorld(), getBlockPos().offset(d));
  }

  /**
   * Convenience method to get a capability from the TileEntity at this postion.
   * @see TileEntity#getCapability(Capability, Direction).
   * @return the value from {@link TileEntity#getCapability} or {@link LazyOptional#empty()} if there is no TileEntity at that position.
   */
  @Nonnull
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    TileEntity t = getBlockTileEntity();
    //noinspection ConstantConditions
    return t != null ? t.getCapability(cap, side) : LazyOptional.empty();
  }

  public ITextComponent singleLineDisplay() {
    return new StringTextComponent("Dimension: \"" + getWorld().func_234923_W_().func_240901_a_().toString()
                                       + "\"; Position: " + getBlockPos() + ";");//TODO: Convert to translation?
  }

  @Override
  public int hashCode() {
    BlockPos pos = getBlockPos();
    return Objects.hash(getWorld(), new ChunkPos(pos), pos);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof UniversalPos) {
      UniversalPos o = (UniversalPos) obj;
      return getWorld().equals(o.getWorld()) && getBlockPos().equals(o.getBlockPos());
    }
    return false;
  }

  @Override
  public int compareTo(@Nonnull IBlockSource o) {
    return COMPARATOR.compare(this, o);
  }
}