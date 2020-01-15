package alpvax.advancedautocrafting.client.data;

import alpvax.advancedautocrafting.AdvancedAutocrafting;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.util.function.Supplier;

import static alpvax.advancedautocrafting.block.AABlocks.CONTROLLER;
import static alpvax.advancedautocrafting.block.AABlocks.REMOTE_MARKER;

public class AABlockstateProvider extends BlockStateProvider {

  public AABlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, AdvancedAutocrafting.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlock(CONTROLLER);
    simpleBlock(REMOTE_MARKER);
  }

  private void simpleBlock(Supplier<Block> sup) {
    simpleBlock(sup.get());
  }
}
