package alpvax.advancedautocrafting.container;


import alpvax.advancedautocrafting.block.tile.RemoteMasterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class RemoteMasterContainer extends AbstractTileEntityContainer<RemoteMasterTileEntity> {
  public RemoteMasterContainer(final int id, final PlayerInventory playerInventory, final RemoteMasterTileEntity tile) {
    super(AAContainerTypes.REMOTE_MASTER.get(), id, tile);

    for(int j = 0; j < 3; ++j) {
      for(int k = 0; k < 9; ++k) {
        this.addSlot(new SlotItemHandler(tile.inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
      }
    }

    //Player inventory
    for(int l = 0; l < 3; ++l) {
      for(int j1 = 0; j1 < 9; ++j1) {
        this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 85 + l * 18));
      }
    }
    //Hotbar
    for(int i1 = 0; i1 < 9; ++i1) {
      this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 143));
    }
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index < 27) {
        if (!this.mergeItemStack(itemstack1, 27, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, 27, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }
}
