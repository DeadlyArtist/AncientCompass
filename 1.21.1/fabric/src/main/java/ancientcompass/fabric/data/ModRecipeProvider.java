package ancientcompass.fabric.data;

import ancientcompass.ModEntry;
import ancientcompass.item.ModItems;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public static class ShapedRecipeUtils {
        public static char findFirstNonSpaceChar(List<String> strings) {
            for (String string : strings) {
                for (char ch : string.toCharArray()) {
                    if (ch != ' ') {
                        return ch;
                    }
                }
            }
            throw new RuntimeException("No non-space character found in the list");
        }

        public static List<Character> getUniqueChars(List<String> strings) {
            // Use a LinkedHashSet to maintain order and uniqueness
            Set<Character> uniqueCharsSet = new LinkedHashSet<>();

            // Iterate over each string and add each character to the set
            for (String str : strings) {
                for (char ch : str.toCharArray()) {
                    if (ch != ' ') {
                        uniqueCharsSet.add(ch);
                    }
                }
            }

            // Convert the set to a list to return
            return new ArrayList<>(uniqueCharsSet);
        }
    }

    public static class Input {
        public final char identifier;
        public final Ingredient ingredient;
        public final ItemConvertible item;
        public final TagKey<Item> tag;
        public final int size;

        public Input(int size, char identifier, Ingredient ingredient, ItemConvertible item, TagKey<Item> tag) {
            this.identifier = identifier;
            this.size = size;
            this.ingredient = ingredient;
            this.item = item;
            this.tag = tag;
        }

        public Input(char identifier, ItemConvertible item) {
            this(1, identifier, Ingredient.ofItems(item), item, null);
        }

        public Input(char identifier, TagKey<Item> tag) {
            this(1, identifier, Ingredient.fromTag(tag), null, tag);
        }

        public Input(int size, ItemConvertible item) {
            this(size, Character.MIN_VALUE, Ingredient.ofItems(item), item, null);
        }

        public Input(int size, TagKey<Item> tag) {
            this(size, Character.MIN_VALUE, Ingredient.fromTag(tag), null, tag);
        }

        public static Input of(char identifier, ItemConvertible item) {
            return new Input(identifier, item);
        }

        public static Input of(char identifier, TagKey<Item> tag) {
            return new Input(identifier, tag);
        }

        public static Input of(ItemConvertible item) {
            return new Input(Character.MIN_VALUE, item);
        }

        public static Input of(TagKey<Item> tag) {
            return new Input(Character.MIN_VALUE, tag);
        }

        public static Input of(ItemConvertible item, int size) {
            return new Input(size, item);
        }

        public static Input of(TagKey<Item> tag, int size) {
            return new Input(size, tag);
        }
    }

    public static class ShapedRecipeBuilderWrapper {
        private final ShapedRecipeJsonBuilder internalBuilder;
        private String path;
        private String namespace = ModEntry.MOD_ID;

        public ShapedRecipeBuilderWrapper(ShapedRecipeJsonBuilder internalBuilder, String path) {
            this.internalBuilder = internalBuilder;
            this.path = path;
        }

        public ShapedRecipeBuilderWrapper pattern(String pattern) {
            internalBuilder.pattern(pattern);
            return this;
        }

        public ShapedRecipeBuilderWrapper input(Character identifier, Ingredient ingredient) {
            internalBuilder.input(identifier, ingredient);
            return this;
        }

        public ShapedRecipeBuilderWrapper criterion(String criterionName, AdvancementCriterion<?> advancementCriterion) {
            internalBuilder.criterion(criterionName, advancementCriterion);
            return this;
        }

        public ShapedRecipeBuilderWrapper setPath(String path) {
            this.path = path;
            return this;
        }

        public ShapedRecipeBuilderWrapper setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public void offer(RecipeExporter exporter) {
            internalBuilder.offerTo(exporter, Identifier.of(namespace, path));
        }
    }

    public static class ShapelessRecipeBuilderWrapper {
        private final ShapelessRecipeJsonBuilder internalBuilder;
        private String path;
        private String namespace = ModEntry.MOD_ID;

        public ShapelessRecipeBuilderWrapper(ShapelessRecipeJsonBuilder internalBuilder, String path) {
            this.internalBuilder = internalBuilder;
            this.path = path;
        }

        public ShapelessRecipeBuilderWrapper input(Ingredient ingredient, int size) {
            internalBuilder.input(ingredient, size);
            return this;
        }

        public ShapelessRecipeBuilderWrapper input(Ingredient ingredient) {
            internalBuilder.input(ingredient, 1);
            return this;
        }

        public ShapelessRecipeBuilderWrapper criterion(String criterionName, AdvancementCriterion<?> advancementCriterion) {
            internalBuilder.criterion(criterionName, advancementCriterion);
            return this;
        }

        public ShapelessRecipeBuilderWrapper setPath(String path) {
            this.path = path;
            return this;
        }

        public ShapelessRecipeBuilderWrapper setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public void offer(RecipeExporter exporter) {
            internalBuilder.offerTo(exporter, Identifier.of(namespace, path));
        }
    }

    public static class CookingRecipeBuilderWrapper {
        private final CookingRecipeJsonBuilder internalBuilder;
        private String path;
        private String namespace = ModEntry.MOD_ID;

        public CookingRecipeBuilderWrapper(CookingRecipeJsonBuilder internalBuilder, String path) {
            this.internalBuilder = internalBuilder;
            this.path = path;
        }

        public CookingRecipeBuilderWrapper criterion(String criterionName, AdvancementCriterion<?> advancementCriterion) {
            internalBuilder.criterion(criterionName, advancementCriterion);
            return this;
        }

        public CookingRecipeBuilderWrapper setPath(String path) {
            this.path = path;
            return this;
        }

        public CookingRecipeBuilderWrapper setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public void offer(RecipeExporter exporter) {
            internalBuilder.offerTo(exporter, Identifier.of(namespace, path));
        }
    }

    public static ShapedRecipeBuilderWrapper createShapedRecipeSpecific(RecipeCategory category, List<String> pattern, List<Input> inputs, ItemConvertible output, int outputCount) {
        ShapedRecipeJsonBuilder internalBuilder = ShapedRecipeJsonBuilder.create(category, output, outputCount);
        ShapedRecipeBuilderWrapper builder = new ShapedRecipeBuilderWrapper(internalBuilder, getItemPath(output));

        pattern.forEach(builder::pattern);
        inputs.forEach(input -> builder.input(input.identifier, input.ingredient));
        inputs.forEach(input -> builder.criterion(hasInput(input), conditionsFromInput(input)));

        return builder;
    }

    public static ShapedRecipeBuilderWrapper createShapedRecipeSpecific(RecipeCategory category, List<String> pattern, List<Input> inputs, ItemConvertible output) {
        return createShapedRecipeSpecific(category, pattern, inputs, output, 1);
    }

    public static ShapedRecipeBuilderWrapper createShapedRecipe(RecipeCategory category, List<String> pattern, List<Input> inputs, ItemConvertible output, int outputCount) {
        List<Character> uniqueChars = ShapedRecipeUtils.getUniqueChars(pattern);
        List<Input> inputsMapped = IntStream.range(0, Math.min(uniqueChars.size(), inputs.size()))
                .mapToObj(i -> new Input(1, uniqueChars.get(i), inputs.get(i).ingredient, inputs.get(i).item, inputs.get(i).tag))
                .toList();
        return createShapedRecipeSpecific(category, pattern, inputsMapped, output, outputCount);
    }

    public static ShapedRecipeBuilderWrapper createShapedRecipe(RecipeCategory category, List<String> pattern, List<Input> inputs, ItemConvertible output) {
        return createShapedRecipe(category, pattern, inputs, output, 1);
    }

    public static ShapedRecipeBuilderWrapper createShapedRecipe(RecipeCategory category, List<String> pattern, Input input, ItemConvertible output, int outputCount) {
        return createShapedRecipe(category, pattern, List.of(input), output, outputCount);
    }

    public static ShapedRecipeBuilderWrapper createShapedRecipe(RecipeCategory category, List<String> pattern, Input input, ItemConvertible output) {
        return createShapedRecipe(category, pattern, input, output, 1);
    }

    public static void offerReversibleCompactingRecipes(RecipeCategory category, RecipeExporter exporter, ItemConvertible item, ItemConvertible compacted) {
        var compact = createShapedRecipe(category, List.of("###", "###", "###"), Input.of(item), compacted);
        var uncompact = createShapelessRecipe(category, Input.of(compacted), item, 9);
        compact.offer(exporter);
        uncompact.offer(exporter);
    }

    public static ShapelessRecipeBuilderWrapper createShapelessRecipe(RecipeCategory category, List<Input> inputs, ItemConvertible output, int outputCount) {
        ShapelessRecipeJsonBuilder internalBuilder = ShapelessRecipeJsonBuilder.create(category, output, outputCount);
        ShapelessRecipeBuilderWrapper builder = new ShapelessRecipeBuilderWrapper(internalBuilder, getItemPath(output));

        inputs.forEach(input -> builder.input(input.ingredient, input.size));
        inputs.forEach(input -> builder.criterion(hasInput(input), conditionsFromInput(input)));

        return builder;
    }

    public static ShapelessRecipeBuilderWrapper createShapelessRecipe(RecipeCategory category, List<Input> inputs, ItemConvertible output) {
        return createShapelessRecipe(category, inputs, output, 1);
    }

    public static ShapelessRecipeBuilderWrapper createShapelessRecipe(RecipeCategory category, Input input, ItemConvertible output, int ouputCount) {
        return createShapelessRecipe(category, List.of(input), output, ouputCount);
    }

    public static ShapelessRecipeBuilderWrapper createShapelessRecipe(RecipeCategory category, Input input, ItemConvertible output) {
        return createShapelessRecipe(category, input, output, 1);
    }

    public static <T extends AbstractCookingRecipe> CookingRecipeBuilderWrapper createCookingRecipe(RecipeCategory category, CookingRecipeSerializer<T> serializer, Input input, ItemConvertible output, int cookingTime, float experience) {
        CookingRecipeJsonBuilder internalBuilder = CookingRecipeJsonBuilder.create(ingredientFromInput(input), category, output, experience, cookingTime, (RecipeSerializer<T>)serializer, (AbstractCookingRecipe.RecipeFactory<T>)serializer.recipeFactory);
        String defaultPath = getItemPath(output) + "_from_" + getItemPath(input.item) + "_in_" + Registries.RECIPE_SERIALIZER.getId(serializer).getPath();
        CookingRecipeBuilderWrapper builder = new CookingRecipeBuilderWrapper(internalBuilder, defaultPath);

        builder.criterion(hasInput(input), conditionsFromInput(input));

        return builder;
    }

    public static String hasTag(TagKey<?> tag) {
        String[] parts = tag.id().getPath().split("/");
        return "has_" + String.join("_", parts);
    }

    public static String hasInput(Input input) {
        return input.item != null ? hasItem(input.item) : hasTag(input.tag);
    }

    public static AdvancementCriterion<?> conditionsFromInput(Input input) {
        return input.item != null ? conditionsFromItem(input.item) : conditionsFromTag(input.tag);
    }

    public static Ingredient ingredientFromInput(Input input) {
        return input.item != null ? Ingredient.ofItems(input.item) : Ingredient.fromTag(input.tag);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        createShapedRecipe(RecipeCategory.TOOLS, List.of(" e ", "ncn", " e "), List.of(Input.of(Items.EMERALD), Input.of(Items.NETHERITE_SCRAP), Input.of(Items.COMPASS)), ModItems.ANCIENT_COMPASS.get()).offer(exporter);
    }
}
