package com.github.ethanicuss.astraladditions;

import com.github.ethanicuss.astraladditions.entities.ModEntities;
import com.github.ethanicuss.astraladditions.fluids.ModFluids;
import com.github.ethanicuss.astraladditions.registry.ModBlocks;
import com.github.ethanicuss.astraladditions.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstralAdditions implements ModInitializer {
	public static final String MOD_ID = "astraladditions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModFluids.registerFluids();
		ModEntities.init();
		ModBlocks.registerBlocks();
		ModItems.registerItems();
		LOGGER.info("Astral Additions is active!");
	}
}