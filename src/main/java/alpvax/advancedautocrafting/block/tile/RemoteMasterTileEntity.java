package alpvax.advancedautocrafting.block.tile;

import alpvax.advancedautocrafting.Capabilities;
import alpvax.advancedautocrafting.block.AABlocks;
import alpvax.advancedautocrafting.container.RemoteMasterContainer;
import alpvax.advancedautocrafting.craftnetwork.INetworkNode;
import alpvax.advancedautocrafting.craftnetwork.SimpleNetworkNode;
import alpvax.advancedautocrafting.util.BlockPosUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;

public class RemoteMasterTileEntity extends TileEntity implements INamedContainerProvider {
  public ItemStackHandler inventory = new ItemStackHandler(27) {
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return BlockPosUtil.hasPosition(stack);
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      //TODO:network.markNodeDirty();
    }
  };
  private LazyOptional<IItemHandler> items = LazyOptional.of(() -> inventory);
  private LazyOptional<INetworkNode> networkCapability = LazyOptional.of(this::makeNetworkNode);

  public RemoteMasterTileEntity() {
    super(AABlocks.TileTypes.REMOTE_MASTER.get());
  }

  private INetworkNode makeNetworkNode() {
    return new INetworkNode() {
      @Override
      public NonNullList<INetworkNode> getChildNodes(Direction inbound) {
        return RemoteMasterTileEntity.this.getItems().stream().map((stack) ->
            new SimpleNetworkNode(BlockPosUtil.readPosFromItemStack(stack))).collect(Collectors.toCollection(NonNullList::create)
        );
      }

      @Override
      public BlockPos getPos() {
        return RemoteMasterTileEntity.this.pos;
      }
    };
  }

  private NonNullList<ItemStack> getItems() {
    NonNullList<ItemStack> list = NonNullList.create();
    for(int i = 0; i < inventory.getSlots(); i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if(!stack.isEmpty()) {
        list.add(stack);
      }
    }
    return list;
  }

  @Nonnull
  public NonNullList<BlockPos> getRemotePositions() {
    return getItems().stream().map(BlockPosUtil::readPosFromItemStack).filter(Objects::nonNull).collect(Collectors.toCollection(NonNullList::create));
  }

  public void dropItems(World worldIn, BlockPos pos, BlockState newState) {
    InventoryHelper.dropItems(worldIn, pos, getItems());
  }

  public ItemStack addItem(ItemStack stack) {
    for(int i = 0; i < inventory.getSlots(); i++) {
      ItemStack s = inventory.getStackInSlot(i);
      if(s.isEmpty()) {
        return inventory.insertItem(i, stack, false);
      }
    }
    return stack;
  }

  @Override
  public void read(CompoundNBT compound) {
    super.read(compound);
    inventory.deserializeNBT(compound.getCompound("remoteItems"));
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    compound.put("remoteItems", inventory.serializeNBT());
    return super.write(compound);
  }

  /*@Override
  public CompoundNBT getUpdateTag() {
    return super.getUpdateTag();
  }

  @Override
  public void handleUpdateTag(CompoundNBT tag) {

  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
    return super.getCapability(cap);
  }*/

  public boolean canPlayerUse(PlayerEntity playerIn) {
    return playerIn.getPosition().withinDistance(getPos(), playerIn.getAttribute(PlayerEntity.REACH_DISTANCE).getValue() + 1);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent(AABlocks.REMOTE_MASTER.get().getTranslationKey());
  }

  @Nullable
  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
    return new RemoteMasterContainer(id, playerInventory, this);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    return cap == Capabilities.NODE_CAPABILITY ? networkCapability.cast() : super.getCapability(cap, side);
  }
}
