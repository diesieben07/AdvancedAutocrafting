package alpvax.advancedautocrafting.client.data;

import alpvax.advancedautocrafting.AdvancedAutocrafting;
import alpvax.advancedautocrafting.block.AABlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AALangProvider extends LanguageProvider {

  public AALangProvider(DataGenerator gen) {
    super(gen, AdvancedAutocrafting.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    add(AdvancedAutocrafting.ITEM_GROUP, "Advanced Autocrafting");

    AABlocks.BLOCKS.getEntries().forEach(this::addBlock);
  }

  private void add(ItemGroup group, String name) {
    add(group.getTranslationKey(), name);
  }

  protected void addBlock(RegistryObject<Block> block) {
    add(block.get(), toEnglishName(block.getId()));
  }

  protected String toEnglishName(ResourceLocation internalName) {
    return "Autocrafting " + formatEnglish(internalName.getPath());
  }

  private String formatEnglish(String name) {
    return Arrays.stream(name.split("_"))
        .map(StringUtils::capitalize)
        .collect(Collectors.joining(" "));
  }
}