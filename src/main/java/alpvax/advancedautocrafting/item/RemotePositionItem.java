package alpvax.advancedautocrafting.item;

import alpvax.advancedautocrafting.client.render.BlockHighlightRender;
import alpvax.advancedautocrafting.util.BlockPosUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RemotePositionItem extends Item {

  public RemotePositionItem(Properties properties) {
    super(properties);
  }

  /**
   * allows items to add custom lines of information to the mouseover description
   */
  @OnlyIn(Dist.CLIENT)
  public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
    tooltip.add(BlockPosUtil.getItemPositionText(stack).func_240699_a_/*.applyTextStyle*/(TextFormatting.GRAY));
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    BlockPos pos = BlockPosUtil.readPosFromItemStack(stack);
    if(world.isRemote) {
      DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
        if(BlockHighlightRender.manager.contains(pos)) {
          BlockHighlightRender.manager.remove(pos);
        } else {
          BlockHighlightRender.manager.add(pos, 69, 120, 18, 160);
        }
      });
    }
    return ActionResult.resultSuccess(stack);
  }

  @Override
  public boolean hasEffect(@Nonnull ItemStack stack) {
    return DistExecutor.unsafeRunForDist(() -> () -> isRendering(stack), () -> () -> super.hasEffect(stack));
  }

  @OnlyIn(Dist.CLIENT)
  private boolean isRendering(ItemStack stack) {
    BlockPos pos = BlockPosUtil.readPosFromItemStack(stack);
    return pos != null && BlockHighlightRender.manager.contains(pos);
  }
}
