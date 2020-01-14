package alpvax.advancedautocrafting;

import alpvax.advancedautocrafting.block.AABlocks;
import alpvax.advancedautocrafting.client.data.AABlockstateProvider;
import alpvax.advancedautocrafting.client.data.AAItemModelProvider;
import alpvax.advancedautocrafting.client.data.AALangProvider;
import alpvax.advancedautocrafting.data.AALootTableProvider;
import alpvax.advancedautocrafting.data.AARecipeProvider;
import alpvax.advancedautocrafting.item.AAItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(AdvancedAutocrafting.MODID)
public class AdvancedAutocrafting {
  public static final String MODID = "advancedautocrafting";

  private static final Logger LOGGER = LogManager.getLogger();

  public static final ItemGroup ITEM_GROUP = (new ItemGroup(MODID) {
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(AABlocks.CONTROLLER.get());
    }
  });

  public AdvancedAutocrafting() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    // General mod setup
    modBus.addListener(this::setup);
    modBus.addListener(this::gatherData);

    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
      // Client setup
      modBus.addListener(this::setupClient);
    });

    MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);

    // Registry objects
    AABlocks.BLOCKS.register(modBus);
    AAItems.ITEMS.register(modBus);
  }

  private void setup(final FMLCommonSetupEvent event) {
    //PacketManager.init();
  }


  @OnlyIn(Dist.CLIENT)
  private void setupClient(final FMLClientSetupEvent event) {
    //ClientRegistry.bindTileEntitySpecialRenderer(DrinkMixerTileEntity.class, new DrinkMixerRenderer());
  }

  private void onServerStarting(final FMLServerStartingEvent event) {
    //Register commands
    //CommandTropicsTeleport.register(event.getServer().getCommandManager().getDispatcher());
  }

  private void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();

    if (event.includeClient()) {
      gen.addProvider(new AABlockstateProvider(gen, event.getExistingFileHelper()));
      //TODO: Generate item models when supported by forge:
      // gen.addProvider(new AAItemModelProvider(gen, event.getExistingFileHelper()));
      gen.addProvider(new AALangProvider(gen));
    }
    if (event.includeServer()) {
      /*
      gen.addProvider(new TropicraftBlockTagsProvider(gen));
      gen.addProvider(new TropicraftItemTagsProvider(gen));
      */
      gen.addProvider(new AARecipeProvider(gen));
      gen.addProvider(new AALootTableProvider(gen));
    }
  }
}