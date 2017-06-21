package alpvax.advancedautocrafting.core;

import alpvax.advancedautocrafting.block.BlockCraftingManager;
import alpvax.advancedautocrafting.item.ItemCraftingLinker;
import alpvax.advancedautocrafting.tile.TileEntityCraftingManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = AdvancedAutocrafting.MOD_ID, guiFactory = "alpvax.advancedautocrafting.core.AutocraftingGuiFactory")
public class AdvancedAutocrafting
{
	public static final String MOD_ID = "advancedautocrafting";

	/*TODO:@SidedProxy(
			clientSide = "alpvax.advancedautocrafting.core.proxy.ClientProxy",
			serverSide = "alpvax.advancedautocrafting.core.proxy.ServerProxy")
	public static CommonProxy proxy;*/

	@Mod.Instance(MOD_ID)
	public static AdvancedAutocrafting instance;

	public void preInit(FMLPreInitializationEvent event)
	{

	}

	public static class Blocks
	{
		public static final Block CRAFTING_MANAGER = new BlockCraftingManager().setUnlocalizedName("craftingmanager").setRegistryName(MOD_ID, "craftingmanager");
	}

	public static class Items
	{
		public static final Item CRAFTING_LINKER = new ItemCraftingLinker().setUnlocalizedName("craftinglinker").setRegistryName(MOD_ID, "craftinglinker");
	}

	@EventBusSubscriber
	public static class EventHandler
	{
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event)
		{
			event.getRegistry().register(Blocks.CRAFTING_MANAGER);
			GameRegistry.registerTileEntity(TileEntityCraftingManager.class, "CraftingManager");
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event)
		{
			event.getRegistry().register(new ItemBlock(Blocks.CRAFTING_MANAGER).setRegistryName(Blocks.CRAFTING_MANAGER.getRegistryName()));
			event.getRegistry().register(Items.CRAFTING_LINKER);
		}
	}

	@EventBusSubscriber(Side.CLIENT)
	public static class EventHandlerClient
	{
		@SubscribeEvent
		public static void registerItems(ModelRegistryEvent event)
		{
			registerModel(Blocks.CRAFTING_MANAGER);
			registerModel(Items.CRAFTING_LINKER);
		}

		private static void registerModel(Block block)
		{
			registerModel(Item.getItemFromBlock(block));
		}

		private static void registerModel(Item item)
		{
			registerModel(item, 0, item.getRegistryName());
		}

		private static void registerModel(Item item, int metadata, ResourceLocation name)
		{
			ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(name, "inventory"));
		}
	}
}
