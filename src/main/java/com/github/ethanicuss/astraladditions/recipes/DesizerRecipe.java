package com.github.ethanicuss.astraladditions.recipes;

import com.github.ethanicuss.astraladditions.AstralAdditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesizerRecipe implements Recipe<SimpleInventory> {
    public static final Logger LOGGER = LoggerFactory.getLogger(AstralAdditions.MOD_ID);
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public DesizerRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems){
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        for (int i = 0; i < 27; i++){
            if (!(recipeItems.get(i).test(inventory.getStack(i)))){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public static class Type implements RecipeType<DesizerRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "desizer";
    }

    public static class Serializer implements RecipeSerializer<DesizerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "desizer";


        @Override
        public DesizerRecipe read(Identifier id, JsonObject json) {
            // Parse output
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            // Parse ingredients
            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(27, Ingredient.EMPTY); // Ensure size matches grid

            for (int i = 0; i < ingredients.size(); i++) {
                JsonElement element = ingredients.get(i);
                inputs.set(i, Ingredient.fromJson(element));
            }


            return new DesizerRecipe(id, output, inputs);
        }

        @Override
        public DesizerRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new DesizerRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, DesizerRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
